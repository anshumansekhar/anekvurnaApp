package com.example.anshuman_hp.CogniChamp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Anshuman-HP on 12-09-2017.
 */

public class subjectListFragment extends Fragment {

    RecyclerView subjectsGrid;
    String className;
    String where="ClassDetails";
    FirebaseRecyclerAdapter<subjectItem,subjectTopicHolde> recyclerAdapter;
    Bundle subjectNameBundle=new Bundle();


    public subjectListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.subjects_list,container,false);
        className=getArguments().getString("ClassNumber");
        where=getArguments().getString("where");
        Log.e("Cla",className);
        subjectsGrid=(RecyclerView)v.findViewById(R.id.subjectGrid);
        subjectsGrid.setLayoutManager(new GridLayoutManager(getActivity(),2));
        setAdapter(className);
        return v;
    }

    public void setAdapter(String name){
        DatabaseReference ref= FirebaseDatabase.getInstance()
                .getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(where)
                .child(name)
                .child("subjects");
        recyclerAdapter=new FirebaseRecyclerAdapter<subjectItem , subjectTopicHolde>
                (subjectItem.class
                ,R.layout.subject_topic_card
                ,subjectTopicHolde.class
                ,ref) {
            @Override
            protected void populateViewHolder(subjectTopicHolde viewHolder, final subjectItem model, int position) {

                TextDrawable drawable=TextDrawable.builder()
                                        .beginConfig()
                                            .width(160)
                                            .fontSize(25)
                                            .height(100)
                                        .endConfig()
                        .buildRect(model.getSubjectName(), ColorGenerator.MATERIAL.getRandomColor());
                viewHolder.imageView.setImageDrawable(drawable);

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO load topic list
                        subjectNameBundle.putString("ClassName",className);
                        subjectNameBundle.putString("where",where);
                        subjectNameBundle.putString("SubjectName",model.getSubjectName());
                        Log.e("gd",getParentFragment().toString());
                        if(where.equals("ClassDetails")) {
                            ((AnotherActivity) getParentFragment()).changeFragmentWithTopic(subjectNameBundle);
                        }else if(where.equals("Favorites")){
                            ((FavoriteVideosActivity) getParentFragment()).changeFragmentWithTopic(subjectNameBundle);
                        }
                    }
                });

            }
        };
        subjectsGrid.setAdapter(recyclerAdapter);
    }
}
