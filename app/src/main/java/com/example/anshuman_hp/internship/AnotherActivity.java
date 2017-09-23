package com.example.anshuman_hp.internship;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AnotherActivity extends Fragment {

    subjectListFragment subjectListFragment = new subjectListFragment();
    TopicListFragment topicListFragment = new TopicListFragment();
    VideosFragment videosFragment = new VideosFragment();


    Bundle classNameBundle = new Bundle();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference ref;
    FirebaseRecyclerAdapter<video, videoHolder> adapter;


    String className = "";



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_another, container, false);


        database.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("UserProfile")
                .child("presentClass")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            className = dataSnapshot.getValue().toString();
                            if(className.equals("11")){
                                className=className+"(Arts)";
                            }
                            else if(className.equals("12")){
                                className=className+"(Commerce)";
                            }
                            else if(className.equals("13")){
                                className=className+"(Science)";
                            }
                            else if(className.equals("14")){
                                className=className+"(Arts)";
                            }
                            else if(className.equals("15")){
                                className=className+"(Commerce)";
                            }
                            else if(className.equals("16")){
                                className=className+"(Science)";
                            }
                            ((NavigationDrawer)getActivity()).actionBar.setTitle("Class-"+className);
                            ((NavigationDrawer)getActivity()).actionBar.setTitle("Class-"+className);
                            classNameBundle.putString("ClassNumber", className);
                            subjectListFragment.setArguments(classNameBundle);

                            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.videoFrame, subjectListFragment);
                            fragmentTransaction.commit();
                            fragmentTransaction.addToBackStack(null);
                        }
                        //TODO class name set
                            //className = "1";

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return v;
    }

    public void changeFragmentWithTopic(Bundle b) {
        topicListFragment.setArguments(b);
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.videoFrame, topicListFragment);
        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);

    }

    public void changeFragmentWithVideo(Bundle b) {
        videosFragment.setArguments(b);
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.videoFrame, videosFragment);
        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);
    }
}
