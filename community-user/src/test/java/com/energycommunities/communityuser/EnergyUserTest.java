package com.energycommunities.communityuser;

import com.energycommunities.communityuser.model.EnergyMessage;
import com.energycommunities.communityuser.producer.EnergyUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EnergyUserTest {
    private RabbitTemplate rabbitTemplate;
    private EnergyUser energyUser;

    @BeforeEach
    void setup() {
        rabbitTemplate = mock(RabbitTemplate.class);

        energyUser = new EnergyUser(rabbitTemplate);
        energyUser.getClass().getDeclaredFields();

        setPrivateField(energyUser, "exchange", "energy_exchange");
        setPrivateField(energyUser, "routingKey", "energy_routing_key");
    }

    @Test
    void sendEnergyUsage_sendsMessageToRabbitMQ() {
        energyUser.sendEnergyUsage();

        ArgumentCaptor<EnergyMessage> captor = ArgumentCaptor.forClass(EnergyMessage.class);
        verify(rabbitTemplate, times(1)).convertAndSend(eq("energy_exchange"), eq("energy_routing_key"), captor.capture());

        EnergyMessage sentMessage = captor.getValue();
        assertNotNull(sentMessage);
        assertEquals("USER", sentMessage.getType());
        assertEquals("COMMUNITY", sentMessage.getAssociation());
        assertTrue(sentMessage.getKwh() > 0);
        assertTrue(Math.abs(sentMessage.getDatetime().compareTo(LocalDateTime.now())) < 2);
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