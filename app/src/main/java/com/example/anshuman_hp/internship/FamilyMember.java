package com.example.anshuman_hp.internship;

/**
 * Created by Anshuman-HP on 13-08-2017.
 */

public class FamilyMember {
    String memberName;
    String memberPhotoUrl;
    String memberRelation;
    String email;
    String phoneNumber;

    public FamilyMember(String memberName, String memberPhotoUrl, String memberRelation, String email, String phoneNumber) {
        this.memberName = memberName;
        this.memberPhotoUrl = memberPhotoUrl;
        this.memberRelation = memberRelation;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public FamilyMember() {
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberPhotoUrl() {
        return memberPhotoUrl;
    }

    public void setMemberPhotoUrl(String memberPhotoUrl) {
        this.memberPhotoUrl = memberPhotoUrl;
    }

    public String getMemberRelation() {
        return memberRelation;
    }

    public void setMemberRelation(String memberRelation) {
        this.memberRelation = memberRelation;
    }
}
