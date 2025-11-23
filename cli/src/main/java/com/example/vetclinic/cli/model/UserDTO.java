package com.example.vetclinic.cli.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    private Long id;
    private String username;
    private String role;
    private java.util.Set<String> roles;
    private String fullName;
    private String email;

    public String getDisplayName() {
        if (fullName != null && !fullName.isEmpty() && !"null".equalsIgnoreCase(fullName)) {
            return fullName;
        }
        return username;
    }
}
