package com.example.anshuman_hp.internship;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Anshuman-HP on 09-09-2017.
 */

public class selectSubjectSpinnerAdapter extends BaseAdapter {
    Context ctx;
    ArrayList<SubjectClass> subjectClasses=new ArrayList<>();

    public selectSubjectSpinnerAdapter(Context ctx, ArrayList<SubjectClass> subjectClasses) {
        this.ctx = ctx;
        this.subjectClasses = subjectClasses;
    }

    @Override
    public int getCount() {
        return subjectClasses.size();
    }

    @Override
    public Object getItem(int position) {
        return subjectClasses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv=new TextView(ctx);
        tv.setTextColor(ctx.getResources().getColor(R.color.com_facebook_blue));
        tv.setText(subjectClasses.get(position).getSubjectName());
        return tv;
    }
}
