package com.energycommunities.communityuser.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnergyMessage implements Serializable {
    private String type;
    private String association;
    private double kwh;
    private LocalDateTime datetime;
}