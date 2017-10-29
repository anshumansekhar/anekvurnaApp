package com.cognichamp.CogniChamp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.cognichamp.CogniChamp.R.dimen;
import com.cognichamp.CogniChamp.R.id;
import com.cognichamp.CogniChamp.R.layout;
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
    GridLayoutManager layoutManager;


    public subjectListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(layout.subjects_list, container, false);
        this.className = this.getArguments().getString("ClassNumber");
        this.where = this.getArguments().getString("where");
        this.subjectsGrid = (RecyclerView) v.findViewById(id.subjectGrid);
        this.layoutManager = new GridLayoutManager(this.getActivity(), 2);
        this.subjectsGrid.setLayoutManager(this.layoutManager);



       FirebaseDatabase.getInstance()
                .getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
               .child(this.where)
               .child(this.className)
                .child("subjects")
       .addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               if (subjectListFragment.this.where.equals("ClassDetails")) {
                   if(!dataSnapshot.exists()) {
                       if (!subjectListFragment.this.className.contains("Age")) {
                           FirebaseDatabase.getInstance()
                                   .getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                   .child(subjectListFragment.this.where)
                                   .child(subjectListFragment.this.className)
                                   .child("subjects")
                                   .child("0")
                                   .setValue(new subjectItem("English"));
                           FirebaseDatabase.getInstance()
                                   .getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                   .child(subjectListFragment.this.where)
                                   .child(subjectListFragment.this.className)
                                   .child("subjects")
                                   .child("1")
                                   .setValue(new subjectItem("Maths"));
                           subjectListFragment.this.setAdapter(subjectListFragment.this.className);

                       }
                       else{
                           subjectListFragment.this.setCommonAdapter(subjectListFragment.this.className);
                       }
                   }
               }
               else{
                   if (subjectListFragment.this.where.equals("Favorites")) {
                       if(dataSnapshot.exists()){
                           subjectListFragment.this.subjectsGrid.setVisibility(View.VISIBLE);
                           subjectListFragment.this.setAdapter(subjectListFragment.this.className);
                       }
                       else{
                           subjectListFragment.this.subjectsGrid.setVisibility(View.GONE);
                           ((FavoriteVideosActivity) subjectListFragment.this.getParentFragment()).setEmptyFragment();
                       }
                   }

               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });
        this.setAdapter(this.className);
        return v;
    }
    public void setCommonAdapter(String name){
        DatabaseReference ref=FirebaseDatabase.getInstance()
                .getReference("Subjects")
                .child(name);
        this.recyclerAdapter = new FirebaseRecyclerAdapter<subjectItem, subjectTopicHolde>(subjectItem.class
                , layout.subject_topic_card
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

                viewHolder.view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        subjectListFragment.this.subjectNameBundle.putString("ClassName", subjectListFragment.this.className);
                        subjectListFragment.this.subjectNameBundle.putString("where", subjectListFragment.this.where);
                        subjectListFragment.this.subjectNameBundle.putString("SubjectName", model.getSubjectName());
                        if (subjectListFragment.this.where.equals("ClassDetails")) {
                            if (subjectListFragment.this.className.contains("Age")) {
                                ((AnotherActivity) subjectListFragment.this.getParentFragment()).changeFragmentWithVideoOnly(subjectListFragment.this.subjectNameBundle);
                            }else {
                                ((AnotherActivity) subjectListFragment.this.getParentFragment()).changeFragmentWithTopic(subjectListFragment.this.subjectNameBundle);
                            }
                        } else if (subjectListFragment.this.where.equals("Favorites")) {
                            if (subjectListFragment.this.className.contains("Age")) {
                                ((FavoriteVideosActivity) subjectListFragment.this.getParentFragment()).changeFragmentWithVideoOnly(subjectListFragment.this.subjectNameBundle);
                            }
                            ((FavoriteVideosActivity) subjectListFragment.this.getParentFragment()).changeFragmentWithTopic(subjectListFragment.this.subjectNameBundle);
                        }
                    }
                });
            }
        };
        this.subjectsGrid.setAdapter(this.recyclerAdapter);
    }

    public void setAdapter(String name){
        DatabaseReference ref= FirebaseDatabase.getInstance()
                .getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(this.where)
                .child(name)
                .child("subjects");
        this.recyclerAdapter = new FirebaseRecyclerAdapter<subjectItem, subjectTopicHolde>
                (subjectItem.class
                        , layout.subject_topic_card
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

                viewHolder.view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO load topic list
                        subjectListFragment.this.subjectNameBundle.putString("ClassName", subjectListFragment.this.className);
                        subjectListFragment.this.subjectNameBundle.putString("where", subjectListFragment.this.where);
                        subjectListFragment.this.subjectNameBundle.putString("SubjectName", model.getSubjectName());
                        if (subjectListFragment.this.where.equals("ClassDetails")) {
                            if (subjectListFragment.this.className.contains("Age")) {
                                ((AnotherActivity) subjectListFragment.this.getParentFragment()).changeFragmentWithVideoOnly(subjectListFragment.this.subjectNameBundle);
                            }
                            else {
                                ((AnotherActivity) subjectListFragment.this.getParentFragment()).changeFragmentWithTopic(subjectListFragment.this.subjectNameBundle);
                            }
                        } else if (subjectListFragment.this.where.equals("Favorites")) {
                            ((FavoriteVideosActivity) subjectListFragment.this.getParentFragment()).changeFragmentWithTopic(subjectListFragment.this.subjectNameBundle);
                        }
                    }
                });

            }
        };
        this.subjectsGrid.setAdapter(this.recyclerAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.subjectsGrid.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                subjectListFragment.this.subjectsGrid.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int viewWidth = subjectListFragment.this.subjectsGrid.getMeasuredWidth();
                if (!subjectListFragment.this.isRemoving()) {
                    if (subjectListFragment.this.getActivity() != null) {
                        float cardViewWidth = subjectListFragment.this.getActivity().getResources().getDimension(dimen.subjectCardSizeWidth);
                        int newSpanCount = (int) Math.floor(viewWidth / cardViewWidth);
                        if (newSpanCount > 0) {
                            subjectListFragment.this.layoutManager.setSpanCount(newSpanCount);
                        }
                        subjectListFragment.this.layoutManager.requestLayout();
                    }
                }
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //subjectsGrid.getViewTreeObserver().removeOnGlobalLayoutListener(subjectsGrid.getViewTreeObserver().g);
    }
}
