package com.example.anshuman_hp.internship;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main2Activity extends AppCompatActivity {
    ImageView notificationImage;
    String photoUrl;
    FrameLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        layout=(FrameLayout)findViewById(R.id.frame);

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                if (key.equals("IMG")) {
                    photoUrl=value;

                }
            }
        }
        for(int i=0;i<MyFirebaseMessagingService.map.size();i++)
        {
            if(!MyFirebaseMessagingService.map.get(""+i).isEmpty()){
                RadioButton rb=new RadioButton(getApplicationContext());
                rb.setText(MyFirebaseMessagingService.map.get(""+i));
            }
        }
        notificationImage=(ImageView)findViewById(R.id.notificationImage);
        Glide.with(getApplicationContext())
                .load(photoUrl)
                .into(notificationImage);
    }
}
