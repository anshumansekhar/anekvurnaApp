package com.cognichamp.CogniChamp;

import android.util.Log;

/**
 * Created by Anshuman-HP on 23-11-2017.
 */

public class ReportCardItem {
    String testType;
    String subjectNames;
    String marksText;
    String percentage;
    String comments;

    public ReportCardItem() {
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getTestType() {
        return this.testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getSubjectNames() {
        return this.subjectNames;
    }

    public void setSubjectNames(String subjectNames) {
        this.subjectNames = subjectNames;
    }

    public String getMarksText() {
        return this.marksText;
    }

    public void setMarksText(String marksText) {
        this.marksText = marksText;
    }

    public void addTosubjects(String subject, String marks) {
        if (!subjectNames.contains(subject)) {
            subjectNames = subjectNames + subject + "\n";
            marksText = marksText + marks + "\n";
            Log.e("marks", marksText);
        } else {

        }
    }

    public String getPercentage() {
        return this.percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
