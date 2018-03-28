package com.netss.supporter.integration.web;


import com.netss.supporter.config.FeignConfiguration;
import com.netss.supporter.domain.Campaign;
import feign.Headers;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient(name = "netss-campaign", url = "${application.integration.netss-campaign.url}", fallback = CampaignClientFallback.class,
configuration = FeignConfiguration.class)
public interface CampaignClient {

    String CAMPAIGN_ID_QUERY_PARAM = "campaignId";

    //TODO: user=fh message='review later, scalability issues if campaign grows'
    @Cacheable(value="campaign-team-by-id")
    @Headers("Content-Type: application/json")
    @RequestMapping(method = GET, value = "/campaigns/team/{teamId}")
    List<Campaign> getCampaignsByTeamId(@PathVariable("teamId") String teamId);

    @Headers("Content-Type: application/json")
    @RequestMapping(method = GET, value = "/campaigns")
    List<Campaign> getCampaignsById(@RequestParam(CAMPAIGN_ID_QUERY_PARAM) List<Long> campaignsId);
}
