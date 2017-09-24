package com.example.anshuman_hp.internship;

import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.gesture.GestureLibraries;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by Anshuman-HP on 20-08-2017.
 */

public class Registration extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final int RESULT_LOAD_IMG = 345;
    EditText emailText;
    EditText passwordText;
    EditText name;
    EditText birthDate;
    RadioButton male,female;
    ImageView ProfileImage;
    Button register;
    String ismale="true";
    String email,password;
    RadioGroup gender;

    String imageUri;

    final String TAG="Registartion";

    public static final int IMAGE_REQUEST=234;

    String myFormat = "dd-MM-yyyy"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog datePickerDialog;

    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    StorageReference ref= FirebaseStorage.getInstance().getReference();

    String mobile;
    String presentClass;
    boolean phoneauth;

    NotificationManager notificationManager;
    NotificationCompat.Builder builder;


    AuthCredential authCredential;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Intent j=getIntent();
        mobile=j.getStringExtra("Mobile");
        phoneauth=j.getBooleanExtra("PhoneAuth",false);
        email=j.getStringExtra("Email");
        password=j.getStringExtra("Password");

        Log.e(TAG,email +""+ password);


        datePickerDialog=new DatePickerDialog(Registration.this,this,2000,1,1);

        gender=(RadioGroup)findViewById(R.id.genderRadio);
        emailText=(EditText)findViewById(R.id.emailRegister);
        passwordText=(EditText)findViewById(R.id.passwordRegister);
        name=(EditText)findViewById(R.id.nameRegister);
        birthDate=(EditText)findViewById(R.id.BirthdateRegister);
        male=(RadioButton)findViewById(R.id.maleRegister);
        female=(RadioButton)findViewById(R.id.femaleRegister);
        ProfileImage=(ImageView)findViewById(R.id.profileImage);
        register=(Button)findViewById(R.id.registerButton);
        emailText.setText(email);
        passwordText.setText(password);


        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId==R.id.maleRegister){
                    ismale="true";
                }
                else if(checkedId==R.id.femaleRegister){
                    ismale="false";
                }
            }
        });

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"Register CLicked");
                if(!passwordText.getText().toString().isEmpty()) {
                    if (phoneauth) {
                        if (checkEmailPattern(emailText.getText().toString())) {
                            authCredential = EmailAuthProvider.getCredential(emailText.getText().toString().trim(), passwordText.getText().toString().trim());
                            Log.e(TAG, authCredential.toString());
                            linkPhoneWithEmail(authCredential);
                        } else {
                            emailText.setError("Enter a Valid email Address");
                        }
                    } else {
                        Log.e(TAG, "Creating user");
                        createUser();
                    }
                }
                else{
                    passwordText.setError("Enter a 8 characters Long Password");
                }
            }
        });

    }

    public boolean checkEmailPattern(String email)
    {
        String emailRegex="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        boolean value= Pattern.matches(emailRegex,email);
        return value ;
    }
    public void linkPhoneWithEmail(AuthCredential credential)
    {
        firebaseAuth.getCurrentUser()
                .linkWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d("tag", "linkWithCredential:success");
                            pushUserProfileDetails();
                        }
                        else {
                            Log.w("tag", "linkWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void pushUserProfileDetails()
    {
        Log.e(TAG,"Pushing user Details");

        user_profile profile=new user_profile(name.getText().toString(),
                birthDate.getText().toString()
        ,ismale
        ,presentClass
        ,"https://firebasestorage.googleapis.com/v0/b/internship2-4d772.appspot.com/o/noimage.png?alt=media&token=9ad0aff6-93aa-4443-94b0-be7746d43c05"
        ,""
        ,""
        ,""
        ,"");
        firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("UserProfile")
                .setValue(profile);
        Log.e(TAG,"Pushing class deatils");
        UserProfileChangeRequest.Builder profileChangeRequest=new UserProfileChangeRequest.Builder();
        profileChangeRequest.setDisplayName(name.getText().toString());
        UploadImage();
        Log.e(TAG,"Changong user data");
        firebaseAuth.getCurrentUser().updateProfile(profileChangeRequest.build())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e(TAG,"Updating succesful");
                        startActivity(new Intent(Registration.this,NavigationDrawer.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,e.toString());
            }
        });
    }
    public void createUser()
    {
        if(checkEmailPattern(emailText.getText().toString())) {
            firebaseAuth.createUserWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Log.e(TAG,"User Created");
                            pushUserProfileDetails();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, e.toString());
                }
            });
        }
        else
            Toast.makeText(getApplicationContext(),"Enter a Valid Email Address",Toast.LENGTH_SHORT);
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        int currentClass;
        int currentYear=Calendar.getInstance().get(Calendar.YEAR);
        if(Calendar.getInstance().get(Calendar.MONTH)>3) {
            currentClass= currentYear-year + 6;
        }else
            currentClass=currentYear-year +5;
        if(currentClass>17){
            currentClass=15;
        }
        presentClass=""+currentClass;
        birthDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==RESULT_LOAD_IMG){
            if(resultCode==RESULT_OK){
                Uri selectedImage = data.getData();
                imageUri=selectedImage.toString();
                ProfileImage.setImageURI(selectedImage);
            }
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
    public void UploadImage(){
        if(imageUri!=null) {
            StorageReference photoRef=ref.child(firebaseAuth.getCurrentUser().getUid());
            showProgressNotification();
            UploadTask task = photoRef.putFile(Uri.parse(imageUri));
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
                    firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                            .child("UserProfile")
                            .child("photourl")
                            .setValue(url);
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(),"No image",Toast.LENGTH_SHORT).show();
        }
    }
}
