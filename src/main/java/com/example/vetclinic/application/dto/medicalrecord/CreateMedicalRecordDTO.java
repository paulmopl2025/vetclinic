package com.example.vetclinic.application.dto.medicalrecord;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateMedicalRecordDTO {
    @NotNull(message = "Record date is required")
    private LocalDateTime recordDate;

    private String diagnosis;
    private String treatment;
    private String notes;
    private BigDecimal weight;
    private BigDecimal temperature;
    private String vaccineAdministered;

    @NotNull(message = "Pet ID is required")
    private Long petId;

    @NotNull(message = "Vet ID is required")
    private Long vetId;

    private Long appointmentId;
}
