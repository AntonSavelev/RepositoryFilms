package com.example.repositoryfilms;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("people/")
    Call<AllPeople> getAllPeoples(@Query("page") int page);
}
