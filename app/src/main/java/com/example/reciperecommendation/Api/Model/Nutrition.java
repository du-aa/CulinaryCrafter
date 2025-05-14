package com.example.reciperecommendation.Api.Model;


import com.example.reciperecommendation.MealPlanningModule.Nutrient;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Nutrition {
    @SerializedName("nutrients")
    @Expose
    private List<Nutrient> nutrients;

    public List<Nutrient> getNutrients() {
        return nutrients;
    }
}
