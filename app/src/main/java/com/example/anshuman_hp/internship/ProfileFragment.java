package com.example.anshuman_hp.internship;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Anshuman-HP on 12-08-2017.
 */

public class ProfileFragment extends Fragment {
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    ImageView profileImage;
    EditText name;
    EditText birthDate;
    RadioGroup gender;
    RadioButton male;
    RadioButton female;

    boolean isChanged=false;


    User user;
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }
    public ProfileFragment() {
        super();
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
         }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.profile_details,container,false);
        profileImage=(ImageView)v.findViewById(R.id.profileImage);
        name=(EditText)v.findViewById(R.id.nameProfile);
        birthDate=(EditText)v.findViewById(R.id.birthdateEdit);
        gender=(RadioGroup)v.findViewById(R.id.genderRadioGroup);
        male=(RadioButton)v.findViewById(R.id.male);
        female=(RadioButton)v.findViewById(R.id.female);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!user.getFullName().equals(s.toString().trim()))
                {
                    isChanged=true;
                    user.setFullName(s.toString());
                }
            }
        });
        birthDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!user.getBirthDate().equals(s.toString().trim()))
                {
                    isChanged=true;
                    user.setBirthDate(s.toString());
                }
            }
        });
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(user.getIsMale().equals("true") && group.getCheckedRadioButtonId()==R.id.male)
                {

                }
                else
                {
                    female.setChecked(true);
                    male.setChecked(false);
                }
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
                database.getReference("Users")
                        .child(firebaseAuth.getCurrentUser().getUid())
                        .setValue(user)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                isChanged=false;
                                dialog.cancel();
                            }
                        });


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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        database.getReference("Users")
                .child(firebaseAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        user=dataSnapshot.getValue(User.class);
                        if(getActivity()==null)
                        {
                            return;
                        }
                        Glide.with(getActivity())
                                .load(user.getPhotoUrl())
                                .into(profileImage);
                        name.setText(user.getFullName());
                        birthDate.setText(user.getBirthDate());
                        if(user.getIsMale().equals("true"))
                        {
                            male.setChecked(true);
                            female.setChecked(false);

                        }
                        else
                        {
                            male.setChecked(false);
                            female.setChecked(true);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
}
