package com.example.vetclinic.application.dto.appointment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateAppointmentDTO {
    private LocalDateTime appointmentDate;
    private String notes;
    private Long vetId;
    private Long serviceId;
}
