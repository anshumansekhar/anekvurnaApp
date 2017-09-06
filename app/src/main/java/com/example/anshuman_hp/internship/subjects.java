package com.example.anshuman_hp.internship;

import java.util.ArrayList;

/**
 * Created by Anshuman-HP on 04-09-2017.
 */

public class subjects {
    ArrayList<subject> Subjects=new ArrayList<>();
    String percentage;
    public subjects() {
        percentage="";
        Subjects.add(new subject(Float.valueOf(0),Float.valueOf(0),"English"));
        Subjects.add(new subject(Float.valueOf(0),Float.valueOf(0),"Maths"));
    }

}
