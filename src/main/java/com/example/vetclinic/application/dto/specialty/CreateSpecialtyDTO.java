package com.example.vetclinic.application.dto.specialty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateSpecialtyDTO {
    @NotBlank(message = "Specialty name is required")
    private String name;
}
