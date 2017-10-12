package com.cognichamp.CogniChamp;


import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.Fragment;
import android.support.v7.app.NotificationCompat.Builder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cognichamp.CogniChamp.R.drawable;
import com.cognichamp.CogniChamp.R.id;
import com.cognichamp.CogniChamp.R.layout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.UploadTask.TaskSnapshot;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportCardFragment extends Fragment {
    private static final int RESULT_LOAD_IMG = 7878;
    ImageView reportCard;
    Button uploadButton;
    Uri imageUri;
    StorageReference storageReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    NotificationManager notificationManager;
    Builder builder;


    public ReportCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(layout.fragment_report_card, container, false);
        this.reportCard = (ImageView) v.findViewById(id.reportCard);
        this.uploadButton = (Button) v.findViewById(id.uploadButton);
        if (this.imageUri == null) {
            this.uploadButton.setVisibility(View.GONE);
        }
        this.storageReference = FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(EducationFragment.className)
                .child("ReportCard");
        this.database.getReference(this.firebaseAuth.getCurrentUser().getUid())
                .child("ClassDetails")
                .child("ReportCards")
                .child(EducationFragment.className)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Glide.with(ReportCardFragment.this.getActivity())
                                    .load(dataSnapshot.getValue().toString())
                                    .into(ReportCardFragment.this.reportCard);
                            ReportCardFragment.this.uploadButton.setVisibility(View.GONE);
                        } else {
                            ReportCardFragment.this.reportCard.setImageResource(drawable.ic_add_box_black_24dp);
                            ReportCardFragment.this.uploadButton.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        this.reportCard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
                ReportCardFragment.this.startActivityForResult(galleryIntent, ReportCardFragment.RESULT_LOAD_IMG);
            }
        });
        this.uploadButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ReportCardFragment.this.imageUri != null) {
                    ReportCardFragment.this.showProgressNotification();
                    UploadTask uploadTask = ReportCardFragment.this.storageReference.putFile(ReportCardFragment.this.imageUri);
                    uploadTask.addOnProgressListener(new OnProgressListener<TaskSnapshot>() {
                        @Override
                        public void onProgress(TaskSnapshot taskSnapshot) {
                            int progress = (int) (taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount()) * 100;
                            ReportCardFragment.this.builder.setProgress(100, progress, false);
                            ReportCardFragment.this.notificationManager.notify(1, ReportCardFragment.this.builder.build());
                        }
                    });
                    uploadTask.addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
                        @Override
                        public void onSuccess(TaskSnapshot taskSnapshot) {
                            ReportCardFragment.this.builder.setContentText("Upload complete")
                                    .setProgress(0, 0, false);
                            ReportCardFragment.this.notificationManager.notify(1, ReportCardFragment.this.builder.build());
                            String url = taskSnapshot.getDownloadUrl().toString();
                            ReportCardFragment.this.database.getReference(ReportCardFragment.this.firebaseAuth.getCurrentUser().getUid())
                                    .child("ClassDetails")
                                    .child("ReportCards")
                                    .child(EducationFragment.className)
                                    .setValue(url);
                        }
                    });

                } else {
                    Toast.makeText(ReportCardFragment.this.getActivity(), "Please Select a Image First", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ReportCardFragment.RESULT_LOAD_IMG) {
            if (resultCode == RESULT_OK) {
                this.uploadButton.setVisibility(View.VISIBLE);
                Uri selectedImage = data.getData();
                this.imageUri = selectedImage;
                Glide.with(this.getActivity())
                        .load(selectedImage)
                        .into(this.reportCard);
            }

        }
    }

    public void showProgressNotification() {
        this.notificationManager = (NotificationManager) this.getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        this.builder = new Builder(this.getActivity());
        this.builder = new Builder(this.getActivity());
        this.builder.setContentTitle("Picture Upload")
                .setContentText("Upload in progress")
                .setSmallIcon(drawable.ic_notifications_black_24dp);


    }

    public void update() {
        if (this.isAdded()) {
            this.database.getReference(this.firebaseAuth.getCurrentUser().getUid())
                    .child("ClassDetails")
                    .child("ReportCards")
                    .child(EducationFragment.className)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Glide.with(ReportCardFragment.this.getActivity())
                                        .load(dataSnapshot.getValue().toString())
                                        .into(ReportCardFragment.this.reportCard);
                                ReportCardFragment.this.uploadButton.setVisibility(View.GONE);
                            } else {
                                ReportCardFragment.this.reportCard.setImageResource(drawable.ic_add_box_black_24dp);
                                ReportCardFragment.this.uploadButton.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

    }
}
