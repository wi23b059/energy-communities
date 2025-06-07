package com.energycommunities.currentpercentageservice.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PercentageMessage {
    private LocalDateTime hour;
    private double communityDepleted;
    private double gridPortion;
}