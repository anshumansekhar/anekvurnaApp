package com.cognichamp.CogniChamp;

import android.Manifest.permission;
import android.R.id;
import android.R.layout;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat.Builder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.cognichamp.CogniChamp.R.array;
import com.cognichamp.CogniChamp.R.drawable;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.UploadTask.TaskSnapshot;

import java.io.ByteArrayOutputStream;
import java.util.regex.Pattern;

public class addFamily extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 456;
    public static final int IMAGE_REQUEST = 102;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PIC_CROP = 189;
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
    Builder builder;
    String imageURI;

    public static boolean checkEmailPattern(String email) {
        String emailRegex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.matches(emailRegex, email);

    }

    public static boolean checkPhonePattern(String Phone) {
        String phoneRegex = "[789]{1}[1234567890]{9}";
        return Pattern.matches(phoneRegex, Phone);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_add_family);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.name = (EditText) this.findViewById(R.id.familyMemberName);
        this.phoneNumber = (EditText) this.findViewById(R.id.PhoneNumberFamily);
        this.email = (EditText) this.findViewById(R.id.EmailIdFamily);
        this.famMemImage = (ImageView) this.findViewById(R.id.photoFamily);
        this.relations = (Spinner) this.findViewById(R.id.familyMemberRelation);
        this.add = (Button) this.findViewById(R.id.addFamilyMem);

        this.actionBar = this.getSupportActionBar();
        this.actionBar.setTitle("Add Family Member");

        this.ref = FirebaseStorage.getInstance()
                .getReference()
                .child(this.auth.getCurrentUser().getUid())
                .child("FamilyMemPhotos");
        this.relationsSpinnerAdapter = ArrayAdapter.createFromResource(this, array.Relation, layout.simple_spinner_dropdown_item);
        this.relationsSpinnerAdapter.setDropDownViewResource(layout.simple_spinner_dropdown_item);
        this.relations.setAdapter(this.relationsSpinnerAdapter);
        this.relations.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addFamily.this.relation = "" + position;

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        this.famMemImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(addFamily.this,
                        permission.READ_EXTERNAL_STORAGE);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
                    addFamily.this.startActivityForResult(galleryIntent, addFamily.RESULT_LOAD_IMG);
                } else {
                    ActivityCompat.requestPermissions(addFamily.this,
                            new String[]{permission.READ_EXTERNAL_STORAGE},
                            addFamily.MY_PERMISSIONS_REQUEST_CAMERA);
                }

            }
        });
        this.add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addFamily.checkEmailPattern(addFamily.this.email.getText().toString())) {
                    if (addFamily.checkPhonePattern(addFamily.this.phoneNumber.getText().toString())) {
                        addFamily.this.uploadImage(addFamily.this.relation);
                        addFamily.this.member = new FamilyMember(addFamily.this.name.getText().toString()
                                , "https://firebasestorage.googleapis.com/v0/b/internship2-4d772.appspot.com/o/noimage.png?alt=media&token=9ad0aff6-93aa-4443-94b0-be7746d43c05"
                                , addFamily.this.relation
                                , addFamily.this.email.getText().toString()
                                , addFamily.this.phoneNumber.getText().toString());
                        addFamily.this.database.getReference(addFamily.this.auth.getCurrentUser().getUid())
                                .child("Family")
                                .child(addFamily.this.member.getMemberName())
                                .setValue(addFamily.this.member)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(addFamily.this.getApplicationContext(), "Added Family Member", Toast.LENGTH_SHORT).show();
                                Intent fr=new Intent(addFamily.this,NavigationDrawer.class);
                                fr.putExtra("PreviousFrag","addFamily");
                                addFamily.this.startActivity(fr);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(addFamily.this.getApplicationContext(), "Failed to Add", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                        addFamily.this.phoneNumber.setError("Enter a Valid 10 digit mobile number");
                }
                else
                    addFamily.this.email.setError("Enter a Valid email Address");
            }
        });
    }

    public void uploadImage(String relation)
    {
        if (this.imageURI != null) {
            StorageReference photoRef = this.ref.child(relation);
            this.showProgressNotification();
            UploadTask task = photoRef.putFile(Uri.parse(this.imageURI));
            task.addOnProgressListener(new OnProgressListener<TaskSnapshot>() {
                @Override
                public void onProgress(TaskSnapshot taskSnapshot) {
                    int progress=(int)(taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount())*100;
                    addFamily.this.builder.setProgress(100, progress, false);
                    addFamily.this.notificationManager.notify(1, addFamily.this.builder.build());

                }
            });
            task.addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
                @Override
                public void onSuccess(TaskSnapshot taskSnapshot) {
                    addFamily.this.builder.setContentText("Upload complete")
                            .setProgress(0,0,false);
                    addFamily.this.notificationManager.notify(1, addFamily.this.builder.build());
                    String url=taskSnapshot.getDownloadUrl().toString();
                    addFamily.this.database.getReference(addFamily.this.auth.getCurrentUser().getUid())
                            .child("Family")
                            .child(addFamily.this.member.getMemberName())
                            .child("memberPhotoUrl")
                            .setValue(url);
                }
            });
        }
        else {
            Toast.makeText(this.getApplicationContext(), "No image", Toast.LENGTH_SHORT).show();
        }
    }

    public void showProgressNotification(){
        this.notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        this.builder = new Builder(this);
        this.builder = new Builder(this);
        this.builder.setContentTitle("Picture Upload")
                .setContentText("Upload in progress")
                .setSmallIcon(drawable.ic_notifications_black_24dp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == addFamily.REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                this.famMemImage.setImageBitmap(imageBitmap);
            }
        } else if (requestCode == addFamily.PIC_CROP) {
            Bundle extras = data.getExtras();
            Bitmap thePic = extras.getParcelable("data");
            this.famMemImage.setImageBitmap(thePic);
            this.imageURI = this.getImageUri(this, thePic).toString();
        } else if (requestCode == addFamily.RESULT_LOAD_IMG) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                this.imageURI = selectedImage.toString();
                this.famMemImage.setImageURI(selectedImage);
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
            this.startActivityForResult(cropIntent, addFamily.PIC_CROP);

        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case addFamily.MY_PERMISSIONS_REQUEST_CAMERA:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, addFamily.RESULT_LOAD_IMG);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(CompressFormat.JPEG, 100, bytes);
        String path = Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case id.home:
                Intent fr = new Intent(this, NavigationDrawer.class);
                fr.putExtra("PreviousFrag","addFamily");
                this.startActivity(fr);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent fr = new Intent(this, NavigationDrawer.class);
        fr.putExtra("PreviousFrag","addFamily");
        this.startActivity(fr);
    }

}
