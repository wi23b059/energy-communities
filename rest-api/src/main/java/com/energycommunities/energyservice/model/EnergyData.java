package com.energycommunities.energyservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EnergyData {
    @JsonProperty("community_pool")
    private double communityPool;

    @JsonProperty("grid_portion")
    private double gridPortion;

    @JsonProperty("community_produced")
    private double communityProduced;

    @JsonProperty("community_used")
    private double communityUsed;

    @JsonProperty("grid_used")
    private double gridUsed;

    private LocalDateTime timestamp;
}