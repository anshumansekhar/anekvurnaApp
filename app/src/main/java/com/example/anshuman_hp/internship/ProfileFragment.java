package com.example.anshuman_hp.internship;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Anshuman-HP on 12-08-2017.
 */

public class ProfileFragment extends Fragment implements DatePickerDialog.OnDateSetListener{
    private static final java.lang.String DATE_FORMAT ="dd-MM-yyyy" ;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    ImageView profileImage;
    EditText name;
    EditText birthDate;
    RadioGroup gender;
    RadioButton male;
    RadioButton female;
    Spinner presentClass;
    EditText Ataddress;
    EditText city;
    Spinner stateSpinner;
    String presentClassText;

    String myFormat = "dd-MM-yyyy"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog datePickerDialog;

    boolean isChanged=false;


    user_profile user_profile;

    public static ProfileFragment newInstance() {
        Log.e("TAG","Creating profile fragment");
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }
    public ProfileFragment() {
        super();
    }
    public boolean isChanged() {
        return isChanged;
    }
    public void setChanged(boolean changed) {
        isChanged = changed;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.profile_details,container,false);
        profileImage=(ImageView)v.findViewById(R.id.profileImage);
        name=(EditText)v.findViewById(R.id.nameProfile);
        birthDate=(EditText)v.findViewById(R.id.birthdateEdit);
        gender=(RadioGroup)v.findViewById(R.id.genderRadioGroup);
        male=(RadioButton)v.findViewById(R.id.male);
        female=(RadioButton)v.findViewById(R.id.female);
        Ataddress=(EditText)v.findViewById(R.id.AtAddress);
        city=(EditText)v.findViewById(R.id.CityAddress);
        presentClass=(Spinner)v.findViewById(R.id.presentClassSpinnerProfile);
        stateSpinner=(Spinner)v.findViewById(R.id.stateSpinner);
        ArrayAdapter stateAdapter=ArrayAdapter.createFromResource(getActivity(),R.array.states,android.R.layout.simple_spinner_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(stateAdapter);
        ArrayAdapter arrayAdapter=ArrayAdapter.createFromResource(getActivity(),R.array.Class,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        presentClass.setAdapter(arrayAdapter);

        datePickerDialog=new DatePickerDialog(getActivity(),this,2000,1,1);
        Log.e("getting","User Profile");
        database.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("UserProfile")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        user_profile=dataSnapshot.getValue(user_profile.class);
                        Log.e("USER PROFILE",user_profile.toString());
                        if(getActivity()==null) {
                            return;
                        }
                        Glide.with(getActivity())
                                .load(user_profile.getPhotourl())
                                .into(profileImage);
                        name.setText(user_profile.getName());
                        birthDate.setText(user_profile.getBirthdate());
                        if(user_profile.getIsMale().equals("true")) {
                            male.setChecked(true);
                            female.setChecked(false);
                        }
                        else {
                            male.setChecked(false);
                            female.setChecked(true);
                        }
                        presentClass.setSelection(Integer.valueOf(user_profile.getPresentClass()));
                        //address.setText(user_profile.getAddress());
                        presentClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if(Integer.valueOf(user_profile.getPresentClass())!=position) {
                                    isChanged=true;
                                    user_profile.setPresentClass(""+position);
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!user_profile.getName().equals(s.toString().trim())) {
                    isChanged=true;
                    user_profile.setName(s.toString());
                }
            }
        });
        birthDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!user_profile.getBirthdate().equals(s.toString().trim())) {
                    user_profile.setBirthdate(s.toString());
                }
            }
        });
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(user_profile.getIsMale().equals("false") && group.getCheckedRadioButtonId()==R.id.male) {
                    male.setChecked(true);
                    female.setChecked(false);
                    user_profile.setIsMale("true");
                    isChanged=true;
                }
                else if(user_profile.getIsMale().equals("true") && group.getCheckedRadioButtonId()==R.id.female){
                    female.setChecked(true);
                    male.setChecked(false);
                    user_profile.setIsMale("false");
                    isChanged=true;
                }
            }
        });
        Ataddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!user_profile.getAtAddress().equals(s.toString().trim())) {
                    isChanged = true;
                    user_profile.setAtAddress(s.toString());
                }

            }
        });
        city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!user_profile.getCityAddress().equals(s.toString().trim())) {
                    isChanged = true;
                    user_profile.setCityAddress(s.toString());
                }

            }
        });
        final String[] states=getActivity().getResources().getStringArray(R.array.states);
        int indexState=Arrays.asList(states).indexOf(user_profile.getState());
        stateSpinner.setSelection(indexState);
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TODO get the state and push the profile details
                isChanged=true;
                user_profile.setState(states[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return v;
    }
    public void saveChanges() {
        Log.e("Showing","Dialog");
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm");
        builder.setMessage("You have some unsaved chages!!");
        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                database.getReference(firebaseAuth.getCurrentUser().getUid())
                        .child("UserProfile")
                        .setValue(user_profile)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                isChanged=false;
                                Log.e("User","Changed");
                                dialog.cancel();
                            }
                        });


            }
        }).setNegativeButton("DISCARD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
        database.getReference("Cities")
                .child(user_profile.getState())
                .child(user_profile.getCityAddress())
                .setValue(user_profile.getCityAddress());
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            birthDate.setText(sdf.format(myCalendar.getTime()));
    }
}
