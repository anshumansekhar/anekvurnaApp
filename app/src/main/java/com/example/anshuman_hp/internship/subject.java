package com.example.anshuman_hp.internship;

/**
 * Created by Anshuman-HP on 16-08-2017.
 */

public class subject {
    String subName;
    String subMarks;
    String totalMarks;

    public subject() {
    }

    public subject(String subName, String subMarks, String totalMarks) {
        this.subName = subName;
        this.subMarks = subMarks;
        this.totalMarks = totalMarks;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getSubMarks() {
        return subMarks;
    }

    public void setSubMarks(String subMarks) {
        this.subMarks = subMarks;
    }

    public String getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(String totalMarks) {
        this.totalMarks = totalMarks;
    }
}
