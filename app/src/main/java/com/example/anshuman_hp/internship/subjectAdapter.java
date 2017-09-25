package com.example.anshuman_hp.internship;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class subjectAdapter extends RecyclerView.Adapter<SubjectNameHolder> {

    ArrayList<subject> list=new ArrayList();
    HashMap<String,subject> map=new HashMap<>();

    DatabaseReference reference;
    DatabaseReference ClassName;
    Context ctx;
    int post;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();




    public subjectAdapter(ArrayList list,int position,DatabaseReference ref,DatabaseReference Class,Context context) {
        this.list = list;
        post=position;
        reference=ref;
        ClassName=Class;
        ctx=context;
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
        map.put(""+(post+position),list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void uploadData()
    {
        Log.e("Uploading","DAta");
        for(int i=post;i<(post+map.size());i++){
            ClassName.child("subjects")
                    .child(""+i)
                    .setValue(map.get(""+i).getSubjectName());
        }
        for (int i=post;i<(post+map.size());i++) {
            for(int j=0;j<marksFragments.tests.length;j++){
                ClassName.child("tests")
                        .child(marksFragments.tests[j])
                        .child("subjects")
                        .child(""+i)
                        .setValue(map.get(""+i));
            }
            ClassName.child("subjects")
                    .child(""+i)
                    .setValue(new subjectItem(map.get(""+i).getSubjectName()));
            Log.e("Pushing",map.get(""+i).toString());
        }
    }


}
