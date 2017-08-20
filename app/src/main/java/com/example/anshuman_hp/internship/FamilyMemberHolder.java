package com.example.anshuman_hp.internship;

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
    public FamilyMemberHolder(View itemView) {
        super(itemView);
        memberName=(EditText)itemView.findViewById(R.id.familyMemberName);
        memPhoto=(ImageView)itemView.findViewById(R.id.familyMemberProfileImage);
        memRelation=(Spinner)itemView.findViewById(R.id.familyMemberRelation);

    }
}
