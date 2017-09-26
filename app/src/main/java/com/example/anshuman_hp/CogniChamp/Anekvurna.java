package com.example.anshuman_hp.CogniChamp;

import android.support.multidex.MultiDexApplication;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Anshuman-HP on 30-08-2017.
 */

public class Anekvurna extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
