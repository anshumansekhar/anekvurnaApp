package com.cognichamp.CogniChamp;

import android.R.layout;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat.Builder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cognichamp.CogniChamp.R.array;
import com.cognichamp.CogniChamp.R.drawable;
import com.cognichamp.CogniChamp.R.id;
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
import com.google.firebase.storage.UploadTask.TaskSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Anshuman-HP on 12-08-2017.
 */

public class ProfileFragment extends Fragment implements OnDateSetListener {
    private static final String DATE_FORMAT = "dd-MM-yyyy";
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
    SimpleDateFormat sdf = new SimpleDateFormat(this.myFormat, Locale.getDefault());

    Calendar myCalendar = Calendar.getInstance();
    NotificationManager notificationManager;
    Builder builder;

    ArrayList cities=new ArrayList();
    ArrayAdapter citiesAdapter;

    DatePickerDialog datePickerDialog;
    boolean isChanged;
    user_profile user_profile;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    public boolean isChanged() {
        return this.isChanged;
    }
    public void setChanged(boolean changed) {
        this.isChanged = changed;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.profile_details,container,false);
        this.profileImage = (ImageView) v.findViewById(id.profileImage);
        this.name = (EditText) v.findViewById(id.nameProfile);
        this.birthDate = (EditText) v.findViewById(id.birthdateEdit);
        this.gender = (RadioGroup) v.findViewById(id.genderRadioGroup);
        this.male = (RadioButton) v.findViewById(id.male);
        this.female = (RadioButton) v.findViewById(id.female);
        this.Ataddress = (EditText) v.findViewById(id.AtAddress);
        this.city = (EditText) v.findViewById(id.CityAddress);
        this.pinCode = (EditText) v.findViewById(id.PinCode);
        this.presentClass = (Spinner) v.findViewById(id.presentClassSpinnerProfile);
        this.stateSpinner = (Spinner) v.findViewById(id.stateSpinner);
        this.districtSpinner = (Spinner) v.findViewById(id.districtSpinner);
        this.citiesAdapter = new ArrayAdapter<String>(this.getActivity(), layout.simple_spinner_item, this.cities);
        this.citiesAdapter.setDropDownViewResource(layout.simple_spinner_dropdown_item);
        this.districtSpinner.setAdapter(this.citiesAdapter);
        ArrayAdapter stateAdapter = ArrayAdapter.createFromResource(this.getActivity(), array.states, layout.simple_spinner_item);
        stateAdapter.setDropDownViewResource(layout.simple_spinner_dropdown_item);
        this.stateSpinner.setAdapter(stateAdapter);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this.getActivity(), array.ClassWithStream, layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(layout.simple_spinner_dropdown_item);
        this.presentClass.setAdapter(arrayAdapter);
        final String[] states = this.getActivity().getResources().getStringArray(array.states);

        this.datePickerDialog = new DatePickerDialog(this.getActivity(), this, 2000, 1, 1);
        this.database.getReference(this.firebaseAuth.getCurrentUser().getUid())
                .child("UserProfile")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ProfileFragment.this.user_profile = dataSnapshot.getValue(user_profile.class);
                        if (ProfileFragment.this.getActivity() == null) {
                            return;
                        }
                        Glide.with(ProfileFragment.this.getActivity())
                                .load(ProfileFragment.this.user_profile.getPhotourl())
                                .apply(new RequestOptions().override(120,120))
                                .into(ProfileFragment.this.profileImage);
                        ProfileFragment.this.name.setText(ProfileFragment.this.user_profile.getName());
                        ProfileFragment.this.birthDate.setText(ProfileFragment.this.user_profile.getBirthdate());
                        ProfileFragment.this.Ataddress.setText(ProfileFragment.this.user_profile.getAtAddress());
                        ProfileFragment.this.city.setText(ProfileFragment.this.user_profile.getCityAddress());
                        ProfileFragment.this.pinCode.setText(ProfileFragment.this.user_profile.getPinCode());
                        if (ProfileFragment.this.user_profile.getIsMale().equals("true")) {
                            ProfileFragment.this.male.setChecked(true);
                            ProfileFragment.this.female.setChecked(false);
                        }
                        else {
                            ProfileFragment.this.male.setChecked(false);
                            ProfileFragment.this.female.setChecked(true);
                        }

                        ProfileFragment.this.presentClass.setSelection(Integer.valueOf(ProfileFragment.this.user_profile.getPresentClass()));
                        int indexState = Arrays.asList(states).indexOf(ProfileFragment.this.user_profile.getState());
                        ProfileFragment.this.stateSpinner.setSelection(indexState);
                        ProfileFragment.this.presentClass.setOnItemSelectedListener(new OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (ProfileFragment.this.user_profile != null) {
                                    if (Integer.valueOf(ProfileFragment.this.user_profile.getPresentClass()) != position) {
                                        ProfileFragment.this.isChanged = true;
                                        ProfileFragment.this.user_profile.setPresentClass("" + position);
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
        this.birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment.this.datePickerDialog.show();
            }
        });
        this.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (ProfileFragment.this.user_profile != null) {
                    if (!ProfileFragment.this.user_profile.getName().equals(s.toString().trim())) {
                        ProfileFragment.this.isChanged = true;
                        ProfileFragment.this.user_profile.setName(s.toString());
                    }
                }
            }
        });
        this.birthDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if (ProfileFragment.this.user_profile != null) {
                    if (!ProfileFragment.this.user_profile.getBirthdate().equals(s.toString().trim())) {
                        ProfileFragment.this.user_profile.setBirthdate(s.toString());
                    }
                }
            }
        });
        this.gender.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (ProfileFragment.this.user_profile != null) {

                    if (ProfileFragment.this.user_profile.getIsMale().equals("false") && group.getCheckedRadioButtonId() == id.male) {
                        ProfileFragment.this.male.setChecked(true);
                        ProfileFragment.this.female.setChecked(false);
                        ProfileFragment.this.user_profile.setIsMale("true");
                        ProfileFragment.this.isChanged = true;
                    } else if (ProfileFragment.this.user_profile.getIsMale().equals("true") && group.getCheckedRadioButtonId() == id.female) {
                        ProfileFragment.this.female.setChecked(true);
                        ProfileFragment.this.male.setChecked(false);
                        ProfileFragment.this.user_profile.setIsMale("false");
                        ProfileFragment.this.isChanged = true;
                    }
                }
            }
        });
        this.Ataddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ProfileFragment.this.user_profile != null) {
                    if (!ProfileFragment.this.user_profile.getAtAddress().equals(s.toString().trim())) {
                        ProfileFragment.this.isChanged = true;
                        ProfileFragment.this.user_profile.setAtAddress(s.toString());
                    }
                }

            }
        });
        this.city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ProfileFragment.this.user_profile != null) {
                    if (!ProfileFragment.this.user_profile.getCityAddress().equals(s.toString().trim())) {
                        ProfileFragment.this.isChanged = true;
                        ProfileFragment.this.user_profile.setCityAddress(s.toString());
                    }

                }
            }
        });

        this.pinCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ProfileFragment.this.user_profile != null) {
                    if (!ProfileFragment.this.user_profile.getPinCode().equals(s.toString().trim())) {
                        ProfileFragment.this.isChanged = true;
                        ProfileFragment.this.user_profile.setPinCode(s.toString());
                    }
                }
            }
        });
        this.stateSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (ProfileFragment.this.user_profile != null) {
                    if (states[position] != ProfileFragment.this.user_profile.getState()) {
                        ProfileFragment.this.isChanged = true;
                        ProfileFragment.this.getCities(position);
                        ProfileFragment.this.user_profile.setState(states[position]);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        this.districtSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProfileFragment.this.user_profile.setDistrict(ProfileFragment.this.cities.get(position).toString());

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        this.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
                ProfileFragment.this.startActivityForResult(galleryIntent, ProfileFragment.RESULT_LOAD_IMG);
            }
        });
        return v;
    }
    public void saveChanges() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Confirm Changes");
        builder.setMessage("Do you want to save the changes ");
        builder.setPositiveButton("Yes", new OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                ProfileFragment.this.database.getReference(ProfileFragment.this.firebaseAuth.getCurrentUser().getUid())
                        .child("UserProfile")
                        .setValue(ProfileFragment.this.user_profile)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                ProfileFragment.this.isChanged = false;
                                dialog.cancel();
                            }
                        });
                ProfileFragment.this.UploadImage();
                ProfileFragment.this.isChanged = false;
            }
        }).setNegativeButton("No", new OnClickListener() {
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
        if (requestCode == ProfileFragment.RESULT_LOAD_IMG) {
            if(resultCode==RESULT_OK){
                Uri selectedImage = data.getData();
                this.imageUri = selectedImage.toString();
                this.profileImage.setImageURI(selectedImage);
            }
        }
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (year < this.myCalendar.get(Calendar.YEAR)) {
            this.birthDate.setError(null);
            this.myCalendar.set(Calendar.YEAR, year);
            this.myCalendar.set(Calendar.MONTH, month);
            this.myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            this.birthDate.setText(this.sdf.format(this.myCalendar.getTime()));
        }
        else{
            this.birthDate.setError("Enter a Valid Date of Birth");
            this.datePickerDialog.show();
        }
    }
    public void UploadImage(){
        if (this.imageUri != null) {
            StorageReference photoRef = this.ref.child(this.firebaseAuth.getCurrentUser().getUid());
            this.showProgressNotification();
            UploadTask task = photoRef.putFile(Uri.parse(this.imageUri));
            task.addOnProgressListener(new OnProgressListener<TaskSnapshot>() {
                @Override
                public void onProgress(TaskSnapshot taskSnapshot) {
                    int progress=(int)(taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount())*100;
                    ProfileFragment.this.builder.setProgress(100, progress, false);
                    ProfileFragment.this.notificationManager.notify(1, ProfileFragment.this.builder.build());

                }
            });
            task.addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
                @Override
                public void onSuccess(TaskSnapshot taskSnapshot) {
                    ProfileFragment.this.builder.setContentText("Upload complete")
                            .setProgress(0,0,false);
                    ProfileFragment.this.notificationManager.notify(1, ProfileFragment.this.builder.build());
                    String url=taskSnapshot.getDownloadUrl().toString();
                    ProfileFragment.this.database.getReference(ProfileFragment.this.firebaseAuth.getCurrentUser().getUid())
                            .child("UserProfile")
                            .child("photourl")
                            .setValue(url);
                }
            });
        }
        else {
            //no image uploaded
        }
    }
    public void showProgressNotification(){
        this.notificationManager = (NotificationManager) this.getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        this.builder = new Builder(this.getActivity());
        this.builder = new Builder(this.getActivity());
        this.builder.setContentTitle("Picture Upload")
                .setContentText("Upload in progress")
                .setSmallIcon(drawable.ic_notifications_black_24dp);


    }
    public void getCities(int state){
        state = state - 1;
        this.citiesAdapter.clear();
        this.cities.clear();
        switch (state){
            case 0:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State1)));
                break;
            case 1:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State2)));
                break;
            case 2:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State3)));
                break;
            case 3:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State4)));
                break;
            case 4:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State5)));
                break;
            case 5:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State6)));
                break;
            case 6:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State7)));
                break;
            case 7:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State8)));
                break;
            case 8:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State9)));
                break;
            case 9:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State10)));
                break;
            case 10:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State11)));
                break;
            case 11:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State12)));
                break;
            case 12:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State13)));
                break;
            case 13:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State14)));
                break;
            case 14:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State15)));
                break;
            case 15:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State16)));
                break;
            case 16:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State17)));
                break;
            case 17:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State18)));
                break;
            case 18:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State19)));
                break;
            case 19:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State20)));
                break;
            case 20:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State21)));
                break;
            case 21:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State22)));
                break;
            case 22:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State23)));
                break;
            case 23:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State24)));
                break;
            case 24:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State25)));
                break;
            case 25:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State26)));
                break;
            case 26:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State27)));
                break;
            case 27:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State28)));
                break;
            case 28:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State29)));
                break;
            case 29:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State30)));
                break;
            case 30:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State31)));
                break;
            case 31:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State32)));
                break;
            case 32:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State33)));
                break;
            case 33:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State34)));
                break;
            case 34:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State35)));
                break;
            case 35:
                this.cities.addAll(Arrays.asList(this.getResources().getStringArray(array.State36)));
                break;
        }
        this.citiesAdapter.notifyDataSetChanged();
        if (this.cities.indexOf(this.user_profile.getDistrict()) != -1) {
            this.districtSpinner.setSelection(this.cities.indexOf(this.user_profile.getDistrict()));
        }
        else{
            this.districtSpinner.setSelection(1);
        }
    }
}
