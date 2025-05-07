package com.energycommunities.weatherservice;

import com.energycommunities.weatherservice.model.WeatherData;
import com.energycommunities.weatherservice.repository.WeatherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WeatherApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WeatherRepository repository;

    @Test
    void contextLoads() {
        // Tests if Spring context loads correctly
    }

    @Test
    void givenWeatherData_whenGetWeather_thenStatus200() throws Exception {
        // Arrange
        WeatherData data = new WeatherData();
        data.setCity("Vienna");
        data.setTemperature(22.5);
        repository.save(data);

        // Act & Assert
        mockMvc.perform(get("/weather/Vienna")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city", is("Vienna")))
                .andExpect(jsonPath("$.temperature", is(22.5)));
    }
}