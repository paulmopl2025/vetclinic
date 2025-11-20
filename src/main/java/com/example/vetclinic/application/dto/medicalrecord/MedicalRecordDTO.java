package com.example.vetclinic.application.dto.medicalrecord;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MedicalRecordDTO {
    private Long id;
    private LocalDateTime recordDate;
    private String diagnosis;
    private String treatment;
    private String notes;
    private BigDecimal weight;
    private BigDecimal temperature;
    private String vaccineAdministered;

    // Pet information
    private Long petId;
    private String petName;

    // Vet information
    private Long vetId;
    private String vetName;

    // Appointment information (optional)
    private Long appointmentId;
}
