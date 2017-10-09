package com.cognichamp.CogniChamp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        subjectsGrid=(RecyclerView)v.findViewById(R.id.subjectGrid);
        subjectsGrid.setLayoutManager(new GridLayoutManager(getActivity(),2));


       FirebaseDatabase.getInstance()
                .getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(where)
                .child(className)
               .child("tests")
                .child("subjects")
       .addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               if(where.equals("ClassDetails") ){
                   if(!dataSnapshot.exists()) {
                       if(!className.contains("Age")) {
                           FirebaseDatabase.getInstance()
                                   .getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                   .child(where)
                                   .child(className)
                                   .child("tests")
                                   .child("subjects")
                                   .child("0")
                                   .setValue(new subjectItem("English"));
                           FirebaseDatabase.getInstance()
                                   .getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                   .child(where)
                                   .child(className)
                                   .child("tests")
                                   .child("subjects")
                                   .child("1")
                                   .setValue(new subjectItem("Maths"));
                           setAdapter(className);

                       }
                       else{
                           setCommonAdapter(className);
                       }
                   }
               }
               else{
                   if(where.equals("Favorites")){
                       if(dataSnapshot.exists()){
                           subjectsGrid.setVisibility(View.VISIBLE);
                           setAdapter(className);
                       }
                       else{
                           subjectsGrid.setVisibility(View.GONE);
                           ((FavoriteVideosActivity) getParentFragment()).setEmptyFragment();
                       }
                   }

               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });
        setAdapter(className);
        return v;
    }
    public void setCommonAdapter(String name){
        DatabaseReference ref=FirebaseDatabase.getInstance()
                .getReference("Subjects")
                .child(name);
        recyclerAdapter=new FirebaseRecyclerAdapter<subjectItem, subjectTopicHolde>(subjectItem.class
                ,R.layout.subject_topic_card
                ,subjectTopicHolde.class
                ,ref) {
            @Override
            protected void populateViewHolder(subjectTopicHolde viewHolder,final subjectItem model, int position) {
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
                        subjectNameBundle.putString("ClassName",className);
                        subjectNameBundle.putString("where",where);
                        subjectNameBundle.putString("SubjectName",model.getSubjectName());
                        if(where.equals("ClassDetails")) {
                            if(className.contains("Age")){
                                ((AnotherActivity) getParentFragment()).changeFragmentWithVideoOnly(subjectNameBundle);
                            }else {
                                ((AnotherActivity) getParentFragment()).changeFragmentWithTopic(subjectNameBundle);
                            }
                        }else if(where.equals("Favorites")){
                            if(className.contains("Age")){
                                ((FavoriteVideosActivity) getParentFragment()).changeFragmentWithVideoOnly(subjectNameBundle);
                            }
                            ((FavoriteVideosActivity) getParentFragment()).changeFragmentWithTopic(subjectNameBundle);
                        }
                    }
                });
            }
        };
        subjectsGrid.setAdapter(recyclerAdapter);
    }

    public void setAdapter(String name){
        DatabaseReference ref= FirebaseDatabase.getInstance()
                .getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(where)
                .child(name)
                .child("tests")
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
                        if(where.equals("ClassDetails")) {
                            if(className.contains("Age")){
                                ((AnotherActivity) getParentFragment()).changeFragmentWithVideoOnly(subjectNameBundle);
                            }
                            else {
                                ((AnotherActivity) getParentFragment()).changeFragmentWithTopic(subjectNameBundle);
                            }
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
