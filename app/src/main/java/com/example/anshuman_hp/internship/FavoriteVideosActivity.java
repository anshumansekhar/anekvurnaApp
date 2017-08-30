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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FavoriteVideosActivity extends AppCompatActivity {
    ActionBar ab;
    RecyclerView favorites;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    DatabaseReference reference;

    FirebaseRecyclerAdapter<video,videoHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_videos);

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
