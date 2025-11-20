package com.example.vetclinic.application.dto.vet;

import com.example.vetclinic.application.dto.specialty.SpecialtyDTO;
import lombok.Data;

import java.util.Set;

@Data
public class VetDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Set<SpecialtyDTO> specialties;
}
