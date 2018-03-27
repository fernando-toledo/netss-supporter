package com.netss.supporter.component.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.netss.supporter.component.util.SupporterBuildHelper;
import com.netss.supporter.domain.Supporter;
import com.netss.supporter.integration.amqp.SupporterMessageListener;
import com.netss.supporter.repository.SupporterRepository;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SupporterRestTest {

    public static final String HTTP_LOCALHOST = "http://localhost:";
    private static TestRestTemplate test;
    private final String SUPPORTER_API_BASE_PATH = "/supporters";
    private final TestRestTemplate testRestTemplate = new TestRestTemplate();

    @Value("${local.server.port}")
    private int localPort;

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

    @BeforeClass
    public static void setup(){
        test = new TestRestTemplate();
    }

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

        ResponseEntity<Supporter> responseEntity = test.exchange(
            String.format(HTTP_LOCALHOST + "%d%s", localPort, SUPPORTER_API_BASE_PATH),
            HttpMethod.POST,
            buildBasicHttpEntity(mapper.writeValueAsString(SupporterBuildHelper.supporterMaria())),
            new ParameterizedTypeReference<Supporter>() {}
        );

        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);

        Supporter createdSupporter = responseEntity.getBody();

        List<Supporter> supporters = supporterRepository.findAll();

        assertEquals(1, supporters.size());

        mvc.perform(MockMvcRequestBuilders.delete(String.format("/supporters/%s", createdSupporter.getId()))
            .contentType(APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNoContent())
            .andReturn();

        List<Supporter> SupportersAfterDeletion = supporterRepository.findAll();
        assertEquals(0, SupportersAfterDeletion.size());

    }

    @Test
    public void shouldGetUniqueSupporter() throws Exception {

        supporterRepository.deleteAll();

        ResponseEntity<Supporter> responseEntity = test.exchange(
            String.format(HTTP_LOCALHOST + "%d%s", localPort, SUPPORTER_API_BASE_PATH),
            HttpMethod.POST,
            buildBasicHttpEntity(mapper.writeValueAsString(SupporterBuildHelper.supporterMaria())),
            new ParameterizedTypeReference<Supporter>() {}
        );

        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);

        Supporter createdSupporter = responseEntity.getBody();

        List<Supporter> supporters = supporterRepository.findAll();
        assertEquals(1, supporters.size());

        MvcResult getResult = mvc.perform(MockMvcRequestBuilders
            .get(String.format("/supporters/%s", supporters.stream().findFirst().get().getId()))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        Supporter getSupporter = mapper.readValue(getResult.getResponse().getContentAsString() , Supporter.class);

        List<Supporter> SupportersAfterDeletion = supporterRepository.findAll();
        assertEquals(1, SupportersAfterDeletion.size());
        assertEquals(getSupporter, createdSupporter);

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

        ResponseEntity<Supporter> responseEntity = test.exchange(
            String.format(HTTP_LOCALHOST + "%d%s", localPort, SUPPORTER_API_BASE_PATH),
            HttpMethod.POST,
            buildBasicHttpEntity(mapper.writeValueAsString(SupporterBuildHelper.supporterMaria())),
            new ParameterizedTypeReference<Supporter>() {}
        );

        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);

        List<Supporter> supporters = supporterRepository.findAll();
        assertEquals(1, supporters.size());

        Supporter createdSupporter = responseEntity.getBody();

        MvcResult updateResult = mvc.perform(MockMvcRequestBuilders.put(SUPPORTER_API_BASE_PATH)
            .content(mapper.writeValueAsString(SupporterBuildHelper.supporterMariaUpdate(createdSupporter.getId())))
            .contentType(APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        Supporter updated = mapper.readValue(updateResult.getResponse().getContentAsString() , Supporter.class);

        List<Supporter> SupportersAfterUpdate = supporterRepository.findAll();

        Assert.assertEquals(1, SupportersAfterUpdate.size());
        Assert.assertEquals(updated, SupporterBuildHelper.supporterMariaUpdate(createdSupporter.getId()));
    }
}
