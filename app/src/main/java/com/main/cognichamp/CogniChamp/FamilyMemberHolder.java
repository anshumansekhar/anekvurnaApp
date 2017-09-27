package com.main.cognichamp.CogniChamp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

/**
 * Created by Anshuman-HP on 13-08-2017.
 */

public class FamilyMemberHolder extends RecyclerView.ViewHolder {
    EditText memberName;
    ImageView memPhoto;
    Spinner memRelation;
    EditText phoneNumber;
    EditText emailId;
    public FamilyMemberHolder(View itemView) {
        super(itemView);
        memberName=(EditText)itemView.findViewById(R.id.familyMemberName);
        memPhoto=(ImageView)itemView.findViewById(R.id.photoFamily);
        memRelation=(Spinner)itemView.findViewById(R.id.familyMemberRelation);
        phoneNumber=(EditText)itemView.findViewById(R.id.PhoneNumberFamily);
        emailId=(EditText)itemView.findViewById(R.id.EmailIdFamily);


    }

}
