package com.energycommunities.energyservice.repository;

import com.energycommunities.energyservice.model.EnergyData;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Repository
public class EnergyRepository {
    private final Map<String, EnergyData> data = new HashMap<>();

    // Speichern der Daten
    public EnergyData save(EnergyData energyData) {
        data.put(energyData.getTimestamp().toString(), energyData);  // Wir verwenden Timestamp als Key
        return energyData;
    }

    // Abrufen der aktuellen Werte
    public EnergyData findLatest() {
        return data.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);  // Gibt null zurück, falls keine Daten vorhanden sind
    }

    // Abrufen der historischen Werte für einen bestimmten Zeitraum
    public Map<String, EnergyData> findByTimeRange(LocalDateTime start, LocalDateTime end) {
        Map<String, EnergyData> result = new HashMap<>();
        data.forEach((key, value) -> {
            if (value.getTimestamp().isAfter(start) && value.getTimestamp().isBefore(end)) {
                result.put(key, value);
            }
        });
        return result;
    }
}