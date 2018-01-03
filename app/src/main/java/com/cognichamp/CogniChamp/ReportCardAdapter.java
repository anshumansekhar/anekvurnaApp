package com.cognichamp.CogniChamp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Anshuman-HP on 23-11-2017.
 */

public class ReportCardAdapter extends RecyclerView.Adapter<ReportCardItemHolder> {
    ArrayList<ReportCardItem> list = new ArrayList<>();
    Context ctx;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();


    public ReportCardAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public ReportCardItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.reportcarditem, parent, false);
        return new ReportCardItemHolder(v);
    }

    @Override
    public void onBindViewHolder(ReportCardItemHolder holder, int position) {
        if (list.get(position).getPercentage().equals("Grade")) {
            holder.percentageText.setVisibility(View.GONE);
        } else {
            holder.percentageText.setVisibility(View.VISIBLE);
            holder.percentageText.setText(list.get(position).getPercentage());
        }
        holder.subjectMarksList.setText(list.get(position).getMarksText());
        holder.subjectNamesList.setText(list.get(position).getSubjectNames());
        holder.testType.setText(list.get(position).getTestType());
        if (list.get(position).getComments() != null) {
            holder.comments.setText(Html.fromHtml("<font color=#2340e1>Teacher's Comments <br></font>" + list.get(position).getComments()), TextView.BufferType.SPANNABLE);
        } else {
            holder.comments.setText(Html.fromHtml("<font color=#2340e1>Teacher's Comments <br></font>"), TextView.BufferType.SPANNABLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addToMap(final String testType, final String percentage) {
        if (percentage.equals("Grade")) {
            final ReportCardItem item = new ReportCardItem();
            item.setTestType(testType);
            item.setPercentage(percentage);
            item.setSubjectNames("");
            item.setMarksText("");
            database.getReference(auth.getCurrentUser().getUid())
                    .child("ClassDetails")
                    .child(EducationFragment.className)
                    .child("Grades")
                    .child(testType)
                    .child("comments")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                item.setComments(dataSnapshot.getValue().toString());
                                list.set(list.indexOf(item), item);
                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            database.getReference(auth.getCurrentUser().getUid())
                    .child("ClassDetails")
                    .child(EducationFragment.className)
                    .child("Grades")
                    .child(testType)
                    .child("topics")
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Grade item1 = dataSnapshot.getValue(Grade.class);
                            String subjectItemName = item1.getTopicName();
                            String subjectItemMarks = item1.getGrade();
                            item.addTosubjects(subjectItemName, subjectItemMarks);
                            list.set(list.indexOf(item), item);
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            Grade item1 = dataSnapshot.getValue(Grade.class);
                            String subjectItemName = item1.getTopicName();
                            String subjectItemMarks = item1.getGrade();
                            item.addTosubjects(subjectItemName, subjectItemMarks);
                            list.set(list.indexOf(item), item);
                            notifyDataSetChanged();

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            Grade item1 = dataSnapshot.getValue(Grade.class);
                            String subjectItemName = item1.getTopicName();
                            String subjectItemMarks = item1.getGrade();
                            item.addTosubjects(subjectItemName, subjectItemMarks);
                            list.set(list.indexOf(item), item);
                            notifyDataSetChanged();

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                            Grade item1 = dataSnapshot.getValue(Grade.class);
                            String subjectItemName = item1.getTopicName();
                            String subjectItemMarks = item1.getGrade();
                            item.addTosubjects(subjectItemName, subjectItemMarks);
                            list.set(list.indexOf(item), item);
                            notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            if (!list.contains(item)) {
                list.add(item);
                notifyDataSetChanged();
            }

        } else {
            final ReportCardItem item = new ReportCardItem();
            item.setTestType(testType);
            item.setPercentage("Percentage:" + percentage);
            item.setSubjectNames("");
            item.setMarksText("");
            database.getReference(auth.getCurrentUser().getUid())
                    .child("ClassDetails")
                    .child(EducationFragment.className)
                    .child("tests")
                    .child(testType)
                    .child("comments")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                item.setComments(dataSnapshot.getValue().toString());
                                list.set(list.indexOf(item), item);
                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            database.getReference(auth.getCurrentUser().getUid())
                    .child("ClassDetails")
                    .child(EducationFragment.className)
                    .child("tests")
                    .child(testType)
                    .child("subjects")
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            subject item1 = dataSnapshot.getValue(subject.class);
                            String subjectItemName = item1.getSubjectName();
                            String subjectItemMarks = item1.getSubMarks() + "/" + item1.getTotalMarks();
                            item.addTosubjects(subjectItemName, subjectItemMarks);
                            list.set(list.indexOf(item), item);
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            subject item1 = dataSnapshot.getValue(subject.class);
                            String subjectItemName = item1.getSubjectName();
                            String subjectItemMarks = item1.getSubMarks() + "/" + item1.getTotalMarks();
                            item.addTosubjects(subjectItemName, subjectItemMarks);
                            list.set(list.indexOf(item), item);
                            notifyDataSetChanged();

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            subject item1 = dataSnapshot.getValue(subject.class);
                            String subjectItemName = item1.getSubjectName();
                            String subjectItemMarks = item1.getSubMarks() + "/" + item1.getTotalMarks();
                            item.addTosubjects(subjectItemName, subjectItemMarks);
                            notifyDataSetChanged();

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                            subject item1 = dataSnapshot.getValue(subject.class);
                            String subjectItemName = item1.getSubjectName();
                            String subjectItemMarks = item1.getSubMarks() + "/" + item1.getTotalMarks();
                            item.addTosubjects(subjectItemName, subjectItemMarks);
                            notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            if (!list.contains(item)) {
                list.add(item);
                notifyDataSetChanged();
            }
        }
    }
}
