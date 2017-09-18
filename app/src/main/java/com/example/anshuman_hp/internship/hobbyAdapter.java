package com.example.anshuman_hp.internship;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    List<Boolean> list;
    Context ctx;
    int ArrayNum;
    String[] array;
    DatabaseReference reference;

    public hobbyAdapter(List<Boolean> list, Context ctx, int arrayNum,DatabaseReference ref) {
        this.list = list;
        this.ctx = ctx;
        ArrayNum = arrayNum;
        reference=ref;
        switch (arrayNum)
        {
            case 0:array=ctx.getResources().getStringArray(R.array.IndoorGames);
                break;
            case 1:array=ctx.getResources().getStringArray(R.array.OutdoorGames);
                break;
            case 2:array=ctx.getResources().getStringArray(R.array.Instruments);
                break;
            case 3:array=ctx.getResources().getStringArray(R.array.Dance);
                break;
            case 4:array=ctx.getResources().getStringArray(R.array.Music);
                break;
            case 5:array=ctx.getResources().getStringArray(R.array.singing);
                break;

        }
    }

    public hobbyAdapter(Context ctx, int arrayNum, DatabaseReference reference) {
        this.ctx = ctx;
        ArrayNum = arrayNum;
        this.reference = reference;
        switch (arrayNum)
        {
            case 0:array=ctx.getResources().getStringArray(R.array.IndoorGames);
                list=new ArrayList<Boolean>(Collections.nCopies(ctx.getResources().getStringArray(R.array.IndoorGames).length,false));
                break;
            case 1:array=ctx.getResources().getStringArray(R.array.OutdoorGames);
                list=new ArrayList<Boolean>(Collections.nCopies(ctx.getResources().getStringArray(R.array.OutdoorGames).length,false));
                break;
            case 2:array=ctx.getResources().getStringArray(R.array.Instruments);
                list=new ArrayList<Boolean>(Collections.nCopies(ctx.getResources().getStringArray(R.array.Instruments).length,false));
                break;
            case 3:array=ctx.getResources().getStringArray(R.array.Dance);
                list=new ArrayList<Boolean>(Collections.nCopies(ctx.getResources().getStringArray(R.array.Dance).length,false));
                break;
            case 4:array=ctx.getResources().getStringArray(R.array.Music);
                list=new ArrayList<Boolean>(Collections.nCopies(ctx.getResources().getStringArray(R.array.Music).length,false));
                break;
            case 5:array=ctx.getResources().getStringArray(R.array.singing);
                list=new ArrayList<Boolean>(Collections.nCopies(ctx.getResources().getStringArray(R.array.singing).length,false));
                break;

        }
    }

    @Override
    public hobbyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(ctx).inflate(R.layout.hobby_item,parent,false);
        return new hobbyHolder(v);
    }

    @Override
    public void onBindViewHolder(hobbyHolder holder, final int position) {
        holder.hobbyItem.setText(array[position]);
        holder.hobbyItem.setChecked(list.get(position));
        holder.hobbyItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                list.set(position,isChecked);
            }
        });
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
        reference.setValue(list)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ctx,"Changes Saved Successfully",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ctx,"Failed to update",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
