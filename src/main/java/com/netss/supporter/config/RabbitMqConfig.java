package com.netss.supporter.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitMqConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqConfig.class);

    public final static String UPDATE_CAMPAIGN_MESSAGE_QUEUE = "UPDATE_CAMPAIGN_QUEUE";
    public final static String UPDATE_CAMPAIGN_MESSAGE_EXCHANGE = "UPDATE_CAMPAIGN_MESSAGE_EXCHANGE";

    @Bean
    public Exchange eventExchange() {
        return new TopicExchange(UPDATE_CAMPAIGN_MESSAGE_EXCHANGE);
    }

    @Bean
    public Queue queue() {
        return new Queue(UPDATE_CAMPAIGN_MESSAGE_QUEUE);
    }

    @Bean
    public Binding binding(Queue queue, Exchange eventExchange) {
        return BindingBuilder.bind(queue).to(eventExchange).with("*").noargs();
    }


}
