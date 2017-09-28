package com.main.cognichamp.CogniChamp;

import android.support.multidex.MultiDexApplication;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Anshuman-HP on 30-08-2017.
 */

public class CogniChamp extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        RequestQueue queue = Volley.newRequestQueue(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
