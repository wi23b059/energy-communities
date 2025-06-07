package com.energycommunities.communityproducer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnergyMessage implements Serializable {
    private String type = "PRODUCER";
    private String association = "COMMUNITY";
    private double kwh;
    private LocalDateTime datetime;
}