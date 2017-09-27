package com.main.cognichamp.CogniChamp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

/**
 * Created by Anshuman-HP on 24-09-2017.
 */

public class feedbackActivity extends Fragment {
    Spinner messageType;
    EditText name;
    EditText phoneNumber;
    EditText emailId;
    EditText message;
    EditText mailsubject;
    Button submitFeedback;

    String messageTypetext,nameText,phone,email,messageText,subjectText;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();

    user_profile profile=new user_profile();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.feedback,container,false);
        messageType=(Spinner)v.findViewById(R.id.MessageTypeSpinner);
        name=(EditText)v.findViewById(R.id.NameFeedback);
        phoneNumber=(EditText)v.findViewById(R.id.phoneFeedback);
        emailId=(EditText)v.findViewById(R.id.EmailFeedback);
        message=(EditText)v.findViewById(R.id.messageFeedback);
        submitFeedback=(Button)v.findViewById(R.id.submitFeedback);
        mailsubject=(EditText)v.findViewById(R.id.emailSubject);

        database.getReference(auth.getCurrentUser().getUid())
                .child("UserProfile")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        profile=dataSnapshot.getValue(user_profile.class);
                        name.setText(profile.getName());
                        if(!auth.getCurrentUser().getPhoneNumber().isEmpty()) {
                            phoneNumber.setText(auth.getCurrentUser().getPhoneNumber().substring(3));
                        }
                        emailId.setText(auth.getCurrentUser().getEmail());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        ArrayAdapter arrayAdapter=ArrayAdapter.createFromResource(getActivity(),R.array.message_type,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        messageType.setAdapter(arrayAdapter);
        messageType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                messageTypetext=getResources().getStringArray(R.array.message_type)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkEmailPattern(emailId.getText().toString())){
                    if(checkPhonePattern(phoneNumber.getText().toString())){
                        if(!message.getText().toString().isEmpty()){
                            email=emailId.getText().toString();
                            phone=phoneNumber.getText().toString();
                            nameText=name.getText().toString();
                            messageText=message.getText().toString();
                            subjectText=mailsubject.getText().toString();
                            //TODO change the email from and to
                            sendEmailTask task=new sendEmailTask();
                            task.execute(messageTypetext+" by "+nameText
                                    ,subjectText
                                    ,email
                                    ,"contact@cognichamp.com,"+email,messageTypetext);
                        }
                        else{
                            message.setError("Message cant be Empty");
                        }
                    }
                    else {
                        phoneNumber.setError("Enter a Valid Phone Number");
                    }
                }
                else {
                    emailId.setError("Enter a Valid Email id");
                }
            }
        });
        return v;
    }
    public class sendEmailTask extends AsyncTask<String,Void,Void>{
        String messageType;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(),"Sending",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getActivity(),"Thank you.Your "+messageType+" has been submitted. Our team will reach out to you shortly.",Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                MailSender sender = new MailSender("cognichamp@gmail.com", "12345678!");
                sender.sendMail(params[0],params[1],params[2],params[3]);
                messageType=params[4];
            } catch (Exception e) {
                Log.e("SendMail", e.toString());
            }

            return null;
        }
    }
    public static boolean checkEmailPattern(String email) {
        String emailRegex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return  Pattern.matches(emailRegex, email);
    }
    public static boolean checkPhonePattern(String Phone) {
        String phoneRegex = "[789]{1}[1234567890]{9}";
       return Pattern.matches(phoneRegex, Phone);

    }
}
