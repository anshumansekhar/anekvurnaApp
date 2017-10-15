package com.cognichamp.CogniChamp;


import android.Manifest.permission;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.content.FileProvider.getUriForFile;
import static com.cognichamp.CogniChamp.addFamily.MY_PERMISSIONS_REQUEST_CAMERA;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportCardFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int RESULT_LOAD_IMG = 7878;
    private static final int MY_PERMISSIONS_REQUEST_CAMERADEVICE = 778;
    private static final int REQUEST_IMAGE_CAPTURE_CAMERA = 7458;
    ImageView reportCard;
    ImageButton addImage;
    Button uploadButton;
    Uri imageUri, imageBeforeUri;
    StorageReference storageReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    String mCurrentPhotoPath;


    public ReportCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(layout.fragment_report_card, container, false);
        reportCard = (ImageView) v.findViewById(R.id.reportCard);
        uploadButton = (Button) v.findViewById(R.id.uploadButton);
        addImage = (ImageButton) v.findViewById(R.id.addImage);
        if (imageUri == null) {
            uploadButton.setVisibility(View.GONE);
        }
        database.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("ClassDetails")
                .child("ReportCards")
                .child(EducationFragment.className)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Glide.with(getActivity())
                                    .load(dataSnapshot.getValue().toString())
                                    .into(reportCard);
                            uploadButton.setVisibility(View.GONE);
                        } else {
                            reportCard.setImageResource(R.drawable.ic_add_box_black_24dp);
                            uploadButton.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Open Camera", "Open Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ReportCardFragment.this.getActivity());
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Open Camera")) {
                            //userChoosenTask="Open Camera";
                            ReportCardFragment.this.cameraIntent();
                        } else if (items[item].equals("Open Gallery")) {
//                            userChoosenTask="Choose from Library";
                            ReportCardFragment.this.galleryIntent();
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });
        reportCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent fgh = new Intent(ReportCardFragment.this.getActivity(), FullscreenImageActivity.class);
                ReportCardFragment.this.startActivity(fgh);

            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportCardFragment.this.uploadImage();
            }
        });
        return v;
    }

    public void cameraIntent() {
        int permissionCheck = ContextCompat.checkSelfPermission(this.getActivity(),
                permission.READ_EXTERNAL_STORAGE);
        int permissionCheck1 = ContextCompat.checkSelfPermission(this.getActivity(),
                permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED && permissionCheck1 == PackageManager.PERMISSION_GRANTED) {
            boolean permissionCheck2 = this.getActivity().getPackageManager().hasSystemFeature("android.hardware.camera");
            if (permissionCheck2) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(this.getActivity().getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = this.createImageFile();
                    } catch (IOException ex) {

                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Log.e("dg", getActivity() + photoFile.toString());
                        FileProvider provider = new FileProvider();
                        Log.e("dad", getUriForFile(this.getActivity(),
                                "com.cognichamp.CogniChamp.fileprovider",
                                photoFile).toString());
                        this.imageBeforeUri = getUriForFile(this.getActivity(),
                                "com.cognichamp.CogniChamp.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, this.imageUri);
                        this.startActivityForResult(takePictureIntent, ReportCardFragment.REQUEST_IMAGE_CAPTURE_CAMERA);

                    }
                } else {
                    Toast.makeText(this.getActivity(), "No Camera Available", Toast.LENGTH_SHORT).show();
                }
            } else {
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{permission.READ_EXTERNAL_STORAGE, permission.WRITE_EXTERNAL_STORAGE},
                        ReportCardFragment.MY_PERMISSIONS_REQUEST_CAMERADEVICE);
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMG) {
            if (resultCode == RESULT_OK) {
                uploadButton.setVisibility(View.VISIBLE);
                Uri selectedImage = data.getData();
                selectedImage = Uri.fromFile(new File(this.compressImage(selectedImage.toString())));
                imageUri = selectedImage;
                System.out.println(this.imageUri);
                Glide.with(getActivity())
                        .load(selectedImage)
                        .into(reportCard);
            }

        } else if (requestCode == ReportCardFragment.REQUEST_IMAGE_CAPTURE_CAMERA) {
            if (resultCode == RESULT_OK) {
                uploadButton.setVisibility(View.VISIBLE);
                Log.e("uri", this.imageBeforeUri.toString());
                this.imageBeforeUri = Uri.fromFile(new File(this.compressImage(this.imageBeforeUri.toString())));
                imageUri = this.imageBeforeUri;
                System.out.println(this.imageUri);
                Glide.with(getActivity())
                        .load(this.imageBeforeUri)
                        .into(reportCard);

            }
        }
    }

    public void showProgressNotification() {
        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(getActivity());
        builder = new NotificationCompat.Builder(getActivity());
        builder.setContentTitle("Picture Upload")
                .setContentText("Upload in progress")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp);


    }

    public void update() {
        if (isAdded()) {
            database.getReference(firebaseAuth.getCurrentUser().getUid())
                    .child("ClassDetails")
                    .child("ReportCards")
                    .child(EducationFragment.className)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Glide.with(getActivity())
                                        .load(dataSnapshot.getValue().toString())
                                        .into(reportCard);
                                uploadButton.setVisibility(View.GONE);
                            } else {
                                reportCard.setImageResource(R.drawable.ic_add_box_black_24dp);
                                uploadButton.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            case ReportCardFragment.MY_PERMISSIONS_REQUEST_CAMERADEVICE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    boolean permissionCheck2 = this.getActivity().getPackageManager().hasSystemFeature("android.hardware.camera");
                    if (permissionCheck2) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(this.getActivity().getPackageManager()) != null) {
                            this.startActivityForResult(takePictureIntent, ReportCardFragment.REQUEST_IMAGE_CAPTURE_CAMERA);
                        }
                    } else {
                        Toast.makeText(this.getActivity(), "No Camera Available", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }

        }
    }

    public String compressImage(String imageUri) {
        String filePath = this.getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;
        Log.e("path", filePath);
        if (filePath.contains("ReportCards")) {
            filePath = Environment.getExternalStorageDirectory().getPath() + "/CogniChamp/" + filePath;
        }
        Options options = new Options();

        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        //TODO max height
        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }
        options.inSampleSize = this.calculateInSampleSize(options, actualWidth, actualHeight);

        options.inJustDecodeBounds = false;

        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + 6);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + 3);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + 8);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        String filename = this.getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;
    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "CogniChamp/ReportCards/" + EducationFragment.className);
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        if (contentUri.getAuthority().equals("com.cognichamp.CogniChamp.fileprovider")) {
            return contentUri.getPath();
        } else {
            Cursor cursor = this.getActivity().getContentResolver().query(contentUri, null, null, null, null);
            if (cursor == null) {
                return contentUri.getPath();
            } else {
                cursor.moveToFirst();
                Log.e("cursor", cursor.getColumnNames()[0] + cursor.getColumnNames()[1]);
                int index = cursor.getColumnIndex(ImageColumns.DATA);
                return cursor.getString(index);
            }
        }
    }

    public int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            int heightRatio = Math.round((float) height / (float) reqHeight);
            int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        float totalPixels = width * height;
        float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public void uploadImage() {
        if (imageUri != null) {
            showProgressNotification();
            UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(EducationFragment.className)
                    .child("ReportCard").putFile(imageUri);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int progress = (int) (taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount()) * 100;
                    builder.setProgress(100, progress, false);
                    notificationManager.notify(1, builder.build());
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    builder.setContentText("Upload complete")
                            .setProgress(0, 0, false);
                    notificationManager.notify(1, builder.build());
                    String url = taskSnapshot.getDownloadUrl().toString();
                    database.getReference(firebaseAuth.getCurrentUser().getUid())
                            .child("ClassDetails")
                            .child("ReportCards")
                            .child(EducationFragment.className)
                            .setValue(url);
                }
            });

        } else {
            Toast.makeText(getActivity(), "Please Select a Image First", Toast.LENGTH_SHORT).show();
        }
    }

    public void galleryIntent() {
        int permissionCheck = ContextCompat.checkSelfPermission(this.getActivity(),
                permission.READ_EXTERNAL_STORAGE);
        int permissionCheck1 = ContextCompat.checkSelfPermission(this.getActivity(),
                permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED && permissionCheck1 == PackageManager.PERMISSION_GRANTED) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
        } else {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{permission.READ_EXTERNAL_STORAGE, permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = this.getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        this.mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}









