package com.example.anshuman_hp.CogniChamp;

/**
 * Created by Anshuman-HP on 10-08-2017.
 */

public class User {
    String email;
    String id;
    String password;
    String mobileNumber;

    public User(String email, String id, String password, String mobileNumber) {
        this.email = email;
        this.id = id;
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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
