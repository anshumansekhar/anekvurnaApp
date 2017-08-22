package com.example.anshuman_hp.internship;

import java.util.ArrayList;

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
        Subjects.add(new subject("English","",""));
        Subjects.add(new subject("Maths","",""));
        Subjects.add(new subject("Computer Science","",""));
        Subjects.add(new subject("Hindi","",""));
    }
}
