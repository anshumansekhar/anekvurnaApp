package com.main.cognichamp.CogniChamp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Anshuman-HP on 13-09-2017.
 */

public class YoutubeVideoHelper extends AsyncTask<String ,JSONObject,JSONObject> {
    Context ctx;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    String url;
    String caption;
    String duration;
    String publishedBy;
    JSONObject object=new JSONObject();


    public YoutubeVideoHelper(Context ctx,String url) {
        this.ctx = ctx;
        this.url=url;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        Log.e("db","Inside do in backfronf");
            if(params[0]!=null){

            }
        return object;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);

    }
}
