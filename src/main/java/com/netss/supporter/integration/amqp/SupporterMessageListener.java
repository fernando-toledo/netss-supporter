package com.netss.supporter.integration.amqp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.netss.supporter.domain.Campaign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static com.netss.supporter.config.RabbitMqConfig.UPDATE_CAMPAIGN_MESSAGE_QUEUE;
import static java.util.stream.Collectors.toList;

@Component
public class SupporterMessageListener {

    private static final Logger log = LoggerFactory.getLogger(SupporterMessageListener.class);

    private ObjectMapper objectMapper;

    public SupporterMessageListener(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = UPDATE_CAMPAIGN_MESSAGE_QUEUE)
    public void receiveMessage(Message message) {
        log.info("Message from {} received", UPDATE_CAMPAIGN_MESSAGE_QUEUE);
        try {
            List<Campaign> campaigns = objectMapper.readValue(message.getBody(), new TypeReference<List<Campaign>>(){});
            List<Long> ids = campaigns.stream().map(c -> c.getId()).collect(toList());
            log.info("Message successfully processed... {}", ids);
        } catch (IOException e) {
            log.error("Fail to read message", e);
        }
    }

}
