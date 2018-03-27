package com.netss.supporter.service;

import com.google.common.collect.ImmutableMap;
import com.netss.supporter.domain.Campaign;
import com.netss.supporter.domain.Supporter;
import com.netss.supporter.domain.SupporterCampaign;
import com.netss.supporter.exception.SupporterNotFoundException;
import com.netss.supporter.integration.web.CampaignClient;
import com.netss.supporter.repository.SupporterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class SupporterService {

    private final SupporterRepository supporterRepository;
    private final CampaignClient campaignClient;

    public SupporterService(SupporterRepository supporterRepository, CampaignClient campaignClient) {
        this.supporterRepository = supporterRepository;
        this.campaignClient = campaignClient;
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
        return supporterRepository.save(supporter);
    }


    public List<Campaign> getSupporterCampaigns(Long id) {
        Supporter supporter = supporterRepository.findById(id).orElseThrow(SupporterNotFoundException::new);
        List<SupporterCampaign> supporterCampaigns = supporterRepository.getSupporterCampaignById(id);

        List<Long> campaingIds = supporterCampaigns.stream()
            .map( sc -> sc.getCampaignId())
            .collect(Collectors.toList());

        Map<String, Object> campaignQueryParameters = ImmutableMap.of(campaignClient.CAMPAIGN_ID_QUERY_PARAM, campaingIds);

        //TODO: user=fh message='change to get campaign only by the ids, not team'
        //List<Campaign> campaigns = campaignClient.getCampaignsByTeamId(supporter.getTeamId());
        List<Campaign> campaigns = campaignClient.getCampaignsById(campaignQueryParameters);

        return null;
    }
}
