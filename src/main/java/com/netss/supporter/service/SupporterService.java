package com.netss.supporter.service;

import com.netss.supporter.domain.Campaign;
import com.netss.supporter.domain.Supporter;
import com.netss.supporter.domain.SupporterCampaign;
import com.netss.supporter.exception.SupporterNotFoundException;
import com.netss.supporter.integration.web.CampaignClient;
import com.netss.supporter.repository.SupporterCampaignRepository;
import com.netss.supporter.repository.SupporterRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class SupporterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SupporterService.class);

    private final SupporterRepository supporterRepository;
    private final CampaignClient campaignClient;
    private final SupporterCampaignRepository supporterCampaignRepository;

    public SupporterService(SupporterRepository supporterRepository, CampaignClient campaignClient, SupporterCampaignRepository supporterCampaignRepository) {
        this.supporterRepository = supporterRepository;
        this.campaignClient = campaignClient;
        this.supporterCampaignRepository = supporterCampaignRepository;
    }

    public Supporter updateSupporter(Supporter supporter) {
        Supporter oldSupporter = supporterRepository.getOne(supporter.getId());

        oldSupporter.setName(supporter.getName());
        oldSupporter.setEmail(supporter.getEmail());
        oldSupporter.setBirthday(supporter.getBirthday());
        oldSupporter.setTeamId(supporter.getTeamId());
        oldSupporter = supporterRepository.save(oldSupporter);

        return oldSupporter;
    }

    public Supporter save(Supporter supporter) {

        Supporter createdSupporter = supporterRepository.save(supporter);
        associateSupporterWithCampaign(createdSupporter, Collections.emptyList());
        return createdSupporter;
    }

    private List<Campaign> associateSupporterWithCampaign(Supporter supporter, List<SupporterCampaign> currentSupportCampaigns) {

        List<Campaign> campaigns;

        try {
            campaigns = campaignClient.getCampaignsByTeamId(supporter.getTeamId());
        } catch (FeignException ex){
            LOGGER.error("Error during campaign call",ex);
            campaigns = Collections.emptyList();
        }

        if(campaigns.isEmpty())
            return campaigns;

        List<SupporterCampaign> supporterCampaigns = campaigns
            .stream()
            .map( campaign -> {
                SupporterCampaign s = new SupporterCampaign();
                s.setCampaignId(campaign.getId());
                s.setSupporterId(supporter.getId());
                return s;
            })
            .collect(Collectors.toList());

        Set<SupporterCampaign> filteredSupporterCampaigns = Stream
            .concat(
                new HashSet<>(currentSupportCampaigns).stream(),
                new HashSet<>(supporterCampaigns).stream())
            .collect(Collectors.toSet());

        //TODO: user=fh message='add relationship between supporter and supporter-campaings entities'
        supporterCampaignRepository.saveAll(filteredSupporterCampaigns);
        return campaigns;
    }


    public List<Campaign> getSupporterCampaigns(Long id) {
        List<SupporterCampaign> supporterCampaigns = supporterRepository.getSupporterCampaignById(id);

        List<Long> campaignIds = supporterCampaigns.stream()
            .map( sc -> sc.getCampaignId())
            .collect(Collectors.toList());

        List<Campaign> campaigns;

        try {
            campaigns = campaignClient.getCampaignsById(campaignIds);
        } catch (FeignException ex){
            LOGGER.error("Error during campaign call",ex);
            campaigns = Collections.emptyList();
        }

        return campaigns;
    }

    public Optional<List<Campaign>> associate(Long id) {

        Supporter supporter = supporterRepository
            .findById(id)
            .orElseThrow(SupporterNotFoundException::new);

        //TODO: user=fh message='associate supporter entity with supporterCampaign'
        List<SupporterCampaign> supporterCampaigns = supporterRepository.getSupporterCampaignById(supporter.getId());

        return Optional.of(associateSupporterWithCampaign(supporter, supporterCampaigns));
    }

    @CacheEvict(value="campaign-team-by-id", allEntries=true)
    public void updateSupporterCampaigns(List<Long> campaignIds) {
        //TODO: user=fh message='business rules are not specified'
    }
}
