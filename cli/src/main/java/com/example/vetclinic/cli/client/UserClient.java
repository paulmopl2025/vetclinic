package com.example.vetclinic.cli.client;

import com.example.vetclinic.cli.model.UserDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

import java.util.List;

public interface UserClient {
    @GET("users")
    Call<List<UserDTO>> getAllUsers(@Header("Authorization") String token);
}
