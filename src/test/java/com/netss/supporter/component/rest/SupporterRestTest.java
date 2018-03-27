package com.netss.supporter.component.rest;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netss.supporter.component.util.CampaignsBuildHelper;
import com.netss.supporter.component.util.SupporterBuildHelper;
import com.netss.supporter.domain.Supporter;
import com.netss.supporter.domain.SupporterCampaign;
import com.netss.supporter.integration.amqp.SupporterMessageListener;
import com.netss.supporter.integration.web.CampaignClient;
import com.netss.supporter.repository.SupporterRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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
    public void shouldAssociateSupporterWithCampaignDuringUserCreation() throws Exception {

        Mockito
            .when(campaignClient.getCampaignsByTeamId(SupporterBuildHelper.supporterMaria().getTeamId()))
            .thenReturn(Arrays.asList(CampaignsBuildHelper.campaignBlackFriday()));

        supporterRepository.deleteAll();

        MvcResult createResult = mvc.perform(MockMvcRequestBuilders.post(SUPPORTER_API_BASE_PATH)
            .content(mapper.writeValueAsString(SupporterBuildHelper.supporterMaria()))
            .contentType(APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn();

        Supporter createdSupporter = mapper.readValue(createResult.getResponse().getContentAsString() , Supporter.class);

        String getSupporterCampaignURL = new StringBuilder()
            .append(SUPPORTER_API_BASE_PATH)
            .append("/")
            .append(SupporterBuildHelper.supporterMaria().getId())
            .append(CAMPAIGNS_PATH)
            .toString();

        MvcResult getSupporterCampaigns = mvc.perform(MockMvcRequestBuilders.get(getSupporterCampaignURL)
            .contentType(APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        List<SupporterCampaign> supporterCampaigns = mapper.readValue(
            getSupporterCampaigns.getResponse().getContentAsString(),
            new TypeReference<List<Supporter>>(){});

        List<SupporterCampaign> supporterCampaignsFromDatabase = supporterRepository.getSupporterCampaignById(createdSupporter.getId());

        Assert.assertEquals(supporterCampaigns.size(), 1);
        Assert.assertEquals(supporterCampaignsFromDatabase.size(), 1);

        Assert.assertEquals(supporterCampaigns.stream().findFirst().get(), supporterCampaignsFromDatabase.stream().findFirst().get());
    }
}
