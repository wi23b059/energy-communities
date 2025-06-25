package com.energycommunities.communityuser.producer;

import com.energycommunities.communityuser.model.EnergyMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EnergyUser {

    private final RabbitTemplate rabbitTemplate;
    private final Random random = new Random();

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.template.routing-key}")
    private String routingKey;

    @PostConstruct
    public void startEnergyLoop() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    sendEnergyUsage(); // Energie-Verbrauch senden

                    // Zufälliges Warten zwischen 1 und 5 Sekunden
                    int delay = 1000 + random.nextInt(4001); // 1000 bis 5000 ms
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        thread.setDaemon(true);
        thread.start(); // Starte den Hintergrund-Thread beim Start der Anwendung
    }

    // @Scheduled(fixedRateString = "${energy.interval}000")
    public void sendEnergyUsage() {
        double kwh = generateUsageBasedOnTime();
        EnergyMessage message = new EnergyMessage(
                "USER",
                "COMMUNITY",
                kwh,
                LocalDateTime.now()
        );

        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        System.out.println("➡️ Sent user message: " + message);
    }

    private double generateUsageBasedOnTime() {
        LocalTime now = LocalTime.now();
        int hour = now.getHour();

        double base = 0.0005;
        double multiplier;

        if (hour >= 6 && hour <= 9) {
            multiplier = 3.0; // Morgenspitze
        } else if (hour >= 17 && hour <= 21) {
            multiplier = 2.5; // Abends
        } else if (hour >= 0 && hour <= 5) {
            multiplier = 0.7; // Nachts
        } else {
            multiplier = 1.0; // Normalverbrauch
        }

        return base * multiplier * (0.8 + 0.4 * random.nextDouble());
    }
}