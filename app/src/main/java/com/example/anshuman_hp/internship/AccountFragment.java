package com.example.anshuman_hp.internship;

import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Anshuman-HP on 11-08-2017.
 */

public class AccountFragment extends Fragment {
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    boolean isChanged=false;
    public boolean isChanged() {
        return isChanged;
    }
    public void setChanged(boolean changed) {
        isChanged = changed;
    }


    EditText email;
    EditText password;
    EditText mobileNumber;
    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        return fragment;
    }
    public AccountFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.account_details,container,false);
        email=(EditText)v.findViewById(R.id.emailaccount);
        password=(EditText)v.findViewById(R.id.passwordaccount);
        mobileNumber=(EditText)v.findViewById(R.id.mobilenumberaccount);
        if(!firebaseAuth.getCurrentUser().getEmail().isEmpty()) {
            email.setText(firebaseAuth.getCurrentUser().getEmail());
        }
        if(!firebaseAuth.getCurrentUser().getPhoneNumber().isEmpty())
        {
            mobileNumber.setText(firebaseAuth.getCurrentUser().getPhoneNumber());
        }
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
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
//                if(user!=null)
//                if(!user.getPassword().equals(s.toString().trim())) {
//                    isChanged = true;
//                    if(MainActivity.checkEmailPattern(s.toString()))
//                        user.setPassword(s.toString());
//                    else
//                        password.setError("Enter a Valid Email Address");
//                }
            }
        });
        mobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
//                if(user!=null)
//                if(!user.getMobileNumber().equals(s.toString().trim())){
//                    isChanged=true;
//                    if(MainActivity.checkPhonePattern(s.toString()))
//                        user.setMobileNumber(s.toString());
//                    else
//                        mobileNumber.setError("Enter a Valid 10 digit Mobile Number");
//                }
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
}
