package com.cognichamp.CogniChamp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Anshuman-HP on 17-10-2017.
 */

public class ReportCardRecyclerHolder extends RecyclerView.ViewHolder {
    TextView testType;
    TextView marks;
    TextView percentage;

    public ReportCardRecyclerHolder(View itemView) {
        super(itemView);
        testType = (TextView) itemView.findViewById(R.id.testTypeReport);
        marks = (TextView) itemView.findViewById(R.id.subjectsReport);
        percentage = (TextView) itemView.findViewById(R.id.percentageReport);
    }
}
