package com.cognichamp.CogniChamp;

import android.R.layout;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.cognichamp.CogniChamp.R.array;
import com.cognichamp.CogniChamp.R.id;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class AnotherActivity extends Fragment {
    Spinner selectClassVideo;


    Bundle classNameBundle = new Bundle();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference ref;
    String className = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_another, container, false);
        this.selectClassVideo = (Spinner) v.findViewById(id.selectClassVideo);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this.getActivity(), array.ClassWithStream, R.layout.spinner_item);
        final ArrayList classess = new ArrayList(Arrays.asList(getActivity().getResources().getStringArray(array.ClassWithStream)));
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        this.selectClassVideo.setAdapter(arrayAdapter);
        this.database.getReference(this.firebaseAuth.getCurrentUser().getUid())
                .child("UserProfile")
                .child("presentClass")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            AnotherActivity.this.className = dataSnapshot.getValue().toString();
                            AnotherActivity.this.selectClassVideo.setSelection(classess.indexOf(className));
//                            if (AnotherActivity.this.className.equals("11")) {
//                                AnotherActivity.this.className = "Class-" + AnotherActivity.this.className + "(Arts)";
//                            } else if (AnotherActivity.this.className.equals("0")) {
//                                AnotherActivity.this.className = "Age (0-1) yrs";
//                            } else if (AnotherActivity.this.className.equals("1")) {
//                                AnotherActivity.this.className = "Age (1-2) yrs";
//                            } else if (AnotherActivity.this.className.equals("2")) {
//                                AnotherActivity.this.className = "Age (2-3) yrs";
//                            } else if (AnotherActivity.this.className.equals("3")) {
//                                AnotherActivity.this.className = "Age (3-4) yrs";
//                            } else if (AnotherActivity.this.className.equals("4")) {
//                                AnotherActivity.this.className = "Age (4-5) yrs";
//                            } else if (AnotherActivity.this.className.equals("5")) {
//                                AnotherActivity.this.className = "Age (5-6) yrs";
//                            } else if (AnotherActivity.this.className.equals("12")) {
//                                AnotherActivity.this.className = "Class-" + "11" + "(Commerce)";
//                            } else if (AnotherActivity.this.className.equals("13")) {
//                                AnotherActivity.this.className = "Class-" + "11" + "(Science)";
//                            } else if (AnotherActivity.this.className.equals("14")) {
//                                AnotherActivity.this.className = "Class-" + "12" + "(Arts)";
//                            } else if (AnotherActivity.this.className.equals("15")) {
//                                AnotherActivity.this.className = "Class-" + "12" + "(Commerce)";
//                            } else if (AnotherActivity.this.className.equals("16")) {
//                                AnotherActivity.this.className = "Class-" + "12" + "(Science)";
//                            }
//                            else{
//                                AnotherActivity.this.className = "Class-" + (Integer.valueOf(AnotherActivity.this.className) - 6);
//                            }
                            AnotherActivity.this.classNameBundle.putString("ClassNumber", AnotherActivity.this.className);
                            AnotherActivity.this.classNameBundle.putString("where", "ClassDetails");
                            subjectListFragment subjectListFragment = new subjectListFragment();
                            subjectListFragment.setArguments(AnotherActivity.this.classNameBundle);
                            if (AnotherActivity.this.isAdded()) {
                                FragmentTransaction fragmentTransaction = AnotherActivity.this.getChildFragmentManager().beginTransaction();
                                fragmentTransaction.replace(id.videoFrame, subjectListFragment);
                                fragmentTransaction.commit();
                                fragmentTransaction.addToBackStack(null);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        this.selectClassVideo.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AnotherActivity.this.className = AnotherActivity.this.getActivity().getResources().getStringArray(array.ClassWithStream)[position];
                AnotherActivity.this.classNameBundle.putString("ClassNumber", AnotherActivity.this.className);
                AnotherActivity.this.classNameBundle.putString("where", "ClassDetails");
                subjectListFragment subjectListFragment = new subjectListFragment();
                subjectListFragment.setArguments(AnotherActivity.this.classNameBundle);
                if (AnotherActivity.this.isAdded()) {
                    FragmentTransaction fragmentTransaction = AnotherActivity.this.getChildFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.videoFrame, subjectListFragment);
                    fragmentTransaction.commit();
                    fragmentTransaction.addToBackStack(null);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return v;
    }

    public void changeFragmentWithTopic(Bundle b) {
        TopicListFragment topicListFragment=new TopicListFragment();
        topicListFragment.setArguments(b);
        if (this.isAdded()) {
            FragmentTransaction fragmentTransaction = this.getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(id.videoFrame, topicListFragment);
            fragmentTransaction.commit();
            fragmentTransaction.addToBackStack(null);
        }

    }

    public void changeFragmentWithVideo(Bundle b) {
        VideosFragment videosFragment=new VideosFragment();
        videosFragment.setArguments(b);
        if (this.isAdded()) {
            FragmentTransaction fragmentTransaction = this.getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(id.videoFrame, videosFragment);
            fragmentTransaction.commit();
            fragmentTransaction.addToBackStack(null);
        }
    }
    public void changeFragmentWithVideoOnly(Bundle b){
        VideosFragment videosFragment=new VideosFragment();
        videosFragment.setArguments(b);
        if (this.isAdded()) {
            FragmentTransaction fragmentTransaction = this.getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(id.videoFrame, videosFragment);
            fragmentTransaction.commit();
            fragmentTransaction.addToBackStack(null);
        }
    }

    public void setEmptyFragment() {
        Bundle b = new Bundle();
        b.putBoolean("Video", true);
        emptyFragment emptyFragment = new emptyFragment();
        emptyFragment.setArguments(b);
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.videoFrame, emptyFragment);
        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);
    }
}
