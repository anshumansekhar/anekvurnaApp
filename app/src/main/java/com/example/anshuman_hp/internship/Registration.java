package com.example.anshuman_hp.internship;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Anshuman-HP on 20-08-2017.
 */

public class Registration extends AppCompatActivity {

    EditText emailText;
    EditText passwordText;
    EditText name;
    EditText birthDate;
    RadioButton male,female;
    ImageView ProfileImage;
    Button register;
    String ismale;

    final String TAG="Registartion";

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


        emailText=(EditText)findViewById(R.id.emailRegister);
        passwordText=(EditText)findViewById(R.id.passwordRegister);
        name=(EditText)findViewById(R.id.nameRegister);
        birthDate=(EditText)findViewById(R.id.BirthdateRegister);
        male=(RadioButton)findViewById(R.id.maleRegister);
        female=(RadioButton)findViewById(R.id.femaleRegister);
        ProfileImage=(ImageView)findViewById(R.id.profileImage);
        register=(Button)findViewById(R.id.registerButton);
        if(male.isChecked())
        {
            ismale="true";
        }
        else
            ismale="false";

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                else
                    createUser();
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
                        if(task.isSuccessful())
                        {
                            Log.d("tag", "linkWithCredential:success");
                            pushUserDetails(makeNewUser(mobile));
                        }
                        else
                        {
                            Log.w("tag", "linkWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void pushUserDetails(User d)
    {
        firebaseDatabase.getReference("Users")
                .child(firebaseAuth.getCurrentUser().getUid())
                .setValue(d)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pushClassDetails();
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
                            pushUserDetails(makeNewUser(""));
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
    public User makeNewUser(String mob)
    {
       User d=new User(emailText.getText().toString()
                ,name.getText().toString()
                ,firebaseAuth.getCurrentUser().getUid()
                ,""
                ,birthDate.getText().toString()
                ,ismale
                ,passwordText.getText().toString()
                ,mob);
        return  d;

    }
    public void pushClassDetails()
    {
        ArrayList<ClassDetails> list=new ArrayList<>();
        for(int i=0;i<12;i++)
        {
            list.add(new ClassDetails());
        }
        firebaseDatabase.getReference("ClassDetails")
                .child(firebaseAuth.getCurrentUser().getUid()).setValue(list);

    }
}
