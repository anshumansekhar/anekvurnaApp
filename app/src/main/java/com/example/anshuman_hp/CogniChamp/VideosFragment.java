package com.example.anshuman_hp.CogniChamp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Created by Anshuman-HP on 12-09-2017.
 */

public class VideosFragment extends Fragment {
    RecyclerView videosList;
    FirebaseRecyclerAdapter<video,videoHolder> firebaseRecyclerAdapter;
    public static HashMap<String,video> videos=new HashMap();
    String className,subjectName,topicName,where;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.videos_fragment,container,false);

        className=getArguments().getString("ClassName");
        subjectName=getArguments().getString("SubjectName");
        topicName=getArguments().getString("TopicName");
        where=getArguments().getString("where");
        videosList=(RecyclerView)v.findViewById(R.id.videosList);
        videosList.setLayoutManager(new LinearLayoutManager(getActivity()));
        SetAdapter(className,subjectName,topicName);
        return v;
    }

    public void SetAdapter(final String className, final String subjectName, final String topicName){

        Log.e("dag",className+subjectName+topicName);
        DatabaseReference ref;
        if(where.equals("Favorites")){
            ref=FirebaseDatabase.getInstance()
                    .getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(where)
                    .child(className)
                    .child("videos")
                    .child(subjectName)
                    .child(topicName);
            Log.e("GDHZ",ref.toString());
        }
        else{
            ref=FirebaseDatabase.getInstance()
                    .getReference("Videos")
                    .child(className)
                    .child(subjectName)
                    .child(topicName);
            Log.e("GDHzdhZ",ref.toString());

        }
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                videos.put(dataSnapshot.getKey(),dataSnapshot.getValue(video.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                videos.put(dataSnapshot.getKey(),dataSnapshot.getValue(video.class));
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
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<video, videoHolder>
                (video.class
                ,R.layout.video_item
                ,videoHolder.class
                ,ref) {
            @Override
            protected void populateViewHolder(videoHolder viewHolder, final video model, final int position) {
                if(!model.getVideoDuration().equals(""))
                    viewHolder.videoDuration.setText("Duration " + model.getVideoDuration());
                else
                    viewHolder.videoDuration.setText("");
                viewHolder.videoCaption.setText(model.getVideoCaption());
                viewHolder.videoDuration.setText(model.getVideoDuration());
                if(where.equals("Favorites")){
                    viewHolder.favorites.setVisibility(View.GONE);
                }
                else{
                    viewHolder.favorites.setVisibility(View.VISIBLE);
                }
                viewHolder.publishedBy.setText("By:-"+model.getPublishedBy());
                Glide.with(getActivity())
                        .load(model.getVideoThumbnailUrl())
                        .into(viewHolder.videoThumbnail);
                viewHolder.shareVideo
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent share = new Intent(Intent.ACTION_SEND);
                                share.setType("text/plain");
                                share.putExtra(Intent.EXTRA_TEXT, model.getVideoUrl());
                                share.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this Video");
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
                viewHolder.favorites
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                v.setSelected(!v.isSelected());
                                if(v.isSelected()) {
                                    FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("Favorites")
                                            .child(model.getVideoID())
                                            .setValue(model);
                                }
                                else {
                                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("Favorites")
                                            .child(model.getVideoID());
                                    if(ref!=null) {
                                        ref.removeValue();
                                    }
                                }
                            }
                        });
                viewHolder.rateTheVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Rate This Video");
                        View view= LayoutInflater.from(getActivity())
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
                                float g;
                                if(model.getRatings().equals("")){
                                    g=ratingBar.getRating();
                                }
                                else {
                                    g=(Float.parseFloat(model.getRatings())+ratingBar.getRating())/2;
                                }
                                model.setRatings(""+g);
                                FirebaseDatabase.getInstance().getReference("Videos")
                                        .child(className)
                                        .child(subjectName)
                                        .child(topicName)
                                        .child(model.getVideoID())
                                        .setValue(model);
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }
        };
        videosList.setAdapter(firebaseRecyclerAdapter);
    }
}