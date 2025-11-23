package com.example.vetclinic.cli.service;

import com.example.vetclinic.cli.client.ApiClient;
import com.example.vetclinic.cli.client.UserClient;
import com.example.vetclinic.cli.model.UserDTO;
import retrofit2.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    private final UserClient userClient;
    private final AuthService authService;

    public UserService(AuthService authService) {
        this.authService = authService;
        this.userClient = ApiClient.createService(UserClient.class);
    }

    private String getToken() {
        return "Bearer " + authService.getSession().getToken();
    }

    public List<UserDTO> getAllUsers() {
        try {
            Response<List<UserDTO>> response = userClient.getAllUsers(getToken()).execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<UserDTO> getVets() {
        return getAllUsers().stream()
                .filter(user -> {
                    boolean isVet = false;
                    if (user.getRole() != null && user.getRole().toUpperCase().contains("VET")) {
                        isVet = true;
                    }
                    if (user.getRoles() != null) {
                        for (String r : user.getRoles()) {
                            if (r.toUpperCase().contains("VET")) {
                                isVet = true;
                                break;
                            }
                        }
                    }
                    return isVet;
                })
                .collect(Collectors.toList());
    }
}
