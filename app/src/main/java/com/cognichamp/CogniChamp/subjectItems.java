package com.cognichamp.CogniChamp;

import java.util.ArrayList;

/**
 * Created by Anshuman-HP on 25-09-2017.
 */

public class subjectItems {
    ArrayList<subjectItem> Subjects=new ArrayList<>();

    public subjectItems(ArrayList<subjectItem> subjects){
        Subjects.add(new subjectItem("English"));
        Subjects.add(new subjectItem("Maths"));
    }

    public ArrayList<subjectItem> getSubjects() {
        return Subjects;
    }

    public void setSubjects(ArrayList<subjectItem> subjects) {
        Subjects = subjects;
    }
}
