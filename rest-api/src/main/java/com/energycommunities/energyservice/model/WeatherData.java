package com.energycommunities.energyservice.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class WeatherData {
    private String city;
    private double temperature;
    private LocalDateTime timestamp;
}