package com.cognichamp.CogniChamp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class emptyFragment extends Fragment {
    TextView tv;

    public emptyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_empty, container, false);
        tv = (TextView) v.findViewById(R.id.emptyText);
        if (getArguments().getBoolean("Video")) {
            tv.setText("No Videos available at this Moment");
        }
        return v;
    }
}
