package com.example.vetclinic.cli.client;

import com.example.vetclinic.cli.model.CreateServiceRequest;
import com.example.vetclinic.cli.model.ServiceDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

import java.util.List;

public interface ServiceClient {
    @GET("services")
    Call<List<ServiceDTO>> getAllServices(@Header("Authorization") String token);

    @retrofit2.http.POST("services")
    Call<ServiceDTO> createService(@Header("Authorization") String token,
            @retrofit2.http.Body CreateServiceRequest service);
}
