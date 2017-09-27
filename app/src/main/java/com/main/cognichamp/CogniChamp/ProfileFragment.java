package com.main.cognichamp.CogniChamp;

import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Anshuman-HP on 12-08-2017.
 */

public class ProfileFragment extends Fragment implements DatePickerDialog.OnDateSetListener{
    private static final java.lang.String DATE_FORMAT ="dd-MM-yyyy" ;
    private static final int RESULT_LOAD_IMG =3524 ;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    StorageReference ref= FirebaseStorage.getInstance().getReference();
    ImageView profileImage;
    EditText name;
    EditText birthDate;
    RadioGroup gender;
    RadioButton male;
    RadioButton female;
    Spinner presentClass;
    EditText Ataddress;
    EditText city;
    EditText pinCode;
    Spinner stateSpinner;
    Spinner districtSpinner;

    String imageUri;
    String myFormat = "dd-MM-yyyy"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

    Calendar myCalendar = Calendar.getInstance();
    NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    ArrayList cities=new ArrayList();
    ArrayAdapter citiesAdapter;

    DatePickerDialog datePickerDialog;
    boolean isChanged=false;
    user_profile user_profile;

    public static ProfileFragment newInstance() {
        Log.e("TAG","Creating profile fragment");
        return new ProfileFragment();
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
        pinCode=(EditText)v.findViewById(R.id.PinCode);
        presentClass=(Spinner)v.findViewById(R.id.presentClassSpinnerProfile);
        stateSpinner=(Spinner)v.findViewById(R.id.stateSpinner);
        districtSpinner=(Spinner)v.findViewById(R.id.districtSpinner);
        citiesAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,cities);
        citiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(citiesAdapter);
        ArrayAdapter stateAdapter=ArrayAdapter.createFromResource(getActivity(),R.array.states,android.R.layout.simple_spinner_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(stateAdapter);
        ArrayAdapter arrayAdapter=ArrayAdapter.createFromResource(getActivity(),R.array.ClassWithStream,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        presentClass.setAdapter(arrayAdapter);
        final String[] states=getActivity().getResources().getStringArray(R.array.states);

        datePickerDialog=new DatePickerDialog(getActivity(),this,2000,1,1);
        Log.e("getting","User Profile");
        database.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("UserProfile")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Log.e("dagd",dataSnapshot.getValue(com.example.anshuman_hp.CogniChamp.user_profile.class).toString());
                        user_profile=dataSnapshot.getValue(user_profile.class);
                        Log.e("USER_PROFILE",user_profile.toString());
                        if(getActivity()==null) {
                            return;
                        }
                        Glide.with(getActivity())
                                .load(user_profile.getPhotourl())
                                .apply(new RequestOptions().override(120,120))
                                .into(profileImage);
                        name.setText(user_profile.getName());
                        birthDate.setText(user_profile.getBirthdate());
                        Ataddress.setText(user_profile.getAtAddress());
                        city.setText(user_profile.getCityAddress());
                        pinCode.setText(user_profile.getPinCode());
                        if(user_profile.getIsMale().equals("true")) {
                            male.setChecked(true);
                            female.setChecked(false);
                        }
                        else {
                            male.setChecked(false);
                            female.setChecked(true);
                        }

                        presentClass.setSelection(Integer.valueOf(user_profile.getPresentClass()));
                        int indexState=Arrays.asList(states).indexOf(user_profile.getState());
                        stateSpinner.setSelection(indexState);
                        presentClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if(user_profile!=null) {
                                    if (Integer.valueOf(user_profile.getPresentClass()) != position) {
                                        isChanged = true;
                                        user_profile.setPresentClass("" + (position ));
                                    }
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
                if(user_profile!=null) {
                    if (!user_profile.getName().equals(s.toString().trim())) {
                        isChanged = true;
                        user_profile.setName(s.toString());
                    }
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
                if(user_profile!=null) {
                    if (!user_profile.getBirthdate().equals(s.toString().trim())) {
                        user_profile.setBirthdate(s.toString());
                    }
                }
            }
        });
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(user_profile!=null) {

                    if (user_profile.getIsMale().equals("false") && group.getCheckedRadioButtonId() == R.id.male) {
                        male.setChecked(true);
                        female.setChecked(false);
                        user_profile.setIsMale("true");
                        isChanged = true;
                    } else if (user_profile.getIsMale().equals("true") && group.getCheckedRadioButtonId() == R.id.female) {
                        female.setChecked(true);
                        male.setChecked(false);
                        user_profile.setIsMale("false");
                        isChanged = true;
                    }
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
                if(user_profile!=null) {
                    if (!user_profile.getAtAddress().equals(s.toString().trim())) {
                        isChanged = true;
                        user_profile.setAtAddress(s.toString());
                    }
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
                if (user_profile != null) {
                    if (!user_profile.getCityAddress().equals(s.toString().trim())) {
                        isChanged = true;
                        user_profile.setCityAddress(s.toString());
                    }

                }
            }
        });

        pinCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(user_profile!=null) {
                    if (!user_profile.getPinCode().equals(s.toString().trim())) {
                        isChanged = true;
                        user_profile.setPinCode(s.toString());
                    }
                }
            }
        });
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(user_profile!=null) {
                    if (states[position] != user_profile.getState()) {
                        isChanged = true;
                        getCities(position);
                        user_profile.setState(states[position]);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user_profile.setDistrict(cities.get(position).toString());

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });
        return v;
    }
    public void saveChanges() {
        Log.e("Showing","Dialog");
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm Changes");
        builder.setMessage("Save Changes");
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
                UploadImage();
                isChanged=false;
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_LOAD_IMG){
            if(resultCode==RESULT_OK){
                Uri selectedImage = data.getData();
                imageUri = selectedImage.toString();
                profileImage.setImageURI(selectedImage);
            }
        }
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if(year<myCalendar.get(Calendar.YEAR)) {
            birthDate.setError(null);
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            birthDate.setText(sdf.format(myCalendar.getTime()));
        }
        else{
            birthDate.setError("Enter a Valid Date of Birth");
            datePickerDialog.show();
        }
    }
    public void UploadImage(){
        if(imageUri!=null) {
            StorageReference photoRef=ref.child(firebaseAuth.getCurrentUser().getUid());
            showProgressNotification();
            UploadTask task = photoRef.putFile(Uri.parse(imageUri));
            task.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int progress=(int)(taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount())*100;
                    builder.setProgress(100,progress,false);
                    notificationManager.notify(1,builder.build());

                }
            });
            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    builder.setContentText("Upload complete")
                            .setProgress(0,0,false);
                    notificationManager.notify(1, builder.build());
                    String url=taskSnapshot.getDownloadUrl().toString();
                    database.getReference(firebaseAuth.getCurrentUser().getUid())
                            .child("UserProfile")
                            .child("photourl")
                            .setValue(url);
                }
            });
        }
        else {
            Toast.makeText(getActivity(),"No image",Toast.LENGTH_SHORT).show();
        }
    }
    public void showProgressNotification(){
        notificationManager=(NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        builder=new NotificationCompat.Builder(getActivity());
        builder = new NotificationCompat.Builder(getActivity());
        builder.setContentTitle("Picture Upload")
                .setContentText("Upload in progress")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp);


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
        if(cities.indexOf(user_profile.getDistrict())!=-1) {
            districtSpinner.setSelection(cities.indexOf(user_profile.getDistrict()));
        }
        else{
            districtSpinner.setSelection(1);
        }
    }
}
