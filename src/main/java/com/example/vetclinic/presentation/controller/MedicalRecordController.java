package com.example.vetclinic.presentation.controller;

import com.example.vetclinic.application.dto.medicalrecord.CreateMedicalRecordDTO;
import com.example.vetclinic.application.dto.medicalrecord.MedicalRecordDTO;
import com.example.vetclinic.application.dto.medicalrecord.UpdateMedicalRecordDTO;
import com.example.vetclinic.application.service.MedicalRecordService;
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
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
@Tag(name = "Medical Records", description = "Medical records management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VET')")
    @Operation(summary = "Get all medical records", description = "Retrieve a list of all medical records")
    public ResponseEntity<List<MedicalRecordDTO>> getAllRecords() {
        return ResponseEntity.ok(medicalRecordService.getAllRecords());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VET')")
    @Operation(summary = "Get medical record by ID", description = "Retrieve medical record details by ID")
    public ResponseEntity<MedicalRecordDTO> getRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(medicalRecordService.getRecordById(id));
    }

    @GetMapping("/pet/{petId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VET', 'RECEPCIONISTA')")
    @Operation(summary = "Get medical records by pet", description = "Retrieve all medical records for a specific pet")
    public ResponseEntity<List<MedicalRecordDTO>> getRecordsByPet(@PathVariable Long petId) {
        return ResponseEntity.ok(medicalRecordService.getRecordsByPet(petId));
    }

    @GetMapping("/vet/{vetId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VET')")
    @Operation(summary = "Get medical records by vet", description = "Retrieve all medical records created by a specific vet")
    public ResponseEntity<List<MedicalRecordDTO>> getRecordsByVet(@PathVariable Long vetId) {
        return ResponseEntity.ok(medicalRecordService.getRecordsByVet(vetId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VET')")
    @Operation(summary = "Create medical record", description = "Create a new medical record")
    public ResponseEntity<MedicalRecordDTO> createRecord(@Valid @RequestBody CreateMedicalRecordDTO createDTO) {
        return new ResponseEntity<>(medicalRecordService.createRecord(createDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VET')")
    @Operation(summary = "Update medical record", description = "Update medical record details")
    public ResponseEntity<MedicalRecordDTO> updateRecord(
            @PathVariable Long id,
            @Valid @RequestBody UpdateMedicalRecordDTO updateDTO) {
        return ResponseEntity.ok(medicalRecordService.updateRecord(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete medical record", description = "Delete a medical record (Admin only)")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        medicalRecordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }
}
