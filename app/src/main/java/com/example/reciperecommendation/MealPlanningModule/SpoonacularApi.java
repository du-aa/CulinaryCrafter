package com.example.reciperecommendation.MealPlanningModule;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpoonacularApi {
    @GET("recipes/random")
    Call<MealResponse> getRandomMeals(
            @Query("number") int number,
            @Query("apiKey") String apiKey
    );

    @GET("recipes/{id}/information")
    Call<RecipeResponse> getRecipeInfo(
            @Path("id") int recipeId,
            @Query("includeNutrition") boolean includeNutrition,
            @Query("apiKey") String apiKey
    );

    @GET("recipes/random")
    Call<MealResponse> getFilteredMeals(
            @Query("apiKey") String apiKey,
            @Query("tags") String tags,  // Example: "vegetarian"
            @Query("number") int number  // Example: 3 meals
    );

    @GET("recipes/{id}/nutritionWidget.json")
    Call<MealNutrition> getMealNutrition(@Path("id") int mealId, @Query("apiKey") String apiKey);

}
