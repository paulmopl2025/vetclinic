package com.example.vetclinic.cli.service;

import com.example.vetclinic.cli.client.ApiClient;
import com.example.vetclinic.cli.client.MedicalRecordClient;
import com.example.vetclinic.cli.model.CreateMedicalRecordRequest;
import com.example.vetclinic.cli.model.MedicalRecord;
import retrofit2.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class MedicalRecordService {
    private final MedicalRecordClient client;
    private final AuthService authService;

    public MedicalRecordService(AuthService authService) {
        this.authService = authService;
        this.client = ApiClient.createService(MedicalRecordClient.class);
    }

    private String getToken() {
        return "Bearer " + authService.getSession().getToken();
    }

    public List<MedicalRecord> getAllMedicalRecords() {
        try {
            Response<List<MedicalRecord>> response = client.getAllMedicalRecords(getToken()).execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<MedicalRecord> getMedicalRecordsByPet(Long petId) {
        try {
            Response<List<MedicalRecord>> response = client.getMedicalRecordsByPet(getToken(), petId).execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public MedicalRecord createMedicalRecord(CreateMedicalRecordRequest request) {
        try {
            Response<MedicalRecord> response = client.createMedicalRecord(getToken(), request).execute();
            if (response.isSuccessful()) {
                return response.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
