package com.example.anshuman_hp.internship;

import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Anshuman-HP on 10-08-2017.
 */

public class HobbyHolder extends RecyclerView.ViewHolder {
    TextView hobbyName;
    public HobbyHolder(View itemView) {
        super(itemView);
        hobbyName=(TextView)itemView.findViewById(R.id.hobbyitem);
    }
}
