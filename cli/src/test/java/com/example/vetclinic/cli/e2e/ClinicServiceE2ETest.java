package com.example.vetclinic.cli.e2e;

import com.example.vetclinic.cli.model.CreateServiceRequest;
import com.example.vetclinic.cli.model.ServiceDTO;
import com.example.vetclinic.cli.model.ServiceType;
import com.example.vetclinic.cli.service.ServiceService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClinicServiceE2ETest extends BaseE2ETest {

    private static ServiceService serviceService;

    @BeforeAll
    public static void setup() {
        serviceService = new ServiceService(authService);
    }

    @Test
    public void testGetAllServices() {
        // Ensure at least one service exists
        CreateServiceRequest newService = new CreateServiceRequest(
                "Test Service", "Description", ServiceType.CHECKUP, new java.math.BigDecimal("50.00"), 30);
        serviceService.createService(newService);

        List<ServiceDTO> services = serviceService.getAllServices();
        assertNotNull(services, "Services list should not be null");
        assertFalse(services.isEmpty(), "Services list should not be empty");
    }
}
