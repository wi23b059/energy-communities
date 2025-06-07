package com.energycommunities.usageservice.service;

import com.energycommunities.usageservice.model.EnergyMessage;
import com.energycommunities.usageservice.model.PercentageMessage;
import com.energycommunities.usageservice.model.UsageData;
import com.energycommunities.usageservice.repository.UsageDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

@Service
public class UsageAggregator {

    private final UsageDataRepository repository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${percentage.exchange}")
    private String percentageExchange;

    @Value("${percentage.routing-key}")
    private String percentageRoutingKey;

    public UsageAggregator(UsageDataRepository repository, RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void processMessage(EnergyMessage msg) {
        LocalDateTime hour = msg.getDatetime().withMinute(0).withSecond(0).withNano(0);
        UsageData data = repository.findById(hour).orElse(new UsageData(hour));

        if (msg.getType() == EnergyMessage.Type.PRODUCER) {
            data.setCommunityProduced(data.getCommunityProduced() + msg.getKwh());
        } else if (msg.getType() == EnergyMessage.Type.USER) {
            double available = data.getCommunityProduced() - data.getCommunityUsed();
            double fromCommunity = Math.min(available, msg.getKwh());
            double fromGrid = msg.getKwh() - fromCommunity;

            data.setCommunityUsed(data.getCommunityUsed() + fromCommunity);
            data.setGridUsed(data.getGridUsed() + fromGrid);
        }

        repository.save(data);

        double communityDepleted = data.getCommunityProduced() > 0
                ? (data.getCommunityUsed() / data.getCommunityProduced()) * 100
                : 0;

        double gridPortion = data.getGridUsed() > 0
                ? (data.getGridUsed() / (data.getCommunityUsed() + data.getGridUsed())) * 100
                : 0;

        PercentageMessage percentageMessage = new PercentageMessage(
                hour,
                communityDepleted,
                gridPortion
        );

        // Sende Nachricht an die Rabbit-Queue
        rabbitTemplate.convertAndSend(percentageExchange, percentageRoutingKey, percentageMessage);
    }
}