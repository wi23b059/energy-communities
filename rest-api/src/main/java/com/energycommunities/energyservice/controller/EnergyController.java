package com.energycommunities.energyservice.controller;

import com.energycommunities.energyservice.model.EnergyData;
import com.energycommunities.energyservice.repository.EnergyRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/energy")
@CrossOrigin
public class EnergyController {
    private final EnergyRepository energyRepository;

    public EnergyController(EnergyRepository energyRepository) {
        this.energyRepository = energyRepository;
    }

    /**
     * Gibt den aktuellen Anteil der Community und des Grids zurück
     */
    @GetMapping("/current")
    public EnergyData getCurrentEnergyData() {
        return energyRepository.findLatest();
    }

    /**
     * Gibt die historischen Daten für einen bestimmten Zeitraum zurück
     */
    @GetMapping("/historical")
    public Map<String, EnergyData> getHistoricalEnergyData(
            @RequestParam("start") String start,
            @RequestParam("end") String end) {

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime startDateTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(end, formatter);

        return energyRepository.findByTimeRange(startDateTime, endDateTime);
    }

    /**
     * Statische Daten setzen, um sie für die Tests zu speichern
     */
    @PostMapping("/setTestData")
    public EnergyData setTestData(@RequestBody EnergyData energyData) {
        return energyRepository.save(energyData);
    }
}