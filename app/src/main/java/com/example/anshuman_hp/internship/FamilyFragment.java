package com.example.anshuman_hp.internship;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Anshuman-HP on 12-08-2017.
 */

public class FamilyFragment extends Fragment {
    //ImageView familyBackgroundImage;
    RecyclerView familyMembers;
    String[] relations;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseRecyclerAdapter<FamilyMember, FamilyMemberHolder> adapter;
    ArrayAdapter relationsSpinnerAdapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public static FamilyFragment newInstance() {
        FamilyFragment fragment = new FamilyFragment();
        return fragment;
    }
    public FamilyFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.family_details, container, false);
        //familyBackgroundImage = (ImageView) v.findViewById(R.id.familyBackgroungImage);
        familyMembers = (RecyclerView) v.findViewById(R.id.familyMembersrecyclerView);
        relations = getResources().getStringArray(R.array.Relation);
        relationsSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.Relation, android.R.layout.simple_spinner_dropdown_item);
        relationsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        familyMembers.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        DatabaseReference ref = database.getReference(auth.getCurrentUser().getUid()).child("Family");
        adapter = new FirebaseRecyclerAdapter<FamilyMember, FamilyMemberHolder>
                (FamilyMember.class,
                        R.layout.family_member,
                        FamilyMemberHolder.class,
                        ref) {
            @Override
            protected void populateViewHolder(FamilyMemberHolder viewHolder, FamilyMember model, int position) {
                viewHolder.memberName.setText(model.getMemberName());
                viewHolder.memRelation.setAdapter(relationsSpinnerAdapter);
                viewHolder.memRelation.setSelection(Integer.valueOf(model.getMemberRelation()));
                viewHolder.emailId.setText(model.getEmail());
                viewHolder.phoneNumber.setText(model.getPhoneNumber());
                Glide.with(getActivity().getApplicationContext())
                        .load(model.getMemberPhotoUrl())
                        .into(viewHolder.memPhoto);

            }
            @Override
            public int getItemViewType(int position) {
                return super.getItemViewType(position);
            }
        };
        familyMembers.setAdapter(adapter);
        return v;
    }

    public View.OnClickListener listener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), addFamily.class));
            }
        };
        return listener;


    }
}
