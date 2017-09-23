package com.example.anshuman_hp.internship;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
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
    ListView familyMembers;
    String[] relations;
    TextView emptyView;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    ArrayAdapter relationsSpinnerAdapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    FirebaseListAdapter<FamilyMember> listAdapter;

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
        familyMembers = (ListView) v.findViewById(R.id.familyMembersrecyclerView);
        emptyView=(TextView)v.findViewById(android.R.id.empty);
        familyMembers.setEmptyView(emptyView);
        relations = getResources().getStringArray(R.array.Relation);
        relationsSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.Relation, android.R.layout.simple_spinner_dropdown_item);
        relationsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DatabaseReference ref = database.getReference(auth.getCurrentUser().getUid()).child("Family");
        listAdapter=new FirebaseListAdapter<FamilyMember>(getActivity(),
                FamilyMember.class,
                R.layout.family_member,
                ref) {
            @Override
            protected void populateView(View v, FamilyMember model, int position) {
                ((EditText)v.findViewById(R.id.familyMemberName)).setText(model.getMemberName());
                ((EditText)v.findViewById(R.id.EmailIdFamily)).setText(model.getEmail());
                ((EditText)v.findViewById(R.id.PhoneNumberFamily)).setText(model.getPhoneNumber());
                ((Spinner)v.findViewById(R.id.familyMemberRelation)).setAdapter(relationsSpinnerAdapter);
                ((Spinner)v.findViewById(R.id.familyMemberRelation)).setSelection(Integer.valueOf(model.getMemberRelation()));
                        Glide.with(getActivity())
                        .load(model.getMemberPhotoUrl())
                        .into((ImageView)v.findViewById(R.id.photoFamily));
            }
        };
        familyMembers.setAdapter(listAdapter);

        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), addFamily.class));
            }
        });
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
