package com.cognichamp.CogniChamp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cognichamp.CogniChamp.R.id;
import com.cognichamp.CogniChamp.R.layout;
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
    final String TAG = "Registartion";
    EditText emailText;
    EditText passwordText;
    Button register;
    String ismale="true";
    String email,password;
    TextView terms;
    boolean phone;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    StorageReference ref= FirebaseStorage.getInstance().getReference();

    String presentClass="0";

    AuthCredential authCredential;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layout.activity_registration);

        Intent j = this.getIntent();
        this.email = j.getStringExtra("Email");
        this.password = j.getStringExtra("Password");
        this.phone = j.getBooleanExtra("PhoneAuth", false);
        this.terms = (TextView) this.findViewById(id.terms);


        this.terms.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Builder builder = new Builder(Registration.this);
                builder.setTitle("Privacy and Terms");
                builder.setView(layout.terms);
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


        Log.e(this.TAG, this.email + "" + this.password);

        this.emailText = (EditText) this.findViewById(id.emailRegister);
        this.passwordText = (EditText) this.findViewById(id.passwordRegister);
        this.register = (Button) this.findViewById(id.registerButton);
        this.emailText.setText(this.email);
        this.passwordText.setText(this.password);


        this.register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(Registration.this.TAG, "Register CLicked");
                if (!Registration.this.passwordText.getText().toString().isEmpty()) {
                    Log.e(Registration.this.TAG, "Creating user");
                    Registration.this.createUser();
                    } else {
                    Registration.this.passwordText.setError("Enter a Minimum 8 characters Long Password");
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
        Log.e(this.TAG, "Pushing user Details");
        user_profile profile=new user_profile(""
                ,""
                , this.ismale
                , this.presentClass
        ,"https://firebasestorage.googleapis.com/v0/b/internship2-4d772.appspot.com/o/noimage.png?alt=media&token=9ad0aff6-93aa-4443-94b0-be7746d43c05"
        ,""
        ,""
        ,""
        ,""
        ,"1");
        this.firebaseDatabase.getReference(this.firebaseAuth.getCurrentUser().getUid())
                .child("UserProfile")
                .setValue(profile)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent i=new Intent(Registration.this,NavigationDrawer.class);
                i.putExtra("IsFirstTime",true);
                Registration.this.startActivity(i);
            }
        });
    }
    public void createUser()
    {
        if (this.checkEmailPattern(this.emailText.getText().toString())) {
            if (!this.phone) {
                this.firebaseAuth.createUserWithEmailAndPassword(this.emailText.getText().toString(), this.passwordText.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Log.e(Registration.this.TAG, "User Created");
                                Registration.this.firebaseAuth.getCurrentUser();
                                Registration.this.pushUserProfileDetails();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(Registration.this.TAG, e.toString());
                    }
                });
            }
            else{
                AuthCredential credential = EmailAuthProvider.getCredential(this.emailText.getText().toString(), this.passwordText.getText().toString());
                this.firebaseAuth.getCurrentUser().linkWithCredential(credential)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Registration.this.firebaseAuth.getCurrentUser()
                                        .sendEmailVerification();
                                Registration.this.pushUserProfileDetails();
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
            Toast.makeText(this.getApplicationContext(), "Enter a Valid Email Address", Toast.LENGTH_SHORT).show();
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
