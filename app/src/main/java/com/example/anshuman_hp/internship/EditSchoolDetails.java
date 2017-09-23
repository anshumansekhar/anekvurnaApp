package com.example.anshuman_hp.internship;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StreamDownloadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class EditSchoolDetails extends AppCompatActivity {

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();

    Spinner chooseState,chooseCity,chooseSchool;
    Button submit;
    CardView addSubjectCard;
    EditText schoolNameEnter,schoolAddressEnter,schoolPinCodeEnter;

    ArrayList<String> cities=new ArrayList<>();
    ArrayList<School> schools=new ArrayList<>();
    ArrayList<String> schoolNames=new ArrayList<>();

    ArrayAdapter stateAdapter;
    ArrayAdapter<String> citiesAdapter;
    ArrayAdapter<String> schoolAdapter;

    String className;
    String currentCity;
    String currentState;
    String currentSchool;

    School s=new School();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_school_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        className=getIntent().getStringExtra("ClassName");
        chooseState=(Spinner)findViewById(R.id.chooseStateSchool);
        chooseCity=(Spinner)findViewById(R.id.chooseCity);
        chooseSchool=(Spinner)findViewById(R.id.selectSchool);
        submit=(Button)findViewById(R.id.submit);
        addSubjectCard=(CardView)findViewById(R.id.addSubjectCard);
        schoolNameEnter=(EditText)findViewById(R.id.schoolnamedit);
        schoolPinCodeEnter=(EditText)findViewById(R.id.schoolPinCodeEdit);
        schoolAddressEnter=(EditText)findViewById(R.id.schoolAddressEdit);
        stateAdapter=ArrayAdapter.createFromResource(getApplicationContext(),R.array.states,android.R.layout.simple_spinner_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseState.setAdapter(stateAdapter);
        final String[] states=getResources().getStringArray(R.array.states);

        chooseState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getCities(states[position]);
                currentState=states[position];
                addSubjectCard.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                citiesAdapter.clear();
            }
        });
        citiesAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,cities);
        citiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseCity.setAdapter(citiesAdapter);

        chooseCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    getSchools(cities.get(position));
                    currentCity=cities.get(position);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    schoolAdapter.clear();
                }
            });
        schoolAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,schoolNames);
        schoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseSchool.setAdapter(schoolAdapter);

        chooseSchool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv=(TextView)view;
                if(tv.getText().toString().equals("Add a New School")){
                    addSubjectCard.setVisibility(View.VISIBLE);
                }
                else{
                    addSubjectCard.setVisibility(View.GONE);
                    Log.e("fqeah",""+position);
                    database.getReference("Schools")
                            .child(currentCity)
                            .child(schoolNames.get(position))
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        s=dataSnapshot.getValue(School.class);
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
                addSubjectCard.setVisibility(View.VISIBLE);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addSubjectCard.getVisibility()==View.VISIBLE){
                    if(!schoolNameEnter.getText().toString().isEmpty() && !schoolPinCodeEnter.getText().toString().isEmpty() && !schoolAddressEnter.getText().toString().isEmpty()){
                        s.setSchoolName(schoolNameEnter.getText().toString());
                        s.setSchoolAddress(schoolAddressEnter.getText().toString());
                        s.setSchoolCity(currentCity);
                        s.setSchoolPin(schoolPinCodeEnter.getText().toString());
                        s.setSchoolLogo("");
                        s.setSchoolState(currentState);
                    }
                    else if(schoolNameEnter.getText().toString().isEmpty()){
                        schoolNameEnter.setError("This Field Cant be Empty");
                    }
                    else if(schoolPinCodeEnter.getText().toString().isEmpty()){
                        schoolPinCodeEnter.setError("This Field Cant be Empty");
                    }
                    else if(schoolAddressEnter.getText().toString().isEmpty()){
                        schoolAddressEnter.setError("This Field Cant be Empty");
                    }
                    Log.e("sf",s.getSchoolName());
                    database.getReference("Schools")
                            .child(s.getSchoolCity())
                            .child(s.getSchoolName())
                            .setValue(s);
                    database.getReference(auth.getCurrentUser().getUid())
                            .child("SchoolDetails")
                            .child(className)
                            .setValue(s)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"School Details Updated",Toast.LENGTH_SHORT).show();
                            //TODO go Back
                        }
                    });
                }
                else{
                    if(s!=null){
                        database.getReference(auth.getCurrentUser().getUid())
                                .child("SchoolDetails")
                                .child(className)
                                .setValue(s)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"School Details Updated",Toast.LENGTH_SHORT).show();
                                //TODO go Back
                            }
                        });
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Unable to Fetch School Details",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }
    public void getCities(String state){
        citiesAdapter.clear();
        cities.clear();

        database.getReference("Cities")
                .child(state)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if(!cities.contains(dataSnapshot.getValue().toString())){
                            cities.add(dataSnapshot.getValue().toString());
                            citiesAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        if(!cities.contains(dataSnapshot.getValue().toString())){
                            cities.add(dataSnapshot.getValue().toString());
                            citiesAdapter.notifyDataSetChanged();
                        }

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
    public void getSchools(String city){
        schoolAdapter.clear();
        schoolNames.clear();
        database.getReference("Schools")
                .child(city)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if(!schoolNames.contains(dataSnapshot.getValue(School.class).getSchoolName())) {
                            schools.add(dataSnapshot.getValue(School.class));
                            schoolNames.add(dataSnapshot.getValue(School.class).getSchoolName());
                            schoolAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        if(!schoolNames.contains(dataSnapshot.getValue(School.class).getSchoolName())) {
                            schools.add(dataSnapshot.getValue(School.class));
                            schoolNames.add(dataSnapshot.getValue(School.class).getSchoolName());
                            schoolAdapter.notifyDataSetChanged();
                        }

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
        schoolNames.add(0,"Add a New School");
        schoolAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
