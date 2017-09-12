package com.example.anshuman_hp.internship;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Anshuman-HP on 03-09-2017.
 */

public class schoolDetails extends Fragment {
    Spinner state;
    Spinner school;
    Spinner city;
    EditText schoolNameEdit;
    Button submit;

    LinearLayout layout;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();

    public static boolean isChanged=false;

    School schoolObject;

    ArrayList<String> Cities=new ArrayList<>();
    ArrayList<String> Schools=new ArrayList<>();

    String selectedState="Odisha";
    String currentCity="";

    ArrayAdapter schoolAdapter;
    ArrayAdapter CityAdapter;

    public schoolDetails() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.school_details,container,false);
        state = (Spinner)v.findViewById(R.id.chooseStateSchool);
        school=(Spinner)v.findViewById(R.id.selectSchool);
        city=(Spinner) v.findViewById(R.id.chooseCity);
        schoolNameEdit=(EditText)v.findViewById(R.id.schoolnamedit);
        submit=(Button)v.findViewById(R.id.schoolnameditSubmit);
        layout=(LinearLayout)v.findViewById(R.id.addSubject);

        layout.setVisibility(View.GONE);

        ArrayAdapter stateAdapter=ArrayAdapter.createFromResource(getActivity(),R.array.states,android.R.layout.simple_spinner_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(stateAdapter);

        CityAdapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, Cities);
        CityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(CityAdapter);

        schoolAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,Schools);
        schoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        school.setAdapter(schoolAdapter);

        final String states[]=getResources().getStringArray(R.array.states);


        final DatabaseReference schoolDetail=firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("SchoolDetails")
                .child(EducationFragment.className);
        schoolDetail.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            schoolObject = dataSnapshot.getValue(School.class);
                            fetchSchools(schoolObject.getCity());
                            fetchCities(schoolObject.getState());
                            if(Cities.size()>0 && Schools.size()>0) {
                                Log.e("setting","School Details");
                                city.setSelection(Cities.indexOf(schoolObject.getCity()));
                                school.setSelection(Schools.indexOf(schoolObject.getSchoolName()));
                            }
                            state.setSelection(Arrays.asList(states).indexOf(schoolObject.getState()));
                        }
                        else {
                            schoolDetail.setValue(new School("","Anadaman and Nicobar Islands","",""));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              if(schoolObject!=null) {
                  Log.e("State","Clicked");
                  isChanged = true;
                  schoolObject.setState(states[position]);
                  selectedState = states[position];
                  fetchCities(selectedState);
                  Schools.clear();
              }
              else
              {
                  Log.e("novoject","Ststee");
                  isChanged=true;
                  fetchCities(selectedState);
                  Schools.clear();

              }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(Cities.size()>0){
                    fetchSchools(Cities.get(position));
                }
                currentCity=Cities.get(position);
                if(schoolObject!=null && !schoolObject.getSchoolName().equals(""))
                    school.setSelection(Schools.indexOf(schoolObject.getSchoolName()));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("onadn","on item nothing");

            }
        });
        school.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(Schools.size()>0) {

                    if (Schools.get(position).equals("Add School")) {
                        layout.setVisibility(View.VISIBLE);
                    } else {
                        layout.setVisibility(View.GONE);
                        schoolObject.setSchoolName(Schools.get(position));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseDatabase.getReference("Schools")
                        .child(currentCity)
                        .child(schoolNameEdit.getText().toString())
                        .setValue(schoolNameEdit.getText().toString());
                firebaseDatabase.getReference("Cities")
                        .child(selectedState)
                        .child(currentCity)
                        .setValue(currentCity);
                firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                        .child("SchoolDetails")
                        .child(EducationFragment.className)
                        .child("schoolName")
                        .setValue(schoolNameEdit.getText().toString());
                layout.setVisibility(View.GONE);
                schoolAdapter.notifyDataSetChanged();
                CityAdapter.notifyDataSetChanged();
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
    void fetchSchools(String city)
    {
        if(!city.equals("") && city!=null) {
            Schools.clear();
            firebaseDatabase.getReference("Schools")
                    .child(city)
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if (dataSnapshot.exists()) {
                                if (!Schools.contains(dataSnapshot.getValue().toString())) {
                                    Schools.add(dataSnapshot.getValue().toString());
                                    Log.e("Schools", Schools.get(0));
//                                    school.setSelection(Schools.indexOf(schoolObject.getSchoolName()));
                                    if(schoolAdapter!=null){
                                        Log.e("notifying","darts");
                                        schoolAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            if (dataSnapshot.exists()) {
                                if (!Schools.contains(dataSnapshot.getValue().toString())) {
                                    Schools.add(dataSnapshot.getValue().toString());
                                    if(schoolAdapter!=null){
                                        Log.e("notifying","darts");
                                        schoolAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                if (!Schools.contains(dataSnapshot.getValue().toString())) {
                                    Schools.add(dataSnapshot.getValue().toString());
                                    if(schoolAdapter!=null){
                                        Log.e("notifying","darts");
                                        schoolAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                            if (dataSnapshot.exists()) {
                                if (!Schools.contains(dataSnapshot.getValue().toString())) {
                                    Schools.add(dataSnapshot.getValue().toString());
                                    if(schoolAdapter!=null){
                                        Log.e("notifying","darts");
                                        schoolAdapter.notifyDataSetChanged();
                                    }
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {


                        }
                    });
            Schools.add("Add School");
        }
    }

    void fetchCities(String state)
    {
        Log.e("State",state);
        Cities.clear();
        firebaseDatabase.getReference("Cities")
                .child(state)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if(dataSnapshot.exists()) {
                            if (!Cities.contains(dataSnapshot.getValue().toString())) {
                                Cities.add(dataSnapshot.getValue().toString());
                                //city.setSelection(Cities.indexOf(schoolObject.getCity()));
                                //Log.e("cities",Cities.get(0));
                                if(CityAdapter!=null){
                                    Log.e("notifying","darts");
                                    CityAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        if(dataSnapshot.exists()) {
                            if (!Cities.contains(dataSnapshot.getValue().toString())) {
                                Cities.add(dataSnapshot.getValue().toString());
                                if(CityAdapter!=null){
                                    Log.e("notifying","darts");
                                    CityAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            if (!Cities.contains(dataSnapshot.getValue().toString())) {
                                Cities.add(dataSnapshot.getValue().toString());
                                if(CityAdapter!=null){
                                    Log.e("notifying","darts");
                                    CityAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        if(dataSnapshot.exists()) {
                            if (!Cities.contains(dataSnapshot.getValue().toString())) {
                                Cities.add(dataSnapshot.getValue().toString());
                                if(CityAdapter!=null){
                                    Log.e("notifying","darts");
                                    CityAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

}
