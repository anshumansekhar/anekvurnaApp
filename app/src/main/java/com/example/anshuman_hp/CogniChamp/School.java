package com.example.anshuman_hp.CogniChamp;

/**
 * Created by Anshuman-HP on 03-09-2017.
 */

public class School {
    String schoolAddress;
    String schoolName;
    String schoolLogo;
    String schoolPin;
    String schoolState;
    String schoolCity;

    public School() {
    }

    public School(String schoolAddress, String schoolName, String schoolLogo, String schoolPin, String schoolState, String schoolCity) {
        this.schoolAddress = schoolAddress;
        this.schoolName = schoolName;
        this.schoolLogo = schoolLogo;
        this.schoolPin = schoolPin;
        this.schoolState = schoolState;
        this.schoolCity = schoolCity;
    }

    public String getSchoolAddress() {
        return schoolAddress;
    }

    public void setSchoolAddress(String schoolAddress) {
        this.schoolAddress = schoolAddress;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolLogo() {
        return schoolLogo;
    }

    public void setSchoolLogo(String schoolLogo) {
        this.schoolLogo = schoolLogo;
    }

    public String getSchoolPin() {
        return schoolPin;
    }

    public void setSchoolPin(String schoolPin) {
        this.schoolPin = schoolPin;
    }

    public String getSchoolState() {
        return schoolState;
    }

    public void setSchoolState(String schoolState) {
        this.schoolState = schoolState;
    }

    public String getSchoolCity() {
        return schoolCity;
    }

    public void setSchoolCity(String schoolCity) {
        this.schoolCity = schoolCity;
    }
}
