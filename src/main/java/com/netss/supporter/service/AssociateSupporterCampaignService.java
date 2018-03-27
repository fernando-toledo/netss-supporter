package com.netss.supporter.service;


import com.netss.supporter.domain.Supporter;
import com.netss.supporter.domain.SupporterCampaigns;
import com.netss.supporter.integration.web.CampaignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssociateSupporterCampaignService {

    private static final Logger log = LoggerFactory.getLogger(AssociateSupporterCampaignService.class);

    @Autowired
    private CampaignClient campaignClient;

    @Autowired
    private SupporterCampaignsRepository supporterCampaignsRepository;

    public SupporterCampaigns associate(Supporter officialSupporter) {

        return null;
    }
}
