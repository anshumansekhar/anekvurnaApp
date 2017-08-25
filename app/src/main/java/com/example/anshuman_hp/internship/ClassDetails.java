package com.example.anshuman_hp.internship;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Anshuman-HP on 21-08-2017.
 */

public class ClassDetails {
    String SchoolName;
    ArrayList<subject> Subjects=new ArrayList<>();
    String percentage;

    public ClassDetails() {
        SchoolName = "";
        percentage = "";
        Subjects.add(new subject(Float.valueOf(0),Float.valueOf(0),"English"));
        Subjects.add(new subject(Float.valueOf(0),Float.valueOf(0),"Maths"));
        Subjects.add(new subject(Float.valueOf(0),Float.valueOf(0),"Hindi"));
    }
}
