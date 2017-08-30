package com.example.anshuman_hp.internship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class AddNewSubject extends AppCompatActivity {

    RecyclerView addSubjectRecycler;
    Button submit;
    AutoCompleteTextView subjectChoose;
    ArrayAdapter arrayAdapter;
    subjectAdapter adapter;

    DatabaseReference ref;
    String reference;

    Intent a;
    int post;

    ArrayList<subject> list=new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_subject);

        a=getIntent();
        post=a.getIntExtra("Position",0);
        reference=a.getStringExtra("Ref");

        ref= FirebaseDatabase.getInstance().getReferenceFromUrl(reference);


        addSubjectRecycler=(RecyclerView)findViewById(R.id.addSubjectRecycler);
        submit=(Button)findViewById(R.id.submitAddSubject);
        subjectChoose=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);

        subjectChoose.setDropDownBackgroundResource(R.color.cardview_dark_background);

        adapter=new subjectAdapter(list,post,ref);

        arrayAdapter=ArrayAdapter.createFromResource(getApplicationContext(),R.array.Subjects,android.R.layout.simple_dropdown_item_1line);
        subjectChoose.setAdapter(arrayAdapter);
        subjectChoose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("a","OnitemClicked");
                subject subject=new subject(0,0,getResources().getStringArray(R.array.Subjects)[position]);
                list.add(subject);
                adapter.notifyDataSetChanged();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.uploadData();
            }
        });
    }

}
