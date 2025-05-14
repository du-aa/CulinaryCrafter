package com.example.reciperecommendation.Api.Model;

import com.example.reciperecommendation.MealPlanningModule.Nutrient;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Results {
    @SerializedName("nutrition")
    @Expose
    private Nutrition nutrition;

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("image")
    @Expose
    private String url;
    @SerializedName("title")
    @Expose
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public Nutrition getNutrition() {
        return nutrition;
    }
    public int getCalories() {
        if (nutrition != null && nutrition.getNutrients() != null) {
            for (Nutrient nutrient : nutrition.getNutrients()) {
                if ("Calories".equalsIgnoreCase(nutrient.getTitle())) {
                    Double amount = Double.valueOf(nutrient.getAmount());
                    if (amount != null) {
                        return amount.intValue();
                    }
                }
            }
        }
        return 0;
    }


}
