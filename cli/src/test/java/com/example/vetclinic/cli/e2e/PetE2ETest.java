package com.example.vetclinic.cli.e2e;

import com.example.vetclinic.cli.model.CreateOwnerRequest;
import com.example.vetclinic.cli.model.CreatePetRequest;
import com.example.vetclinic.cli.model.Owner;
import com.example.vetclinic.cli.model.Pet;
import com.example.vetclinic.cli.service.OwnerService;
import com.example.vetclinic.cli.service.PetService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PetE2ETest extends BaseE2ETest {

    private static PetService petService;
    private static OwnerService ownerService;
    private static Long ownerId;
    private static Long createdPetId;

    @BeforeAll
    public static void setup() {
        petService = new PetService(authService);
        ownerService = new OwnerService(authService);

        // Create an owner for the pet
        CreateOwnerRequest ownerRequest = new CreateOwnerRequest(
                "Pet", "Owner", "pet.owner@example.com", "555-0200", "456 Pet Ln", "City");
        Owner owner = ownerService.createOwner(ownerRequest);
        ownerId = owner.getId();
    }

    @Test
    @Order(1)
    public void testCreatePet() {
        CreatePetRequest request = new CreatePetRequest(
                "Buddy", "Dog", "Golden Retriever", LocalDate.now().minusYears(2), ownerId);
        Pet pet = petService.createPet(request);
        assertNotNull(pet, "Created pet should not be null");
        assertNotNull(pet.getId(), "Created pet should have an ID");
        assertEquals("Buddy", pet.getName());
        createdPetId = pet.getId();
    }

    @Test
    @Order(2)
    public void testGetAllPets() {
        List<Pet> pets = petService.getAllPets();
        assertNotNull(pets, "Pets list should not be null");
        assertFalse(pets.isEmpty(), "Pets list should not be empty");
        boolean found = pets.stream().anyMatch(p -> p.getId().equals(createdPetId));
        assertTrue(found, "Created pet should be in the list");
    }

    @Test
    @Order(3)
    public void testDeletePet() {
        assertNotNull(createdPetId, "Pet ID should exist");
        boolean deleted = petService.deletePet(createdPetId);
        assertTrue(deleted, "Delete should return true");
    }
}
