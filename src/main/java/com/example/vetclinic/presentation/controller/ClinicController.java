package com.example.vetclinic.presentation.controller;

import com.example.vetclinic.application.dto.clinic.ClinicConfigDTO;
import com.example.vetclinic.application.dto.clinic.ClinicDTO;
import com.example.vetclinic.application.dto.clinic.ClinicStatsDTO;
import com.example.vetclinic.application.service.ClinicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clinic")
@RequiredArgsConstructor
@Tag(name = "Clinic", description = "Clinic configuration and statistics endpoints")
@SecurityRequirement(name = "bearerAuth")
public class ClinicController {

    private final ClinicService clinicService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VET', 'RECEPCIONISTA')")
    @Operation(summary = "Get clinic configuration", description = "Retrieve clinic configuration details")
    public ResponseEntity<ClinicDTO> getClinicConfig() {
        return ResponseEntity.ok(clinicService.getClinicConfig());
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update clinic configuration", description = "Update clinic configuration (Admin only)")
    public ResponseEntity<ClinicDTO> updateClinicConfig(@Valid @RequestBody ClinicConfigDTO configDTO) {
        return ResponseEntity.ok(clinicService.updateClinicConfig(configDTO));
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'VET')")
    @Operation(summary = "Get clinic statistics", description = "Retrieve clinic statistics including appointments, revenue, etc.")
    public ResponseEntity<ClinicStatsDTO> getClinicStats() {
        return ResponseEntity.ok(clinicService.getClinicStats());
    }
}
