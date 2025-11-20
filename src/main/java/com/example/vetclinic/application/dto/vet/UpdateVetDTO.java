package com.example.vetclinic.application.dto.vet;

import lombok.Data;

import java.util.Set;

@Data
public class UpdateVetDTO {
    private String firstName;
    private String lastName;
    private Set<Long> specialtyIds;
}
