package com.example.anshuman_hp.CogniChamp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

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
