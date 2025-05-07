package com.energycommunities.weatherservice.repository;

import com.energycommunities.weatherservice.model.WeatherData;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class WeatherRepository {
    private final Map<String, WeatherData> data = new HashMap<>();

    public WeatherData save(WeatherData weatherData) {
        data.put(weatherData.getCity(), weatherData);
        return weatherData;
    }

    public WeatherData findByCity(String city) {
        return data.get(city);
    }
}