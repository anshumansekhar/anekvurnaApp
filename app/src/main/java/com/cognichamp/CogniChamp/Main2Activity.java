package com.cognichamp.CogniChamp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Main2Activity extends AppCompatActivity {
    ImageView notificationImage;
    String photoUrl;
    FrameLayout layout;
    LinearLayout linearLayout;
    String[] optionNames;
    String subject;
    RadioGroup group;
    Button submitButton;
    boolean radio=true;
    TextView question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        layout=(FrameLayout)findViewById(R.id.radioButtonFrame);
        linearLayout=(LinearLayout)findViewById(R.id.linearLayout);
        group=(RadioGroup)layout.findViewById(R.id.notificationsRadioButtonGroup);
        submitButton=(Button)layout.findViewById(R.id.submitNotificationResponse);


        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                if (key.equals("IMG")) {
                    photoUrl=value;

                }
                else if(key.equals("Options")){
                    optionNames=value.split(",");
                }
                else if(key.equals("Subject")){
                    subject=value;
                }
                else if(key.equals("radio")){
                    radio = value.equals("true");
                }
            }
        }
        notificationImage=(ImageView)findViewById(R.id.notificationImage);
        Glide.with(getApplicationContext())
                .load(photoUrl)
                .into(notificationImage);
        if(optionNames.length!=0){
            if(radio){
                for(int i=0;i<optionNames.length;i++) {
                    RadioButton button=new RadioButton(Main2Activity.this);
                    button.setText(optionNames[i]);
                    group.addView(button,i);
                }
            }
            else {
                for(int i=0;i<optionNames.length;i++) {
                    CheckBox button=new CheckBox(Main2Activity.this);
                    button.setText(optionNames[i]);
                    linearLayout.addView(button,i);
                }

            }
        }
        question.setText(subject);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String option=((RadioButton)group.findViewById(group.getCheckedRadioButtonId())).getText().toString();
                FirebaseDatabase.getInstance()
                        .getReference("PushNotifications")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(subject)
                        .setValue(option)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Main2Activity.this,"We Recieved Your Response",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
