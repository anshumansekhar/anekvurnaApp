package com.cognichamp.CogniChamp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.HashMap;

/**
 * Created by Anshuman-HP on 04-09-2017.
 */

public class subjectItemAdapter extends RecyclerView.Adapter<subjectHolder> {
    HashMap<String,subject> Subjects=new HashMap<>();
    Context ctx;

    public subjectItemAdapter(HashMap<String, subject> subjects, Context ctx) {
        Subjects = subjects;
        this.ctx = ctx;
    }

    @Override
    public subjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(ctx)
                .inflate(R.layout.subject_item,parent,false);
        return new subjectHolder(v);
    }

    @Override
    public void onBindViewHolder(subjectHolder holder, int position) {
        holder.subjectName.setText(Subjects.get(""+position).getSubjectName());
        holder.subjectMarks.setText(""+Subjects.get(""+position).getSubMarks());
        holder.totalMArks.setText(""+Subjects.get(""+position).getTotalMarks());



    }

    @Override
    public int getItemCount() {
        return Subjects.size();
    }
}
