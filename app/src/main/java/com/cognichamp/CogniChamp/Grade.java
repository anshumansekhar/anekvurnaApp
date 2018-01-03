package com.cognichamp.CogniChamp;

/**
 * Created by Anshuman-HP on 26-11-2017.
 */

public class Grade {
    String topicName;
    String grade;

    public Grade() {
    }

    public Grade(String topicName, String grade) {
        this.topicName = topicName;
        this.grade = grade;
    }

    public String getTopicName() {
        return this.topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getGrade() {
        return this.grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
