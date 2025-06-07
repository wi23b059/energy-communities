package com.energycommunities.energyservice.controller;

import com.energycommunities.energyservice.model.PercentageData;
import com.energycommunities.energyservice.model.UsageData;
import com.energycommunities.energyservice.repository.PercentageDataRepository;
import com.energycommunities.energyservice.repository.UsageDataRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/energy")
@CrossOrigin
public class EnergyController {
    private final PercentageDataRepository percentageDataRepository;
    private final UsageDataRepository usageDataRepository;

    public EnergyController(PercentageDataRepository percentageDataRepository, UsageDataRepository usageDataRepository) {
        this.percentageDataRepository = percentageDataRepository;
        this.usageDataRepository = usageDataRepository;
    }

    /**
     * Gibt den aktuellen Anteil der Community und des Grids zurück
     */
    @GetMapping("/current")
    public Map<String, Object> getCurrentEnergyData() {
        PercentageData latest = percentageDataRepository.findTopByOrderByHourDesc();

        if (latest == null) {
            return Map.of("error", "No data found");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("hour", latest.getHour());
        response.put("community_depleted", latest.getCommunityDepleted());
        response.put("grid_portion", latest.getGridPortion());

        return response;
    }


    /**
     * Gibt die historischen Daten für einen bestimmten Zeitraum zurück
     */
    @GetMapping("/historical")
    public List<Map<String, Object>> getHistoricalEnergyData(
            @RequestParam("start") String start,
            @RequestParam("end") String end) {

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime startDateTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(end, formatter);

        List<UsageData> usageList = usageDataRepository.findByHourBetween(startDateTime, endDateTime);

        return usageList.stream().map(u -> {
            Map<String, Object> entry = new HashMap<>();
            entry.put("timestamp", u.getHour());
            entry.put("community_produced", u.getCommunityProduced());
            entry.put("community_used", u.getCommunityUsed());
            entry.put("grid_used", u.getGridUsed());
            return entry;
        }).collect(Collectors.toList());
    }
}