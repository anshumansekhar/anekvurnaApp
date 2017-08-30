package com.example.anshuman_hp.internship;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Anshuman-HP on 16-08-2017.
 */

public class subjectAdapter extends RecyclerView.Adapter<subjectHolder> {

    ArrayList<subject> list=new ArrayList();
    HashMap<String,subject> map=new HashMap<>();

    DatabaseReference reference;
    int post;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();


    public subjectAdapter(ArrayList list,int position,DatabaseReference ref) {
        this.list = list;
        post=position;
        reference=ref;
    }

    @Override
    public subjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subject,parent,false);
        return new subjectHolder(v) ;
    }

    @Override
    public void onBindViewHolder(subjectHolder holder, int position) {
        holder.subjectName.setText(""+list.get(position).getSubjectName());
        map.put(""+(post+position),list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void uploadData()
    {
        reference.setValue(map);

    }


}
