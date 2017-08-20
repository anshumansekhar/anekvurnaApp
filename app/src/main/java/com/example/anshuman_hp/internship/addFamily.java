package com.example.anshuman_hp.internship;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class addFamily extends AppCompatActivity {
    ArrayAdapter relationsSpinnerAdapter;
    Spinner relations;
    EditText name;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    String relation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_family);
        name=(EditText)findViewById(R.id.familyMemberName);
        relations=(Spinner)findViewById(R.id.familyMemberRelation);

        final String[] array=getResources().getStringArray(R.array.Relation);
        relationsSpinnerAdapter= ArrayAdapter.createFromResource(getApplicationContext(),R.array.Relation,android.R.layout.simple_spinner_dropdown_item);
        relationsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        relations.setAdapter(relationsSpinnerAdapter);

        relations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                relation=""+position;
                database.getReference("Family")
                        .child(auth.getCurrentUser().getUid())
                        .push()
                        .setValue(new FamilyMember(name.getText().toString(),"",relation))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
