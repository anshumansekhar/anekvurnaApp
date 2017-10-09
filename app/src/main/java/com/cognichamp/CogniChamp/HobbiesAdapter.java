package com.cognichamp.CogniChamp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anshuman-HP on 26-08-2017.
 */

public class HobbiesAdapter extends RecyclerView.Adapter<HobbyHolder> {
    Context ctx;
    List<Boolean> list=new ArrayList<>();
    String[] array;

    public HobbiesAdapter(Context ctx, List list, int positin) {
        this.ctx = ctx;
        this.list=list;
        switch (positin)
        {
            case 1:array=ctx.getResources().getStringArray(R.array.IndoorGames);
                break;
            case 2:array=ctx.getResources().getStringArray(R.array.OutdoorGames);
                break;
            case 3:array=ctx.getResources().getStringArray(R.array.Instruments);
                break;
            case 4:array=ctx.getResources().getStringArray(R.array.Dance);
                break;
            case 5:array=ctx.getResources().getStringArray(R.array.Music);
                break;
            case 6:array=ctx.getResources().getStringArray(R.array.singing);
                break;

        }
    }

    @Override
    public HobbyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(ctx).inflate(R.layout.hobby_item,parent,false);
        return new HobbyHolder(v);
    }

    @Override
    public void onBindViewHolder(HobbyHolder holder, int position) {
        holder.hobby.setChecked(list.get(position));
        holder.hobby.setText(array[position]);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
