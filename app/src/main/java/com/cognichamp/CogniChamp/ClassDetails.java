package com.cognichamp.CogniChamp;

import android.support.annotation.Keep;

import java.util.HashMap;

/**
 * Created by Anshuman-HP on 21-08-2017.
 */
@Keep
public class ClassDetails {
    HashMap<String,subjects> Tests=new HashMap<>();
//    HashMap<String,subjectItem> Subjects=new HashMap<>();

    public ClassDetails() {
        Tests.put("UnitTest",new subjects());
        Tests.put("CycleTest",new subjects());
        Tests.put("HalfYearly",new subjects());
        Tests.put("Yearly",new subjects());
//        Subjects.put("0",new subjectItem("English"));
//        Subjects.put("1",new subjectItem("Maths"));
    }
    public ClassDetails(int n){
        Tests.put("UnitTest",new subjects());
        Tests.put("CycleTest",new subjects());
        Tests.put("HalfYearly",new subjects());
        Tests.put("Pre-Boards",new subjects());
        Tests.put("Boards",new subjects());

//        Subjects.put("0",new subjectItem("English"));
//        Subjects.put("1",new subjectItem("Maths"));
    }

    public HashMap<String, subjects> getTests() {
        return Tests;
    }

    public void setTests(HashMap<String, subjects> tests) {
        Tests = tests;
    }

//    public HashMap<String, subjectItem> getSubjects() {
//        return Subjects;
//    }
//
//    public void setSubjects(HashMap<String, subjectItem> subjects) {
//        Subjects = subjects;
//    }
}
