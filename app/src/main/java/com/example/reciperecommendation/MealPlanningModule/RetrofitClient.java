package com.example.reciperecommendation.MealPlanningModule;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RetrofitClient {
    private static final String BASE_URL = "https://api.spoonacular.com/";
    private static Retrofit retrofit;


    public static SpoonacularApi getApi() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(SpoonacularApi.class);
    }
}
