package com.example.vetclinic.cli.e2e;

import com.example.vetclinic.cli.service.AuthService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuthE2ETest {

    @org.junit.jupiter.api.BeforeEach
    public void cleanup() {
        new com.example.vetclinic.cli.storage.StorageService().clearSession();
    }

    @Test
    public void testLoginSuccess() {
        AuthService authService = new AuthService(new com.example.vetclinic.cli.storage.StorageService());
        String result = authService.login("admin", "admin123"); // Adjust credentials as needed
        assertNull(result, "Login should succeed with valid credentials");
        assertNotNull(authService.getSession(), "Session should not be null after login");
        assertNotNull(authService.getSession().getToken(), "Token should not be null");
    }

    @Test
    public void testLoginFailure() {
        AuthService authService = new AuthService(new com.example.vetclinic.cli.storage.StorageService());
        String result = authService.login("wronguser", "wrongpass");
        assertNotNull(result, "Login should fail with invalid credentials");
        assertNull(authService.getSession(), "Session should be null after failed login");
    }
}
