package com.example.anshuman_hp.internship;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AnotherActivity extends AppCompatActivity {
    RecyclerView videoRecycler;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference ref;
    Intent g;
    String className;
    FirebaseRecyclerAdapter<video,videoHolder> adapter;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);
        g=getIntent();
        className=g.getStringExtra("SelectedClass");
        videoRecycler=(RecyclerView)findViewById(R.id.videoRecycler);
        videoRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        actionBar=getSupportActionBar();
        actionBar.setTitle(className);
        ref=database.getReference("Videos")
                .child(className)
                .child("group");

        adapter=new FirebaseRecyclerAdapter<video, videoHolder>(video.class,
                R.layout.video_item,
                videoHolder.class,
                ref) {
            @Override
            protected void populateViewHolder(videoHolder viewHolder, final video model, int position) {
                viewHolder.videoDuration.setText("Duration "+model.getVideoDuration());
                viewHolder.videoCaption.setText(model.getVideoCaption());
                Glide.with(getApplicationContext())
                        .load(model.getVideoThumbnailUrl())
                        .into(viewHolder.videoThumbnail);
                viewHolder.shareVideo
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent share = new Intent(Intent.ACTION_SEND);
                                share.setType("text/plain");
                                share.putExtra(Intent.EXTRA_STREAM, Uri.parse(model.getVideoUrl()));
                                startActivity(Intent.createChooser(share, "Share Video"));
                            }
                        });
                viewHolder.view
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i=new Intent(AnotherActivity.this,YoutubeActivity.class);
                                i.putExtra("VideoID",model.getVideoID());
                                i.putExtra("VideoURL",model.getVideoUrl());
                                startActivity(i);
                            }
                        });

            }
        };

        videoRecycler.setAdapter(adapter);
    }
}
