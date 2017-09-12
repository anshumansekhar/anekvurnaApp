package com.example.anshuman_hp.internship;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AnotherActivity extends AppCompatActivity {

    subjectListFragment subjectListFragment=new subjectListFragment();
    TopicListFragment topicListFragment=new TopicListFragment();
    VideosFragment videosFragment=new VideosFragment();


    Bundle classNameBundle=new Bundle();

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    DatabaseReference ref;
    FirebaseRecyclerAdapter<video,videoHolder> adapter;
    ActionBar actionBar;
    Spinner selectSubject;
    Spinner selectTopic;
    TextView groupNameText;
    String groupName;

    String className;
    String selectedSubject;
    String selectedTopic;

    ArrayList subjectsList=new ArrayList();
    HashMap<String,String> topicsList=new HashMap<>();
    ArrayAdapter spinnerAdapter;
    ArrayAdapter topicsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);


        database.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("UserProfile")
                .child("presentClass")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            className = dataSnapshot.getValue().toString();
                        } else
                            className = "1";
                        actionBar = getSupportActionBar();
                        actionBar.setTitle("Class " + className);
                        classNameBundle.putString("ClassNumber", className);
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
                        subjectListFragment.setArguments(classNameBundle);
                        fragmentTransaction.replace(R.id.videoFrame, subjectListFragment);
                        fragmentTransaction.commit();
                        fragmentTransaction.addToBackStack(null);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.videoactivitymenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.favoritesMenu)
        {
            startActivity(new Intent(AnotherActivity.this,FavoriteVideosActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public  void  changeFragmentWithTopic(Bundle b)
    {
        topicListFragment.setArguments(b);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.videoFrame,topicListFragment);
        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);

    }
    public  void  changeFragmentWithVideo(Bundle b)
    {
        videosFragment.setArguments(b);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.videoFrame,videosFragment);
        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);
    }

}
