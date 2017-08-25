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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Anshuman-HP on 12-08-2017.
 */

public class HobbiesFragment extends Fragment {
    TextView addTextView;
    RecyclerView indoorGames,outdoorGames,music,dance,instruments,singing;
    TextView indoorgamesheading,outdoorgamesheading,danceheading,singingheading,instrumentsheading,musicheading;
    FirebaseRecyclerAdapter<hobby,HobbyHolder> indoorGamesAdapter,outDoorGamesAdapter,musicAdpater,danceAdpater,instrumentsAdapter,singingAdapter;
    DatabaseReference indoorGamesRef,outdoorGamesRef,MusicRef,DanceRef,singingRef,instrumentsRef;
    NestedScrollView scrollView;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseUser user=firebaseAuth.getCurrentUser();
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.new_hobby,container,false);
        scrollView=(NestedScrollView)v.findViewById(R.id.HobbyView);
        indoorGames=(RecyclerView)v.findViewById(R.id.indoorGamesRecycler);
        outdoorGames=(RecyclerView)v.findViewById(R.id.outdoorGamesRecycler);
        music=(RecyclerView)v.findViewById(R.id.musicRecycler);
        dance=(RecyclerView)v.findViewById(R.id.musicRecycler);
        instruments=(RecyclerView)v.findViewById(R.id.InstrumentsRecycler);
        singing=(RecyclerView)v.findViewById(R.id.siningRecyelr);
        musicheading=(TextView)v.findViewById(R.id.musciheadng);
        indoorgamesheading=(TextView)v.findViewById(R.id.indoorGamesHeading);
        outdoorgamesheading=(TextView)v.findViewById(R.id.outdoorGamesHEading);
        danceheading=(TextView)v.findViewById(R.id.danceheading);
        singingheading=(TextView)v.findViewById(R.id.singinheading);
        instrumentsheading=(TextView)v.findViewById(R.id.instrumentsheading);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        indoorGames.setLayoutManager(layoutManager);
        outdoorGames.setLayoutManager(layoutManager);
        music.setLayoutManager(layoutManager);
        singing.setLayoutManager(layoutManager);
        dance.setLayoutManager(layoutManager);
        instruments.setLayoutManager(layoutManager);
        addTextView=(TextView)v.findViewById(R.id.addHobby);
        addTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AddHobbyActivityt.class));
            }
        });

        indoorGamesAdapter=new FirebaseRecyclerAdapter<hobby, HobbyHolder>(hobby.class
        ,R.layout.hobby_item
        ,HobbyHolder.class
        ,indoorGamesRef) {
            @Override
            protected void populateViewHolder(HobbyHolder viewHolder, hobby model, int position) {

            }
        };
        return v;
    }
}
