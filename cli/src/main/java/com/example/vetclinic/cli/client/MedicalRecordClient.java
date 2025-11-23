package com.example.vetclinic.cli.client;

import com.example.vetclinic.cli.model.CreateMedicalRecordRequest;
import com.example.vetclinic.cli.model.MedicalRecord;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface MedicalRecordClient {
    @GET("medical-records")
    Call<List<MedicalRecord>> getAllMedicalRecords(@retrofit2.http.Header("Authorization") String token);

    @GET("medical-records/pet/{petId}")
    Call<List<MedicalRecord>> getMedicalRecordsByPet(@retrofit2.http.Header("Authorization") String token,
            @Path("petId") Long petId);

    @POST("medical-records")
    Call<MedicalRecord> createMedicalRecord(@retrofit2.http.Header("Authorization") String token,
            @Body CreateMedicalRecordRequest request);
}
