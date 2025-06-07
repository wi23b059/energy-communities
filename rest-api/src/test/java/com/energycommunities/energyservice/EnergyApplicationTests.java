package com.energycommunities.energyservice;

import com.energycommunities.energyservice.model.EnergyData;
import com.energycommunities.energyservice.repository.EnergyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EnergyApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EnergyRepository repository;

    @Test
    void contextLoads() {
        // Basic test to ensure context loads
    }

    @Test
    void givenEnergyData_whenGetCurrentEnergy_thenStatus200() throws Exception {
        // Arrange
        EnergyData data = new EnergyData();
        data.setCommunityDepleted(100.0);
        data.setGridPortion(5.63);
        data.setCommunityProduced(18.05);
        data.setCommunityUsed(18.05);
        data.setGridUsed(1.076);
        data.setTimestamp(LocalDateTime.of(2025, 1, 10, 14, 0));
        repository.save(data);

        // Act & Assert
        mockMvc.perform(get("/energy/current")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.communityPool", is(100.0)))
                .andExpect(jsonPath("$.gridPortion", is(5.63)));
    }
}