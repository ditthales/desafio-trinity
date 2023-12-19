package com.example.trinitydesafio;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CepAPI {
    @GET("{cep}/json/")
    Call<CepResponse> getCep(@NonNull @Path("cep") String cep);
}
