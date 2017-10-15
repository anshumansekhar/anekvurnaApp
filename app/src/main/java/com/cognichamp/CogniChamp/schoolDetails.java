package com.cognichamp.CogniChamp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
        View v = inflater.inflate(R.layout.school_card, container, false);
        schoolName = (TextView) v.findViewById(R.id.schoolName);
        schoolCity = (TextView) v.findViewById(R.id.schoolCity);
        schoolAddress = (TextView) v.findViewById(R.id.schoolAddress);
        schoolState = (TextView) v.findViewById(R.id.schoolState);
        schoolPinCode = (TextView) v.findViewById(R.id.schoolPinCode);
        schoolLogo = (ImageView) v.findViewById(R.id.schoolLogo);
        editButton = (ImageButton) v.findViewById(R.id.schoolDetailsEdit);
        update();
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EditSchoolDetails.class);
                i.putExtra("ClassName", EducationFragment.className);
                startActivity(i);
            }
        });
        return v;
    }
    public  void update(){
        if (isAdded()) {
            DatabaseReference schoolDetail = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                    .child("SchoolDetails")
                    .child(EducationFragment.className);
            schoolDetail.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        schoolObject = dataSnapshot.getValue(School.class);
                        schoolName.setText(schoolObject.getSchoolName());
                        schoolCity.setText(schoolObject.getSchoolCity());
                        schoolPinCode.setText("Pin:-" + schoolObject.getSchoolPin());
                        schoolAddress.setText("At:-" + schoolObject.getSchoolAddress());
                        schoolState.setText(schoolObject.getSchoolState());

                        Glide.with(getActivity())
                                .load(schoolObject.getSchoolLogo())
                                .into(schoolLogo);

                    } else {
                        schoolLogo.setVisibility(View.GONE);
                        schoolName.setText(R.string.addSchool);
                        schoolName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(getActivity(), EditSchoolDetails.class);
                                i.putExtra("ClassName", EducationFragment.className);
                                startActivity(i);
                            }
                        });
                        schoolCity.setText("");
                        schoolPinCode.setText("");
                        schoolAddress.setText("");
                        schoolState.setText("");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


    }
}
