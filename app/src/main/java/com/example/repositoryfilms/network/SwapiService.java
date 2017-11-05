package com.example.repositoryfilms.network;

import com.example.repositoryfilms.model.AllCharacters;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SwapiService {
    @GET("people/")
    Call<AllCharacters> getAllPeoples(@Query("page") int page);

    @GET("people/")
    Call<AllCharacters> getPeopleSearch(@Query("search") String searchName);
}
