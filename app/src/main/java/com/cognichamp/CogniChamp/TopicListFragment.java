package com.cognichamp.CogniChamp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.cognichamp.CogniChamp.R.id;
import com.cognichamp.CogniChamp.R.layout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Anshuman-HP on 12-09-2017.
 */

public class TopicListFragment extends Fragment {
    RecyclerView topicsGrid;
    FirebaseRecyclerAdapter<subjectItem,subjectTopicHolde> firebaseRecyclerAdapter;

    String className,subjectName,where;

    Bundle topicsBundle=new Bundle();

    VideosFragment videosFragment=new VideosFragment();

    public TopicListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(layout.topics_list, container, false);
        this.topicsGrid = (RecyclerView) v.findViewById(id.topicsGrid);
        this.topicsGrid.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));
        this.className = this.getArguments().getString("ClassName");
        this.subjectName = this.getArguments().getString("SubjectName");
        this.where = this.getArguments().getString("where");
        this.setAdapter(this.className, this.subjectName);
        return v;
    }

    public void setAdapter(final String className, String subName){
        DatabaseReference ref;
        if (!this.where.equals("Favorites")) {
            this.where = "Subjects";
            ref= FirebaseDatabase.getInstance()
                    .getReference(this.where)
                    .child(className)
                    .child(subName)
                    .child("topics");
        }
        else
        {
            ref= FirebaseDatabase.getInstance()
                    .getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(this.where)
                    .child(className)
                    .child("topics")
                    .child(subName);
        }
        this.firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<subjectItem, subjectTopicHolde>
                (subjectItem.class
                        , layout.subject_topic_card
                ,subjectTopicHolde.class
                ,ref) {
            @Override
            protected void populateViewHolder(subjectTopicHolde viewHolder, final subjectItem model, int position) {



                TextDrawable drawable=TextDrawable.builder()
                        .beginConfig()
                        .width(160)
                        .height(100)
                        .fontSize(25)
                        .endConfig()
                        .buildRect(model.getSubjectName(), ColorGenerator.MATERIAL.getRandomColor());



                viewHolder.imageView.setImageDrawable(drawable);

                viewHolder.view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        TopicListFragment.this.topicsBundle.putString("ClassName", className);
                        TopicListFragment.this.topicsBundle.putString("SubjectName", TopicListFragment.this.subjectName);
                        TopicListFragment.this.topicsBundle.putString("TopicName", model.getSubjectName());
                        TopicListFragment.this.topicsBundle.putString("where", TopicListFragment.this.where);
                        if (TopicListFragment.this.where.equals("Subjects")) {
                            ((AnotherActivity) TopicListFragment.this.getParentFragment()).changeFragmentWithVideo(TopicListFragment.this.topicsBundle);

                        }
                        else {
                            ((FavoriteVideosActivity) TopicListFragment.this.getParentFragment()).changeFragmentWithVideo(TopicListFragment.this.topicsBundle);
                        }
                    }
                });

            }
        };
        this.topicsGrid.setAdapter(this.firebaseRecyclerAdapter);
    }

}
