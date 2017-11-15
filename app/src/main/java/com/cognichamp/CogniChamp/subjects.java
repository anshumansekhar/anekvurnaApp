package com.cognichamp.CogniChamp;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Anshuman-HP on 04-09-2017.
 */

public class subjects {
    HashMap<String, subject> Subjects = new HashMap<>();
    public subjects() {
        Subjects.put("English", new subject(Float.valueOf(0), Float.valueOf(0), "English"));
        Subjects.put("Maths", new subject(Float.valueOf(0), Float.valueOf(0), "Maths"));
    }

    public HashMap<String, subject> getSubjects() {
        return Subjects;
    }

    public void setSubjects(HashMap<String, subject> subjects) {
        Subjects = subjects;
    }

}
