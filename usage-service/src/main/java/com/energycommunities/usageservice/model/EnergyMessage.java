package com.energycommunities.usageservice.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EnergyMessage {
    public enum Type { PRODUCER, USER }
    public enum Association { COMMUNITY }

    private Type type;
    private Association association;
    private double kwh;
    private LocalDateTime datetime;

    // Getters, setters, constructors
}