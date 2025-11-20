package com.example.vetclinic.application.dto.appointment;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateAppointmentDTO {
    @NotNull(message = "Appointment date is required")
    @Future(message = "Appointment date must be in the future")
    private LocalDateTime appointmentDate;

    private String notes;

    @NotNull(message = "Pet ID is required")
    private Long petId;

    @NotNull(message = "Vet ID is required")
    private Long vetId;

    private Long serviceId;
}
