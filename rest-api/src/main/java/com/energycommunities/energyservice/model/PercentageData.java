package com.energycommunities.energyservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "percentage_data")
public class PercentageData {

    @Id
    @Column(name = "hour")
    private LocalDateTime hour;

    @Column(name = "community_depleted")
    private Double communityDepleted;

    @Column(name = "grid_portion")
    private Double gridPortion;
}