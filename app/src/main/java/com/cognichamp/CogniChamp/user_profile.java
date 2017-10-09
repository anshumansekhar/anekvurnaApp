package com.cognichamp.CogniChamp;

/**
 * Created by Anshuman-HP on 23-08-2017.
 */

public class user_profile {
    String name;
    String birthdate;
    String isMale;
    String presentClass;
    String photourl;
    String atAddress;
    String cityAddress;
    String state;
    String pinCode;
    String district;

    public user_profile() {
    }

    public user_profile(String name, String birthdate, String isMale, String presentClass, String photourl, String atAddress, String cityAddress, String state, String pinCode, String district) {
        this.name = name;
        this.birthdate = birthdate;
        this.isMale = isMale;
        this.presentClass = presentClass;
        this.photourl = photourl;
        this.atAddress = atAddress;
        this.cityAddress = cityAddress;
        this.state = state;
        this.pinCode = pinCode;
        this.district = district;
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

    public String getAtAddress() {
        return atAddress;
    }

    public void setAtAddress(String atAddress) {
        this.atAddress = atAddress;
    }

    public String getCityAddress() {
        return cityAddress;
    }

    public void setCityAddress(String cityAddress) {
        this.cityAddress = cityAddress;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
