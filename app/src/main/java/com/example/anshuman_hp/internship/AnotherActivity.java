package com.example.anshuman_hp.internship;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

public class AnotherActivity extends AppCompatActivity {

    RecyclerView videoRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);

        videoRecycler=(RecyclerView)findViewById(R.id.videoRecycler);


    }
}
