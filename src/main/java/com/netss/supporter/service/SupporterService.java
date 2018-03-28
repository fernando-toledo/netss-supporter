package com.netss.supporter.service;

import com.google.common.collect.ImmutableMap;
import com.netss.supporter.domain.Campaign;
import com.netss.supporter.domain.Supporter;
import com.netss.supporter.domain.SupporterCampaign;
import com.netss.supporter.exception.SupporterNotFoundException;
import com.netss.supporter.integration.web.CampaignClient;
import com.netss.supporter.repository.SupporterCampaignRepository;
import com.netss.supporter.repository.SupporterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class SupporterService {

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
        associateSupporterWithCampaign(createdSupporter);
        return createdSupporter;
    }

    private List<Campaign> associateSupporterWithCampaign(Supporter createdSupporter) {
        List<Campaign> campaigns = campaignClient.getCampaignsByTeamId(createdSupporter.getTeamId());

        if(campaigns.isEmpty())
            return campaigns;

        List<SupporterCampaign> supporterCampaigns = campaigns
            .stream()
            .map( campaign -> {
                SupporterCampaign s = new SupporterCampaign();
                s.setCampaignId(campaign.getId());
                s.setSupporterId(createdSupporter.getId());
                return s;
            })
            .collect(Collectors.toList());

        //TODO: user=fh message='add relationship between supporter and supporter-campaings entities'
        supporterCampaignRepository.saveAll(supporterCampaigns);
        return campaigns;
    }


    public List<Campaign> getSupporterCampaigns(Long id) {
        List<SupporterCampaign> supporterCampaigns = supporterRepository.getSupporterCampaignById(id);

        List<Long> campaignIds = supporterCampaigns.stream()
            .map( sc -> sc.getCampaignId())
            .collect(Collectors.toList());

        Map<String, Object> campaignQueryParameters = ImmutableMap.of(campaignClient.CAMPAIGN_ID_QUERY_PARAM, campaignIds);
        List<Campaign> campaigns = campaignClient.getCampaignsById(campaignQueryParameters);

        return campaigns;
    }

    public Optional<List<Campaign>> associate(Long id) {

        Supporter supporter = supporterRepository
            .findById(id)
            .orElseThrow(SupporterNotFoundException::new);

        return Optional.of(associateSupporterWithCampaign(supporter));
    }

    public void updateSupporterCampaigns(List<Long> campaignIds) {
        //TODO: user=fh message='business rules are not specified'
    }
}
