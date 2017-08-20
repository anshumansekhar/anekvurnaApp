package com.example.anshuman_hp.internship;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Anshuman-HP on 16-08-2017.
 */

public class subjectHolder extends RecyclerView.ViewHolder {
    ConstraintLayout layout;
    EditText subjectName;
    EditText subjectMarks;
    EditText totalMArks;
    public subjectHolder(View itemView) {
        super(itemView);
        subjectName=(EditText) itemView.findViewById(R.id.subjectNameEdit);
        subjectMarks=(EditText)itemView.findViewById(R.id.subjectMarkEdit);
        totalMArks=(EditText)itemView.findViewById(R.id.totalMarksEdit);
        layout=(ConstraintLayout)itemView.findViewById(R.id.layout);
    }

}
