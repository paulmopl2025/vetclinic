package com.example.vetclinic.application.dto.medicalrecord;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UpdateMedicalRecordDTO {
    private LocalDateTime recordDate;
    private String diagnosis;
    private String treatment;
    private String notes;
    private BigDecimal weight;
    private BigDecimal temperature;
    private String vaccineAdministered;
}
