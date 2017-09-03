package com.example.anshuman_hp.internship;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Anshuman-HP on 11-08-2017.
 */

public class EducationFragment extends Fragment {
    Spinner selectClass;
    ViewPager pager;
    TabLayout tabLayout;



    HashMap<String,subject> map=new HashMap<>();


    String SchoolName;
    static String className="-1";

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
        final View view = inflater.inflate(R.layout.educationnew, container, false);
        selectClass = (Spinner) view.findViewById(R.id.selectClassEducation);
        pager=(ViewPager)view.findViewById(R.id.pager);
        tabLayout=(TabLayout)view.findViewById(R.id.tabLayout);

        ArrayAdapter classAdapter=ArrayAdapter.createFromResource(getActivity(),R.array.Class,android.R.layout.simple_spinner_item);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectClass.setAdapter(classAdapter);

        pagerAdapter pagerAdapter=new pagerAdapter(getActivity().getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(pager);



        classes = getResources().getStringArray(R.array.Class);
        selectClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!className.equals("-1") && isChanged)
                {
//                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
//                    builder.setTitle("Confirm");
//                    builder.setMessage("You have some unsaved chages!!");
//                    builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            saveChanges(ref,dref,subjectsRef);
//                        }
//                    }).setNegativeButton("DISCARD", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
//                    AlertDialog dialog=builder.create();
//                    dialog.show();

                }
                className = "" + position ;
                if(!className.equals("-1")){
                    AlertDialog dialog=null;
                    if(className.equals("10") || className.equals("11"))
                    {
                        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                        builder.setSingleChoiceItems(R.array.Stream, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ref=database.getReference(auth.getCurrentUser().getUid())
                                        .child("ClassDetails")
                                        .child(className)
                                        .child(""+getActivity().getResources().getStringArray(R.array.Stream)[which])
                                        .child("SchoolName");
                                dref=database.getReference(auth.getCurrentUser().getUid())
                                        .child("ClassDetails")
                                        .child(className)
                                        .child(""+getActivity().getResources().getStringArray(R.array.Stream)[which])
                                        .child("percentage");
                                subjectsRef=database.getReference(auth.getCurrentUser().getUid())
                                        .child("ClassDetails")
                                        .child(className)
                                        .child(""+getActivity().getResources().getStringArray(R.array.Stream)[which])
                                        .child("Subjects");
                                setUp(className);
                                dialog.dismiss();
                            }
                        });
                        dialog=builder.create();
                        dialog.setCancelable(false);
                        dialog.show();
                    }
                    else
                        setUp(className);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveChanges(ref,dref,subjectsRef);
//            }
//        });
//        addnewSubject.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(getActivity(), AddNewSubject.class);
//                Log.e("size",""+map.size());
//                i.putExtra("Position",map.size());
//                i.putExtra("Ref",subjectsRef.getRef().toString());
//                startActivity(i);
//            }
//        });
        return view;
    }
    public void setUp(final String classN) {
        map.clear();
        Log.e("Class",""+classN);
        if(!classN.equals("10") && !classN.equals("11")) {
            ref = database.getReference(auth.getCurrentUser().getUid()).child("ClassDetails")
                    .child(classN)
                    .child("SchoolName");
            dref = database.getReference(auth.getCurrentUser().getUid()).child("ClassDetails")
                    .child(classN)
                    .child("percentage");
            subjectsRef = database.getReference(auth.getCurrentUser().getUid()).child("ClassDetails")
                    .child(classN)
                    .child("Subjects");
        }
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    SchoolName = dataSnapshot.getValue().toString();
                   // schoolName.setText(SchoolName);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
             // percentage.setText("Percentage: "+dataSnapshot.getValue().toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
//        schoolName.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                if(!SchoolName.equals(s.toString().trim())) {
//                    isChanged=true;
//                }
//            }
//        });
        //setUpAdapter(subjectsRef);
    }
    public void saveChanges(DatabaseReference sref,DatabaseReference pref,DatabaseReference subref) {
        float percentage=0;
        float marks = 0;
        float totalmarks = 0;
        for(subject e:map.values())
        {
            Log.e(e.getSubjectName(),""+e.getSubMarks());
            totalmarks=totalmarks+ e.getTotalMarks();
            marks=marks+e.getSubMarks();
        }
        percentage=(marks/totalmarks)*100;
//        sref.setValue(schoolName.getText().toString());
        isChanged=false;
       pref.setValue(""+percentage);
        isChanged=false;
            subref.setValue(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            isChanged=false;
                            return;
                        }
                    });
    }}
//    public void setUpAdapter(DatabaseReference subjectReference)
//    {
//        recyclerAdapter = new FirebaseRecyclerAdapter<subject, subjectHolder>(subject.class,
//                R.layout.subject_entry
//                , subjectHolder.class
//                , subjectReference) {
//            @Override
//            protected void populateViewHolder(final subjectHolder viewHolder, subject model, final int position) {
//                map.put(""+position,new subject(model.getSubMarks(),model.getTotalMarks(),model.getSubjectName()));
//                Log.e("psotiotion",""+position);
//                viewHolder.subjectName.setText(model.getSubjectName());
//                viewHolder.totalMArks.setText(""+model.getTotalMarks());
//                viewHolder.subjectMarks.setText(""+model.getSubMarks());
//                viewHolder.totalMArks.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        viewHolder.totalMArks.setText("");
//                    }
//                });
//                viewHolder.subjectMarks.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        viewHolder.subjectMarks.setText("");
//                    }
//                });
//                viewHolder.totalMArks.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                    }
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    }
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                        if(!s.toString().equals(""))
//                            if(!(map.get(""+position).getTotalMarks()==Float.parseFloat(s.toString().trim()))) {
//                                isChanged=true;
//                                map.get(""+position).setTotalMarks(Float.parseFloat(s.toString()));
//                            }
//                    }
//                });
//                viewHolder.subjectMarks.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                    }
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    }
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                        if(!s.toString().equals(""))
//                            if(!(map.get(""+position).getSubMarks()==Float.parseFloat(s.toString().trim()))) {
//                                isChanged=true;
//                                if(!s.toString().equals("")) {
//                                    map.get("" + position).setSubMarks(Float.parseFloat(s.toString()));
//                                }
//                            }
//                    }
//                });
//            }
//        };
//        subjectsList.setAdapter(recyclerAdapter);
//
//    }
//}
