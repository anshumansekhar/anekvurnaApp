package com.example.anshuman_hp.internship;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Anshuman-HP on 11-08-2017.
 */

public class EducationFragment extends Fragment {
    ArrayAdapter adapter;
    Spinner selectClass;
    EditText schoolName;
    TextView percentage;
    RecyclerView subjects;
    //Button addSubject;
    Button save;

    ArrayList<subject> list=new ArrayList<>();

    Float marks = Float.valueOf(0);
    Float totalmarks = Float.valueOf(0);

    FirebaseRecyclerAdapter<subject, subjectHolder> recyclerAdapter;

    String SchoolName;
    String className;

    boolean isChanged = false;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference ref;
    DatabaseReference dref;
    DatabaseReference subjectsRef;
    String[] classes;

    public static EducationFragment newInstance() {
        EducationFragment fragment = new EducationFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.new_education_details, container, false);
        selectClass = (Spinner) view.findViewById(R.id.selectclassEducation);
        schoolName = (EditText) view.findViewById(R.id.schoolnameEducation);
        percentage = (TextView) view.findViewById(R.id.percentageEducation);
        subjects = (RecyclerView) view.findViewById(R.id.subjectRecyclerEducation);
//        addSubject = (Button) view.findViewById(R.id.addSubject);
        save=(Button)view.findViewById(R.id.save);
        subjects.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Class, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectClass.setAdapter(adapter);
        classes = getResources().getStringArray(R.array.Class);
        className = "-1";
        selectClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!className.equals("-1") && isChanged)
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirm");
                    builder.setMessage("You have some unsaved chages!!");
                    builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveChanges();
                        }
                    }).setNegativeButton("DISCARD", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog=builder.create();
                    dialog.show();
                }
                className = "" + position ;
                if(!className.equals("-1"))
                    setUp(className);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });
//        addSubject.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                database.getReference("ClassDetails")
//                        .child(auth.getCurrentUser().getUid())
//                        .child(className)
//                        .child("Subjects")
//                        .push()
//                        .setValue(new subject("Subject", "", ""));
//            }
//        });
        return view;
    }

    public void setUp(final String classN) {
        list.clear();
        ref = database.getReference("ClassDetails").child(auth.getCurrentUser().getUid())
                .child(classN)
                .child("SchoolName");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    SchoolName = dataSnapshot.getValue().toString();
                    schoolName.setText(SchoolName);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        dref = database.getReference("ClassDetails").child(auth.getCurrentUser().getUid())
                .child(classN)
                .child("percentage");
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              percentage.setText("Percentage: "+dataSnapshot.getValue().toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        schoolName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!SchoolName.equals(s.toString().trim())) {
                    isChanged=true;
                }
            }
        });
        subjectsRef = database.getReference("ClassDetails").child(auth.getCurrentUser().getUid())
                .child(classN)
                .child("Subjects");
        recyclerAdapter = new FirebaseRecyclerAdapter<subject, subjectHolder>(subject.class,
                R.layout.subject_entry
                , subjectHolder.class
                , subjectsRef) {
            @Override
            protected void populateViewHolder(subjectHolder viewHolder, subject model, final int position) {
                list.add(new subject(model.getSubName(),model.getSubMarks(),model.getTotalMarks()));
                viewHolder.subjectName.setText(model.getSubName());
                viewHolder.totalMArks.setText(model.getTotalMarks());
                viewHolder.subjectMarks.setText(model.getSubMarks());
//                viewHolder.subjectName.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                    }
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    }
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                        if(!list.get(position).getSubName().equals(s.toString().trim())) {
//                            isChanged=true;
//                            list.get(position).setSubName(s.toString());
//                        }
//                    }
//                });
                viewHolder.totalMArks.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        if(!list.get(position).getTotalMarks().equals(s.toString().trim())) {
                            isChanged=true;
                            list.get(position).setTotalMarks(s.toString());
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
                        if(!list.get(position).getSubMarks().equals(s.toString().trim())) {
                            isChanged=true;
                            list.get(position).setSubMarks(s.toString());
                        }
                    }
                });
            }
        };
        subjects.setAdapter(recyclerAdapter);
    }
    public void saveChanges() {
        Float percentage=Float.valueOf(0);
        for(subject e:list)
        {
            totalmarks=totalmarks+Float.valueOf(e.getTotalMarks());
            marks=marks+Float.valueOf(e.getSubMarks());
        }
        percentage=(marks/totalmarks)*100;
        database.getReference("ClassDetails")
                .child(auth.getCurrentUser().getUid())
                .child(className)
                .child("SchoolName")
                .setValue(schoolName.getText().toString());
        isChanged=false;
        database.getReference("ClassDetails")
                .child(auth.getCurrentUser().getUid())
                .child(className)
                .child("percentage")
                .setValue(""+percentage);
        isChanged=false;
        database.getReference("ClassDetails")
                .child(auth.getCurrentUser().getUid())
                .child(className)
                .child("Subjects")
                .removeValue();
            database.getReference("ClassDetails")
                    .child(auth.getCurrentUser().getUid())
                    .child(className)
                    .child("Subjects")
                    .setValue(list)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            isChanged=false;
                            return;
                        }
                    });
    }
}
