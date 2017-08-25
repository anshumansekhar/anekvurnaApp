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
    TextView subjectName;
    EditText subjectMarks;
    EditText totalMArks;
    public subjectHolder(View itemView) {
        super(itemView);
        subjectName=(TextView) itemView.findViewById(R.id.subjectNameEducation);
        subjectMarks=(EditText)itemView.findViewById(R.id.marksSubject);
        totalMArks=(EditText)itemView.findViewById(R.id.totalMarksSubject);
        layout=(ConstraintLayout)itemView.findViewById(R.id.layout);
    }

}
