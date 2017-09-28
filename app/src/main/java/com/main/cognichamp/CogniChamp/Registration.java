package com.main.cognichamp.CogniChamp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.regex.Pattern;

/**
 * Created by Anshuman-HP on 20-08-2017.
 */

public class Registration extends AppCompatActivity  {
    private static final int RESULT_LOAD_IMG = 345;
    EditText emailText;
    EditText passwordText;
    Button register;
    String ismale="true";
    String email,password;
    TextView terms;

    boolean phone;

    final String TAG="Registartion";

    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    StorageReference ref= FirebaseStorage.getInstance().getReference();

    String presentClass="0";

    AuthCredential authCredential;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Intent j=getIntent();
        email=j.getStringExtra("Email");
        password=j.getStringExtra("Password");
        phone=j.getBooleanExtra("PhoneAuth",false);
        terms=(TextView)findViewById(R.id.terms);


        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(Registration.this);
                builder.setTitle("Privacy and Terms");
                builder.setView(R.layout.terms);
                builder.setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setPositiveButton("I Agree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });


        Log.e(TAG,email +""+ password);

        emailText=(EditText)findViewById(R.id.emailRegister);
        passwordText=(EditText)findViewById(R.id.passwordRegister);
        register=(Button)findViewById(R.id.registerButton);
        emailText.setText(email);
        passwordText.setText(password);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"Register CLicked");
                    if (!passwordText.getText().toString().isEmpty()) {
                        Log.e(TAG, "Creating user");
                        createUser();
                    } else {
                        passwordText.setError("Enter a Minimum 8 characters Long Password");
                    }
            }
        });
    }

    public boolean checkEmailPattern(String email)
    {
        String emailRegex="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.matches(emailRegex,email);
    }
    public void pushUserProfileDetails()
    {
        Log.e(TAG,"Pushing user Details");
        user_profile profile=new user_profile(""
                ,""
        ,ismale
        ,presentClass
        ,"https://firebasestorage.googleapis.com/v0/b/internship2-4d772.appspot.com/o/noimage.png?alt=media&token=9ad0aff6-93aa-4443-94b0-be7746d43c05"
        ,""
        ,""
        ,""
        ,""
        ,"1");
        firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("UserProfile")
                .setValue(profile)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent i=new Intent(Registration.this,NavigationDrawer.class);
                i.putExtra("IsFirstTime",true);
                startActivity(i);
            }
        });
    }
    public void createUser()
    {
        if(checkEmailPattern(emailText.getText().toString())) {
            if(!phone) {
                firebaseAuth.createUserWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Log.e(TAG, "User Created");
                                firebaseAuth.getCurrentUser();
//                                sendEmailTask task=new sendEmailTask();
//                                //TODO add message
//                                task.execute("Welcome to CogniChamp"
//                                        ,""
//                                        ,email
//                                        ,"contact@cognichamp.com,"+email,messageTypetext);
                                pushUserProfileDetails();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
            }
            else{
                AuthCredential credential=EmailAuthProvider.getCredential(emailText.getText().toString(),passwordText.getText().toString());
                firebaseAuth.getCurrentUser().linkWithCredential(credential)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                firebaseAuth.getCurrentUser()
                                        .sendEmailVerification();
                                pushUserProfileDetails();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ASg",e.toString());
                    }
                });
            }
        }
        else
            Toast.makeText(getApplicationContext(),"Enter a Valid Email Address",Toast.LENGTH_SHORT).show();
    }
    public class sendEmailTask extends AsyncTask<String,Void,Void> {
        String messageType;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Registration.this,"Sending",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Toast.makeText(getActivity(),"Thank you.Your "+messageType+" has been submitted. Our team will reach out to you shortly.",Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
//                MailSender sender = new MailSender(username,pass);
//                sender.sendMail(params[0],params[1],params[2],params[3]);
//                messageType=params[4];
            } catch (Exception e) {
                Log.e("SendMail", e.toString());
            }

            return null;
        }
    }
}
