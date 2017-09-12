package com.example.anshuman_hp.internship;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Anshuman-HP on 12-09-2017.
 */

public class subjectTopicHolde extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView textView;
    CardView view;
    public subjectTopicHolde(View itemView) {
        super(itemView);

        imageView=(ImageView)itemView.findViewById(R.id.subject_topicImage);
        textView=(TextView)itemView.findViewById(R.id.subject_topicName);
        view=(CardView)itemView.findViewById(R.id.CardView);

    }
}
