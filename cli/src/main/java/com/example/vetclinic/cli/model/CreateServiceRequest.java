package com.example.vetclinic.cli.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateServiceRequest {
    private String name;
    private String description;
    private ServiceType serviceType;
    private BigDecimal baseCost;
    private Integer estimatedDurationMinutes;
}
