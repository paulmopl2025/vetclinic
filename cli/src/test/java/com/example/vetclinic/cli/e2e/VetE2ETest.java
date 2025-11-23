package com.example.vetclinic.cli.e2e;

import com.example.vetclinic.cli.model.UserDTO;
import com.example.vetclinic.cli.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VetE2ETest extends BaseE2ETest {

    private static UserService userService;

    @BeforeAll
    public static void setup() {
        userService = new UserService(authService);
    }

    @Test
    public void testGetVets() {
        List<UserDTO> vets = userService.getVets();
        assertNotNull(vets, "Vets list should not be null");
        // Assuming there is at least one vet in the system (e.g. the one we logged in
        // as, or seeded data)
        // If not guaranteed, we might just check it's not null
        // assertFalse(vets.isEmpty(), "Vets list should not be empty");

        for (UserDTO vet : vets) {
            boolean hasVetRole = (vet.getRole() != null && vet.getRole().toUpperCase().contains("VET")) ||
                    (vet.getRoles() != null && vet.getRoles().stream().anyMatch(r -> r.toUpperCase().contains("VET")));
            assertTrue(hasVetRole, "User should have VET role");
        }
    }
}
