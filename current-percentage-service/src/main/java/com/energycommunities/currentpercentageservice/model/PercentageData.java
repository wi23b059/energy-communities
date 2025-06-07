package com.energycommunities.currentpercentageservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "percentage_data")
public class PercentageData {

    @Id
    private LocalDateTime hour;

    private double communityDepleted;
    private double gridPortion;
}