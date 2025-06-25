package com.energycommunities.communityproducer.producer;

import com.energycommunities.communityproducer.model.EnergyMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EnergyProducer {

    private final RabbitTemplate rabbitTemplate;
    private final RestTemplate restTemplate = new RestTemplate();
    private final Random random = new Random();

    @Value("${energy.weather-api-key}")
    private String apiKey;

    @Value("${energy.location}")
    private String location;

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.template.routing-key}")
    private String routingKey;

    @PostConstruct
    public void startEnergyLoop() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    sendEnergyData(); // Energie-Daten erzeugen und senden

                    // Zufälliges Warten zwischen 1 und 5 Sekunden
                    int delay = 1000 + random.nextInt(4001); // 1000 bis 5000 ms
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        thread.setDaemon(true); // Wird automatisch beendet, wenn alle normalen (nicht-Daemon-)Threads beendet sind
        thread.start(); // Starte den Hintergrund-Thread beim Start der Anwendung
    }

    // @Scheduled(fixedRateString = "${energy.interval}000")
    public void sendEnergyData() {
        double sunlightFactor = getSunlightFactor();  // 0.0 - 1.0
        double baseKwh = 0.045 + (0.045 * random.nextDouble());  // 0.002 - 0.004
        double kwh = baseKwh * sunlightFactor;

        EnergyMessage message = new EnergyMessage(
                "PRODUCER",                  // type
                "COMMUNITY",                 // association
                kwh,                       // kWh (z. B. aus Zufallsgenerator)
                LocalDateTime.now()         // Zeitstempel
        );
        rabbitTemplate.convertAndSend(exchange, routingKey, message);

        System.out.println("➡️ Sent producer message: " + message);
    }

    private double getSunlightFactor() {
        try {
            String url = String.format(
                    "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s",
                    location, apiKey
            );

            var json = restTemplate.getForObject(url, String.class);
            if (json != null && json.contains("\"clear\"")) {
                return 1.0; // sonnig
            } else if (json != null && json.contains("\"clouds\"")) {
                return 0.6;
            } else {
                return 0.3; // regen, schnee etc.
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0.5; // fallback
        }
    }
}