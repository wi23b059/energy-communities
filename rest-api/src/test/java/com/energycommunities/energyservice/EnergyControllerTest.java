package com.energycommunities.energyservice;

import com.energycommunities.energyservice.model.PercentageData;
import com.energycommunities.energyservice.model.UsageData;
import com.energycommunities.energyservice.repository.PercentageDataRepository;
import com.energycommunities.energyservice.repository.UsageDataRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EnergyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PercentageDataRepository percentageDataRepository;

    @MockBean
    private UsageDataRepository usageDataRepository;

    @Test
    void testGetCurrentEnergyData_returnsData() throws Exception {
        PercentageData data = new PercentageData();
        data.setHour(LocalDateTime.of(2025, 6, 20, 14, 0));
        data.setCommunityDepleted(75.0);
        data.setGridPortion(25.0);

        Mockito.when(percentageDataRepository.findTopByOrderByHourDesc()).thenReturn(data);

        mockMvc.perform(get("/energy/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.community_depleted").value(75.0))
                .andExpect(jsonPath("$.grid_portion").value(25.0))
                .andExpect(jsonPath("$.hour").value("2025-06-20T14:00:00"));
    }

    @Test
    void testGetCurrentEnergyData_noData_returnsError() throws Exception {
        Mockito.when(percentageDataRepository.findTopByOrderByHourDesc()).thenReturn(null);

        mockMvc.perform(get("/energy/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("No data found"));
    }

    @Test
    void testGetHistoricalEnergyData_returnsList() throws Exception {
        UsageData usage1 = new UsageData();
        usage1.setHour(LocalDateTime.of(2025, 6, 20, 10, 0));
        usage1.setCommunityProduced(10.5);
        usage1.setCommunityUsed(8.0);
        usage1.setGridUsed(2.5);

        UsageData usage2 = new UsageData();
        usage2.setHour(LocalDateTime.of(2025, 6, 20, 11, 0));
        usage2.setCommunityProduced(12.0);
        usage2.setCommunityUsed(9.0);
        usage2.setGridUsed(3.0);

        Mockito.when(usageDataRepository.findByHourBetween(
                        LocalDateTime.of(2025, 6, 20, 9, 0),
                        LocalDateTime.of(2025, 6, 20, 12, 0)))
                .thenReturn(List.of(usage1, usage2));

        mockMvc.perform(get("/energy/historical")
                        .param("start", "2025-06-20T09:00:00")
                        .param("end", "2025-06-20T12:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].community_produced").value(10.5))
                .andExpect(jsonPath("$[1].grid_used").value(3.0));
    }
}