package com.energycommunities.weatherservice.controller;

import com.energycommunities.weatherservice.model.WeatherData;
import com.energycommunities.weatherservice.repository.WeatherRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weather")
@CrossOrigin
public class WeatherController {
    private final WeatherRepository repository;

    public WeatherController(WeatherRepository repository) {
        this.repository = repository;
    }

    /**
     * Returns the current temperature for a given city.
     * GET method is used since no data is modified.
     */
    @GetMapping("/{city}")
    public WeatherData getTemperature(@PathVariable String city) {
        return repository.findByCity(city);
    }

    /**
     * Weather stations use this endpoint to send new temperature data.
     * POST method is used to submit new data.
     */
    @PostMapping
    public WeatherData submitTemperature(@RequestBody WeatherData data) {
        return repository.save(data);
    }
}