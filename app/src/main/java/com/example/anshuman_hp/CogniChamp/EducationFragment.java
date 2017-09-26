package com.example.anshuman_hp.CogniChamp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
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
    Spinner selectClass;
    ViewPager pager;
    TabLayout tabLayout;

    HashMap<String, subject> map = new HashMap<>();

    static String className = "Class-1";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    static DatabaseReference classRef;
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
        pager = (ViewPager) view.findViewById(R.id.pager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        final ArrayAdapter classAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.Class, android.R.layout.simple_spinner_item);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectClass.setAdapter(classAdapter);

        final pagerAdapter pagerAdapter = new pagerAdapter(getActivity().getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(pager);
        classes = getResources().getStringArray(R.array.Class);

        selectClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                className=classes[position];
                if(!className.equals("Class-11")&&!className.equals("Class-12") && !className.equals("Class-10")) {
                    classRef = database.getReference(auth.getCurrentUser().getUid())
                            .child("ClassDetails")
                            .child(className);
                    classRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                pagerAdapter.update(EducationFragment.classRef.child("tests").child("UnitTest").child("subjects"),getActivity());
                            }
                            else {
                                classRef.setValue(new ClassDetails());
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else if(className.equals("Class-10")){
                    classRef=database.getReference(auth.getCurrentUser().getUid())
                            .child("ClassDetails")
                            .child(className);
                    classRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                pagerAdapter.update(EducationFragment.classRef.child("tests").child("UnitTest").child("Subjects"),getActivity());

                            }
                            else{
                                classRef.setValue(new ClassDetails(10));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                        builder.setSingleChoiceItems(R.array.Stream, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                classRef=database.getReference(auth.getCurrentUser().getUid())
                                        .child("ClassDetails")
                                        .child(className)
                                        .child(""+getActivity().getResources().getStringArray(R.array.Stream)[which]);
                                classRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            //TODO get the data
                                            pagerAdapter.update(EducationFragment.classRef.child("tests").child("UnitTest").child("Subjects"),getActivity());

                                        }
                                        else {
                                            if(className.equals("Class-12")){
                                                classRef.setValue(new ClassDetails(12));
                                            }else {
                                                classRef.setValue(new ClassDetails());
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                dialog.dismiss();
                            }
                        });
                    AlertDialog dialog=builder.create();
                    dialog.setCancelable(false);
                    dialog.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }
}
