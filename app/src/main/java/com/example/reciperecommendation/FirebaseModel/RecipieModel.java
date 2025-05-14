package com.example.reciperecommendation.FirebaseModel;

public class RecipieModel {
    private String UUID = "" ;
    private String RECID = "" ;
    private String Name = "" ;
    private String Desciption = "" ;
    private String picture = "" ;

    public RecipieModel() {
    }

    public RecipieModel(String UUID, String RECID, String name, String desciption, String uri ) {
        this.UUID = UUID;

        this.RECID = RECID;
        Name = name;
        Desciption = desciption;
    picture = uri;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getRECID() {
        return RECID;
    }

    public void setRECID(String RECID) {
        this.RECID = RECID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDesciption() {
        return Desciption;
    }

    public void setDesciption(String desciption) {
        Desciption = desciption;
    }
}
