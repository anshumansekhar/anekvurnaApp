package com.cognichamp.CogniChamp;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;


/**
 * Created by Anshuman-HP on 12-09-2017.
 */

public class subjectTopicHolde extends RecyclerView.ViewHolder {
    ImageView imageView;
    CardView view;
    public subjectTopicHolde(View itemView) {
        super(itemView);
        imageView=(ImageView)itemView.findViewById(R.id.subject_topicImage);
        view=(CardView)itemView.findViewById(R.id.CardView);

    }
}
