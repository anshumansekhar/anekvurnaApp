package com.cognichamp.CogniChamp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

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

        Class=EducationFragment.classRef;
        ref= marksFragments.testRef.child("subjects");



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
                Intent fr=new Intent(AddNewSubject.this,NavigationDrawer.class);
                fr.putExtra("PreviousFrag","addSubject");
                startActivity(fr);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent fr=new Intent(AddNewSubject.this,NavigationDrawer.class);
                fr.putExtra("PreviousFrag","addSubject");
                startActivity(fr);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
