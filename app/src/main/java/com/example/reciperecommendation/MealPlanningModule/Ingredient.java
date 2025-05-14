package com.example.reciperecommendation.MealPlanningModule;

import com.google.gson.annotations.SerializedName;

public class Ingredient {
    @SerializedName("name")
    private String name;

    @SerializedName("amount")
    private float amount;

    @SerializedName("unit")
    private String unit;

    public String getName() {
        return name;
    }

    public String getFormattedAmount() {
        return amount + " " + unit;
    }
}
