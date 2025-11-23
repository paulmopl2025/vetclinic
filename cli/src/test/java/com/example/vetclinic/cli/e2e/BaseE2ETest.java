package com.example.vetclinic.cli.e2e;

import com.example.vetclinic.cli.service.AuthService;
import org.junit.jupiter.api.BeforeAll;

public class BaseE2ETest {

    protected static AuthService authService;
    protected static String authToken;

    @BeforeAll
    public static void setupAuth() {
        authService = new AuthService(new com.example.vetclinic.cli.storage.StorageService());
        // Login with a known admin or vet user to perform actions
        // Assuming 'admin' / 'admin123' or similar exists and has permissions
        // Or 'vet1' / 'password'
        String loginError = authService.login("admin", "admin123");
        if (loginError != null) {
            // Fallback to vet if admin fails, or fail test
            loginError = authService.login("vet1", "password");
        }

        if (loginError == null) {
            authToken = authService.getSession().getToken();
        } else {
            throw new RuntimeException("Failed to login for E2E tests: " + loginError);
        }
    }
}
