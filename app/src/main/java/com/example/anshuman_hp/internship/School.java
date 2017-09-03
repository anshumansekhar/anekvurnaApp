package com.example.anshuman_hp.internship;

/**
 * Created by Anshuman-HP on 03-09-2017.
 */

public class School {
    String schoolName;
    String state;
    String city;

    public School() {
    }

    public School(String schoolName, String state, String city) {
        this.schoolName = schoolName;
        this.state = state;
        this.city = city;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
