package com.energycommunities.currentpercentageservice.repository;

import com.energycommunities.currentpercentageservice.model.PercentageData;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

public interface PercentageDataRepository extends JpaRepository<PercentageData, LocalDateTime> {
}