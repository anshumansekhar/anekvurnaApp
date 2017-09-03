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
import android.view.View;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Scanner;

public class FavoriteVideosActivity extends AppCompatActivity {
    ActionBar ab;
    RecyclerView favorites;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    DatabaseReference reference;
    Intent j;
    String caption;


    FirebaseRecyclerAdapter<video,videoHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_videos);

        j=getIntent();
        String action=j.getAction();
        String type=j.getType();

        if (Intent.ACTION_SEND.equals(action)) {
            String sharedText = j.getStringExtra(Intent.EXTRA_TEXT);
            if(sharedText!=null)
            {
                AlertDialog.Builder builder=new AlertDialog.Builder(FavoriteVideosActivity.this);
                View v=getLayoutInflater().inflate(R.layout.edittextdialog,null,false);
                builder.setView(v);
                final EditText ed=(EditText)v.findViewById(R.id.editText);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        caption=ed.getText().toString().trim();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                Scanner s=new Scanner(sharedText).useDelimiter("\\s*https://youtu.be/");
                String videoID=s.next();
                video video=new video("https://img.youtube.com/vi/"+videoID+"/default.jpg",caption,"",sharedText,videoID,"");
                database.getReference(auth.getCurrentUser().getUid())
                        .child("Favorites")
                        .child(caption)
                        .setValue(video);
            }
        }

        favorites=(RecyclerView)findViewById(R.id.favoriteVideos);
        favorites.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        reference=database.getReference(auth.getCurrentUser().getUid())
                .child("Favorites");

        adapter=new FirebaseRecyclerAdapter<video, videoHolder>(
                video.class
                ,R.layout.video_item
                ,videoHolder.class
                ,reference) {
            @Override
            protected void populateViewHolder(videoHolder viewHolder,final video model, int position) {
                viewHolder.videoCaption.setText(model.getVideoCaption());
                viewHolder.videoDuration.setText(model.getVideoDuration());
                Glide.with(getApplicationContext())
                        .load(model.getVideoThumbnailUrl())
                        .into(viewHolder.videoThumbnail);
                viewHolder.favorites.setVisibility(View.GONE);
                viewHolder.rateTheVideo.setVisibility(View.GONE);
                viewHolder.shareVideo.setOnClickListener(new View.OnClickListener() {
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
                                Intent i = new Intent(FavoriteVideosActivity.this, YoutubeActivity.class);
                                i.putExtra("VideoID", model.getVideoID());
                                i.putExtra("VideoURL", model.getVideoUrl());
                                startActivity(i);
                            }
                        });


            }
        };

        ab=getSupportActionBar();
        ab.setTitle("Favorites");
        favorites.setAdapter(adapter);
    }
}
