package com.example.vetclinic.cli.e2e;

import com.example.vetclinic.cli.model.*;
import com.example.vetclinic.cli.service.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppointmentE2ETest extends BaseE2ETest {

    private static AppointmentService appointmentService;
    private static PetService petService;
    private static OwnerService ownerService;
    private static UserService userService;
    private static ServiceService serviceService;

    private static Long petId;
    private static Long vetId;
    private static Long serviceId;
    private static Long createdAppointmentId;

    @BeforeAll
    public static void setup() {
        appointmentService = new AppointmentService(authService);
        petService = new PetService(authService);
        ownerService = new OwnerService(authService);
        userService = new UserService(authService);
        serviceService = new ServiceService(authService);

        // Setup dependencies
        Owner owner = ownerService
                .createOwner(new CreateOwnerRequest("Appt", "Owner", "appt@test.com", "555", "Addr", "City"));
        Pet pet = petService
                .createPet(new CreatePetRequest("ApptPet", "Cat", "Siamese", LocalDate.now(), owner.getId()));
        petId = pet.getId();

        List<UserDTO> vets = userService.getVets();
        if (!vets.isEmpty()) {
            vetId = vets.get(0).getId();
        } else {
            fail("No vets available for appointment test");
        }

        // Ensure service exists
        CreateServiceRequest newService = new CreateServiceRequest(
                "Appt Service", "Desc", ServiceType.CHECKUP, new java.math.BigDecimal("100.00"), 30);
        ServiceDTO createdService = serviceService.createService(newService);
        if (createdService != null) {
            serviceId = createdService.getId();
        } else {
            // Fallback to fetching existing
            List<ServiceDTO> services = serviceService.getAllServices();
            if (!services.isEmpty()) {
                serviceId = services.get(0).getId();
            } else {
                fail("No services available for appointment test");
            }
        }
    }

    @Test
    @Order(1)
    public void testCreateAppointment() {
        CreateAppointmentRequest request = new CreateAppointmentRequest(
                LocalDateTime.now().plusDays(1),
                "Checkup",
                petId,
                vetId,
                serviceId);
        Appointment appt = appointmentService.createAppointment(request);
        assertNotNull(appt, "Created appointment should not be null");
        createdAppointmentId = appt.getId();
    }

    @Test
    @Order(2)
    public void testGetAllAppointments() {
        List<Appointment> appts = appointmentService.getAllAppointments();
        assertNotNull(appts);
        assertTrue(appts.stream().anyMatch(a -> a.getId().equals(createdAppointmentId)));
    }

    @Test
    @Order(3)
    public void testConfirmAppointment() {
        boolean confirmed = appointmentService.confirmAppointment(createdAppointmentId);
        assertTrue(confirmed, "Appointment confirmation should succeed");

        // Verify
        List<Appointment> appts = appointmentService.getAllAppointments();
        Appointment appt = appts.stream().filter(a -> a.getId().equals(createdAppointmentId)).findFirst().orElse(null);
        assertNotNull(appt);
        assertEquals("CONFIRMED", appt.getStatus());
    }
}
