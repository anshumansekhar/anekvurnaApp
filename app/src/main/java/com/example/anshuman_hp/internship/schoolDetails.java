package com.example.anshuman_hp.internship;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Anshuman-HP on 03-09-2017.
 */

public class schoolDetails extends Fragment {
    Spinner state;
    Spinner school;
    AutoCompleteTextView city;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();

    public static boolean isChanged=false;

    School schoolObject;

    ArrayList<String> Cities=new ArrayList();
    ArrayList<String> Schools=new ArrayList<>();

    public schoolDetails() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.school_details,container,false);
        state = (Spinner)v.findViewById(R.id.chooseStateSchool);
        school=(Spinner)v.findViewById(R.id.selectSchool);
        city=(AutoCompleteTextView)v.findViewById(R.id.chooseCity);

        ArrayAdapter stateAdapter=ArrayAdapter.createFromResource(getActivity(),R.array.states,android.R.layout.simple_spinner_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(stateAdapter);

        firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("SchoolDetails")
                .child(EducationFragment.className)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        schoolObject=dataSnapshot.getValue(School.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        final String[] states=getActivity().getResources().getStringArray(R.array.states);
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TODO get the state and accordingly load all the cities\
                isChanged=true;
                schoolObject.setState(states[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        firebaseDatabase.getReference("Cities")
                .child(schoolObject.getState())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if(!Cities.contains(dataSnapshot.getValue().toString())){
                            Cities.add(dataSnapshot.getValue().toString());
                        }
                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        if(!Cities.contains(dataSnapshot.getValue().toString())){
                            Cities.add(dataSnapshot.getValue().toString());
                        }
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        if(!Cities.contains(dataSnapshot.getValue().toString())){
                            Cities.add(dataSnapshot.getValue().toString());
                        }
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        if(!Cities.contains(dataSnapshot.getValue().toString())){
                            Cities.add(dataSnapshot.getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        ArrayAdapter CityAdapter=new ArrayAdapter(getActivity(),android.R.layout.simple_dropdown_item_1line,Cities);
        city.setAdapter(CityAdapter);
        city.setThreshold(1);
        city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!schoolObject.getCity().equals(s.toString())) {
                    schoolObject.setCity(s.toString());
                }
            }
        });
        firebaseDatabase.getReference("Schools")
                .child(schoolObject.getCity())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if(!Schools.contains(dataSnapshot.getValue().toString())) {
                            Schools.add(dataSnapshot.getValue().toString());
                        }

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        if(!Schools.contains(dataSnapshot.getValue().toString())) {
                            Schools.add(dataSnapshot.getValue().toString());
                        }

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        if(!Schools.contains(dataSnapshot.getValue().toString())) {
                            Schools.add(dataSnapshot.getValue().toString());
                        }

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        if(!Schools.contains(dataSnapshot.getValue().toString())) {
                            Schools.add(dataSnapshot.getValue().toString());
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {


                    }
                });
        ArrayAdapter schoolAdapter=new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,Schools);
        schoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        school.setAdapter(schoolAdapter);
        school.setSelection(Schools.indexOf(schoolObject.getSchoolName()));

        school.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                schoolObject.setSchoolName(Schools.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }

    public void saveChanges()
    {
        firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("SchoolDetails")
                .child(EducationFragment.className)
                .setValue(schoolObject);
        firebaseDatabase.getReference("Schools")
                .child(schoolObject.getCity())
                .push()
                .setValue(schoolObject.getSchoolName());
        firebaseDatabase.getReference("Cities")
                .child(schoolObject.getState())
                .push()
                .setValue(schoolObject.getCity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveChanges();
    }

}
