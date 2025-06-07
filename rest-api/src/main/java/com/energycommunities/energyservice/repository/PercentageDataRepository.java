package com.energycommunities.energyservice.repository;

import com.energycommunities.energyservice.model.PercentageData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PercentageDataRepository extends JpaRepository<PercentageData, Long> {
    PercentageData findTopByOrderByHourDesc();
    List<PercentageData> findByHourBetween(LocalDateTime start, LocalDateTime end);
}