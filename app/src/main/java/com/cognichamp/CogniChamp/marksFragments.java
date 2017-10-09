package com.cognichamp.CogniChamp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Created by Anshuman-HP on 03-09-2017.
 */

public class marksFragments extends Fragment {
    Spinner testType;
    static RecyclerView subjectsList;
    Button save,addnewSubject;
    TextView percentage;
    ArrayAdapter testAdapter;
    public static HashMap<String,subject> Subjects=new HashMap<>();

    static FirebaseRecyclerAdapter<subject,subjectHolder> Adapter;

    public static DatabaseReference testRef;

    public static String[] tests;


    FirebaseRecyclerAdapter<subject, subjectHolder> recyclerAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.new_education_details,container,false);
        testType=(Spinner)v.findViewById(R.id.selectTestType);
        subjectsList=(RecyclerView)v.findViewById(R.id.subjectRecyclerEducation);
        subjectsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        save=(Button)v.findViewById(R.id.save);
        addnewSubject=(Button)v.findViewById(R.id.addnewSubject);
        percentage=(TextView)v.findViewById(R.id.percentageEducation);


        testType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                testRef=EducationFragment.classRef.child("tests").child(tests[position]);
                setUpRecyclerView(EducationFragment.classRef.child("tests").child(tests[position]).child("subjects"),getActivity());
                EducationFragment.classRef.child("tests").child(tests[position]).child("percentage")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists())
                                    percentage.setText("Percentage:"+ dataSnapshot.getValue().toString());
                                else{
                                    percentage.setText("Percentage:");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges(testRef);
            }
        });
        addnewSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),AddNewSubject.class);
                i.putExtra("Position",Subjects.size());
                i.putExtra("Class",EducationFragment.classRef.toString());
                i.putExtra("Ref",testRef.child("subjects").toString());
                startActivity(i);
            }
        });
        return v;
    }
    public void setSpinnerAdapter(){
        if(EducationFragment.className.equals("Class-10")||EducationFragment.className.equals("Class-12")) {
            testAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.testsBoard, android.R.layout.simple_spinner_item);
            testAdapter.notifyDataSetChanged();
            tests=getActivity().getResources().getStringArray(R.array.testsBoard);
            testAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            testType.setAdapter(testAdapter);
        }
        else{
            testAdapter=ArrayAdapter.createFromResource(getActivity(), R.array.testsNormal, android.R.layout.simple_spinner_item);
            testAdapter.notifyDataSetChanged();
            tests=getActivity().getResources().getStringArray(R.array.testsNormal);
            testAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            testType.setAdapter(testAdapter);

        }

    }
    public  void setUpRecyclerView(final DatabaseReference ref, final Context ctx)
    {
        fillHashmap(ref);
        Adapter=new FirebaseRecyclerAdapter<subject, subjectHolder>
                (subject.class
                ,R.layout.subject_entry
                ,subjectHolder.class
                ,ref) {
            @Override
            protected void populateViewHolder(final subjectHolder viewHolder, final subject model,  int position) {
                viewHolder.subjectMarks.setText(""+model.getSubMarks());
                viewHolder.totalMArks.setText(""+model.getTotalMarks());
                viewHolder.subjectName.setText(model.getSubjectName());

                viewHolder.subjectMarks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.subjectMarks.setText("");
                    }
                });
                viewHolder.totalMArks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.totalMArks.setText("");
                    }
                });
                final int pos=position;
                viewHolder.deleteSubject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder=new AlertDialog.Builder(ctx);
                        builder.setTitle("Confirmation");
                        builder.setMessage("Are you Sure to Delete the Subject");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ref.child(""+pos)
                                        .removeValue();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog dialog=builder.create();
                        dialog.show();
                    }
                });
                viewHolder.totalMArks.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(!s.toString().equals("")&&model.getTotalMarks()!=Float.parseFloat(s.toString())){
                           Subjects.get(""+pos).setTotalMarks(Float.parseFloat(s.toString()));
                        }

                    }
                });
                viewHolder.subjectMarks.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(!s.toString().equals("")&&model.getSubMarks()!=Float.parseFloat(s.toString())){
                            Subjects.get(""+pos).setSubMarks(Float.parseFloat(s.toString()));
                        }
                    }
                });
            }

        };
        subjectsList.setAdapter(Adapter);
    }
    public void saveChanges(DatabaseReference testReference){

        testReference.child("subjects").setValue(Subjects);
        testReference.child("percentage").setValue(""+calculatePercentage());
    }
    public static void fillHashmap(DatabaseReference ref)
    {
        Subjects.clear();
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Subjects.put(dataSnapshot.getKey(),dataSnapshot.getValue(subject.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Subjects.put(dataSnapshot.getKey(),dataSnapshot.getValue(subject.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public float calculatePercentage()
    {
        float marks=Float.parseFloat("0.0");
        float totalMarks=Float.parseFloat("0.0");
        for(subject e:Subjects.values())
        {
            marks=marks+e.getSubMarks();
            totalMarks=totalMarks+e.getTotalMarks();
        }
        return (marks/totalMarks)*100;
    }
}
