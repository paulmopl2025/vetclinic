package com.example.vetclinic.cli.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMedicalRecordRequest {
    private LocalDateTime recordDate;
    private String diagnosis;
    private String treatment;
    private String notes;
    private BigDecimal weight;
    private BigDecimal temperature;
    private String vaccineAdministered;
    private Long petId;
    private Long vetId;
    private Long appointmentId;
}
