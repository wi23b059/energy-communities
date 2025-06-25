package com.energycommunities.communityproducer;

import com.energycommunities.communityproducer.model.EnergyMessage;
import com.energycommunities.communityproducer.producer.EnergyProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EnergyProducerTest {

    private RabbitTemplate rabbitTemplate;
    private EnergyProducer energyProducer;

    @BeforeEach
    void setUp() {
        rabbitTemplate = mock(RabbitTemplate.class);

        energyProducer = new EnergyProducer(rabbitTemplate);
        // Testdaten setzen
        energyProducer.getClass().getDeclaredFields();

        // Direkt Werte setzen (alternativ: Constructor erweitern oder Setter verwenden)
        setPrivateField(energyProducer, "exchange", "energy_exchange");
        setPrivateField(energyProducer, "routingKey", "energy_routing_key");
        setPrivateField(energyProducer, "apiKey", "05667723432b8d734b9c135fae9e147c");
        setPrivateField(energyProducer, "location", "Berlin");
    }

    @Test
    void testSendEnergyData_sendsMessage() {
        energyProducer.sendEnergyData();

        ArgumentCaptor<EnergyMessage> captor = ArgumentCaptor.forClass(EnergyMessage.class);
        verify(rabbitTemplate, times(1)).convertAndSend(eq("energy_exchange"), eq("energy_routing_key"), captor.capture());

        EnergyMessage sentMessage = captor.getValue();
        assertNotNull(sentMessage);
        assertEquals("PRODUCER", sentMessage.getType());
        assertEquals("COMMUNITY", sentMessage.getAssociation());
        assertTrue(sentMessage.getKwh() >= 0.0);
        assertNotNull(sentMessage.getDatetime());
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