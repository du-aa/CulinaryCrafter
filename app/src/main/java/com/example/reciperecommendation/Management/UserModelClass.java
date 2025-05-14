package com.example.reciperecommendation.Management;

public class UserModelClass  implements  Comparable<UserModelClass> {
private String UUID = "" ;
private  String UserName  = "" ;
private  String Emails = "" ;
private  String Password = "" ;
private String Cnic = "" ;
private  String profilePhoto= "" ;
private  String Phone = "" ;
private  String UserType = "" ;
private  String Lattitude = "" ;
private  String Longitude = "" ;
private  String IsApprove = "" ;
private  String IsOnline = "" ;
private  String Rating = "" ;
private  String Dietary_preferences = "" ;
private  String Food_allergies = "" ;

    public UserModelClass(String UUID, String userName, String emails, String password, String cnic, String profilePhoto, String phone , String userType  ,
                          String lattitude , String longitude, String isApprove , String isOnline ,
                          String rating
    , String dietary_preferences , String food_allergies
    ) {
        this.UUID = UUID;
        IsOnline = isOnline ;
        Rating = rating ;
        Lattitude = lattitude ;
        Longitude = longitude ;
        UserName = userName;
        Emails = emails;
        IsApprove = isApprove ;
        Password = password;
        Cnic = cnic;
        this.profilePhoto = profilePhoto;
        Phone = phone;
        UserType = userType ;
        Dietary_preferences = dietary_preferences ;
        Food_allergies = food_allergies;
    }

    public String getIsOnline() {
        return IsOnline;
    }

    public void setIsOnline(String isOnline) {
        IsOnline = isOnline;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public String getLattitude() {
        return Lattitude;
    }

    public void setLattitude(String lattitude) {
        Lattitude = lattitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public String getIsApprove() {
        return IsApprove;
    }

    public void setIsApprove(String isApprove) {
        IsApprove = isApprove;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public UserModelClass() {
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmails() {
        return Emails;
    }

    public void setEmails(String emails) {
        Emails = emails;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getCnic() {
        return Cnic;
    }

    public void setCnic(String cnic) {
        Cnic = cnic;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    @Override
    public int compareTo(UserModelClass o) {
        return this.getRating().compareTo(o.getRating());
    }

    public String getDietary_preferences() {
        return Dietary_preferences;
    }

    public void setDietary_preferences(String dietary_preferences) {
        Dietary_preferences = dietary_preferences;
    }

    public String getFood_allergies() {
        return Food_allergies;
    }

    public void setFood_allergies(String food_allergies) {
        Food_allergies = food_allergies;
    }
}
