package com.cognichamp.CogniChamp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Anshuman-HP on 23-11-2017.
 */

public class ReportCardItemHolder extends RecyclerView.ViewHolder {
    TextView testType;
    TextView subjectNamesList;
    TextView subjectMarksList;
    TextView percentageText;
    TextView comments;

    public ReportCardItemHolder(View itemView) {
        super(itemView);
        testType = (TextView) itemView.findViewById(R.id.testTypeReport);
        subjectNamesList = (TextView) itemView.findViewById(R.id.subjectNameReport);
        subjectMarksList = (TextView) itemView.findViewById(R.id.marksGrade);
        percentageText = (TextView) itemView.findViewById(R.id.percentageReport);
        comments = (TextView) itemView.findViewById(R.id.comments);
    }
}
