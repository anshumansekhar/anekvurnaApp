package com.example.anshuman_hp.CogniChamp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;


public class changePassword extends AppCompatActivity {

    Button changeButton;
    EditText newPassword;
    EditText confirmPassword;
    EditText currentPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setTitle("Change Password");

        changeButton=(Button)findViewById(R.id.changeButton);
        newPassword=(EditText)findViewById(R.id.newPassword);
        confirmPassword=(EditText)findViewById(R.id.confirmPassword);
        currentPassword=(EditText)findViewById(R.id.currentPassword);

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!currentPassword.getText().toString().isEmpty()){
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(firebaseUser.getEmail(), currentPassword.getText().toString());
                    firebaseUser.reauthenticate(credential)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    if(newPassword.getText().toString().equals(confirmPassword.getText().toString())){
                                        for (UserInfo user: FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
                                            if (user.getProviderId().equals("facebook.com")) {
                                                Toast.makeText(changePassword.this,"Facebook Password cannot be changed here",Toast.LENGTH_SHORT).show();
                                                Log.e("xx_xx_provider_info", "User is signed in with Facebook");
                                            } else if (user.getProviderId().equals("google.com")) {
                                                Toast.makeText(changePassword.this,"Google Password cannot be changed here",Toast.LENGTH_SHORT).show();
                                                Log.e("xx_xx_provider_info", "User is signed in with Google");
                                            }
                                            else{
                                                firebaseUser.updatePassword(confirmPassword.getText().toString())
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(changePassword.this,"Password Updated.Please Sign in again",Toast.LENGTH_SHORT).show();
                                                                FirebaseAuth.getInstance().signOut();
                                                                startActivity(new Intent(changePassword.this,SignUpChooseActivity.class));
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(changePassword.this,"Failed to update Password Try Again",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    }
                                    else {
                                        confirmPassword.setError("Password Does not Match");
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(changePassword.this,"Not able to authenticate you Please sign in Again",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    currentPassword.setError("This Field cannot be Left Blank");
                }
            }
        });
    }
}
