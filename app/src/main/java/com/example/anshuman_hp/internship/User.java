package com.example.anshuman_hp.internship;

/**
 * Created by Anshuman-HP on 10-08-2017.
 */

public class User {
    String email;
    String fullName;
    String id;
    String photoUrl;
    String birthDate;
    String isMale;
    String password;
    String mobileNumber;

    public User(String email, String fullName, String id, String photoUrl, String birthDate, String isMale, String password, String mobileNumber) {
        this.email = email;
        this.fullName = fullName;
        this.id = id;
        this.photoUrl = photoUrl;
        this.birthDate = birthDate;
        this.isMale = isMale;
        this.password = password;
        this.mobileNumber = mobileNumber;
    }
    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getIsMale() {
        return isMale;
    }

    public void setIsMale(String isMale) {
        this.isMale = isMale;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
