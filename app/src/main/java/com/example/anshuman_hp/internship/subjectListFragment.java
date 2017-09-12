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

public class subjectListFragment extends Fragment {

    RecyclerView subjectsGrid;
    String className;
    FirebaseRecyclerAdapter<subjectItem,subjectTopicHolde> recyclerAdapter;
    Bundle subjectNameBundle=new Bundle();


    public subjectListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.subjects_list,container,false);
        className=getArguments().getString("ClassNumber");
        Log.e("Cla",className);
        subjectsGrid=(RecyclerView)v.findViewById(R.id.subjectGrid);
        subjectsGrid.setLayoutManager(new GridLayoutManager(getActivity(),2));
        setAdapter("Class-"+className);
        return v;
    }

    public void setAdapter(String name){
        DatabaseReference ref= FirebaseDatabase.getInstance()
                .getReference("Subjects")
                .child(name)
                .child("Subjects");
        recyclerAdapter=new FirebaseRecyclerAdapter<subjectItem , subjectTopicHolde>
                (subjectItem.class
                ,R.layout.subject_topic_card
                ,subjectTopicHolde.class
                ,ref) {
            @Override
            protected void populateViewHolder(subjectTopicHolde viewHolder, final subjectItem model, int position) {

                TextDrawable drawable=TextDrawable.builder()
                                        .beginConfig()
                                            .width(100)
                                            .height(100)
                                        .endConfig()
                        .buildRect(model.getSubjectName(), ColorGenerator.MATERIAL.getRandomColor());

                viewHolder.textView.setText(model.getSubjectName());
                //TODO add textDrawable
                viewHolder.imageView.setImageDrawable(drawable);

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO load topic list
                        subjectNameBundle.putString("ClassName",className);
                        subjectNameBundle.putString("SubjectName",model.getSubjectName());
                        ((AnotherActivity)getActivity()).changeFragmentWithTopic(subjectNameBundle);

                    }
                });

            }
        };
        subjectsGrid.setAdapter(recyclerAdapter);
    }
}
