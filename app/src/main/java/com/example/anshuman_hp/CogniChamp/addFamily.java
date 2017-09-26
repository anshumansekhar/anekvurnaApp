package com.example.anshuman_hp.CogniChamp;

import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.regex.Pattern;

public class addFamily extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PIC_CROP = 189;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA =456 ;
    private static final int RESULT_LOAD_IMG = 654;


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
        relationsSpinnerAdapter= ArrayAdapter.createFromResource(addFamily.this,R.array.Relation,android.R.layout.simple_spinner_dropdown_item);
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
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkEmailPattern(email.getText().toString())) {
                    if(checkPhonePattern(phoneNumber.getText().toString())) {
                        uploadImage(relation);
                        member = new FamilyMember(name.getText().toString()
                                , "https://firebasestorage.googleapis.com/v0/b/internship2-4d772.appspot.com/o/noimage.png?alt=media&token=9ad0aff6-93aa-4443-94b0-be7746d43c05"
                                , relation
                                , email.getText().toString()
                                , phoneNumber.getText().toString());
                        database.getReference(auth.getCurrentUser().getUid())
                                .child("Family")
                                .child(member.getMemberName())
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
                            .child("Family")
                            .child(member.getMemberName())
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
        if(requestCode==REQUEST_IMAGE_CAPTURE){
            if(resultCode==RESULT_OK){
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                famMemImage.setImageBitmap(imageBitmap);
            }
        }
        else if(requestCode == PIC_CROP){
            Bundle extras = data.getExtras();
            Bitmap thePic = extras.getParcelable("data");
            famMemImage.setImageBitmap(thePic);
            imageURI=getImageUri(addFamily.this,thePic).toString();
        }
        else if(requestCode==RESULT_LOAD_IMG){
            if(resultCode==RESULT_OK){
                Uri selectedImage = data.getData();
                imageURI=selectedImage.toString();
                famMemImage.setImageURI(selectedImage);
            }

        }
    }
    public void performCrop(Uri uri){
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(uri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);

        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Log.e("sg",errorMessage);
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Log.e("sdg",path);
        Log.e("GH",Uri.parse(path).toString());
        return Uri.parse(path);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public static boolean checkEmailPattern(String email) {
        String emailRegex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        boolean value = Pattern.matches(emailRegex, email);
        return value;
    }

    public static boolean checkPhonePattern(String Phone) {
        String phoneRegex = "[789]{1}[1234567890]{9}";
        boolean value1 = Pattern.matches(phoneRegex, Phone);
        return value1;
    }
}
