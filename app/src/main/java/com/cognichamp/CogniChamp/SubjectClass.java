package com.cognichamp.CogniChamp;

import com.google.firebase.database.GenericTypeIndicator;

import java.util.List;

/**
 * Created by Anshuman-HP on 09-09-2017.
 */

public class SubjectClass extends GenericTypeIndicator {

    String subjectName;
    List topics;

    public SubjectClass() {
    }

    public SubjectClass(String subjectName, List topics) {
        this.subjectName = subjectName;
        this.topics = topics;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public List getTopics() {
        return topics;
    }

    public void setTopics(List topics) {
        this.topics = topics;
    }
}
