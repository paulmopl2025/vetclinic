package com.example.vetclinic.presentation.controller;

import com.example.vetclinic.application.dto.specialty.CreateSpecialtyDTO;
import com.example.vetclinic.application.dto.specialty.SpecialtyDTO;
import com.example.vetclinic.application.dto.specialty.UpdateSpecialtyDTO;
import com.example.vetclinic.application.service.SpecialtyService;
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
@RequestMapping("/api/specialties")
@RequiredArgsConstructor
@Tag(name = "Specialties", description = "Specialty management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VET', 'RECEPCIONISTA')")
    @Operation(summary = "Get all specialties", description = "Retrieve a list of all specialties")
    public ResponseEntity<List<SpecialtyDTO>> getAllSpecialties() {
        return ResponseEntity.ok(specialtyService.getAllSpecialties());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VET', 'RECEPCIONISTA')")
    @Operation(summary = "Get specialty by ID", description = "Retrieve specialty details by ID")
    public ResponseEntity<SpecialtyDTO> getSpecialtyById(@PathVariable Long id) {
        return ResponseEntity.ok(specialtyService.getSpecialtyById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create specialty", description = "Create a new specialty")
    public ResponseEntity<SpecialtyDTO> createSpecialty(@Valid @RequestBody CreateSpecialtyDTO createDTO) {
        return new ResponseEntity<>(specialtyService.createSpecialty(createDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update specialty", description = "Update specialty details")
    public ResponseEntity<SpecialtyDTO> updateSpecialty(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSpecialtyDTO updateDTO) {
        return ResponseEntity.ok(specialtyService.updateSpecialty(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete specialty", description = "Delete a specialty (Admin only)")
    public ResponseEntity<Void> deleteSpecialty(@PathVariable Long id) {
        specialtyService.deleteSpecialty(id);
        return ResponseEntity.noContent().build();
    }
}
