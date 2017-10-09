package com.cognichamp.CogniChamp;

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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class AddVideoActivity extends AppCompatActivity {
    static ProgressDialog dialog;
    Spinner subject,topic,Class;
    CardView videoLayout;
    static ImageView videoThumbnail;
    static TextView videoCaption,videoDuration,publishedBy;
    Button addVideo;
    LinearLayout buttonsVideoItem;

    private static String youtubeAPIKey="AIzaSyCHE0gIbODh4UZ-KmQcSS7pOD4rVdQEYtM";


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
    video video;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);
        ctx=AddVideoActivity.this;
        subject=(Spinner)findViewById(R.id.subjectAddApinner);
        topic=(Spinner)findViewById(R.id.topicAddSpinner);
        Class=(Spinner)findViewById(R.id.classSpinner);
        videoLayout=(CardView)findViewById(R.id.videoItemLayout);
        videoThumbnail=(ImageView)videoLayout.findViewById(R.id.videoThumbnail);
        videoCaption=(TextView)videoLayout.findViewById(R.id.videoCaption);
        videoDuration=(TextView)videoLayout.findViewById(R.id.videoDuration);
        publishedBy=(TextView)videoLayout.findViewById(R.id.videoPublishedBy);
        addVideo=(Button)findViewById(R.id.AddVideo);
        buttonsVideoItem=(LinearLayout)videoLayout.findViewById(R.id.buttonsVideoItem);
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
                Scanner s=new Scanner(sharedText).useDelimiter("\\s*https://youtu.be/");
                String videoID=s.next();
                URL embededURL = null;
                try {
                    embededURL = new URL("https://www.googleapis.com/youtube/v3/videos?part=contentDetails%2Csnippet&id="+videoID+"&fields=items(contentDetails%2Fduration%2Cid%2Csnippet(channelTitle%2Cthumbnails%2Fdefault%2Ctitle))&key="+youtubeAPIKey);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                dialog.show();
                RequestQueue queue = Volley.newRequestQueue(ctx);
                try {
                    JsonObjectRequest request = new JsonObjectRequest
                            (embededURL.toString(), null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        response=response.getJSONArray("items").getJSONObject(0);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Log.e("db", response.toString());
                                    dialog.hide();
                                    String caption = null;
                                    try {
                                        caption = response.getJSONObject("snippet").getString("title");
                                        videoCaption.setText(caption);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    String duration = null;
                                    try {
                                        duration = response.getJSONObject("contentDetails").getString("duration");
                                        if(duration.substring(2,duration.length()-1).replaceAll("[^0-9]", ":").length()<=2){
                                            duration="0:"+duration.substring(2,duration.length()-1).replaceAll("[^0-9]", ":");
                                        }else {
                                            duration=duration.substring(2,duration.length()-1).replaceAll("[^0-9]", ":");
                                        }
                                        videoDuration.setText(duration);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    String publishedby = null;
                                    try {
                                        publishedby = response.getJSONObject("snippet").getString("channelTitle");
                                        publishedBy.setText(publishedby);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Scanner s = new Scanner(sharedText).useDelimiter("\\s*https://youtu.be/");
                                    String videoID = s.next();
                                    Glide.with(ctx)
                                            .load("https://img.youtube.com/vi/" + videoID + "/default.jpg")
                                            .into(videoThumbnail);
                                    video=new video("https://img.youtube.com/vi/" + videoID + "/default.jpg"
                                            ,caption
                                    ,duration
                                    ,sharedText
                                    ,videoID
                                    ,""
                                    ,publishedby);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO Auto-generated method stub

                                    Log.e("err", error.toString());
                                }
                            });
                    queue.add(request);
                    queue.start();
                }catch (Exception e){
                    Log.e("db",e.toString());
                }

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
                        AddVideoToDatabase(video);
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
    void AddVideoToDatabase(video v){
        if(className.equals("Age")){
            topicName="videos";
        }
        database.getReference("Videos")
                .child(className)
                .child(subjectName)
                .child(topicName)
                .child(v.getVideoID())
                .setValue(v);
        database.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("Favorites")
                .child(className)
                .child("tests")
                .child("subjects")
                .child(subjectName)
                .setValue(new subjectItem(subjectName));
        if(className.contains("Age")){
            database.getReference(firebaseAuth.getCurrentUser().getUid())
                    .child("Favorites")
                    .child(className)
                    .child("tests")
                    .child("topics")
                    .child(subjectName)
                    .child("videos")
                    .setValue(new subjectItem(topicName));

        }else {
            database.getReference(firebaseAuth.getCurrentUser().getUid())
                    .child("Favorites")
                    .child(className)
                    .child("tests")
                    .child("topics")
                    .child(subjectName)
                    .push()
                    .setValue(new subjectItem(topicName));
        }
        database.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("Favorites")
                .child(className)
                .child("tests")
                .child("videos")
                .child(subjectName)
                .child(topicName)
                .child(video.getVideoID())
                .setValue(video)
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
                    .child("tests")
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
    public  void setUpVideoItem(video v){
        videoCaption.setText(v.getVideoCaption());
        String duration;
        if(v.getVideoDuration().substring(2,v.getVideoDuration().length()-1).replaceAll("[^0-9]", ":").length()<=2){
            duration="0:"+v.getVideoDuration().substring(2,v.getVideoDuration().length()-1).replaceAll("[^0-9]", ":");
        }else {
            duration=v.getVideoDuration().substring(2,v.getVideoDuration().length()-1).replaceAll("[^0-9]", ":");
        }
        videoDuration.setText(duration);
        publishedBy.setText("By: "+v.getPublishedBy());
        Glide.with(ctx)
                .load(v.getVideoThumbnailUrl())
                .into(videoThumbnail);
    }
    public void getAgeTopics(){
        topicsList.add("videos");
        topicAdapter.notifyDataSetChanged();
    }
}
