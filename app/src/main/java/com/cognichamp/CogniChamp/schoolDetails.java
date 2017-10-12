package com.cognichamp.CogniChamp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cognichamp.CogniChamp.R.id;
import com.cognichamp.CogniChamp.R.string;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Anshuman-HP on 03-09-2017.
 */

public class schoolDetails extends Fragment {
    TextView schoolName,schoolAddress,schoolCity,schoolPinCode,schoolState;
    ImageButton editButton;
    ImageView schoolLogo;

    LinearLayout layout;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();

    School schoolObject;

    public schoolDetails() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(layout.school_card, container, false);
        this.schoolName = (TextView) v.findViewById(id.schoolName);
        this.schoolCity = (TextView) v.findViewById(id.schoolCity);
        this.schoolAddress = (TextView) v.findViewById(id.schoolAddress);
        this.schoolState = (TextView) v.findViewById(id.schoolState);
        this.schoolPinCode = (TextView) v.findViewById(id.schoolPinCode);
        this.schoolLogo = (ImageView) v.findViewById(id.schoolLogo);
        this.editButton = (ImageButton) v.findViewById(id.schoolDetailsEdit);
        this.update();
        this.editButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(schoolDetails.this.getActivity(), EditSchoolDetails.class);
                i.putExtra("ClassName", EducationFragment.className);
                schoolDetails.this.startActivity(i);
            }
        });
        return v;
    }
    public  void update(){
        if (this.isAdded()) {
            DatabaseReference schoolDetail = this.firebaseDatabase.getReference(this.firebaseAuth.getCurrentUser().getUid())
                    .child("SchoolDetails")
                    .child(EducationFragment.className);
            schoolDetail.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        schoolDetails.this.schoolObject = dataSnapshot.getValue(School.class);
                        schoolDetails.this.schoolName.setText(schoolDetails.this.schoolObject.getSchoolName());
                        schoolDetails.this.schoolCity.setText(schoolDetails.this.schoolObject.getSchoolCity());
                        schoolDetails.this.schoolPinCode.setText("Pin:-" + schoolDetails.this.schoolObject.getSchoolPin());
                        schoolDetails.this.schoolAddress.setText("At:-" + schoolDetails.this.schoolObject.getSchoolAddress());
                        schoolDetails.this.schoolState.setText(schoolDetails.this.schoolObject.getSchoolState());

                        Glide.with(schoolDetails.this.getActivity())
                                .load(schoolDetails.this.schoolObject.getSchoolLogo())
                                .into(schoolDetails.this.schoolLogo);

                    } else {
                        schoolDetails.this.schoolLogo.setVisibility(View.GONE);
                        schoolDetails.this.schoolName.setText(string.addSchool);
                        schoolDetails.this.schoolName.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(schoolDetails.this.getActivity(), EditSchoolDetails.class);
                                i.putExtra("ClassName", EducationFragment.className);
                                schoolDetails.this.startActivity(i);
                            }
                        });
                        schoolDetails.this.schoolCity.setText("");
                        schoolDetails.this.schoolPinCode.setText("");
                        schoolDetails.this.schoolAddress.setText("");
                        schoolDetails.this.schoolState.setText("");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


    }
}
