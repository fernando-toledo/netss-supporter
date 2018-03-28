package com.netss.supporter.component.rest;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netss.supporter.component.util.CampaignsBuildHelper;
import com.netss.supporter.component.util.SupporterBuildHelper;
import com.netss.supporter.domain.Campaign;
import com.netss.supporter.domain.Supporter;
import com.netss.supporter.domain.SupporterCampaign;
import com.netss.supporter.integration.amqp.SupporterMessageListener;
import com.netss.supporter.integration.web.CampaignClient;
import com.netss.supporter.repository.SupporterCampaignRepository;
import com.netss.supporter.repository.SupporterRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
public class SupporterRestTest {

    public static final String CAMPAIGNS_PATH = "/campaigns";
    private final String SUPPORTER_API_BASE_PATH = "/supporters";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private SupporterRepository supporterRepository;

    @Autowired
    private SupporterCampaignRepository supporterCampaignRepository;

    //=========================
    //INTEGRATION MOCKED BEANS:
    //=========================
    // These integration beans are mocked to facilitate integration issues.

    @MockBean
    private SupporterMessageListener listener;

    @MockBean
    private CampaignClient campaignClient;

    @Test
    public void shouldGetAllSupporter() throws Exception {

        supporterRepository.deleteAll();

        mvc.perform(MockMvcRequestBuilders.post(SUPPORTER_API_BASE_PATH)
            .content(mapper.writeValueAsString(SupporterBuildHelper.supporterMaria()))
            .contentType(APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated());

        mvc.perform(MockMvcRequestBuilders.post(SUPPORTER_API_BASE_PATH)
            .content(mapper.writeValueAsString(SupporterBuildHelper.supporterTiao()))
            .contentType(APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated());

        List<Supporter> Supporters = supporterRepository.findAll();

        assertEquals(2, Supporters.size());
    }

    @Test
    public void shouldCreateUniqueSupporter() throws Exception {

        supporterRepository.deleteAll();

        mvc.perform(MockMvcRequestBuilders.post(SUPPORTER_API_BASE_PATH)
            .content(mapper.writeValueAsString(SupporterBuildHelper.supporterMaria()))
            .contentType(APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated());

        List<Supporter> Supporters = supporterRepository.findAll();

        assertEquals(1, Supporters.size());
        assertEquals("maria", Supporters.get(0).getName());
    }

    @Test
    public void shouldDeleteUniqueSupporter() throws Exception {

        supporterRepository.deleteAll();

        MvcResult createResult = mvc.perform(MockMvcRequestBuilders.post(SUPPORTER_API_BASE_PATH)
            .content(mapper.writeValueAsString(SupporterBuildHelper.supporterMaria()))
            .contentType(APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn();

        Supporter created = mapper.readValue(createResult.getResponse().getContentAsString() , Supporter.class);

        List<Supporter> SupportersBeforeDeletion = supporterRepository.findAll();
        assertEquals(1, SupportersBeforeDeletion.size());

        mvc.perform(MockMvcRequestBuilders.delete(String.format("/supporters/%s", created.getId()))
            .content(mapper.writeValueAsString(SupporterBuildHelper.supporterMaria()))
            .contentType(APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        List<Supporter> SupportersAfterDeletion = supporterRepository.findAll();
        assertEquals(0, SupportersAfterDeletion.size());

    }

    @Test
    public void shouldGetUniqueSupporter() throws Exception {

        supporterRepository.deleteAll();

        MvcResult createResult = mvc.perform(MockMvcRequestBuilders.post(SUPPORTER_API_BASE_PATH)
            .content(mapper.writeValueAsString(SupporterBuildHelper.supporterMaria()))
            .contentType(APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn();

        Supporter created = mapper.readValue(createResult.getResponse().getContentAsString() , Supporter.class);

        List<Supporter> supporters = supporterRepository.findAll();
        assertEquals(1, supporters.size());

        MvcResult getResult = mvc.perform(MockMvcRequestBuilders.get(String.format("/supporters/%s", supporters.stream().findFirst().get().getId()))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        Supporter getSupporter = mapper.readValue(getResult.getResponse().getContentAsString() , Supporter.class);

        List<Supporter> SupportersAfterDeletion = supporterRepository.findAll();
        assertEquals(1, SupportersAfterDeletion.size());
        assertEquals(getSupporter, created);

    }

    private HttpEntity<String> buildBasicHttpEntity(String jsonBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
        return entity;
    }

    @Test
    public void shouldUpdateUniqueSupporter() throws Exception {

        supporterRepository.deleteAll();

        MvcResult createResult = mvc.perform(MockMvcRequestBuilders.post(SUPPORTER_API_BASE_PATH)
            .content(mapper.writeValueAsString(SupporterBuildHelper.supporterMaria()))
            .contentType(APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn();

        Supporter created = mapper.readValue(createResult.getResponse().getContentAsString() , Supporter.class);

        List<Supporter> SupportersBeforeUpdate = supporterRepository.findAll();
        assertEquals(1, SupportersBeforeUpdate.size());

        MvcResult updateResult = mvc.perform(MockMvcRequestBuilders.put(SUPPORTER_API_BASE_PATH)
            .content(mapper.writeValueAsString(SupporterBuildHelper.supporterMariaUpdate(created.getId())))
            .contentType(APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        Supporter updated = mapper.readValue(updateResult.getResponse().getContentAsString() , Supporter.class);

        List<Supporter> SupportersAfterUpdate = supporterRepository.findAll();

        Assert.assertEquals(1, SupportersAfterUpdate.size());
        Assert.assertEquals(updated, SupporterBuildHelper.supporterMariaUpdate(created.getId()));
    }

    @Test
    public void shouldAssociateSupporterWithCampaignDuringSupporterCreation() throws Exception {

        Mockito
            .when(campaignClient.getCampaignsById(ArgumentMatchers.isNotNull()))
            .thenReturn(Arrays.asList(CampaignsBuildHelper.campaignBlackFriday()));
        Mockito
            .when(campaignClient.getCampaignsByTeamId(ArgumentMatchers.isNotNull()))
            .thenReturn(Arrays.asList(CampaignsBuildHelper.campaignBlackFriday()));

        supporterRepository.deleteAll();

        MvcResult createResult = mvc.perform(MockMvcRequestBuilders.post(SUPPORTER_API_BASE_PATH)
            .content(mapper.writeValueAsString(SupporterBuildHelper.supporterMaria()))
            .contentType(APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn();

        Supporter createdSupporter = mapper.readValue(createResult.getResponse().getContentAsString() , Supporter.class);

        String getSupporterCampaignURL = getSupporterCampaignURL(createdSupporter);

        MvcResult getSupporterCampaigns = mvc.perform(MockMvcRequestBuilders.get(getSupporterCampaignURL)
            .contentType(APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        List<Campaign> supporterCampaigns = mapper.readValue(
            getSupporterCampaigns.getResponse().getContentAsString(),
            new TypeReference<List<Campaign>>(){});

        List<SupporterCampaign> supporterCampaignsFromDatabase = supporterRepository.getSupporterCampaignById(createdSupporter.getId());

        Assert.assertEquals(supporterCampaigns.size(), 1);
        Assert.assertEquals(supporterCampaignsFromDatabase.size(), 1);

        Assert.assertEquals(
            supporterCampaigns.stream().findFirst().get().getId(),
            supporterCampaignsFromDatabase.stream().findFirst().get().getCampaignId());
    }

    @Test
    public void shouldAssociateSupporterWithCampaignAfterSupporterCreation() throws Exception {

        Mockito
            .when(campaignClient.getCampaignsById(ArgumentMatchers.isNotNull()))
            .thenReturn(Collections.emptyList());
        Mockito
            .when(campaignClient.getCampaignsByTeamId(ArgumentMatchers.isNotNull()))
            .thenReturn(Collections.emptyList());

        supporterRepository.deleteAll();
        supporterCampaignRepository.deleteAll();

        MvcResult createResult = mvc.perform(MockMvcRequestBuilders.post(SUPPORTER_API_BASE_PATH)
            .content(mapper.writeValueAsString(SupporterBuildHelper.supporterMaria()))
            .contentType(APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn();

        Supporter createdSupporter = mapper.readValue(createResult.getResponse().getContentAsString() , Supporter.class);

        String getSupporterCampaignURL = getSupporterCampaignURL(createdSupporter);

        MvcResult getSupporterCampaigns = mvc.perform(MockMvcRequestBuilders.get(getSupporterCampaignURL)
            .contentType(APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        List<Campaign> supporterCampaigns = mapper.readValue(
            getSupporterCampaigns.getResponse().getContentAsString(),
            new TypeReference<List<Campaign>>(){});

        List<SupporterCampaign> supporterCampaignsFromDatabase = supporterRepository.getSupporterCampaignById(createdSupporter.getId());

        Assert.assertEquals(supporterCampaigns.size(), 0);
        Assert.assertEquals(supporterCampaignsFromDatabase.size(), 0);

        Mockito
            .when(campaignClient.getCampaignsById(ArgumentMatchers.isNotNull()))
            .thenReturn(Arrays.asList(CampaignsBuildHelper.campaignBlackFriday()));
        Mockito
            .when(campaignClient.getCampaignsByTeamId(ArgumentMatchers.isNotNull()))
            .thenReturn(Arrays.asList(CampaignsBuildHelper.campaignBlackFriday()));

        mvc.perform(MockMvcRequestBuilders.post(getSupporterAssociateCampaignURL(createdSupporter))
            .contentType(APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        MvcResult getSupporterCampaignsAfterAssociation = mvc.perform(MockMvcRequestBuilders.get(getSupporterCampaignURL)
            .contentType(APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        List<Campaign> supporterCampaignsAfterAssociation = mapper.readValue(
            getSupporterCampaignsAfterAssociation.getResponse().getContentAsString(),
            new TypeReference<List<Campaign>>(){});

        List<SupporterCampaign> supporterCampaignsFromDatabaseAfterAssociation = supporterRepository.getSupporterCampaignById(createdSupporter.getId());

        Assert.assertEquals(
            supporterCampaignsAfterAssociation.stream().findFirst().get().getId(),
            supporterCampaignsFromDatabaseAfterAssociation.stream().findFirst().get().getCampaignId());
    }

    private String getSupporterCampaignURL(Supporter createdSupporter) {
        return new StringBuilder()
                .append(SUPPORTER_API_BASE_PATH)
                .append("/")
                .append(createdSupporter.getId())
                .append(CAMPAIGNS_PATH)
                .toString();
    }

    private String getSupporterAssociateCampaignURL(Supporter createdSupporter) {
        return new StringBuilder()
            .append(SUPPORTER_API_BASE_PATH)
            .append("/")
            .append(createdSupporter.getId())
            .append(CAMPAIGNS_PATH)
            .append(":associate")
            .toString();
    }
}
