package com.example.anshuman_hp.internship;

import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Created by Anshuman-HP on 20-09-2017.
 */

public class LoginEmail extends AppCompatActivity {
    private static final String TAG ="LoginEmail" ;
    Button loginButton;
    EditText emailLogin;
    EditText passwordLogin;
    EditText mobileLogin;
    TextView forgotPassword;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    String phoneText;
    String emailText;
    String passwordText;

    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_login);

        loginButton=(Button)findViewById(R.id.loginButton);
        emailLogin=(EditText)findViewById(R.id.emailLogin);
        passwordLogin=(EditText)findViewById(R.id.passwordLogin);
        mobileLogin=(EditText)findViewById(R.id.mobileLogin);
        forgotPassword=(TextView)findViewById(R.id.ForgotPasswordLogin);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.e(TAG, "onVerificationCompleted:" + phoneAuthCredential);
                if (phoneAuthCredential != null)
                    signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.e(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Log.e(TAG, "onCodeSent:" + s);
                final String id = s;
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginEmail.this);
                builder.setTitle("Verification");
                builder.setMessage("Enter the Code sent to " + phoneText);
                final EditText code = new EditText(getApplicationContext());
                builder.setView(code);
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, code.getText().toString().trim());
                        signInWithPhoneAuthCredential(credential);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();
            }
        };
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginEmail.this,ForgotPassword.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailLogin.getText().toString().isEmpty() && mobileLogin.getText().toString().isEmpty()){
                    emailLogin.setError("Email Cannot be Empty");
                }
                else if(emailLogin.getText().toString().isEmpty() && !mobileLogin.getText().toString().isEmpty()){
                    if(checkPhonePattern(mobileLogin.getText().toString())){
                        phoneText=mobileLogin.getText().toString().trim();
                        signInWithPhone(phoneText);
                    }
                    else {
                        mobileLogin.setError("Enter a Valid 10 digit Mobile Number");
                    }
                }
                else if(!emailLogin.getText().toString().isEmpty() && mobileLogin.getText().toString().isEmpty()){
                    if(checkEmailPattern(emailLogin.getText().toString())) {
                        if (passwordLogin.getText().toString().isEmpty()) {
                            passwordLogin.setError("Password Cannot be Empty");
                        } else{
                            firebaseAuth.signInWithEmailAndPassword(emailText, passwordText)
                                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            Log.e(TAG, "Sign in Successful starting next activity");
                                            startActivity(new Intent(LoginEmail.this, NavigationDrawer.class));
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, e.toString());
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginEmail.this);
                                    builder.setTitle("Confirmation");
                                    builder.setMessage("The entered Emaild has not Yet been Registered.Do you want to Register now?");
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent f = new Intent(LoginEmail.this, Registration.class);
                                            f.putExtra("PhoneAuth", false);
                                            f.putExtra("Email", emailText);
                                            f.putExtra("Password", passwordText);
                                            Log.e(TAG, "Staring Registration");
                                            startActivity(f);
                                        }
                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            });
                        }
                    }
                    else {
                        emailLogin.setError("Enter a Valid Email Address");
                    }
                }
            }
        });

    }
    public static boolean checkEmailPattern(String email) {
        String emailRegex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        boolean value = Pattern.matches(emailRegex, email);
        return value;
    }

    public static boolean checkPhonePattern(String Phone) {
        String phoneRegex = "[789]{1}[1234567890]{9}";
        boolean value1 = Pattern.matches(phoneRegex, Phone);
        return value1;
    }
    public void signInWithPhone(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        firebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (firebaseAuth.getCurrentUser().getEmail()==null) {
                                Intent i = new Intent(LoginEmail.this, Registration.class);
                                i.putExtra("Mobile", phoneText);
                                i.putExtra("PhoneAuth", true);
                                startActivity(i);
                            }
                            else {
                                startActivity(new Intent(LoginEmail.this,NavigationDrawer.class));
                            }
                        } else
                            Log.e(TAG, "TaskFailed");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.toString());
            }
        });
    }
}
