package com.example.anshuman_hp.internship;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Anshuman-HP on 12-08-2017.
 */

public class HobbiesFragment extends Fragment {
    List<Boolean> indoorGamesValues=new ArrayList<>();
    List<Boolean> outdoorGamesValues=new ArrayList<>();
    List<Boolean> musicValue=new ArrayList<>();
    List<Boolean> danceValues=new ArrayList<>();
    List<Boolean> instrumentsValues=new ArrayList<>();
    List<Boolean> singingValues=new ArrayList<>();
    List<String> indoorGamesValuesS=new ArrayList<>();
    List<String> outdoorGamesValuesS=new ArrayList<>();
    List<String> musicValueS=new ArrayList<>();
    List<String> danceValuesS=new ArrayList<>();
    List<String> instrumentsValuesS=new ArrayList<>();
    List<String> singingValuesS=new ArrayList<>();

    DatabaseReference indoorGamesRef,outdoorGamesRef,MusicRef,DanceRef,singingRef,instrumentsRef;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseUser user=firebaseAuth.getCurrentUser();
    ExpandableListView listView;
    expandableListAdapter listAdapter;
    TextView emptyView;
    List<String> header=new ArrayList<>();
    HashMap<String, List<String>> listDataChild=new HashMap<>();
    String[] hobby,indoorGames,outdoorGames,Music,Dance,singing,Instruments;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    public static HobbiesFragment newInstance() {
        HobbiesFragment fragment = new HobbiesFragment();
        return fragment;
    }
    public HobbiesFragment() {
        super();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.hobby_frag,container,false);
        listView=(ExpandableListView)v.findViewById(R.id.listView);
        emptyView=(TextView)v.findViewById(android.R.id.empty);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AddHobbyActivityt.class));
            }
        });
        listAdapter=new expandableListAdapter(getActivity(),listDataChild,header);
        listView.setEmptyView(emptyView);
        listView.setAdapter(listAdapter);
        hobby=getActivity().getResources().getStringArray(R.array.hobbyGroup);
        indoorGames=getActivity().getResources().getStringArray(R.array.IndoorGames);
        outdoorGames=getActivity().getResources().getStringArray(R.array.OutdoorGames);
        Music=getActivity().getResources().getStringArray(R.array.Music);
        Dance=getActivity().getResources().getStringArray(R.array.Dance);
        Instruments=getActivity().getResources().getStringArray(R.array.Instruments);
        singing=getActivity().getResources().getStringArray(R.array.singing);
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
                    for (int i=0;i<indoorGamesValues.size();i++) {
                        if(indoorGamesValues.get(i)) {
                            if(!header.contains(hobby[0]))
                                header.add(hobby[0]);
                            indoorGamesValuesS.add(indoorGames[i]);
                        }
                    }
                    listDataChild.put(hobby[0],indoorGamesValuesS);
                    listAdapter.notifyDataSetChanged();
                }
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
                    for (int i=0;i<outdoorGamesValues.size();i++) {
                        if (outdoorGamesValues.get(1)) {
                            if(!header.contains(hobby[1]))
                                 header.add(hobby[1]);
                            outdoorGamesValuesS.add( outdoorGames[i]);
                        }
                    }
                    listDataChild.put(hobby[1],outdoorGamesValuesS);
                    listAdapter.notifyDataSetChanged();
                }
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
                    for(int i=0;i<danceValues.size();i++)
                    {
                        if(danceValues.get(i)) {
                            if(!header.contains(hobby[3]))
                                header.add(hobby[3]);
                            danceValuesS.add( Dance[i]);
                        }
                    }
                    listDataChild.put(hobby[3],danceValuesS);
                    listAdapter.notifyDataSetChanged();
                }
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
                    for(int i=0;i<musicValue.size();i++)
                    {
                        if(musicValue.get(i)){
                            if(!header.contains(hobby[4]))
                                header.add(hobby[4]);
                            musicValueS.add(Music[i]);
                        }

                    }
                    listDataChild.put(hobby[4],musicValueS);
                    listAdapter.notifyDataSetChanged();
                }
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
                    for(int i=0;i<instrumentsValues.size();i++)
                    {
                        if(instrumentsValues.get(i)){
                            if(!header.contains(hobby[2]))
                                header.add(hobby[2]);
                            instrumentsValuesS.add(Instruments[i]);
                        }

                    }
                    listDataChild.put(hobby[2],instrumentsValuesS);
                    listAdapter.notifyDataSetChanged();
                }
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
                    for(int i=0;i<singingValues.size();i++)
                    {
                        if(singingValues.get(i)) {
                            if(!header.contains(hobby[5]))
                                header.add(hobby[5]);
                            singingValuesS.add( singing[i]);
                        }
                    }
                    listDataChild.put(hobby[5],singingValuesS);
                    listAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return v;
    }
}
