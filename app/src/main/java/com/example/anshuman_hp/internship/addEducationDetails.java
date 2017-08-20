package com.example.anshuman_hp.internship;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Anshuman-HP on 16-08-2017.
 */

public class addEducationDetails extends AppCompatActivity {

    EditText schoolName;
    EditText className;
    RecyclerView subjects;
    Button submit;
    FloatingActionButton addSubject;
    ArrayList<subject> arrayList=new ArrayList<>();
    FirebaseDatabase database=FirebaseDatabase.getInstance();

    ArrayList subjectList=new ArrayList();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_education_details);
        schoolName=(EditText)findViewById(R.id.schoolname);
        className=(EditText)findViewById(R.id.className);
        subjects=(RecyclerView)findViewById(R.id.subjectRecycler);
        submit=(Button)findViewById(R.id.Submit);
        addSubject=(FloatingActionButton)findViewById(R.id.addSubject);
        LayoutInflater inflater=getLayoutInflater();

        final subjectAdapter adapter=new subjectAdapter(arrayList);
        subjects.setAdapter(adapter);

        final View view=inflater.inflate(R.layout.subject_entry,null);
        final AlertDialog.Builder builder=new AlertDialog.Builder(addEducationDetails.this);
        builder.setTitle("Add a new Subject");
//        final EditText subjectname=(EditText)view.findViewById(R.id.subjectNameedit);
//        final EditText subjectMarks=(EditText)view.findViewById(R.id.subjectMarkEdit);
//        final EditText totalMarks=(EditText)view.findViewById(R.id.TotalMarkEdit);
        builder.setView(view);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("wr","Adding subject");
//                arrayList.add(new subject(subjectname.getText().toString()
//                        ,subjectMarks.getText().toString(),
//                        totalMarks.getText().toString()));
//                adapter.notifyDataSetChanged();
                dialog.cancel();

            }
        }).setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog dialog=builder.create();
        addSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("wr","Showing Dialog");
                adapter.notifyDataSetChanged();
                dialog.show();
            }
        });
        subjects.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalMarks=0;
                int marks=0;
                StringBuilder subjects = new StringBuilder();
                StringBuilder subjectMarks = new StringBuilder();
                for(int i=0;i<arrayList.size();i++)
                {
                    totalMarks=totalMarks+Integer.valueOf(arrayList.get(i).getTotalMarks());
                    marks=marks+Integer.valueOf(arrayList.get(i).getSubMarks());
                    subjectMarks.append(arrayList.get(i).getSubMarks());
                    subjectMarks.append(System.getProperty ("line.separator"));
                    subjects.append(arrayList.get(i).getSubName());
                    subjects.append(System.getProperty ("line.separator"));
                }
                Log.e("wr","Calculating Percentage");
                float percentage=(float)marks/(float)totalMarks * 100;
                ClassObject classObject=new ClassObject(schoolName.getText().toString()
                        ,className.getText().toString()
                ,""+percentage,subjects.toString(),subjectMarks.toString());
                Log.e("wr","Adding to class");
                database.getReference("classList")
                        .child(className.getText().toString())
                        .setValue(className.getText().toString());
                database.getReference("Classes")
                        .child(className.getText().toString())
                        .setValue(classObject)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.e("wr","Added");
                                startActivity(new Intent(addEducationDetails.this,NavigationDrawer.class));
                            }
                        });
            }
        });

    }
}
