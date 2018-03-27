package com.netss.supporter.integration.web;


import com.netss.supporter.config.FeignConfiguration;
import com.netss.supporter.domain.Campaign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient(name = "netss-campaign", url = "${httpClient.promotion-api.url}", fallback = CampaignClientFallback.class,
configuration = FeignConfiguration.class)
public interface CampaignClient {

    @RequestMapping(method = GET, value = "/campaigns/team/{teamId}")
    List<Campaign> getCampaignsByTeamId(@PathVariable("teamId") String teamId);

}
