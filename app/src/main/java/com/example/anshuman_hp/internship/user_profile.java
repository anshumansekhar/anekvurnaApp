package com.example.anshuman_hp.internship;

/**
 * Created by Anshuman-HP on 23-08-2017.
 */

public class user_profile {
    String name;
    String birthdate;
    String address;
    String isMale;
    String presentClass;
    String photourl;

    public user_profile() {
    }

    public user_profile(String name, String birthdate, String address, String isMale, String presentClass, String photourl) {
        this.name = name;
        this.birthdate = birthdate;
        this.address = address;
        this.isMale = isMale;
        this.presentClass = presentClass;
        this.photourl = photourl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIsMale() {
        return isMale;
    }

    public void setIsMale(String isMale) {
        this.isMale = isMale;
    }

    public String getPresentClass() {
        return presentClass;
    }

    public void setPresentClass(String presentClass) {
        this.presentClass = presentClass;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }
}
