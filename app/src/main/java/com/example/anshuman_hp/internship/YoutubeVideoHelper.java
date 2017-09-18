package com.example.anshuman_hp.internship;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

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


    public YoutubeVideoHelper(Context ctx,String url) {
        this.ctx = ctx;
        this.url=url;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        Log.e("Xn",params[0]);
        JSONObject object;
        try{
            if(params[0]!=null){
                URL embededURL = new URL("http://www.youtube.com/oembed?url=" +
                        params[0]+ "&format=json"
                );
                object=new JSONObject(IOUtils.toString(embededURL));
                Log.e("dgda",object.toString());
                Log.e("dg",object.getString("title"));
                return object;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e("zh",e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        try {
            caption = jsonObject.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Scanner s=new Scanner(url).useDelimiter("\\s*https://youtu.be/");
        String videoID=s.next();
        video video=new video("https://img.youtube.com/vi/"+videoID+"/default.jpg",caption,"",url,videoID,"");
        AddVideoActivity.videoItem=video;
        AddVideoActivity.setUpVideoItem();
        AddVideoActivity.dialog.hide();
    }
}
