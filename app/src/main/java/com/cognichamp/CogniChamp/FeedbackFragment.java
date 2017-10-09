package com.cognichamp.CogniChamp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Anshuman-HP on 28-09-2017.
 */

public class FeedbackFragment extends Fragment{

    TextView tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.new_feedback,container,false);
        tv=(TextView)v.findViewById(R.id.feedbackText);


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:contact@cognichamp.com?subject=" );
                intent.setData(data);
                startActivity(intent);
            }
        });
        return v;
    }
}
