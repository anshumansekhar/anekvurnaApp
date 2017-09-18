package com.example.anshuman_hp.internship;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AppRatingActivity extends Fragment {
    RatingBar appRatingBar;
    EditText feedBack;
    Button submit;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();

    double ratings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activity_app_rating,container,false);
        appRatingBar=(RatingBar)v.findViewById(R.id.ratingApp);
        feedBack=(EditText)v.findViewById(R.id.feedback);
        submit=(Button)v.findViewById(R.id.submitRating);

        database.getReference("AppRatings")
                .child("ratings")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ratings=Double.parseDouble(dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratings=(appRatingBar.getRating()+ratings)/2;
                database.getReference("AppRatings")
                        .child("ratings")
                        .setValue(ratings);
                if(!feedBack.getText().toString().isEmpty()) {
                    database.getReference("AppRatings")
                            .child("feedback")
                            .push()
                            .setValue(feedBack.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(),"We Recieved your Valuable Feedback",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        return v;
    }
}
