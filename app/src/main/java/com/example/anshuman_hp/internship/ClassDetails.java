package com.example.anshuman_hp.internship;

import android.support.annotation.Keep;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Anshuman-HP on 21-08-2017.
 */
@Keep
public class ClassDetails {
    HashMap<String,subjects> Tests=new HashMap<>();

    public ClassDetails() {
        Tests.put("UnitTest",new subjects());
        Tests.put("CycleTest",new subjects());
        Tests.put("HalfYearly",new subjects());
        Tests.put("Yearly",new subjects());
    }
    public ClassDetails(int n){
        Tests.put("UnitTest",new subjects());
        Tests.put("CycleTest",new subjects());
        Tests.put("HalfYearly",new subjects());
        Tests.put("Pre-Boards",new subjects());
        Tests.put("Boards",new subjects());
    }

    public HashMap<String, subjects> getTests() {
        return Tests;
    }

    public void setTests(HashMap<String, subjects> tests) {
        Tests = tests;
    }
}
