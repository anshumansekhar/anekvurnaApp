package com.cognichamp.CogniChamp;

/**
 * Created by Anshuman-HP on 16-08-2017.
 */

public class subject {
    int subMarks;
    int totalMarks;
    String subjectName;

    public subject() {
    }

    public subject(int subMarks, int totalMarks, String subjectName) {
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

    public int getSubMarks() {
        return this.subMarks;
    }

    public void setSubMarks(int subMarks) {
        this.subMarks = subMarks;
    }

    public int getTotalMarks() {
        return this.totalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }
}
