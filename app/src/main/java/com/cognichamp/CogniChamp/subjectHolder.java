package com.cognichamp.CogniChamp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * Created by Anshuman-HP on 16-08-2017.
 */

public class subjectHolder extends RecyclerView.ViewHolder {

    TextView subjectName;
    EditText subjectMarks;
    EditText totalMArks;
    ImageButton deleteSubject;
    public subjectHolder(View itemView) {
        super(itemView);
        subjectName=(TextView) itemView.findViewById(R.id.subjectNameEducation);
        subjectMarks = (EditText) itemView.findViewById(R.id.marksGrade);
        totalMArks=(EditText)itemView.findViewById(R.id.totalMarksSubject);
        deleteSubject = (ImageButton) itemView.findViewById(R.id.deleteGradeItem);
    }
    public subject loadDataToMap() {
        return new subject(Integer.parseInt(subjectMarks.getText().toString()), Integer.parseInt(totalMArks.getText().toString()), subjectName.getText().toString());
    }
}
