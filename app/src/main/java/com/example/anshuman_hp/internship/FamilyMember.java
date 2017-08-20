package com.example.anshuman_hp.internship;

/**
 * Created by Anshuman-HP on 13-08-2017.
 */

public class FamilyMember {
    String memberName;
    String memberPhotoUrl;
    String memberRelation;

    public FamilyMember(String memberName, String memberPhotoUrl, String memberRelation) {
        this.memberName = memberName;
        this.memberPhotoUrl = memberPhotoUrl;
        this.memberRelation = memberRelation;
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
