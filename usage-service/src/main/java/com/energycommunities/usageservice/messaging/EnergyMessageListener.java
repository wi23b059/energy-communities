package com.energycommunities.usageservice.messaging;

import com.energycommunities.usageservice.model.EnergyMessage;
import com.energycommunities.usageservice.service.UsageAggregator;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EnergyMessageListener {

    private final UsageAggregator aggregator;

    public EnergyMessageListener(UsageAggregator aggregator) {
        this.aggregator = aggregator;
    }

    @RabbitListener(queues = "energy_queue") // use your queue name
    public void receiveMessage(EnergyMessage msg) {
        aggregator.processMessage(msg);
    }
}