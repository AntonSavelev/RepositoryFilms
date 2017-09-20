package com.example.repositoryfilms.network;

import com.example.repositoryfilms.model.AllPeople;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SwapiService {
    @GET("people/")
    Call<AllPeople> getAllPeoples(@Query("page") int page);
}
