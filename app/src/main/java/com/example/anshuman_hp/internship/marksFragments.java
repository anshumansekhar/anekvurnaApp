package com.example.anshuman_hp.internship;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

/**
 * Created by Anshuman-HP on 03-09-2017.
 */

public class marksFragments extends Fragment {
    Spinner testType;
    RecyclerView subjectsList;
    Button save,addnewSubject;
    TextView percentage;

    FirebaseRecyclerAdapter<subject, subjectHolder> recyclerAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.new_education_details,container,false);
        testType=(Spinner)v.findViewById(R.id.selectTestType);
        subjectsList=(RecyclerView)v.findViewById(R.id.subjectRecyclerEducation);
        save=(Button)v.findViewById(R.id.save);
        addnewSubject=(Button)v.findViewById(R.id.addnewSubject);
        percentage=(TextView)v.findViewById(R.id.percentageEducation);

        ArrayAdapter testAdapter=ArrayAdapter.createFromResource(getActivity(),R.array.tests,android.R.layout.simple_spinner_item);
        testAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        testType.setAdapter(testAdapter);
        String[] test=getActivity().getResources().getStringArray(R.array.tests);
        testType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TODO get the test type and show the subject details related to it
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }
}
