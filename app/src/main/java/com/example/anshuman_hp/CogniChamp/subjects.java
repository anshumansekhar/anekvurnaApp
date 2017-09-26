package com.example.anshuman_hp.CogniChamp;

import java.util.ArrayList;

/**
 * Created by Anshuman-HP on 04-09-2017.
 */

public class subjects {
    ArrayList<subject> Subjects=new ArrayList<>();
    public subjects() {
        Subjects.add(new subject(Float.valueOf(0),Float.valueOf(0),"English"));
        Subjects.add(new subject(Float.valueOf(0),Float.valueOf(0),"Maths"));
    }

    public ArrayList<subject> getSubjects() {
        return Subjects;
    }

    public void setSubjects(ArrayList<subject> subjects) {
        Subjects = subjects;
    }

}
