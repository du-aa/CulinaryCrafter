package com.example.reciperecommendation.MealPlanningModule;

import java.util.List;

public class MealPlanDetails {
    private String  id ;
    private int  meal_id  ;
    private String title;
    private String Status; // which repressent weekly , daily or montly
    private String UUID;
    private String image;
    private String calories = "0.0";
    private String carbs = "0.0";
    private String fat= "0.0";
    private String protein = "0.0";
    private List<Nutrient> good;
    private List<Nutrient> bad;

    private String Date ="";
    public MealPlanDetails() {
    }
    public MealPlanDetails(String id, String title, String image, String calories, String carbs, String fat, String protein, List<Nutrient> good, List<Nutrient> bad
    , String status , String uuid , String date , int mid ) {
        Date = date ;
        meal_id = mid ;
        this.id = id;
        this.title = title;
        this.image = image;
        this.calories = calories;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;
        this.good = good;
        this.bad = bad;
        Status =  status ;
        UUID = uuid ;
    }

    public int getMeal_id() {
        return meal_id;
    }

    public void setMeal_id(int meal_id) {
        this.meal_id = meal_id;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public void setGood(List<Nutrient> good) {
        this.good = good;
    }

    public void setBad(List<Nutrient> bad) {
        this.bad = bad;
    }

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
