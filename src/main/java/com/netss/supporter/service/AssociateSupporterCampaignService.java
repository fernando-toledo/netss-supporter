package com.netss.supporter.service;


import com.netss.supporter.domain.Supporter;
import com.netss.supporter.domain.SupporterCampaign;
import com.netss.supporter.integration.web.CampaignClient;
import com.netss.supporter.repository.SupporterCampaignsRepository;
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

    public SupporterCampaign associate(Supporter officialSupporter) {

        return null;
    }
}
