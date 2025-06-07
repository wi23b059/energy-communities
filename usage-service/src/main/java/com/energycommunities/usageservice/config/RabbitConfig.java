package com.energycommunities.usageservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.MessageConverter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("com.energycommunities.communityproducer.model.EnergyMessage",
                com.energycommunities.usageservice.model.EnergyMessage.class);

        typeMapper.setIdClassMapping(idClassMapping);
        converter.setJavaTypeMapper(typeMapper);

        return converter;
    }

    @Bean
    public Queue percentageQueue() {
        return new Queue("percentage_queue", true);
    }

    @Bean
    public DirectExchange percentageExchange() {
        return new DirectExchange("percentage_exchange");
    }

    @Bean
    public Binding percentageBinding() {
        return BindingBuilder
                .bind(percentageQueue())
                .to(percentageExchange())
                .with("percentage_routing_key");
    }
}