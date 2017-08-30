package com.example.anshuman_hp.internship;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
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
        database.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("UserProfile")
                .child("presentClass")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                            className=dataSnapshot.getValue().toString();
                        else
                            className="1";
                        Log.e("class",className);
                        actionBar=getSupportActionBar();
                        actionBar.setTitle("Class "+className);

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
        ,R.layout.spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        selectGroup.setAdapter(arrayAdapter);
        selectGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                groupNameText.setText(getResources().getStringArray(R.array.videoGroup)[position]);
                groupName=""+position;
                Log.e("groupName",groupName);
                if (className!=null) {
                    ref = database.getReference("Videos")
                            .child(className)
                            .child(groupName);
                }
                setAdapter();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void setAdapter()
    {
        if(ref!=null) {
            Log.e("ref",ref.toString());
            adapter = new FirebaseRecyclerAdapter<video, videoHolder>(video.class,
                    R.layout.video_item,
                    videoHolder.class,
                    ref) {
                @Override
                protected void populateViewHolder(final videoHolder viewHolder, final video model, final int position) {
                    viewHolder.videoDuration.setText("Duration " + model.getVideoDuration());
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
                                    Intent i = new Intent(AnotherActivity.this, YoutubeActivity.class);
                                    i.putExtra("VideoID", model.getVideoID());
                                    i.putExtra("VideoURL", model.getVideoUrl());
                                    startActivity(i);
                                }
                            });
                    viewHolder.favorites
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    v.setSelected(!v.isSelected());
                                    if(v.isSelected()) {
                                        database.getReference(firebaseAuth.getCurrentUser().getUid())
                                                .child("Favorites")
                                                .child(model.getVideoCaption())
                                                .setValue(model);
                                    }
                                    else {
                                        DatabaseReference ref=database.getReference(firebaseAuth.getCurrentUser().getUid())
                                                .child("Favorites")
                                                .child(model.getVideoCaption());
                                        if(ref!=null)
                                        {
                                            ref.removeValue();
                                        }

                                    }
                                }
                            });
                    viewHolder.rateTheVideo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AnotherActivity.this);
                            builder.setTitle("Rate This Video");
                            View view= LayoutInflater.from(getApplicationContext())
                                    .inflate(R.layout.ratings,null,false);
                            final RatingBar ratingBar =(RatingBar)view.findViewById(R.id.ratingBar);
                            builder.setView(view);
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    float g=(Float.parseFloat(model.getRatings())+ratingBar.getRating())/2;
                                    model.setRatings(""+g);
                                    database.getReference("Videos")
                                            .child(className)
                                            .child(groupName)
                                            .child(""+position)
                                            .setValue(model);
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });
                }
            };
            videoRecycler.setAdapter(adapter);
        }
        else
            Log.e("Ref","REf null");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.videoactivitymenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.favoritesMenu)
        {
            startActivity(new Intent(AnotherActivity.this,FavoriteVideosActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
