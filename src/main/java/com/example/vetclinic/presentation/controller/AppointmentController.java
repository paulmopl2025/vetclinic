package com.example.vetclinic.presentation.controller;

import com.example.vetclinic.application.dto.appointment.AppointmentDTO;
import com.example.vetclinic.application.dto.appointment.CreateAppointmentDTO;
import com.example.vetclinic.application.dto.appointment.UpdateAppointmentDTO;
import com.example.vetclinic.application.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointments", description = "Appointment management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VET', 'RECEPCIONISTA')")
    @Operation(summary = "Get all appointments", description = "Retrieve a list of all appointments")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VET', 'RECEPCIONISTA')")
    @Operation(summary = "Get appointment by ID", description = "Retrieve appointment details by ID")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @GetMapping("/pet/{petId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VET', 'RECEPCIONISTA')")
    @Operation(summary = "Get appointments by pet", description = "Retrieve all appointments for a specific pet")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByPet(@PathVariable Long petId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPet(petId));
    }

    @GetMapping("/vet/{vetId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VET')")
    @Operation(summary = "Get appointments by vet", description = "Retrieve all appointments for a specific veterinarian")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByVet(@PathVariable Long vetId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByVet(vetId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    @Operation(summary = "Create appointment", description = "Schedule a new appointment")
    public ResponseEntity<AppointmentDTO> createAppointment(@Valid @RequestBody CreateAppointmentDTO createDTO) {
        return new ResponseEntity<>(appointmentService.createAppointment(createDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    @Operation(summary = "Update appointment", description = "Update appointment details")
    public ResponseEntity<AppointmentDTO> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAppointmentDTO updateDTO) {
        return ResponseEntity.ok(appointmentService.updateAppointment(id, updateDTO));
    }

    @PatchMapping("/{id}/confirm")
    @PreAuthorize("hasAnyRole('ADMIN', 'VET', 'RECEPCIONISTA')")
    @Operation(summary = "Confirm appointment", description = "Confirm a pending appointment")
    public ResponseEntity<AppointmentDTO> confirmAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.confirmAppointment(id));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    @Operation(summary = "Cancel appointment", description = "Cancel an appointment")
    public ResponseEntity<AppointmentDTO> cancelAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(id));
    }
}
