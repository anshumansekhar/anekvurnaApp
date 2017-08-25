package com.example.anshuman_hp.internship;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddHobbyActivityt extends AppCompatActivity {
    Spinner chooseHobbyGroup;
    RecyclerView hobbyGroupRecycler;
    hobbyAdapter hobbyAdapter;
    Button save;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hobby_activityt);

        ArrayAdapter adapter=ArrayAdapter.createFromResource(getApplicationContext()
                ,R.array.hobbyGroup,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseHobbyGroup=(Spinner)findViewById(R.id.chooseHobbyGroup);
        save=(Button)findViewById(R.id.saveHooby);
        chooseHobbyGroup.setAdapter(adapter);
        hobbyGroupRecycler=(RecyclerView)findViewById(R.id.hobbyRecyclerChoose);
        hobbyGroupRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        chooseHobbyGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position)
                {
                    case 0:hobbyAdapter=new hobbyAdapter(getApplicationContext(),1);
                        hobbyGroupRecycler.setAdapter(hobbyAdapter);
                        break;
                    case 1:hobbyAdapter=new hobbyAdapter(getApplicationContext(),2);
                        hobbyGroupRecycler.setAdapter(hobbyAdapter);
                        break;
                    case 2:hobbyAdapter=new hobbyAdapter(getApplicationContext(),3);
                        hobbyGroupRecycler.setAdapter(hobbyAdapter);
                        break;
                    case 3:hobbyAdapter=new hobbyAdapter(getApplicationContext(),5);
                        hobbyGroupRecycler.setAdapter(hobbyAdapter);
                        break;
                    case 4:hobbyAdapter=new hobbyAdapter(getApplicationContext(),4);
                        hobbyGroupRecycler.setAdapter(hobbyAdapter);
                        break;
                    case 5:hobbyAdapter=new hobbyAdapter(getApplicationContext(),6);
                        hobbyGroupRecycler.setAdapter(hobbyAdapter);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                new hobbyAdapter(getApplicationContext(),1);
                hobbyGroupRecycler.setAdapter(hobbyAdapter);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hobbyAdapter.saveChanges();
            }
        });
    }

}
