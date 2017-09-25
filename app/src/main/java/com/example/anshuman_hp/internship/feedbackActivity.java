package com.example.anshuman_hp.internship;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
    Button submitFeedback;

    String messageTypetext,nameText,phone,email,messageText;

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
                            //TODO change the email from and to
                            sendEmailTask task=new sendEmailTask();
                            task.execute(messageTypetext+" by "+nameText
                                    ,messageText+"\n"+nameText+"\n"+email+"\n"+phone
                                    ,"anshumansekhar@hotmail.com"
                                    ,"anshumansekhardash@gmail.com");

                        }
                    }
                }

            }
        });
        return v;
    }
    public class sendEmailTask extends AsyncTask<String,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(),"Sending",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getActivity(),"We Received Your Valuable Response",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                MailSender sender = new MailSender("anshumansekhardash@gmail.com", "Anshuman@GOOGLE");
                sender.sendMail(params[0],params[1],params[2],params[3]);
            } catch (Exception e) {
                Log.e("SendMail", e.toString());
            }

            return null;
        }
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
