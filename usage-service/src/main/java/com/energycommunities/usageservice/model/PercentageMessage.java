package com.energycommunities.usageservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PercentageMessage {
    private LocalDateTime hour;
    private double communityDepleted;
    private double gridPortion;
}