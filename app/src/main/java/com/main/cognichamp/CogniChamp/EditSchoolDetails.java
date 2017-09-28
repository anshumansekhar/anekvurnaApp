package com.main.cognichamp.CogniChamp;

import android.content.Intent;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

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
        stateAdapter=ArrayAdapter.createFromResource(EditSchoolDetails.this,R.array.states,android.R.layout.simple_spinner_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseState.setAdapter(stateAdapter);
        final String[] states=getResources().getStringArray(R.array.states);

        chooseState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getCities(position);
                currentState=states[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                citiesAdapter.clear();
            }
        });
        citiesAdapter=new ArrayAdapter<String>(EditSchoolDetails.this,android.R.layout.simple_spinner_item,cities);
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
        schoolAdapter=new ArrayAdapter<String>(EditSchoolDetails.this,android.R.layout.simple_spinner_item,schoolNames);
        schoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseSchool.setAdapter(schoolAdapter);

        chooseSchool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv=(TextView)view;
                addSubjectCard.setVisibility(View.VISIBLE);
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
                                        Toast.makeText(getApplicationContext(), "School Details Updated", Toast.LENGTH_SHORT).show();
                                    }
                                });
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
    public void getCities(int state){
        citiesAdapter.clear();
        cities.clear();
        Log.e("gd",""+state);
        switch (state){
            case 0:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State1)));
                break;
            case 1:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State2)));
                break;
            case 2:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State3)));
                break;
            case 3:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State4)));
                break;
            case 4:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State5)));
                break;
            case 5:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State6)));
                break;
            case 6:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State7)));
                break;
            case 7:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State8)));
                break;
            case 8:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State9)));
                break;
            case 9:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State10)));
                break;
            case 10:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State11)));
                break;
            case 11:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State12)));
                break;
            case 12:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State13)));
                break;
            case 13:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State14)));
                break;
            case 14:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State15)));
                break;
            case 15:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State16)));
                break;
            case 16:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State17)));
                break;
            case 17:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State18)));
                break;
            case 18:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State19)));
                break;
            case 19:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State20)));
                break;
            case 20:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State21)));
                break;
            case 21:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State22)));
                break;
            case 22:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State23)));
                break;
            case 23:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State24)));
                break;
            case 24:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State25)));
                break;
            case 25:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State26)));
                break;
            case 26:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State27)));
                break;
            case 27:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State28)));
                break;
            case 28:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State29)));
                break;
            case 29:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State30)));
                break;
            case 30:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State31)));
                break;
            case 31:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State32)));
                break;
            case 32:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State33)));
                break;
            case 33:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State34)));
                break;
            case 34:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State35)));
                break;
            case 35:
                cities.addAll(Arrays.asList(getResources().getStringArray(R.array.State36)));
                break;
        }
        citiesAdapter.notifyDataSetChanged();
       Log.e("sgd",""+citiesAdapter.getCount());
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
        Intent fr=new Intent(EditSchoolDetails.this,NavigationDrawer.class);
        fr.putExtra("PreviousFrag","addSchool");
        startActivity(fr);
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
                Intent fr=new Intent(EditSchoolDetails.this,NavigationDrawer.class);
                fr.putExtra("PreviousFrag","addSchool");
                startActivity(fr);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
