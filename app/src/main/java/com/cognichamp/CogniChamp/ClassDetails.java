package com.cognichamp.CogniChamp;

import android.support.annotation.Keep;

import java.util.HashMap;

/**
 * Created by Anshuman-HP on 21-08-2017.
 */
@Keep
public class ClassDetails {
    HashMap<String,subjects> Tests=new HashMap<>();

    public ClassDetails() {
        Tests.put("UnitTest-1", new subjects());
        Tests.put("UnitTest-2", new subjects());
        Tests.put("CycleTest-1", new subjects());
        Tests.put("CycleTest-2", new subjects());
        Tests.put("HalfYearly",new subjects());
        Tests.put("Yearly",new subjects());
    }
    public ClassDetails(int n){
        Tests.put("UnitTest-1", new subjects());
        Tests.put("UnitTest-2", new subjects());
        Tests.put("CycleTest-1", new subjects());
        Tests.put("CycleTest-2", new subjects());
        Tests.put("HalfYearly",new subjects());
        Tests.put("Pre-Board", new subjects());
        Tests.put("Board", new subjects());
    }

    public HashMap<String, subjects> getTests() {
        return Tests;
    }

    public void setTests(HashMap<String, subjects> tests) {
        Tests = tests;
    }

}
