package com.example.anshuman_hp.internship;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Scanner;

public class AddVideoActivity extends AppCompatActivity {
    static ProgressDialog dialog;
    Spinner subject,topic;
    CardView videoItemLayout;
    static ImageView videoThumbnail;
    static TextView videoCaption,videoDuration;
    Button addVideo;
    LinearLayout buttonsVideoItem;

    ArrayList subjectsList=new ArrayList();
    ArrayList topicsList=new ArrayList();
    ArrayAdapter subjectAdapter,topicAdapter;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    String className;
    String subjectName;
    String topicName;

    Intent j;
    static video videoItem;
    static Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);
        ctx=AddVideoActivity.this;
        subject=(Spinner)findViewById(R.id.subjectAddApinner);
        topic=(Spinner)findViewById(R.id.topicAddSpinner);
        videoItemLayout=(CardView)findViewById(R.id.videoItemLayout);
        videoThumbnail=(ImageView)videoItemLayout.findViewById(R.id.videoThumbnail);
        videoCaption=(TextView)videoItemLayout.findViewById(R.id.videoCaption);
        videoDuration=(TextView)videoItemLayout.findViewById(R.id.videoDuration);
        addVideo=(Button)findViewById(R.id.AddVideo);
        buttonsVideoItem=(LinearLayout)videoItemLayout.findViewById(R.id.buttonsVideoItem);
        buttonsVideoItem.setVisibility(View.INVISIBLE);

        subjectAdapter=new ArrayAdapter(AddVideoActivity.this,android.R.layout.simple_spinner_item,subjectsList);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subject.setAdapter(subjectAdapter);

        topicAdapter=new ArrayAdapter(AddVideoActivity.this,android.R.layout.simple_spinner_item,topicsList);
        topicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        topic.setAdapter(topicAdapter);

        dialog=new ProgressDialog(AddVideoActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Alert");
        dialog.setIndeterminate(true);
        dialog.setMessage("Please wait while the categories are loaded");
        dialog.show();

        j=getIntent();
        String action=j.getAction();

        if (Intent.ACTION_SEND.equals(action)) {
            final String sharedText = j.getStringExtra(Intent.EXTRA_TEXT);
            if(sharedText!=null)
            {
                YoutubeVideoHelper helper=new YoutubeVideoHelper(AddVideoActivity.this,sharedText);
                dialog.show();
                helper.execute(sharedText);
            }
        }

        database.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("UserProfile")
                .child("presentClass")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            subjectsList.clear();
                            subjectAdapter.clear();
                            className=dataSnapshot.getValue().toString();
                            if(className.equals("11")){
                                className=className+"(Arts)";
                            }
                            else if(className.equals("12")){
                                className="11"+"(Commerce)";
                            }
                            else if(className.equals("13")){
                                className="11"+"(Science)";
                            }
                            else if(className.equals("14")){
                                className="12"+"(Arts)";
                            }
                            else if(className.equals("15")){
                                className="12"+"(Commerce)";
                            }
                            else if(className.equals("16")){
                                className="12"+"(Science)";
                            }
                            className="Class-"+className;
                            database.getReference("Subjects")
                                    .child(className)
                                    .child("Subjects")
                                    .addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                            subjectsList.add(dataSnapshot.getValue(subjectItem.class).getSubjectName());
                                            dialog.hide();
                                            subjectAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                            subjectsList.add(dataSnapshot.getValue(subjectItem.class).getSubjectName());
                                            dialog.hide();
                                            subjectAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                topicsList.clear();
                topicAdapter.clear();
                subjectName=subjectsList.get(position).toString();
                getTopics(subjectName);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        topic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                topicName=topicsList.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(AddVideoActivity.this);
                builder.setMessage("Confirmation");
                builder.setMessage("Add This video ");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddVideoToDatabase();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });

    }
    void AddVideoToDatabase(){
        //TODO add to database
        database.getReference("Videos")
                .child(className)
                .child(subjectName)
                .child(topicName)
                .child(videoItem.getVideoID())
                .setValue(videoItem);
        database.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("Favorites")
                .child(videoItem.getVideoID())
                .setValue(videoItem)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //TODO goto navigation drawer activity
                        startActivity(new Intent(AddVideoActivity.this,NavigationDrawer.class));
                    }
                });
    }
    public void getTopics(String subName){
        database.getReference("Subjects")
                .child(className)
                .child(subName)
                .child("topics")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        topicsList.add(dataSnapshot.getValue(subjectItem.class).getSubjectName());
                        topicAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        topicsList.add(dataSnapshot.getValue(subjectItem.class).getSubjectName());
                        topicAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
    public static void setUpVideoItem(){
        videoCaption.setText(videoItem.getVideoCaption());
        videoDuration.setText(videoItem.getVideoDuration());
        Glide.with(ctx)
                .load(videoItem.getVideoThumbnailUrl())
                .into(videoThumbnail);
    }
}
