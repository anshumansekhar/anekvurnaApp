package com.example.anshuman_hp.internship;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

/**
 * Created by Anshuman-HP on 20-08-2017.
 */

public class Registration extends AppCompatActivity {

    EditText emailText;
    EditText passwordText;
    EditText name;
    EditText birthDate;
    RadioButton male,female;
    ImageView ProfileImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }
}
