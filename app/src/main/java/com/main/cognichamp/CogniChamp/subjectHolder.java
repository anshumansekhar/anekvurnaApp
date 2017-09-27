package com.main.cognichamp.CogniChamp;

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
        subjectMarks=(EditText)itemView.findViewById(R.id.marksSubject);
        totalMArks=(EditText)itemView.findViewById(R.id.totalMarksSubject);
        deleteSubject=(ImageButton)itemView.findViewById(R.id.deleteSubjectItem);
    }
    public subject loadDataToMap() {
        return new subject(Float.parseFloat(subjectMarks.getText().toString()),Float.parseFloat(totalMArks.getText().toString()),subjectName.getText().toString());
    }
}
