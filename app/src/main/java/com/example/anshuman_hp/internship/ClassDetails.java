package com.example.anshuman_hp.internship;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Anshuman-HP on 21-08-2017.
 */

public class ClassDetails {
    HashMap<String,subjects> Tests=new HashMap<>();

    public ClassDetails() {
        Tests.put("UnitTest",new subjects());
        Tests.put("CycleTest",new subjects());
        Tests.put("HalfYearly",new subjects());
        Tests.put("PreBoard",new subjects());
        Tests.put("Boards",new subjects());
    }
}
