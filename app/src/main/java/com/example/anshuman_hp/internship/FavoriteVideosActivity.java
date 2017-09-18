package com.example.anshuman_hp.internship;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Scanner;

public class FavoriteVideosActivity extends Fragment{
    ActionBar ab;
    RecyclerView favorites;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    DatabaseReference reference;
    Intent j;
    String caption;


    FirebaseRecyclerAdapter<video,videoHolder> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activity_favorite_videos,container,false);


        favorites=(RecyclerView)v.findViewById(R.id.favoriteVideos);
        favorites.setLayoutManager(new LinearLayoutManager(getActivity()));

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
                Glide.with(getActivity())
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
                                Intent i = new Intent(getActivity(), YoutubeActivity.class);
                                i.putExtra("VideoID", model.getVideoID());
                                i.putExtra("VideoURL", model.getVideoUrl());
                                startActivity(i);
                            }
                        });


            }
        };
        favorites.setAdapter(adapter);
        return v;
    }
}

