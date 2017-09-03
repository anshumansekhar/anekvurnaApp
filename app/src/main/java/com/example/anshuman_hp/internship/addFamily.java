package com.example.anshuman_hp.internship;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class addFamily extends AppCompatActivity {
    ArrayAdapter relationsSpinnerAdapter;
    Spinner relations;
    EditText name;
    Button add;
    FamilyMember member;
    EditText phoneNumber;
    EditText email;
    ImageView famMemImage;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    String relation;
    StorageReference ref;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_family);
        name=(EditText)findViewById(R.id.familyMemberName);
        phoneNumber=(EditText)findViewById(R.id.PhoneNumberFamily);
        email=(EditText)findViewById(R.id.EmailIdFamily);
        famMemImage=(ImageView)findViewById(R.id.photoFamily);
        relations=(Spinner)findViewById(R.id.familyMemberRelation);
        add=(Button)findViewById(R.id.addFamilyMem);

        actionBar=getSupportActionBar();
        actionBar.setTitle("Add Family Member");

        ref=FirebaseStorage.getInstance()
                .getReference()
                .child(auth.getCurrentUser().getUid())
                .child("FamilyMemPhotos");
        relationsSpinnerAdapter= ArrayAdapter.createFromResource(getApplicationContext(),R.array.Relation,android.R.layout.simple_spinner_dropdown_item);
        relationsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        relations.setAdapter(relationsSpinnerAdapter);
        relations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                relation=""+position;

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        famMemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(addFamily.this,fullImageActivity.class));

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.checkEmailPattern(email.getText().toString())) {
                    if(MainActivity.checkPhonePattern(phoneNumber.getText().toString())) {
                        uploadImage(relation);
                        member = new FamilyMember(name.getText().toString()
                                , "https://firebasestorage.googleapis.com/v0/b/internship2-4d772.appspot.com/o/noimage.png?alt=media&token=9ad0aff6-93aa-4443-94b0-be7746d43c05"
                                , relation
                                , email.getText().toString()
                                , phoneNumber.getText().toString());
                        database.getReference(auth.getCurrentUser().getUid())
                                .child("Family")
                                .child(member.getMemberRelation())
                                .setValue(member);
                    }
                    else
                        phoneNumber.setError("Enter a Valid 10 digit mobile number");
                }
                else
                    email.setError("Enter a Valid email Address");
            }
        });
    }
    public void uploadImage(final String relation)
    {
        if(fullImageActivity.selectedImageUri!=null) {
            StorageReference photoRef=ref.child(relation);
            UploadTask task = photoRef.putFile(fullImageActivity.selectedImageUri);
            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String url=taskSnapshot.getDownloadUrl().toString();
                    database.getReference(auth.getCurrentUser().getUid())
                            .child("Famliy")
                            .child(relation)
                            .child("memberPhotoUrl")
                            .setValue(url);
                }
            });
        }
    }
}
