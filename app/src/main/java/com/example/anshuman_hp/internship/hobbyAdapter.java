package com.example.anshuman_hp.internship;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Anshuman-HP on 24-08-2017.
 */

public class hobbyAdapter extends RecyclerView.Adapter<hobbyAdapter.hobbyHolder> {
    String[] array;
    Context ctx;
    int ArrayNum;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    SparseBooleanArray booleanArray=new SparseBooleanArray();
    HashMap<Integer,Boolean> isCheckedArray=new HashMap<>();
    HashMap<Integer,Boolean> isCheckedIndoorGames=new HashMap<>();
    HashMap<Integer,Boolean> isCheckedOutdoorGames=new HashMap<>();
    HashMap<Integer,Boolean> isCheckedDancing=new HashMap<>();
    HashMap<Integer,Boolean> isCheckedSinging=new HashMap<>();
    HashMap<Integer,Boolean> isCheckedInstruments=new HashMap<>();
    HashMap<Integer,Boolean> isCheckedMusic=new HashMap<>();

    public hobbyAdapter(Context ctx,int arrayNum) {
        this.ctx = ctx;
        ArrayNum=arrayNum;
        getData();
        switch(ArrayNum){
            case 1:array=ctx.getResources().getStringArray(R.array.IndoorGames);
                isCheckedArray=isCheckedIndoorGames;
                    break;
            case 2:array=ctx.getResources().getStringArray(R.array.OutdoorGames);
                isCheckedArray=isCheckedOutdoorGames;
                break;
            case 3:array=ctx.getResources().getStringArray(R.array.Instruments);
                isCheckedArray=isCheckedInstruments;
                break;
            case 4:array=ctx.getResources().getStringArray(R.array.Music);
                isCheckedArray=isCheckedMusic;
                break;
            case 5:array=ctx.getResources().getStringArray(R.array.Dance);
                isCheckedArray=isCheckedDancing;
                break;
            case 6:array=ctx.getResources().getStringArray(R.array.singing);
                isCheckedArray=isCheckedSinging;
                break;
        }
    }
    public void getData() {
        DatabaseReference indoorGamesRef=database.getReference(auth.getCurrentUser().getUid())
                .child("Hobbies")
                .child("IndoorGames");
        indoorGamesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e(dataSnapshot.getKey(),""+dataSnapshot.getValue());
                isCheckedIndoorGames.put(Integer.parseInt(dataSnapshot.getKey()),dataSnapshot.getValue(Boolean.class));
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference outdoorGamesRef=database.getReference(auth.getCurrentUser().getUid())
                .child("Hobbies")
                .child("OutdoorGames");
        indoorGamesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e(dataSnapshot.getKey(),""+dataSnapshot.getValue());
                isCheckedOutdoorGames.put(Integer.parseInt(dataSnapshot.getKey()),dataSnapshot.getValue(Boolean.class));
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference danceRef=database.getReference(auth.getCurrentUser().getUid())
                .child("Hobbies")
                .child("Dance");
        danceRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e(dataSnapshot.getKey(),""+dataSnapshot.getValue());
                isCheckedDancing.put(Integer.parseInt(dataSnapshot.getKey()),dataSnapshot.getValue(Boolean.class));
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference musicRef=database.getReference(auth.getCurrentUser().getUid())
                .child("Hobbies")
                .child("Music");
        musicRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e(dataSnapshot.getKey(),""+dataSnapshot.getValue());
                isCheckedMusic.put(Integer.parseInt(dataSnapshot.getKey()),dataSnapshot.getValue(Boolean.class));
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference singingRef=database.getReference(auth.getCurrentUser().getUid())
                .child("Hobbies")
                .child("IndoorGames");
        singingRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e(dataSnapshot.getKey(),""+dataSnapshot.getValue());
                isCheckedSinging.put(Integer.parseInt(dataSnapshot.getKey()),dataSnapshot.getValue(Boolean.class));
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference instrumentsRef=database.getReference(auth.getCurrentUser().getUid())
                .child("Hobbies")
                .child("Instruments");
        instrumentsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e(dataSnapshot.getKey(),""+dataSnapshot.getValue());
                isCheckedInstruments.put(Integer.parseInt(dataSnapshot.getKey()),dataSnapshot.getValue(Boolean.class));
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public hobbyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(ctx).inflate(R.layout.hobby_item,parent,false);
        return new hobbyHolder(v);
    }

    @Override
    public void onBindViewHolder(hobbyHolder holder, final int position) {
        getData();
        holder.hobbyItem.setText(array[position]);
        if(!isCheckedArray.isEmpty()) {
            holder.hobbyItem.setChecked(isCheckedArray.get(position));
            holder.hobbyItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    isCheckedArray.put(position, isChecked);
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return array.length;
    }
    class hobbyHolder extends RecyclerView.ViewHolder{
        CheckBox hobbyItem;
        public hobbyHolder(View itemView) {
            super(itemView);
            hobbyItem=(CheckBox)itemView.findViewById(R.id.hobbyItem);
        }
    }

    public void saveChanges()
    {
        switch (ArrayNum){
            case 1:database.getReference(auth.getCurrentUser().getUid())
                    .child("Hobbies")
                    .child("IndoorGames")
                    .setValue(isCheckedIndoorGames);
                break;
            case 2:database.getReference(auth.getCurrentUser().getUid())
                    .child("Hobbies")
                    .child("OutdoorGames")
                    .setValue(isCheckedOutdoorGames);
                break;
            case 5:database.getReference(auth.getCurrentUser().getUid())
                    .child("Hobbies")
                    .child("Dance")
                    .setValue(isCheckedDancing);
                break;
            case 3:database.getReference(auth.getCurrentUser().getUid())
                    .child("Hobbies")
                    .child("Instruments")
                    .setValue(isCheckedInstruments);
                break;
            case 4:database.getReference(auth.getCurrentUser().getUid())
                    .child("Hobbies")
                    .child("Music")
                    .setValue(isCheckedMusic);
                break;
            case 6:database.getReference(auth.getCurrentUser().getUid())
                    .child("Hobbies")
                    .child("Singing")
                    .setValue(isCheckedSinging);
                break;

        }
    }
}
