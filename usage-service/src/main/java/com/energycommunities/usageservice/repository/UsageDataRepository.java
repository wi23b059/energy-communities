package com.energycommunities.usageservice.repository;

import com.energycommunities.usageservice.model.UsageData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface UsageDataRepository extends JpaRepository<UsageData, LocalDateTime> {

}