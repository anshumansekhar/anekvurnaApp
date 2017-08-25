package com.example.anshuman_hp.internship;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class AnotherActivity extends AppCompatActivity {
    RecyclerView videoRecycler;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    DatabaseReference ref;
    Intent g;
    String className;
    FirebaseRecyclerAdapter<video,videoHolder> adapter;
    ActionBar actionBar;
    Spinner selectGroup;
    TextView groupNameText;
    String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);
        g=getIntent();
        database.getReference("UserProfile")
                .child(firebaseAuth.getCurrentUser().getUid())
                .child("presentClass")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                            className=dataSnapshot.getValue().toString();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        videoRecycler=(RecyclerView)findViewById(R.id.videoRecycler);
        selectGroup=(Spinner)findViewById(R.id.videoSpinner);
        groupNameText=(TextView)findViewById(R.id.GroupName);
        videoRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        ArrayAdapter arrayAdapter=ArrayAdapter.createFromResource(getApplicationContext()
        ,R.array.videoGroup
        ,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        selectGroup.setAdapter(arrayAdapter);

        actionBar=getSupportActionBar();
        actionBar.setTitle(className);

        selectGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                groupNameText.setText(getResources().getStringArray(R.array.videoGroup)[position]);
                groupName=""+position;
                ref=database.getReference("Videos")
                        .child(className)
                        .child(groupName);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
