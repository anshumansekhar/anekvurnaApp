package com.example.anshuman_hp.internship;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Anshuman-HP on 12-08-2017.
 */

public class HobbiesFragment extends Fragment {
    RecyclerView hobbies;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseUser user=firebaseAuth.getCurrentUser();
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseRecyclerAdapter<hobby,HobbyHolder> adapter;
    public static HobbiesFragment newInstance() {
        HobbiesFragment fragment = new HobbiesFragment();
        return fragment;
    }
    public HobbiesFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.hobbies,container,false);
        hobbies=(RecyclerView)v.findViewById(R.id.hobbiesRecyclerView);
        DatabaseReference ref=database.getReference("Hobbies").child(user.getUid());
        adapter=new FirebaseRecyclerAdapter<hobby, HobbyHolder>(hobby.class
        ,R.layout.hobby_item
        ,HobbyHolder.class
        ,ref) {
            @Override
            protected void populateViewHolder(final HobbyHolder viewHolder, hobby model, int position) {
                viewHolder.hobbyName.setText(model.getHobbyName());
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity().getApplicationContext());
                        builder.setMessage("Are You Sure?");
                        builder.setTitle("Confirmation");
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.getRef(viewHolder.getAdapterPosition()).removeValue();
                                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                            }
                        });
                        AlertDialog dialog=builder.create();
                        dialog.show();
                        return false;
                    }
                });
            }
        };
        hobbies.setLayoutManager(new LinearLayoutManager(getActivity()));
        hobbies.setAdapter(adapter);
        return v;
    }
    public View.OnClickListener listener()
    {
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        };
        return listener;
    }
    public void showDialog()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Add a New Intrest/Hobby");
        final EditText hobby=new EditText(getActivity());
        builder.setView(hobby);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.getReference("Hobbies")
                        .child(firebaseAuth.getCurrentUser().getUid())
                        .push()
                        .setValue(new hobby(hobby.getText().toString()));
            }
        });
        AlertDialog dialog =builder.create();
        dialog.show();

    }
}
