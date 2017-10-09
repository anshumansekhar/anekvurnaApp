package com.cognichamp.CogniChamp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {
    EditText emailToReset;
    Button resetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password2);
        emailToReset=(EditText)findViewById(R.id.emailToReset);
        resetPassword=(Button)findViewById(R.id.resetPassword);

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkEmailPattern(emailToReset.getText().toString())){
                    FirebaseAuth.getInstance().sendPasswordResetEmail(emailToReset.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),"Password Reset Email Sent",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ForgotPassword.this,LoginEmail.class));
                                }
                            });
                }
                else {
                    Toast.makeText(getApplicationContext(),"Enter a Valid Email Address",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public static boolean checkEmailPattern(String email) {
        String emailRegex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.matches(emailRegex, email);
    }
}
