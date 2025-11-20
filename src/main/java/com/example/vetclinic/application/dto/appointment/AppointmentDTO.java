package com.example.vetclinic.application.dto.appointment;

import com.example.vetclinic.domain.model.AppointmentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentDTO {
    private Long id;
    private LocalDateTime appointmentDate;
    private AppointmentStatus status;
    private String notes;

    // Pet information
    private Long petId;
    private String petName;

    // Vet information
    private Long vetId;
    private String vetName;

    // Service information
    private Long serviceId;
    private String serviceName;
}
