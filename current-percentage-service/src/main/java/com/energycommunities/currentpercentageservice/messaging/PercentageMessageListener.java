package com.energycommunities.currentpercentageservice.messaging;

import com.energycommunities.currentpercentageservice.model.PercentageData;
import com.energycommunities.currentpercentageservice.model.PercentageMessage;
import com.energycommunities.currentpercentageservice.repository.PercentageDataRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PercentageMessageListener {

    private final PercentageDataRepository repository;

    public PercentageMessageListener(PercentageDataRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "percentage_queue")
    public void receive(PercentageMessage message) {
        PercentageData data = new PercentageData(
                message.getHour(),
                message.getCommunityDepleted(),
                message.getGridPortion()
        );
        repository.save(data);
        System.out.println("✅ Received & saved percentage: " + data);
    }
}