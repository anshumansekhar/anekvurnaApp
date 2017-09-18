package com.example.anshuman_hp.internship;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class fullImageActivity extends AppCompatActivity {
    ImageView imageView;
    Button openGallery;
    Button okayButton;

    private static final int SELECT_PICTURE = 1;
    public static Uri selectedImageUri=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView=(ImageView)findViewById(R.id.fullPhoto);
        openGallery=(Button)findViewById(R.id.openGallery);
        okayButton=(Button)findViewById(R.id.okayFullImage);

        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedImageUri!=null) {
                    Intent returnIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    Log.e("ash",selectedImageUri.toString());
                    returnIntent.putExtra("ImageURI",selectedImageUri.toString());
                    setResult(Activity.RESULT_OK,returnIntent);
                    returnIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),"No Image Uploaded",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                imageView.setImageURI(selectedImageUri);
            }
        }
    }
}
