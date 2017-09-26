package com.example.anshuman_hp.CogniChamp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddNewSubject extends AppCompatActivity {

    RecyclerView addSubjectRecycler;
    Button submit;
    AutoCompleteTextView subjectChoose;
    ArrayAdapter arrayAdapter;
    subjectAdapter adapter;

    DatabaseReference ref;
    String reference;
    DatabaseReference Class;

    Intent a;
    int post;

    ArrayList<subject> list=new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_subject);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        a=getIntent();
        post=a.getIntExtra("Position",0);
        reference=a.getStringExtra("Ref");

        Class=FirebaseDatabase.getInstance().getReferenceFromUrl(a.getStringExtra("Class"));
        ref= FirebaseDatabase.getInstance().getReferenceFromUrl(reference);



        addSubjectRecycler=(RecyclerView)findViewById(R.id.addSubjectRecycler);
        addSubjectRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        submit=(Button)findViewById(R.id.submitAddSubject);
        subjectChoose=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);


        adapter=new subjectAdapter(list,post,ref,Class,AddNewSubject.this);
        addSubjectRecycler.setAdapter(adapter);
        arrayAdapter=ArrayAdapter.createFromResource(AddNewSubject.this,R.array.Subjects,android.R.layout.simple_dropdown_item_1line);
        subjectChoose.setAdapter(arrayAdapter);
        subjectChoose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("a","OnitemClicked");
                TextView tv=(TextView)view;
                subject subject=new subject(0,0,tv.getText().toString());
                list.add(subject);
                adapter.notifyDataSetChanged();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.uploadData();
                startActivity(new Intent(AddNewSubject.this,NavigationDrawer.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent fr=new Intent(AddNewSubject.this,NavigationDrawer.class);
        fr.putExtra("PreviousFrag","addSubject");
        startActivity(fr);
    }
}
