package com.example.vetclinic.presentation.controller;

import com.example.vetclinic.application.dto.vet.CreateVetDTO;
import com.example.vetclinic.application.dto.vet.UpdateVetDTO;
import com.example.vetclinic.application.dto.vet.VetDTO;
import com.example.vetclinic.application.service.VetService;
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
@RequestMapping("/api/vets")
@RequiredArgsConstructor
@Tag(name = "Veterinarians", description = "Veterinarian management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class VetController {

    private final VetService vetService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VET', 'RECEPCIONISTA')")
    @Operation(summary = "Get all vets", description = "Retrieve a list of all veterinarians")
    public ResponseEntity<List<VetDTO>> getAllVets() {
        return ResponseEntity.ok(vetService.getAllVets());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VET', 'RECEPCIONISTA')")
    @Operation(summary = "Get vet by ID", description = "Retrieve veterinarian details by ID")
    public ResponseEntity<VetDTO> getVetById(@PathVariable Long id) {
        return ResponseEntity.ok(vetService.getVetById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create vet", description = "Create a new veterinarian")
    public ResponseEntity<VetDTO> createVet(@Valid @RequestBody CreateVetDTO createDTO) {
        return new ResponseEntity<>(vetService.createVet(createDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update vet", description = "Update veterinarian details")
    public ResponseEntity<VetDTO> updateVet(
            @PathVariable Long id,
            @Valid @RequestBody UpdateVetDTO updateDTO) {
        return ResponseEntity.ok(vetService.updateVet(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete vet", description = "Delete a veterinarian (Admin only)")
    public ResponseEntity<Void> deleteVet(@PathVariable Long id) {
        vetService.deleteVet(id);
        return ResponseEntity.noContent().build();
    }
}
