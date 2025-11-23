package com.example.vetclinic.cli.e2e;

import com.example.vetclinic.cli.model.CreateOwnerRequest;
import com.example.vetclinic.cli.model.Owner;
import com.example.vetclinic.cli.model.UpdateOwnerRequest;
import com.example.vetclinic.cli.service.OwnerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OwnerE2ETest extends BaseE2ETest {

    private static OwnerService ownerService;
    private static Long createdOwnerId;

    @BeforeAll
    public static void setup() {
        ownerService = new OwnerService(authService);
    }

    @Test
    @Order(1)
    public void testCreateOwner() {
        CreateOwnerRequest request = new CreateOwnerRequest(
                "Test", "Owner", "test.owner@example.com", "555-0100", "123 Test St", "City");
        Owner owner = ownerService.createOwner(request);
        assertNotNull(owner, "Created owner should not be null");
        assertNotNull(owner.getId(), "Created owner should have an ID");
        assertEquals("Test", owner.getFirstName());
        createdOwnerId = owner.getId();
    }

    @Test
    @Order(2)
    public void testGetAllOwners() {
        List<Owner> owners = ownerService.getAllOwners();
        assertNotNull(owners, "Owners list should not be null");
        assertFalse(owners.isEmpty(), "Owners list should not be empty");
        boolean found = owners.stream().anyMatch(o -> o.getId().equals(createdOwnerId));
        assertTrue(found, "Created owner should be in the list");
    }

    @Test
    @Order(3)
    public void testUpdateOwner() {
        assertNotNull(createdOwnerId, "Owner ID should exist from previous test");
        UpdateOwnerRequest request = new UpdateOwnerRequest(
                "TestUpdated", "Owner", "test.owner@example.com", "555-0100", "123 Test St", "City");
        Owner updated = ownerService.updateOwner(createdOwnerId, request);
        assertNotNull(updated, "Updated owner should not be null");
        assertEquals("TestUpdated", updated.getFirstName());
    }

    @Test
    @Order(4)
    public void testDeleteOwner() {
        assertNotNull(createdOwnerId, "Owner ID should exist");
        boolean deleted = ownerService.deleteOwner(createdOwnerId);
        assertTrue(deleted, "Delete should return true");

        // Verify deletion
        List<Owner> owners = ownerService.getAllOwners();
        boolean found = owners.stream().anyMatch(o -> o.getId().equals(createdOwnerId));
        assertFalse(found, "Deleted owner should not be in the list");
    }
}
