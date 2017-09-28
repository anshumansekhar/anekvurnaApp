package com.main.cognichamp.CogniChamp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
    private static String youtubeAPIKey="AIzaSyCHE0gIbODh4UZ-KmQcSS7pOD4rVdQEYtM";
    Context ctx;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    String url;
    String caption;
    String duration;
    String publishedBy;


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
                Scanner s=new Scanner(url).useDelimiter("\\s*https://youtu.be/");
                String videoID=s.next();
                Log.e("sh",videoID);
                URL embededURL = new URL("https://www.googleapis.com/youtube/v3/videos?part=contentDetails%2Csnippet&id="+videoID+"&fields=items(contentDetails%2Fduration%2Cid%2Csnippet(channelTitle%2Cthumbnails%2Fdefault%2Ctitle))&key="+youtubeAPIKey
                );
                object=new JSONObject(IOUtils.toString(embededURL)).getJSONArray("items").getJSONObject(0);
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
            caption = jsonObject.getJSONObject("snippet").getString("title");
            duration=jsonObject.getJSONObject("contentDetails").getString("duration");
            publishedBy=jsonObject.getJSONObject("snippet").getString("channelTitle");
            Log.e("caption","ad"+caption+duration+publishedBy);
            Scanner s=new Scanner(url).useDelimiter("\\s*https://youtu.be/");
            String videoID=s.next();
            AddVideoActivity.videoItem=new video("https://img.youtube.com/vi/"+videoID+"/default.jpg",caption,duration,url,videoID,"",publishedBy);
            AddVideoActivity.setUpVideoItem();
            AddVideoActivity.dialog.hide();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
