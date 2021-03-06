package com.cognichamp.CogniChamp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Created by Anshuman-HP on 11-08-2017.
 */

public class AccountFragment extends Fragment {
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    boolean isChanged=false;


    TextView changePassword;
    EditText email;
    EditText mobileNumber;
    Button registerPhone;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    public static AccountFragment newInstance() {
        return new AccountFragment();

    }
    public AccountFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.account_details,container,false);
        email=(EditText)v.findViewById(R.id.emailaccount);
        mobileNumber=(EditText)v.findViewById(R.id.mobilenumberaccount);
        registerPhone=(Button)v.findViewById(R.id.registerPhone);
        changePassword=(TextView)v.findViewById(R.id.changePassword);
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                if (phoneAuthCredential != null){
                    linkWithAccount(phoneAuthCredential);
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                final String id = s;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Verification");
                builder.setMessage("Enter the Code sent to " + mobileNumber.getText().toString());
                final EditText code = new EditText(getActivity());
                builder.setView(code);
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, code.getText().toString().trim());
                        linkWithAccount(credential);
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
        if(firebaseAuth.getCurrentUser()!=null) {
            if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
                if (FirebaseAuth.getInstance().getCurrentUser().getEmail() != null) {
                    email.setText(firebaseAuth.getCurrentUser().getEmail());
                }
                if (FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() != null) {
                    mobileNumber.setText(firebaseAuth.getCurrentUser().getPhoneNumber());
                    registerPhone.setVisibility(View.INVISIBLE);
                }
            }
        }

        mobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equals("") || s.toString().length()<10){
                    registerPhone.setVisibility(View.VISIBLE);
                }

            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
//                if(user!=null)
//                if(!user.getEmail().equals(s.toString().trim())) {
//                    isChanged=true;
//                    user.setEmail(s.toString());
//                }
            }
        });
        registerPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPhonePattern(mobileNumber.getText().toString())){
                    signInWithPhone(mobileNumber.getText().toString());
                }
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), com.cognichamp.CogniChamp.changePassword.class));
            }
        });
        return v;
    }
    public void saveChanges() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm");
        builder.setMessage("You have some unsaved chages!!");
        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
//                database.getReference("Users")
//                        .child(firebaseAuth.getCurrentUser().getUid())
//                        .setValue(user)
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                isChanged=false;
//                                dialog.cancel();
//                            }
//                        });
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
    public void linkWithAccount(AuthCredential credential){
        firebaseAuth.getCurrentUser().linkWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getActivity(),"Successfully Added Mobile Number Login",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"Failed to add Mobile Number",Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void signInWithPhone(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }
    public static boolean checkEmailPattern(String email) {
        String emailRegex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return  Pattern.matches(emailRegex, email);

    }

    public static boolean checkPhonePattern(String Phone) {
        String phoneRegex = "[789]{1}[1234567890]{9}";
        return Pattern.matches(phoneRegex, Phone);

    }
}
