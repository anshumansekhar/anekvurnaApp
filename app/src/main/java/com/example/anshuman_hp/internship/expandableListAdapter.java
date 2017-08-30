package com.example.anshuman_hp.internship;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Anshuman-HP on 28-08-2017.
 */

public class expandableListAdapter extends BaseExpandableListAdapter {

    Context ctx;
    HashMap<String,List<String>> Children=new HashMap<>();
    List headerNames=new ArrayList();

    public expandableListAdapter(Context ctx, HashMap<String, List<String>> children, List headerNames) {
        this.ctx = ctx;
        Children = children;
        this.headerNames = headerNames;
    }

    @Override
    public int getGroupCount() {
        return headerNames.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return Children.get(headerNames.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headerNames.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return Children.get(headerNames.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String header=getGroup(groupPosition).toString();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_header, null);
        }
        TextView ListHeader = (TextView) convertView.findViewById(R.id.headerText);
        ListHeader.setTypeface(null, Typeface.BOLD);
        ListHeader.setText(header);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.listItem);
        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
