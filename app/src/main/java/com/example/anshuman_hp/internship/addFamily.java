package com.example.anshuman_hp.internship;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
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

    NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    String imageURI;

    public static final int IMAGE_REQUEST=102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_family);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                startActivityForResult(new Intent(addFamily.this,fullImageActivity.class),IMAGE_REQUEST);

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
                                .setValue(member)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"Added Family Member",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Failed to Add",Toast.LENGTH_SHORT).show();
                            }
                        });
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
        if(imageURI!=null) {
            StorageReference photoRef=ref.child(relation);
            showProgressNotification();
            UploadTask task = photoRef.putFile(Uri.parse(imageURI));
            task.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int progress=(int)(taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount())*100;
                    builder.setProgress(100,progress,false);
                    notificationManager.notify(1,builder.build());

                }
            });
            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    builder.setContentText("Upload complete")
                            .setProgress(0,0,false);
                    notificationManager.notify(1, builder.build());
                    String url=taskSnapshot.getDownloadUrl().toString();
                    database.getReference(auth.getCurrentUser().getUid())
                            .child("Famliy")
                            .child(relation)
                            .child("memberPhotoUrl")
                            .setValue(url);
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(),"No image",Toast.LENGTH_SHORT).show();
        }
    }

    public void showProgressNotification(){
        notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        builder=new NotificationCompat.Builder(this);
        builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Picture Upload")
                .setContentText("Upload in progress")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==IMAGE_REQUEST){
            if(resultCode==RESULT_OK){
                imageURI=data.getStringExtra("ImageURI");
                Log.e("ashsc",imageURI.toString());
                Glide.with(getApplicationContext())
                        .load(imageURI)
                        .apply(new RequestOptions().override(100,120))
                        .into(famMemImage);
            }
        }
    }
}
