package com.example.reciperecommendation.MealPlanningModule;

import java.util.List;

public class MealNutrition {
    private String calories;
    private String carbs;
    private String fat;
    private String protein;
    private List<Nutrient> good;
    private List<Nutrient> bad;

    public String getCalories() {
        return calories;
    }

    public String getCarbs() {
        return carbs;
    }

    public String getFat() {
        return fat;
    }

    public String getProtein() {
        return protein;
    }

    public List<Nutrient> getGood() {
        return good;
    }

    public List<Nutrient> getBad() {
        return bad;
    }
}
