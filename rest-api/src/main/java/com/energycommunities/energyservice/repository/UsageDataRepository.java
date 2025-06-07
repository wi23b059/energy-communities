package com.energycommunities.energyservice.repository;

import com.energycommunities.energyservice.model.UsageData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UsageDataRepository extends JpaRepository<UsageData, Long> {
    UsageData findTopByOrderByHourDesc();
    List<UsageData> findByHourBetween(LocalDateTime start, LocalDateTime end);
}