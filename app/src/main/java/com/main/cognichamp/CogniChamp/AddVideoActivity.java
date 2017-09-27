package com.main.cognichamp.CogniChamp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class AddVideoActivity extends AppCompatActivity {
    static ProgressDialog dialog;
    Spinner subject,topic,Class;
    CardView videoItemLayout;
    static ImageView videoThumbnail;
    static TextView videoCaption,videoDuration,publishedBy;
    Button addVideo;
    LinearLayout buttonsVideoItem;

    ArrayList subjectsList=new ArrayList();
    ArrayList topicsList=new ArrayList();
    ArrayList classList=new ArrayList();
    ArrayAdapter subjectAdapter,topicAdapter,classAdapter;

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
        Class=(Spinner)findViewById(R.id.classSpinner);
        videoItemLayout=(CardView)findViewById(R.id.videoItemLayout);
        videoThumbnail=(ImageView)videoItemLayout.findViewById(R.id.videoThumbnail);
        videoCaption=(TextView)videoItemLayout.findViewById(R.id.videoCaption);
        videoDuration=(TextView)videoItemLayout.findViewById(R.id.videoDuration);
        publishedBy=(TextView)videoItemLayout.findViewById(R.id.videoPublishedBy);
        addVideo=(Button)findViewById(R.id.AddVideo);
        buttonsVideoItem=(LinearLayout)videoItemLayout.findViewById(R.id.buttonsVideoItem);
        buttonsVideoItem.setVisibility(View.INVISIBLE);
        classList=new ArrayList(Arrays.asList(getResources().getStringArray(R.array.ClassWithStream)));
        classAdapter=new ArrayAdapter(AddVideoActivity.this,android.R.layout.simple_spinner_item,classList);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Class.setAdapter(classAdapter);
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
        if(firebaseAuth.getCurrentUser()!=null) {

            database.getReference(firebaseAuth.getCurrentUser().getUid())
                    .child("UserProfile")
                    .child("presentClass")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                subjectsList.clear();
                                subjectAdapter.clear();
                                className = dataSnapshot.getValue().toString();
                                if (className.equals("11")) {
                                    className = "Class-" + className + "(Arts)";
                                } else if (className.equals("0")) {
                                    className = "Age (0-1) yrs";
                                } else if (className.equals("1")) {
                                    className = "Age (1-2) yrs";
                                } else if (className.equals("2")) {
                                    className = "Age (2-3) yrs";
                                } else if (className.equals("3")) {
                                    className = "Age (3-4) yrs";
                                } else if (className.equals("4")) {
                                    className = "Age (4-5) yrs";
                                } else if (className.equals("5")) {
                                    className = "Age (5-6) yrs";
                                } else if (className.equals("12")) {
                                    className = "Class-" + "11" + "(Commerce)";
                                } else if (className.equals("13")) {
                                    className = "Class-" + "11" + "(Science)";
                                } else if (className.equals("14")) {
                                    className = "Class-" + "12" + "(Arts)";
                                } else if (className.equals("15")) {
                                    className = "Class-" + "12" + "(Commerce)";
                                } else if (className.equals("16")) {
                                    className = "Class-" + "12" + "(Science)";
                                } else {
                                    className = "Class-" + (Integer.valueOf(className) - 6);
                                }
                                Log.e("aj", className);
                                Log.e("sfbk", "" + classList.indexOf(className));
                                Class.setSelection(classList.indexOf(className));
                                getSubjects(className);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
        else{
            Toast.makeText(AddVideoActivity.this,"Please Sign in First",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddVideoActivity.this,SignUpChooseActivity.class));
        }
        Class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subjectsList.clear();
                subjectAdapter.clear();
                topicsList.clear();
                topicAdapter.clear();
                className=classList.get(position).toString();
                getSubjects(className);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                topicsList.clear();
                topicAdapter.clear();
                subjectName=subjectsList.get(position).toString();
                if(className.contains("Age")) {
                    getAgeTopics();
                }
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
        if(className.equals("Age")){
            topicName="videos";
        }
        database.getReference("Videos")
                .child(className)
                .child(subjectName)
                .child(topicName)
                .child(videoItem.getVideoID())
                .setValue(videoItem);
        database.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("Favorites")
                .child(className)
                .child("subjects")
                .child(subjectName)
                .setValue(new subjectItem(subjectName));
        database.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("Favorites")
                .child(className)
                .child("topics")
                .child(subjectName)
                .push()
                .setValue(new subjectItem(topicName));
        database.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("Favorites")
                .child(className)
                .child("videos")
                .child(subjectName)
                .child(topicName)
                .child(videoItem.getVideoID())
                .setValue(videoItem)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(AddVideoActivity.this,NavigationDrawer.class));
                    }
                });

    }
    public void getSubjects(final String Class){
        if(!Class.contains("Age")) {
            database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("ClassDetails")
                    .child(Class)
                    .child("subjects")
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
        else{
            database.getReference("Subjects")
                    .child(Class)
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
                            if(subjectsList.isEmpty()){
                                Toast.makeText(AddVideoActivity.this,"No Subjects Present for the selected Class ",Toast.LENGTH_SHORT).show();
                            }
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
        if(videoItem.getVideoDuration().length()==5){
            videoDuration.setText("0:"+videoItem.getVideoDuration().substring(2,4));
        }
        else if(videoItem.getVideoDuration().length()==7){
            videoDuration.setText(videoItem.getVideoDuration().substring(2,2)+":"+videoItem.getVideoDuration().substring(4,5));
        }
        else if(videoItem.getVideoDuration().length()==8){
            videoDuration.setText(videoItem.getVideoDuration().substring(2,3)+":"+videoItem.getVideoDuration().substring(5,6));
        }
        else if(videoItem.getVideoDuration().length()==9){
            videoDuration.setText(videoItem.getVideoDuration().substring(2,2)+":"+videoItem.getVideoDuration().substring(6,8)+":"+videoItem.getVideoDuration().substring(10,12));
        }
        publishedBy.setText("By: "+videoItem.getPublishedBy());
        Glide.with(ctx)
                .load(videoItem.getVideoThumbnailUrl())
                .into(videoThumbnail);
    }
    public void getAgeTopics(){
        topicsList.add("videos");
        topicAdapter.notifyDataSetChanged();
    }
}
