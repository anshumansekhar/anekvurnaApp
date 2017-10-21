package com.cognichamp.CogniChamp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Anshuman-HP on 17-10-2017.
 */

public class reportCardAdapter extends RecyclerView.Adapter<ReportCardRecyclerHolder> {
    HashMap<Integer, String> tests = new HashMap<>();
    Context ctx;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    ArrayList<subject> subjects = new ArrayList<>();
    String sub = "";


    public reportCardAdapter(HashMap<Integer, String> tests, Context ctx) {
        this.tests = tests;
        this.ctx = ctx;
    }

    @Override
    public ReportCardRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.report_card_item, parent, false);
        return new ReportCardRecyclerHolder(v);
    }

    @Override
    public void onBindViewHolder(final ReportCardRecyclerHolder holder, final int position) {
        firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("ClassDetails")
                .child(EducationFragment.className)
                .child("tests")
                .child(tests.get(position))
                .child("subjects")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            while (dataSnapshot.getChildren().iterator().hasNext()) {
                                Log.e("dzb", dataSnapshot.getValue().toString());

                            }
                        } else {
                            Log.e(tests.get(position), "no exits");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
//                .addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        holder.marks.setText(dataSnapshot.getValue().toString());
//                        subjects.add(dataSnapshot.getValue(subject.class));
////                        subjects.set(Integer.parseInt(dataSnapshot.getKey()),dataSnapshot.getValue(subject.class));
//                        Log.e("fs"+Integer.parseInt(dataSnapshot.getKey()),dataSnapshot.getValue(subject.class).toString());
//                        //holder.marks.setText(addToSubjectsTest());
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                        subjects.add(dataSnapshot.getValue(subject.class));
////                        subjects.set(Integer.parseInt(dataSnapshot.getKey()),dataSnapshot.getValue(subject.class));
//                        Log.e("sv"+Integer.parseInt(dataSnapshot.getKey()),dataSnapshot.getValue(subject.class).toString());
//                       // holder.marks.setText(addToSubjectsTest());
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
        firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("ClassDetails")
                .child(EducationFragment.className)
                .child("tests")
                .child("tests")
                .child(tests.get(position))
                .child("percentage")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            holder.percentage.setText("Percentage:" + dataSnapshot.getValue().toString());
                            holder.testType.setText(tests.get(position));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return tests.size();

    }

    String addToSubjectsTest() {

        for (int i = 0; i < subjects.size(); i++) {
            sub = sub.concat(subjects.get(i).getSubjectName() + ":" + subjects.get(i).getSubMarks() + "/" + subjects.get(i).getTotalMarks() + "\n");
        }
        return sub;
    }
}
