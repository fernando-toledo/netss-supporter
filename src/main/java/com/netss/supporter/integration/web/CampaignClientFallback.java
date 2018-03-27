package com.netss.supporter.integration.web;

import com.netss.supporter.domain.Campaign;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Collections.emptyList;

@Component
public class CampaignClientFallback implements CampaignClient {

    @Override
    public List<Campaign> getCampaignsByTeamId(String itemId) {
        return emptyList();
    }

}
