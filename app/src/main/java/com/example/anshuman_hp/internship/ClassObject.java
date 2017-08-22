package com.example.anshuman_hp.internship;

import java.util.ArrayList;

/**
 * Created by Anshuman-HP on 10-08-2017.
 */

public class ClassObject {
    String schoolName;
    String classnum;
    String percentage;
    String subjectName;
    String subjectMarks;

    public ClassObject() {
    }
    public ClassObject(String schoolName, String classnum, String percentage, String subjectName, String subjectMarks) {
        this.schoolName = schoolName;
        this.classnum = classnum;
        this.percentage = percentage;
        this.subjectName = subjectName;
        this.subjectMarks = subjectMarks;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getClassnum() {
        return classnum;
    }

    public void setClassnum(String classnum) {
        this.classnum = classnum;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectMarks() {
        return subjectMarks;
    }

    public void setSubjectMarks(String subjectMarks) {
        this.subjectMarks = subjectMarks;
    }
}
