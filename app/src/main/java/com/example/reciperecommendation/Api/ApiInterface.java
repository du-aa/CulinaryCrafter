package com.example.reciperecommendation.Api;

import com.example.reciperecommendation.Api.Model.All;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("complexSearch")
    Call<All> getAll(
            @Query("apiKey") String apiKey,
            @Query("query") String query

    );
    @GET("information")
    Call<All> getInfo(
            @Query("apiKey") String apiKey,
            @Query("id") String id
    );

    @GET("complexSearch")
    Call<All> getAllWithCuisine(
            @Query("apiKey") String apiKey,
            @Query("cuisine") String cuisine
    );

    @GET("complexSearch")
    Call<All> getAllSort(
            @Query("apiKey") String apiKey,
            @Query("query") String query,
            @Query("sort") String sort,                    // e.g., "calories"
            @Query("sortDirection") String sortDirection,
            @Query("diet") String diet                     // e.g., "vegetarian", "vegan"
    );

}
