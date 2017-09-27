package com.main.cognichamp.CogniChamp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AddHobbyActivityt extends AppCompatActivity {
    Spinner chooseHobbyGroup;
    RecyclerView hobbyGroupRecycler;
    hobbyAdapter hobbyAdapter;
    Button save;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    ActionBar ab;

    List indoorGamesValues;
    List outdoorGamesValues;
    List musicValue;
    List danceValues;
    List instrumentsValues;
    List singingValues;
    hobbyAdapter indoorGamesAdapter,OutdoorGamesAdpater,DanceAdapter,MusicAdapter,singingAdapter,instrumentsAdapter;

    DatabaseReference indoorGamesRef,outdoorGamesRef,MusicRef,DanceRef,singingRef,instrumentsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hobby_activityt);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ab=getSupportActionBar();
        ab.setTitle("Add Hobby");

        indoorGamesRef=database.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("Hobbies")
                .child("IndoorGames");
        outdoorGamesRef=database.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("Hobbies")
                .child("OutdoorGames");
        MusicRef=database.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("Hobbies")
                .child("Music");
        DanceRef=database.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("Hobbies")
                .child("Dance");
        singingRef=database.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("Hobbies")
                .child("singing");
        instrumentsRef=database.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("Hobbies")
                .child("Instruments");

        indoorGamesRef.addValueEventListener(new ValueEventListener() {
            GenericTypeIndicator<List<Boolean>> t = new GenericTypeIndicator<List<Boolean>>() {};
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    indoorGamesValues = dataSnapshot.getValue(t);
                    indoorGamesAdapter=new hobbyAdapter(indoorGamesValues,AddHobbyActivityt.this,0,indoorGamesRef);
                }
                else
                    indoorGamesAdapter= new hobbyAdapter(AddHobbyActivityt.this,0,indoorGamesRef);
                indoorGamesAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        outdoorGamesRef.addValueEventListener(new ValueEventListener() {
            GenericTypeIndicator<List<Boolean>> t = new GenericTypeIndicator<List<Boolean>>() {};
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    outdoorGamesValues = dataSnapshot.getValue(t);
                    OutdoorGamesAdpater=new hobbyAdapter(outdoorGamesValues,AddHobbyActivityt.this,1,outdoorGamesRef);
                }
                else
                    OutdoorGamesAdpater=new hobbyAdapter(AddHobbyActivityt.this,1,outdoorGamesRef);
                OutdoorGamesAdpater.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DanceRef.addValueEventListener(new ValueEventListener() {
            GenericTypeIndicator<List<Boolean>> t = new GenericTypeIndicator<List<Boolean>>() {};
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    danceValues = dataSnapshot.getValue(t);
                    DanceAdapter=new hobbyAdapter(danceValues,AddHobbyActivityt.this,3,DanceRef);

                }
                else
                    DanceAdapter=new hobbyAdapter(AddHobbyActivityt.this,3,DanceRef);
                DanceAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        MusicRef.addValueEventListener(new ValueEventListener() {
            GenericTypeIndicator<List<Boolean>> t = new GenericTypeIndicator<List<Boolean>>() {};
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    musicValue = dataSnapshot.getValue(t);
                    MusicAdapter=new hobbyAdapter(musicValue,AddHobbyActivityt.this,4,MusicRef);
                }
                else
                    MusicAdapter=new hobbyAdapter(AddHobbyActivityt.this,4,MusicRef);
                MusicAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        instrumentsRef.addValueEventListener(new ValueEventListener() {
            GenericTypeIndicator<List<Boolean>> t = new GenericTypeIndicator<List<Boolean>>() {};
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    instrumentsValues=dataSnapshot.getValue(t);
                    instrumentsAdapter=new hobbyAdapter(instrumentsValues,AddHobbyActivityt.this,2,instrumentsRef);

                }
                else
                    instrumentsAdapter=new hobbyAdapter(AddHobbyActivityt.this,2,instrumentsRef);
                instrumentsAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        singingRef.addValueEventListener(new ValueEventListener() {
            GenericTypeIndicator<List<Boolean>> t = new GenericTypeIndicator<List<Boolean>>() {};
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    singingValues = dataSnapshot.getValue(t);
                    singingAdapter=new hobbyAdapter(singingValues,AddHobbyActivityt.this,5,singingRef);

                }
                else
                    singingAdapter=new hobbyAdapter(AddHobbyActivityt.this,5,singingRef);
                singingAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ArrayAdapter adapter=ArrayAdapter.createFromResource(AddHobbyActivityt.this
                ,R.array.hobbyGroup,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseHobbyGroup=(Spinner)findViewById(R.id.chooseHobbyGroup);
        save=(Button)findViewById(R.id.saveHooby);
        chooseHobbyGroup.setAdapter(adapter);
        hobbyGroupRecycler=(RecyclerView)findViewById(R.id.hobbyRecyclerChoose);
        hobbyGroupRecycler.setLayoutManager(new LinearLayoutManager(AddHobbyActivityt.this));
        chooseHobbyGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position)
                {
                    case 0:hobbyAdapter=(com.main.cognichamp.CogniChamp.hobbyAdapter)hobbyGroupRecycler.getAdapter();
                            if(hobbyAdapter!=null)
                                hobbyAdapter.saveChanges();
                            hobbyGroupRecycler.setAdapter(indoorGamesAdapter);
                             indoorGamesAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        hobbyAdapter=(com.main.cognichamp.CogniChamp.hobbyAdapter)hobbyGroupRecycler.getAdapter();
                            if(hobbyAdapter!=null)
                                hobbyAdapter.saveChanges();
                            hobbyGroupRecycler.setAdapter(OutdoorGamesAdpater);
                        OutdoorGamesAdpater.notifyDataSetChanged();
                        break;
                    case 2:
                        hobbyAdapter=(com.main.cognichamp.CogniChamp.hobbyAdapter)hobbyGroupRecycler.getAdapter();
                            if(hobbyAdapter!=null)
                                hobbyAdapter.saveChanges();
                            hobbyGroupRecycler.setAdapter(instrumentsAdapter);
                        instrumentsAdapter.notifyDataSetChanged();
                        break;
                    case 3:
                        hobbyAdapter=(com.main.cognichamp.CogniChamp.hobbyAdapter)hobbyGroupRecycler.getAdapter();
                            if(hobbyAdapter!=null)
                                hobbyAdapter.saveChanges();
                            hobbyGroupRecycler.setAdapter(DanceAdapter);
                        DanceAdapter.notifyDataSetChanged();
                        break;
                    case 4:
                        hobbyAdapter=(com.main.cognichamp.CogniChamp.hobbyAdapter)hobbyGroupRecycler.getAdapter();
                            if(hobbyAdapter!=null)
                                hobbyAdapter.saveChanges();
                            hobbyGroupRecycler.setAdapter(MusicAdapter);
                        MusicAdapter.notifyDataSetChanged();
                        break;
                    case 5:
                        hobbyAdapter=(com.main.cognichamp.CogniChamp.hobbyAdapter)hobbyGroupRecycler.getAdapter();
                            if(hobbyAdapter!=null)
                                hobbyAdapter.saveChanges();
                            hobbyGroupRecycler.setAdapter(singingAdapter);
                        singingAdapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hobbyAdapter=(com.main.cognichamp.CogniChamp.hobbyAdapter)hobbyGroupRecycler.getAdapter();
                hobbyAdapter.saveChanges();
            }
        });
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
                Intent fr=new Intent(AddHobbyActivityt.this,NavigationDrawer.class);
                fr.putExtra("PreviousFrag","Hobby");
                startActivity(fr);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent fr=new Intent(AddHobbyActivityt.this,NavigationDrawer.class);
        fr.putExtra("PreviousFrag","Hobby");
        startActivity(fr);
    }
}
