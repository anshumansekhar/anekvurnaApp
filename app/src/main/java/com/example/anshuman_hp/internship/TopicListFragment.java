package com.example.anshuman_hp.internship;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Anshuman-HP on 12-09-2017.
 */

public class TopicListFragment extends Fragment {
    RecyclerView topicsGrid;
    FirebaseRecyclerAdapter<subjectItem,subjectTopicHolde> firebaseRecyclerAdapter;

    String className,subjectName;

    Bundle topicsBundle=new Bundle();

    VideosFragment videosFragment=new VideosFragment();

    public TopicListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.topics_list,container,false);
        topicsGrid=(RecyclerView)v.findViewById(R.id.topicsGrid);
        topicsGrid.setLayoutManager(new GridLayoutManager(getActivity(),2));
        className=getArguments().getString("ClassName");
        subjectName=getArguments().getString("SubjectName");
        Log.e("csl",className + subjectName);
        setAdapter("Class-"+className,subjectName);
        return v;
    }

    public void setAdapter(final String className, String subName){
        DatabaseReference ref= FirebaseDatabase.getInstance()
                .getReference("Subjects")
                .child(className)
                .child(subName)
                .child("topics");
        Log.e("ad",ref.toString());
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<subjectItem, subjectTopicHolde>
                (subjectItem.class
                ,R.layout.subject_topic_card
                ,subjectTopicHolde.class
                ,ref) {
            @Override
            protected void populateViewHolder(subjectTopicHolde viewHolder, final subjectItem model, int position) {

                viewHolder.textView.setText(model.getSubjectName());
                //TODO text drawable

                TextDrawable drawable=TextDrawable.builder()
                        .beginConfig()
                        .width(100)
                        .height(100)
                        .fontSize(25)
                        .endConfig()
                        .buildRect(model.getSubjectName(), ColorGenerator.MATERIAL.getRandomColor());

                viewHolder.textView.setText(model.getSubjectName());
                //TODO add textDrawable
                viewHolder.imageView.setImageDrawable(drawable);

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO load the videos in the category

                        topicsBundle.putString("ClassName",className);
                        topicsBundle.putString("SubjectName",subjectName);
                        topicsBundle.putString("TopicName",model.getSubjectName());

                        ((AnotherActivity)getParentFragment()).changeFragmentWithVideo(topicsBundle);
                    }
                });

            }
        };
        topicsGrid.setAdapter(firebaseRecyclerAdapter);
    }

}
