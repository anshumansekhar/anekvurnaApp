package com.cognichamp.CogniChamp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Anshuman-HP on 16-08-2017.
 */

public class subjectAdapter extends RecyclerView.Adapter<SubjectNameHolder> {

    ArrayList<subject> list=new ArrayList<subject>();
    HashMap<String,subject> map=new HashMap<>();

    DatabaseReference reference;
    DatabaseReference ClassName;
    Context ctx;
    int post;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    String testType;


    public subjectAdapter(ArrayList list, int position, DatabaseReference ref, DatabaseReference Class, Context context, String test) {
        this.list = list;
        post=position;
        reference=ref;
        ClassName=Class;
        ctx=context;
        testType = test;
    }

    @Override
    public SubjectNameHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subject,parent,false);
        return new SubjectNameHolder(v) ;
    }

    @Override
    public void onBindViewHolder(SubjectNameHolder holder, int position) {
        holder.subjectName.setText(""+list.get(position).getSubjectName());
        map.put(list.get(position).getSubjectName(), list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void uploadData()
    {
        for (String key : map.keySet()) {
            ClassName.child("tests")
                    .child(testType)
                    .child("subjects")
                    .child(key)
                    .setValue(map.get(key));
            Log.e("d","adding to subjects");
            ClassName.child("subjects")
                    .child(key)
                    .setValue(new subjectItem(key));
        }
    }
}
