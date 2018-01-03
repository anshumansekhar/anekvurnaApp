package com.cognichamp.CogniChamp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Anshuman-HP on 26-11-2017.
 */

public class GradeHolder extends RecyclerView.ViewHolder {
    EditText gradeEntry;
    TextView topicName;
    ImageButton deleteTopic;

    public GradeHolder(View itemView) {
        super(itemView);
        gradeEntry = (EditText) itemView.findViewById(R.id.marksGrade);
        topicName = (TextView) itemView.findViewById(R.id.gradeMarks);
        deleteTopic = (ImageButton) itemView.findViewById(R.id.deleteGradeItem);
    }
}
