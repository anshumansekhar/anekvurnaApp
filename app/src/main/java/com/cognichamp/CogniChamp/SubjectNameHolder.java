package com.cognichamp.CogniChamp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


/**
 * Created by Anshuman-HP on 30-08-2017.
 */

public class SubjectNameHolder extends RecyclerView.ViewHolder {
    TextView subjectName;
    public SubjectNameHolder(View itemView) {
        super(itemView);
        subjectName=(TextView)itemView.findViewById(R.id.subject);
    }
}
