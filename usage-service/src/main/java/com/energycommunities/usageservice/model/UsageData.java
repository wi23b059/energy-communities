package com.energycommunities.usageservice.model;

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
@Table(name = "usage_data")
public class UsageData {

    @Id
    private LocalDateTime hour;

    private double communityProduced = 0;
    private double communityUsed = 0;
    private double gridUsed = 0;

    // Getters, setters, constructors

    public UsageData(LocalDateTime hour) {
        this.hour = hour;
    }
}