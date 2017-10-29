package com.cognichamp.CogniChamp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Created by Anshuman-HP on 11-08-2017.
 */

public class EducationFragment extends Fragment {
    static String className = "Class-1";
    static DatabaseReference classRef;
    Spinner selectClass;
    ViewPager pager;
    TabLayout tabLayout;
    HashMap<String, subject> map = new HashMap<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String[] classes;

    public static EducationFragment newInstance() {
        return new EducationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.educationnew, container, false);
        selectClass = (Spinner) view.findViewById(R.id.selectClassEducation);
        pager = (ViewPager) view.findViewById(R.id.pager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        final ArrayAdapter classAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.Class, R.layout.spinner_item);
        classAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        selectClass.setAdapter(classAdapter);

        final pagerAdapter pagerAdapter = new pagerAdapter(getActivity().getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(pager);
        classes = getResources().getStringArray(R.array.Class);

        selectClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                className=classes[position];
                if(!className.contains("Class-12") && !className.contains("Class-10")) {
                    classRef = database.getReference(auth.getCurrentUser().getUid())
                            .child("ClassDetails")
                            .child(className);
                    classRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                classRef.child("tests")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.exists()) {
                                                    pagerAdapter.update(EducationFragment.classRef.child("tests").child("UnitTest").child("subjects"), getActivity());
                                                }else {
                                                    // addSubjectswith(12);
                                                    addItems();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                            }
                            else {
                                //addSubjectswith(12);
                                addItems();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else if(className.equals("Class-10") ){
                    classRef=database.getReference(auth.getCurrentUser().getUid())
                            .child("ClassDetails")
                            .child(className);
                    classRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                classRef.child("tests")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.exists()) {
                                                    pagerAdapter.update(EducationFragment.classRef.child("tests").child("UnitTest").child("subjects"), getActivity());
                                                }else {
                                                    addSubjectswith(12);
                                                    addItems();
                                                }
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                            }
                            else{
                                //addSubjectswith(12);
                                addItems();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else if(className.contains("Class-12")){
                    classRef=database.getReference(auth.getCurrentUser().getUid())
                            .child("ClassDetails")
                            .child(className);
                                classRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()) {
                                            classRef.child("tests")
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists()) {
                                                                pagerAdapter.update(EducationFragment.classRef.child("tests").child("UnitTest-1").child("subjects"), getActivity());
                                                            } else {
                                                                addSubjectswith(12);
                                                                addItems();
                                                            }
                                                        }
                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                        }
                                                    });
                                        }
                                        else {
                                            //addSubjectswith(12);
                                            addItems();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }
    public void addSubjects(){
        classRef.setValue(new ClassDetails());
    }

    public void addItems() {
        classRef
                .child("subjects")
                .child("0")
                .setValue(new subjectItem("English"));
        classRef
                .child("subjects")
                .child("1")
                .setValue(new subjectItem("Maths"));
    }
    public void addSubjectswith(int num){
        classRef.setValue(new ClassDetails(num));
    }
}
