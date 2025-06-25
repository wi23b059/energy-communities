package com.energycommunities.usageservice;

import com.energycommunities.usageservice.model.EnergyMessage;
import com.energycommunities.usageservice.model.PercentageMessage;
import com.energycommunities.usageservice.model.UsageData;
import com.energycommunities.usageservice.repository.UsageDataRepository;
import com.energycommunities.usageservice.service.UsageAggregator;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.energycommunities.usageservice.model.EnergyMessage.Type;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UsageAggregatorTest {
    @Test
    void testProcessProducerMessage() {
        // Setup
        UsageDataRepository repository = mock(UsageDataRepository.class);
        RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);

        UsageAggregator aggregator = new UsageAggregator(repository, rabbitTemplate);
        aggregator.getClass().getDeclaredFields();

        setPrivateField(aggregator, "percentageExchange", "percentage_exchange");
        setPrivateField(aggregator, "percentageRoutingKey", "percentage_routing_key");
        // Set exchange & routing key manually (normally injected)
        //aggregator.percentageExchange = "test_exchange";
        //aggregator.percentageRoutingKey = "test_routing_key";

        LocalDateTime time = LocalDateTime.of(2024, 1, 1, 10, 45);
        EnergyMessage message = new EnergyMessage();
        message.setType(Type.PRODUCER);
        message.setDatetime(time);
        message.setKwh(100);

        when(repository.findById(any())).thenReturn(Optional.empty());

        // Act
        aggregator.processMessage(message);

        // Verify saved UsageData
        ArgumentCaptor<UsageData> usageCaptor = ArgumentCaptor.forClass(UsageData.class);
        verify(repository).save(usageCaptor.capture());

        UsageData saved = usageCaptor.getValue();
        assertEquals(100, saved.getCommunityProduced());
        assertEquals(0, saved.getCommunityUsed());
        assertEquals(0, saved.getGridUsed());

        // Verify RabbitTemplate message
        verify(rabbitTemplate).convertAndSend(eq("percentage_exchange"), eq("percentage_routing_key"), any(PercentageMessage.class));
    }

    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}