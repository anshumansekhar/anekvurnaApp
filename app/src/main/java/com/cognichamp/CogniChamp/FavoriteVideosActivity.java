package com.cognichamp.CogniChamp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FavoriteVideosActivity extends Fragment {
    Spinner selectClassVideo;
    emptyFragment emptyFragment=new emptyFragment();
    Bundle classNameBundle = new Bundle();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference ref;
    String className = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_another, container, false);
        ((NavigationDrawer)getActivity()).actionBar.setTitle("My Favourites");
        selectClassVideo = (Spinner) v.findViewById(R.id.selectClassVideo);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.ClassWithStream, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectClassVideo.setAdapter(arrayAdapter);
        database.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("UserProfile")
                .child("presentClass")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            className = dataSnapshot.getValue().toString();
                            selectClassVideo.setSelection(Integer.valueOf(className));
                            if (className.equals("11")) {
                                className = "Class-" + className + "(Arts)";
                            } else if (className.equals("0")) {
                                className = "Age (0-1) yrs";
                            } else if (className.equals("1")) {
                                className = "Age (1-2) yrs";
                            } else if (className.equals("2")) {
                                className = "Age (2-3) yrs";
                            } else if (className.equals("3")) {
                                className = "Age (3-4) yrs";
                            } else if (className.equals("4")) {
                                className = "Age (4-5) yrs";
                            } else if (className.equals("5")) {
                                className = "Age (5-6) yrs";
                            } else if (className.equals("12")) {
                                className = "Class-" + "11" + "(Commerce)";
                            } else if (className.equals("13")) {
                                className = "Class-" + "11" + "(Science)";
                            } else if (className.equals("14")) {
                                className = "Class-" + "12" + "(Arts)";
                            } else if (className.equals("15")) {
                                className = "Class-" + "12" + "(Commerce)";
                            } else if (className.equals("16")) {
                                className = "Class-" + "12" + "(Science)";
                            } else {
                                className = "Class-" + (Integer.valueOf(className) - 6);
                            }
                            classNameBundle.putString("ClassNumber", className);
                            classNameBundle.putString("where","Favorites");
                            subjectListFragment subjectListFragment = new subjectListFragment();
                            subjectListFragment.setArguments(classNameBundle);
                            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.videoFrame, subjectListFragment);
                            fragmentTransaction.commit();
                            fragmentTransaction.addToBackStack(null);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        selectClassVideo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                className = getActivity().getResources().getStringArray(R.array.ClassWithStream)[position];
                classNameBundle.putString("ClassNumber", className);
                classNameBundle.putString("where","Favorites");
                subjectListFragment subjectListFragment = new subjectListFragment();
                subjectListFragment.setArguments(classNameBundle);
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.videoFrame, subjectListFragment);
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return v;
    }
    public void setEmptyFragment(){
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.videoFrame, emptyFragment);
        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);
    }

    public void changeFragmentWithTopic(Bundle b) {
        TopicListFragment topicListFragment=new TopicListFragment();
        topicListFragment.setArguments(b);
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.videoFrame, topicListFragment);
        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);

    }

    public void changeFragmentWithVideo(Bundle b) {
        VideosFragment videosFragment=new VideosFragment();
        videosFragment.setArguments(b);
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.videoFrame, videosFragment);
        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);
    }
    public void changeFragmentWithVideoOnly(Bundle b){
        VideosFragment videosFragment=new VideosFragment();
        videosFragment.setArguments(b);
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.videoFrame, videosFragment);
        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);
    }
}