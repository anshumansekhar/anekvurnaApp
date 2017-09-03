package com.example.anshuman_hp.internship;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by Anshuman-HP on 20-08-2017.
 */

public class Registration extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText emailText;
    EditText passwordText;
    EditText name;
    EditText birthDate;
    RadioButton male,female;
    ImageView ProfileImage;
    Button register;
    String ismale;
    String email,password;

    final String TAG="Registartion";

    String myFormat = "dd/mm/yyyy"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog datePickerDialog;

    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();

    String mobile;
    boolean phoneauth;


    AuthCredential authCredential;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Intent j=getIntent();
        mobile=j.getStringExtra("Mobile");
        phoneauth=j.getBooleanExtra("PhoneAuth",false);
        email=j.getStringExtra("Email");
        password=j.getStringExtra("Password");

        Log.e(TAG,email +""+ password);


        datePickerDialog=new DatePickerDialog(Registration.this,this,2000,1,1);



        emailText=(EditText)findViewById(R.id.emailRegister);
        passwordText=(EditText)findViewById(R.id.passwordRegister);
        name=(EditText)findViewById(R.id.nameRegister);
        birthDate=(EditText)findViewById(R.id.BirthdateRegister);
        male=(RadioButton)findViewById(R.id.maleRegister);
        female=(RadioButton)findViewById(R.id.femaleRegister);
        ProfileImage=(ImageView)findViewById(R.id.profileImage);
        register=(Button)findViewById(R.id.registerButton);
        emailText.setText(email);
        passwordText.setText(password);
        if(male.isChecked()) {
            ismale="true";
        }
        else
            ismale="false";

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"Register CLicked");
                if(phoneauth) {
                    if (checkEmailPattern(emailText.getText().toString())) {
                        authCredential = EmailAuthProvider.getCredential(emailText.getText().toString().trim(), passwordText.getText().toString().trim());
                        Log.e(TAG,authCredential.toString());
                        linkPhoneWithEmail(authCredential);
                    }
                    else{
                        emailText.setError("Enter a Valid email Address");
                    }
                }
                else {
                    Log.e(TAG,"Creating user");
                    createUser();
                }
            }
        });

    }

    public boolean checkEmailPattern(String email)
    {
        String emailRegex="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        boolean value= Pattern.matches(emailRegex,email);
        return value ;
    }
    public void linkPhoneWithEmail(AuthCredential credential)
    {
        firebaseAuth.getCurrentUser()
                .linkWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d("tag", "linkWithCredential:success");
                            pushUserProfileDetails();
                        }
                        else {
                            Log.w("tag", "linkWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void pushUserProfileDetails()
    {
        Log.e(TAG,"Pushing user Details");
        user_profile profile=new user_profile(name.getText().toString(),
                birthDate.getText().toString()
        ,ismale
        ,"1"
        ,"https://firebasestorage.googleapis.com/v0/b/internship2-4d772.appspot.com/o/noimage.png?alt=media&token=9ad0aff6-93aa-4443-94b0-be7746d43c05"
        ,""
        ,""
        ,"");
        firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("UserProfile")
                .setValue(profile);
        Log.e(TAG,"Pushing class deatils");
        pushClassDetails();
        UserProfileChangeRequest.Builder profileChangeRequest=new UserProfileChangeRequest.Builder();
        profileChangeRequest.setDisplayName(name.getText().toString());
        //TODO photo uri
        Log.e(TAG,"Changong user data");
        firebaseAuth.getCurrentUser().updateProfile(profileChangeRequest.build())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e(TAG,"Updating succesful");
                        startActivity(new Intent(Registration.this,NavigationDrawer.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,e.toString());
            }
        });
    }
    public void createUser()
    {
        if(checkEmailPattern(emailText.getText().toString())) {
            firebaseAuth.createUserWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Log.e(TAG,"User Created");
                            pushUserProfileDetails();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, e.toString());
                }
            });
        }
        else
            Toast.makeText(getApplicationContext(),"Enter a Valid Email Address",Toast.LENGTH_SHORT);
    }
    public void pushClassDetails()
    {
        ArrayList<ClassDetails> list=new ArrayList<>();
        for(int i=0;i<12;i++) {
            if (i != 10 && i != 11) {
                list.add(new ClassDetails());
                firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                        .child("ClassDetails").setValue(list);
            } else if (i == 11) {
                HashMap<String, ClassDetails> map = new HashMap<>();
                map.put("Arts", new ClassDetails());
                map.put("Commerce", new ClassDetails());
                map.put("Science", new ClassDetails());
                firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                        .child("ClassDetails")
                        .child("11")
                        .setValue(map);
            } else if (1 == 10) {
                HashMap<String, ClassDetails> map = new HashMap<>();
                map.put("Arts", new ClassDetails());
                map.put("Commerce", new ClassDetails());
                map.put("Science", new ClassDetails());
                firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                        .child("ClassDetails")
                        .child("10")
                        .setValue(map);
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        birthDate.setText(sdf.format(myCalendar.getTime()));
    }
}
