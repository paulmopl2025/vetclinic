package com.example.vetclinic.cli.client;

import com.example.vetclinic.cli.model.CreatePetRequest;
import com.example.vetclinic.cli.model.Pet;
import com.example.vetclinic.cli.model.UpdatePetRequest;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface PetClient {
    @GET("pets")
    Call<List<Pet>> getAllPets(@Header("Authorization") String token);

    @GET("pets/{id}")
    Call<Pet> getPetById(@Header("Authorization") String token, @Path("id") Long id);

    @POST("pets")
    Call<Pet> createPet(@retrofit2.http.Header("Authorization") String token, @Body CreatePetRequest request);

    @retrofit2.http.PUT("pets/{id}")
    Call<Pet> updatePet(@retrofit2.http.Header("Authorization") String token, @Path("id") Long id,
            @Body UpdatePetRequest request);

    @retrofit2.http.DELETE("pets/{id}")
    Call<Void> deletePet(@retrofit2.http.Header("Authorization") String token, @Path("id") Long id);
}
