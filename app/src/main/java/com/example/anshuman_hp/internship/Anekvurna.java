package com.example.anshuman_hp.internship;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Anshuman-HP on 30-08-2017.
 */

public class Anekvurna extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
