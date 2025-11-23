package com.example.vetclinic.cli.e2e;

import com.example.vetclinic.cli.model.*;
import com.example.vetclinic.cli.service.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MedicalRecordE2ETest extends BaseE2ETest {

    private static MedicalRecordService medicalRecordService;
    private static PetService petService;
    private static OwnerService ownerService;
    private static UserService userService;

    private static Long petId;
    private static Long vetId;
    private static Long createdRecordId;

    @BeforeAll
    public static void setup() {
        medicalRecordService = new MedicalRecordService(authService);
        petService = new PetService(authService);
        ownerService = new OwnerService(authService);
        userService = new UserService(authService);

        // Setup dependencies
        Owner owner = ownerService
                .createOwner(new CreateOwnerRequest("Med", "Owner", "med@test.com", "555", "Addr", "City"));
        Pet pet = petService
                .createPet(new CreatePetRequest("MedPet", "Bird", "Parrot", LocalDate.now(), owner.getId()));
        petId = pet.getId();

        List<UserDTO> vets = userService.getVets();
        if (!vets.isEmpty()) {
            vetId = vets.get(0).getId();
        } else {
            fail("No vets available for medical record test");
        }
    }

    @Test
    @Order(1)
    public void testCreateMedicalRecord() {
        CreateMedicalRecordRequest request = new CreateMedicalRecordRequest(
                LocalDateTime.now(),
                "Healthy",
                "None",
                "Routine check",
                new BigDecimal("0.5"),
                new BigDecimal("38.0"),
                "None",
                petId,
                vetId,
                null);
        MedicalRecord record = medicalRecordService.createMedicalRecord(request);
        assertNotNull(record, "Created record should not be null");
        createdRecordId = record.getId();
    }

    @Test
    @Order(2)
    public void testGetAllMedicalRecords() {
        List<MedicalRecord> records = medicalRecordService.getAllMedicalRecords();
        assertNotNull(records);
        assertTrue(records.stream().anyMatch(r -> r.getId().equals(createdRecordId)));
    }
}
