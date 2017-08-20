package com.example.anshuman_hp.internship;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Anshuman-HP on 16-08-2017.
 */

public class subjectAdapter extends RecyclerView.Adapter<subjectHolder> {

    ArrayList<subject> list=new ArrayList();

    public subjectAdapter(ArrayList list) {
        this.list = list;
    }

    @Override
    public subjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subject,parent,false);
        return new subjectHolder(v) ;
    }

    @Override
    public void onBindViewHolder(subjectHolder holder, int position) {
//        holder.subject.setText(list.get(position).getSubName()+"       "+list.get(position).getSubMarks());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
