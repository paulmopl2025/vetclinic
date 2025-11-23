package com.example.vetclinic.cli.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecord {
    private Long id;
    private LocalDateTime recordDate;
    private String diagnosis;
    private String treatment;
    private String notes;
    private BigDecimal weight;
    private BigDecimal temperature;
    private String vaccineAdministered;
    private Long petId;
    private String petName;
    private Long vetId;
    private String vetName;
    private Long appointmentId;
}
