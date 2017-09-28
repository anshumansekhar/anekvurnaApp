package com.main.cognichamp.CogniChamp;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Anshuman-HP on 18-08-2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
    }
}
