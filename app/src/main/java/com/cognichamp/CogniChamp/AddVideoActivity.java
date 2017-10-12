package com.cognichamp.CogniChamp;

import android.R.layout;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.cognichamp.CogniChamp.R.array;
import com.cognichamp.CogniChamp.R.id;
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
    private static final String youtubeAPIKey = "AIzaSyCHE0gIbODh4UZ-KmQcSS7pOD4rVdQEYtM";
    static ProgressDialog dialog;
    static ImageView videoThumbnail;
    static TextView videoCaption,videoDuration,publishedBy;
    Spinner subject, topic, Class;
    CardView videoLayout;
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
    video video;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_add_video);
        this.ctx = this;
        this.subject = (Spinner) this.findViewById(id.subjectAddApinner);
        this.topic = (Spinner) this.findViewById(id.topicAddSpinner);
        this.Class = (Spinner) this.findViewById(id.classSpinner);
        this.videoLayout = (CardView) this.findViewById(id.videoItemLayout);
        AddVideoActivity.videoThumbnail = (ImageView) this.videoLayout.findViewById(id.videoThumbnail);
        AddVideoActivity.videoCaption = (TextView) this.videoLayout.findViewById(id.videoCaption);
        AddVideoActivity.videoDuration = (TextView) this.videoLayout.findViewById(id.videoDuration);
        AddVideoActivity.publishedBy = (TextView) this.videoLayout.findViewById(id.videoPublishedBy);
        this.addVideo = (Button) this.findViewById(id.AddVideo);
        this.buttonsVideoItem = (LinearLayout) this.videoLayout.findViewById(id.buttonsVideoItem);
        this.buttonsVideoItem.setVisibility(View.INVISIBLE);
        this.classList = new ArrayList(Arrays.asList(this.getResources().getStringArray(array.ClassWithStream)));
        this.classAdapter = new ArrayAdapter(this, layout.simple_spinner_item, this.classList);
        this.classAdapter.setDropDownViewResource(layout.simple_spinner_dropdown_item);
        this.Class.setAdapter(this.classAdapter);
        this.subjectAdapter = new ArrayAdapter(this, layout.simple_spinner_item, this.subjectsList);
        this.subjectAdapter.setDropDownViewResource(layout.simple_spinner_dropdown_item);
        this.subject.setAdapter(this.subjectAdapter);

        this.topicAdapter = new ArrayAdapter(this, layout.simple_spinner_item, this.topicsList);
        this.topicAdapter.setDropDownViewResource(layout.simple_spinner_dropdown_item);
        this.topic.setAdapter(this.topicAdapter);

        AddVideoActivity.dialog = new ProgressDialog(this);
        AddVideoActivity.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        AddVideoActivity.dialog.setTitle("Alert");
        AddVideoActivity.dialog.setIndeterminate(true);
        AddVideoActivity.dialog.setMessage("Please wait while the categories are loaded");
        AddVideoActivity.dialog.show();

        this.j = this.getIntent();
        String action = this.j.getAction();

        if (Intent.ACTION_SEND.equals(action)) {
            final String sharedText = this.j.getStringExtra(Intent.EXTRA_TEXT);
            if(sharedText!=null)
            {
                Scanner s=new Scanner(sharedText).useDelimiter("\\s*https://youtu.be/");
                String videoID=s.next();
                URL embededURL = null;
                try {
                    embededURL = new URL("https://www.googleapis.com/youtube/v3/videos?part=contentDetails%2Csnippet&id=" + videoID + "&fields=items(contentDetails%2Fduration%2Cid%2Csnippet(channelTitle%2Cthumbnails%2Fdefault%2Ctitle))&key=" + AddVideoActivity.youtubeAPIKey);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                AddVideoActivity.dialog.show();
                RequestQueue queue = Volley.newRequestQueue(this.ctx);
                try {
                    JsonObjectRequest request = new JsonObjectRequest
                            (embededURL.toString(), null, new Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        response=response.getJSONArray("items").getJSONObject(0);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Log.e("db", response.toString());
                                    AddVideoActivity.dialog.hide();
                                    String caption = null;
                                    try {
                                        caption = response.getJSONObject("snippet").getString("title");
                                        AddVideoActivity.videoCaption.setText(caption);
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
                                        AddVideoActivity.videoDuration.setText(duration);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    String publishedby = null;
                                    try {
                                        publishedby = response.getJSONObject("snippet").getString("channelTitle");
                                        AddVideoActivity.publishedBy.setText(publishedby);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Scanner s = new Scanner(sharedText).useDelimiter("\\s*https://youtu.be/");
                                    String videoID = s.next();
                                    Glide.with(AddVideoActivity.this.ctx)
                                            .load("https://img.youtube.com/vi/" + videoID + "/default.jpg")
                                            .into(AddVideoActivity.videoThumbnail);
                                    AddVideoActivity.this.video = new video("https://img.youtube.com/vi/" + videoID + "/default.jpg"
                                            ,caption
                                    ,duration
                                    ,sharedText
                                    ,videoID
                                    ,""
                                    ,publishedby);
                                }
                            }, new ErrorListener() {
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
        if (this.firebaseAuth.getCurrentUser() != null) {

            this.database.getReference(this.firebaseAuth.getCurrentUser().getUid())
                    .child("UserProfile")
                    .child("presentClass")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                AddVideoActivity.this.subjectsList.clear();
                                AddVideoActivity.this.subjectAdapter.clear();
                                AddVideoActivity.this.className = dataSnapshot.getValue().toString();
                                if (AddVideoActivity.this.className.equals("11")) {
                                    AddVideoActivity.this.className = "Class-" + AddVideoActivity.this.className + "(Arts)";
                                } else if (AddVideoActivity.this.className.equals("0")) {
                                    AddVideoActivity.this.className = "Age (0-1) yrs";
                                } else if (AddVideoActivity.this.className.equals("1")) {
                                    AddVideoActivity.this.className = "Age (1-2) yrs";
                                } else if (AddVideoActivity.this.className.equals("2")) {
                                    AddVideoActivity.this.className = "Age (2-3) yrs";
                                } else if (AddVideoActivity.this.className.equals("3")) {
                                    AddVideoActivity.this.className = "Age (3-4) yrs";
                                } else if (AddVideoActivity.this.className.equals("4")) {
                                    AddVideoActivity.this.className = "Age (4-5) yrs";
                                } else if (AddVideoActivity.this.className.equals("5")) {
                                    AddVideoActivity.this.className = "Age (5-6) yrs";
                                } else if (AddVideoActivity.this.className.equals("12")) {
                                    AddVideoActivity.this.className = "Class-" + "11" + "(Commerce)";
                                } else if (AddVideoActivity.this.className.equals("13")) {
                                    AddVideoActivity.this.className = "Class-" + "11" + "(Science)";
                                } else if (AddVideoActivity.this.className.equals("14")) {
                                    AddVideoActivity.this.className = "Class-" + "12" + "(Arts)";
                                } else if (AddVideoActivity.this.className.equals("15")) {
                                    AddVideoActivity.this.className = "Class-" + "12" + "(Commerce)";
                                } else if (AddVideoActivity.this.className.equals("16")) {
                                    AddVideoActivity.this.className = "Class-" + "12" + "(Science)";
                                } else {
                                    AddVideoActivity.this.className = "Class-" + (Integer.valueOf(AddVideoActivity.this.className) - 6);
                                }
                                AddVideoActivity.this.Class.setSelection(AddVideoActivity.this.classList.indexOf(AddVideoActivity.this.className));
                                AddVideoActivity.this.getSubjects(AddVideoActivity.this.className);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
        else{
            Toast.makeText(this, "Please Sign in First", Toast.LENGTH_SHORT).show();
            this.startActivity(new Intent(this, SignUpChooseActivity.class));
        }
        this.Class.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AddVideoActivity.this.subjectsList.clear();
                AddVideoActivity.this.subjectAdapter.clear();
                AddVideoActivity.this.topicsList.clear();
                AddVideoActivity.this.topicAdapter.clear();
                AddVideoActivity.this.className = AddVideoActivity.this.classList.get(position).toString();
                AddVideoActivity.this.getSubjects(AddVideoActivity.this.className);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        this.subject.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AddVideoActivity.this.topicsList.clear();
                AddVideoActivity.this.topicAdapter.clear();
                AddVideoActivity.this.subjectName = AddVideoActivity.this.subjectsList.get(position).toString();
                if (AddVideoActivity.this.className.contains("Age")) {
                    AddVideoActivity.this.getAgeTopics();
                }
                AddVideoActivity.this.getTopics(AddVideoActivity.this.subjectName);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        this.topic.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AddVideoActivity.this.topicName = AddVideoActivity.this.topicsList.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        this.addVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Builder builder = new Builder(AddVideoActivity.this);
                builder.setMessage("Confirmation");
                builder.setMessage("Add This video ");
                builder.setPositiveButton("OK", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddVideoActivity.this.AddVideoToDatabase(AddVideoActivity.this.video);
                    }
                }).setNegativeButton("Cancel", new OnClickListener() {
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
        if (this.className.equals("Age")) {
            this.topicName = "videos";
        }
        this.database.getReference("Videos")
                .child(this.className)
                .child(this.subjectName)
                .child(this.topicName)
                .child(v.getVideoID())
                .setValue(v);
        this.database.getReference(this.firebaseAuth.getCurrentUser().getUid())
                .child("Favorites")
                .child(this.className)
                .child("tests")
                .child("subjects")
                .child(this.subjectName)
                .setValue(new subjectItem(this.subjectName));
        if (this.className.contains("Age")) {
            this.database.getReference(this.firebaseAuth.getCurrentUser().getUid())
                    .child("Favorites")
                    .child(this.className)
                    .child("tests")
                    .child("topics")
                    .child(this.subjectName)
                    .child("videos")
                    .setValue(new subjectItem(this.topicName));

        }else {
            this.database.getReference(this.firebaseAuth.getCurrentUser().getUid())
                    .child("Favorites")
                    .child(this.className)
                    .child("tests")
                    .child("topics")
                    .child(this.subjectName)
                    .push()
                    .setValue(new subjectItem(this.topicName));
        }
        this.database.getReference(this.firebaseAuth.getCurrentUser().getUid())
                .child("Favorites")
                .child(this.className)
                .child("tests")
                .child("videos")
                .child(this.subjectName)
                .child(this.topicName)
                .child(this.video.getVideoID())
                .setValue(this.video)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AddVideoActivity.this.startActivity(new Intent(AddVideoActivity.this, NavigationDrawer.class));
                    }
                });

    }

    public void getSubjects(String Class) {
        if(!Class.contains("Age")) {
            this.database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("ClassDetails")
                    .child(Class)
                    .child("tests")
                    .child("subjects")
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            AddVideoActivity.this.subjectsList.add(dataSnapshot.getValue(subjectItem.class).getSubjectName());
                            AddVideoActivity.dialog.hide();
                            AddVideoActivity.this.subjectAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            AddVideoActivity.this.subjectsList.add(dataSnapshot.getValue(subjectItem.class).getSubjectName());
                            AddVideoActivity.dialog.hide();
                            AddVideoActivity.this.subjectAdapter.notifyDataSetChanged();
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
            this.database.getReference("Subjects")
                    .child(Class)
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            AddVideoActivity.this.subjectsList.add(dataSnapshot.getValue(subjectItem.class).getSubjectName());
                            AddVideoActivity.dialog.hide();
                            AddVideoActivity.this.subjectAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            AddVideoActivity.this.subjectsList.add(dataSnapshot.getValue(subjectItem.class).getSubjectName());
                            AddVideoActivity.dialog.hide();
                            AddVideoActivity.this.subjectAdapter.notifyDataSetChanged();
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
    public void getTopics(String subName){
        this.database.getReference("Subjects")
                .child(this.className)
                .child(subName)
                .child("topics")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        AddVideoActivity.this.topicsList.add(dataSnapshot.getValue(subjectItem.class).getSubjectName());
                        AddVideoActivity.this.topicAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        AddVideoActivity.this.topicsList.add(dataSnapshot.getValue(subjectItem.class).getSubjectName());
                        AddVideoActivity.this.topicAdapter.notifyDataSetChanged();
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
        AddVideoActivity.videoCaption.setText(v.getVideoCaption());
        String duration;
        if(v.getVideoDuration().substring(2,v.getVideoDuration().length()-1).replaceAll("[^0-9]", ":").length()<=2){
            duration="0:"+v.getVideoDuration().substring(2,v.getVideoDuration().length()-1).replaceAll("[^0-9]", ":");
        }else {
            duration=v.getVideoDuration().substring(2,v.getVideoDuration().length()-1).replaceAll("[^0-9]", ":");
        }
        AddVideoActivity.videoDuration.setText(duration);
        AddVideoActivity.publishedBy.setText("By: " + v.getPublishedBy());
        Glide.with(this.ctx)
                .load(v.getVideoThumbnailUrl())
                .into(AddVideoActivity.videoThumbnail);
    }
    public void getAgeTopics(){
        this.topicsList.add("videos");
        this.topicAdapter.notifyDataSetChanged();
    }
}
