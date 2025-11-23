package com.example.vetclinic.cli.service;

import com.example.vetclinic.cli.client.ApiClient;
import com.example.vetclinic.cli.client.ServiceClient;
import com.example.vetclinic.cli.model.CreateServiceRequest;
import com.example.vetclinic.cli.model.ServiceDTO;
import retrofit2.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ServiceService {
    private final ServiceClient serviceClient;
    private final AuthService authService;

    public ServiceService(AuthService authService) {
        this.authService = authService;
        this.serviceClient = ApiClient.createService(ServiceClient.class);
    }

    private String getToken() {
        return "Bearer " + authService.getSession().getToken();
    }

    public List<ServiceDTO> getAllServices() {
        try {
            Response<List<ServiceDTO>> response = serviceClient.getAllServices(getToken()).execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public ServiceDTO createService(CreateServiceRequest service) {
        try {
            Response<ServiceDTO> response = serviceClient.createService(getToken(), service).execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                System.err.println("Failed to create service: " + response.code() + " " + response.message());
                if (response.errorBody() != null) {
                    System.err.println("Error body: " + response.errorBody().string());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
