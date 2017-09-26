package com.example.anshuman_hp.CogniChamp;

/**
 * Created by Anshuman-HP on 16-08-2017.
 */

public class subject {
    float subMarks;
    float totalMarks;
    String subjectName;

    public subject() {
    }

    public subject(float subMarks, float totalMarks, String subjectName) {
        this.subMarks = subMarks;
        this.totalMarks = totalMarks;
        this.subjectName = subjectName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public float getSubMarks() {
        return subMarks;
    }

    public void setSubMarks(float subMarks) {
        this.subMarks = subMarks;
    }

    public float getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(float totalMarks) {
        this.totalMarks = totalMarks;
    }
}
