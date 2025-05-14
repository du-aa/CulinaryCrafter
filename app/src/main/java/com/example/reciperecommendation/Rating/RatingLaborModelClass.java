package com.example.reciperecommendation.Rating;

public class RatingLaborModelClass {
   private String Name  = "";
   private String Rating = "";
    private String Comment  = "" ;
    private String Key  = "";
    private String Date  = "";

    public RatingLaborModelClass() {
    }

    public RatingLaborModelClass(String name, String rating, String comment, String key , String date) {
        Name = name;
        Rating = rating;
        Comment = comment;
        Key = key;
        Date = date ;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getName() {
        return Name;
    }

    public String getRating() {
        return Rating;
    }

    public String getComment() {
        return Comment;
    }

    public String getKey() {
        return Key;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public void setKey(String key) {
        Key = key;
    }
}
