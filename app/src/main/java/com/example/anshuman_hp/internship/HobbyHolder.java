package com.example.anshuman_hp.internship;

import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Anshuman-HP on 10-08-2017.
 */

public class HobbyHolder extends RecyclerView.ViewHolder {
    CheckBox hobby;
    public HobbyHolder(View itemView) {
        super(itemView);
        hobby=(CheckBox)itemView.findViewById(R.id.hobbyItem);
    }
}
