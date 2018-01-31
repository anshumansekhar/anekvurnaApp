package com.cognichamp.CogniChamp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Created by Anshuman-HP on 03-09-2017.
 */

public class marksFragments extends Fragment {
    public static HashMap<String, subject> Subjects = new HashMap<>();
    public static HashMap<String, Grade> grades = new HashMap<>();
    public static DatabaseReference testRef;
    public static String[] tests;
    static RecyclerView subjectsList;
    static FirebaseRecyclerAdapter<subject, subjectHolder> Adapter;
    Spinner testType;
    Button save,addnewSubject;
    TextView percentage, subjectsHeading, testTypeTextView;
    ArrayAdapter testAdapter;
    String testTypeText;
    FirebaseRecyclerAdapter<subject, subjectHolder> recyclerAdapter;
    AdapterView.OnItemSelectedListener spinnerListener;


    public static void fillHashmap(DatabaseReference ref) {
        Subjects.clear();
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Subjects.put(dataSnapshot.getKey(), dataSnapshot.getValue(subject.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Subjects.put(dataSnapshot.getKey(), dataSnapshot.getValue(subject.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static void fillHashMapGrades(DatabaseReference ref) {
        grades.clear();
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                grades.put(dataSnapshot.getKey(), dataSnapshot.getValue(Grade.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                grades.put(dataSnapshot.getKey(), dataSnapshot.getValue(Grade.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.new_education_details,container,false);
        testType=(Spinner)v.findViewById(R.id.selectTestType);
        subjectsList=(RecyclerView)v.findViewById(R.id.subjectRecyclerEducation);
        subjectsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        save=(Button)v.findViewById(R.id.save);
        addnewSubject=(Button)v.findViewById(R.id.addnewSubject);
        percentage=(TextView)v.findViewById(R.id.percentageEducation);
        subjectsHeading = (TextView) v.findViewById(R.id.subjectsHeading);
        testTypeTextView = (TextView) v.findViewById(R.id.testType);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges(testRef);
            }
        });

        return v;
    }

    public void setSpinnerAdapter() {
        if (isAdded()) {
            if (EducationFragment.className.equals("Class-10") || EducationFragment.className.equals("Class-12")) {
                spinnerListener = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        testRef = EducationFragment.classRef.child("tests").child(tests[position]);
                        testTypeText = tests[position];
                        testRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    testRef.setValue(new subjects());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        setUpRecyclerView(EducationFragment.classRef.child("tests").child(tests[position]).child("subjects"), getActivity());
                        EducationFragment.classRef.child("tests").child(tests[position]).child("percentage")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists())
                                            percentage.setText("Percentage:" + dataSnapshot.getValue().toString());
                                        else {
                                            percentage.setText("Percentage:");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                };
                testType.setOnItemSelectedListener(spinnerListener);
                addnewSubject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), AddNewSubject.class);
                        i.putExtra("Position", Subjects.size());
                        i.putExtra("Class", EducationFragment.classRef.toString());
                        i.putExtra("TestType", testTypeText);
                        i.putExtra("Ref", testRef.child("subjects").toString());
                        startActivity(i);
                    }
                });
                testAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.testsBoard, R.layout.spinner_item);
                testAdapter.notifyDataSetChanged();
                tests = getActivity().getResources().getStringArray(R.array.testsBoard);
                testAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                testType.setAdapter(testAdapter);
            } else if (EducationFragment.className.contains("Age")) {
                spinnerListener = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        testRef = EducationFragment.classRef.child("Grades").child(tests[position]);
                        testTypeText = tests[position];
                        testRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    //TODO no data available
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        setUpRecyclerViewForAge(EducationFragment.classRef.child("Grades").child(tests[position]).child("topics"), getActivity());


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                };
                testType.setOnItemSelectedListener(spinnerListener);
                addnewSubject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), AddNewSubject.class);
                        i.putExtra("Position", grades.size());
                        i.putExtra("Class", EducationFragment.classRef.toString());
                        i.putExtra("TestType", testTypeText);
                        i.putExtra("Ref", testRef.child("topics").toString());
                        startActivity(i);
                    }
                });
                testAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.gradesTestType, R.layout.spinner_item);
                testAdapter.notifyDataSetChanged();
                tests = getActivity().getResources().getStringArray(R.array.gradesTestType);
                testAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                testType.setAdapter(testAdapter);

            } else {
                spinnerListener = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        testRef = EducationFragment.classRef.child("tests").child(tests[position]);
                        testTypeText = tests[position];
                        testRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    testRef.setValue(new subjects());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        setUpRecyclerView(EducationFragment.classRef.child("tests").child(tests[position]).child("subjects"), getActivity());
                        EducationFragment.classRef.child("tests").child(tests[position]).child("percentage")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists())
                                            percentage.setText("Percentage:" + dataSnapshot.getValue().toString());
                                        else {
                                            percentage.setText("Percentage:");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                };
                testType.setOnItemSelectedListener(spinnerListener);
                testAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.testsNormal, R.layout.spinner_item);
                testAdapter.notifyDataSetChanged();
                tests = getActivity().getResources().getStringArray(R.array.testsNormal);
                testAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                testType.setAdapter(testAdapter);

            }
        }

    }

    public void setUpRecyclerView(final DatabaseReference ref, final Context ctx) {
        fillHashmap(ref);
        Adapter=new FirebaseRecyclerAdapter<subject, subjectHolder>
                (subject.class
                        , R.layout.subject_entry
                        , subjectHolder.class
                        , ref) {
            @Override
            protected void populateViewHolder(final subjectHolder viewHolder, final subject model, int position) {
                viewHolder.subjectMarks.setText(""+model.getSubMarks());
                viewHolder.totalMArks.setText(""+model.getTotalMarks());
                viewHolder.subjectName.setText(model.getSubjectName());
                viewHolder.subjectMarks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.subjectMarks.setText("");
                    }
                });
                viewHolder.totalMArks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.totalMArks.setText("");
                    }
                });
                final int pos=position;
                viewHolder.deleteSubject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder=new AlertDialog.Builder(ctx);
                        builder.setTitle("Confirmation");
                        builder.setMessage("Are you Sure to Delete the Subject");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ref.child(model.getSubjectName())
                                        .removeValue();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog dialog=builder.create();
                        dialog.show();
                    }
                });
                viewHolder.totalMArks.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(!s.toString().equals("")&&model.getTotalMarks()!=Float.parseFloat(s.toString())){
                            Subjects.get(model.getSubjectName()).setTotalMarks(Integer.parseInt(s.toString()));
                        } else if (s.toString().isEmpty() && model.getTotalMarks() != Float.parseFloat("0.0")) {
                            Subjects.get(model.getSubjectName()).setTotalMarks(Integer.parseInt("0.0"));
                        }

                    }
                });
                viewHolder.subjectMarks.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(!s.toString().equals("")&&model.getSubMarks()!=Float.parseFloat(s.toString())){
                            Subjects.get(model.getSubjectName()).setSubMarks(Integer.parseInt(s.toString()));
                        } else if (s.toString().isEmpty() && model.getSubMarks() != Float.parseFloat("0.0")) {
                            Subjects.get(model.getSubjectName()).setSubMarks(Integer.parseInt("0.0"));
                        }
                    }
                });
            }

        };
        subjectsList.setAdapter(Adapter);
    }

    public void saveChanges(DatabaseReference testReference){

        testReference.child("subjects").setValue(Subjects);
        testReference.child("percentage").setValue(""+calculatePercentage());
    }

    public float calculatePercentage() {
        int marks = Integer.parseInt("0");
        int totalMarks = Integer.parseInt("0");
        for (subject e : Subjects.values()) {
            marks=marks+e.getSubMarks();
            try {
                if (e.getTotalMarks() == 0) {
                    if (e.getSubMarks() != 0) {
                        throw new Exception("Max Marks can't be Zero ");
                    }
                } else if (e.getSubMarks() > e.getTotalMarks()) {
                    throw new Exception("Max Marks  less than Subject Marks");
                }
                totalMarks = totalMarks + e.getTotalMarks();
            } catch (Exception we) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View layout = inflater.inflate(R.layout.toastlayout,
                        (ViewGroup) getActivity().findViewById(R.id.toastContainer));

                TextView text = (TextView) layout.findViewById(R.id.toastText);
                text.setText(we.getMessage());

                Toast toast = new Toast(getActivity());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
                return new Float(0.0);
            }
        }
        return ((Float.valueOf(marks)) / (Float.valueOf(totalMarks)) * 100);
    }

    public void addSubjects() {
        EducationFragment.classRef.setValue(new ClassDetails());
    }

    public void setUpLayoutForAge() {
        testTypeTextView.setText("Select Group");
        percentage.setText("");
        addnewSubject.setText("Add");
        subjectsHeading.setText("Groups");
    }

    public void setUpForNonAge() {
        testTypeTextView.setText("Select TestType");
        percentage.setText("Percentage");
        addnewSubject.setText("Add New Subject");
        subjectsHeading.setText("Subjects");

    }

    public void setUpRecyclerViewForAge(final DatabaseReference ref, final Context ctx) {
        FirebaseRecyclerAdapter<Grade, GradeHolder> firebaseRecyclerAdapter;
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Grade, GradeHolder>
                (Grade.class,
                        R.layout.grade_entry,
                        GradeHolder.class,
                        ref) {
            @Override
            protected void populateViewHolder(final GradeHolder viewHolder, final Grade model, int position) {
                viewHolder.topicName.setText("" + model.getTopicName());
                viewHolder.gradeEntry.setText("" + model.getGrade());
                viewHolder.gradeEntry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.gradeEntry.setText("");
                    }
                });
                final int pos = position;
                viewHolder.deleteTopic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                        builder.setTitle("Confirmation");
                        builder.setMessage("Are you Sure to Delete the Subject");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ref.child(model.getTopicName())
                                        .removeValue();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
                viewHolder.gradeEntry.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!s.toString().equals("") && model.getGrade() != s.toString()) {
                            grades.get(model.getTopicName()).setGrade(s.toString());
                        } else if (s.toString().isEmpty() && model.getGrade() != "NA") {
                            grades.get(model.getTopicName()).setGrade("NA");
                        }
                    }
                });
            }
        };
        subjectsList.setAdapter(firebaseRecyclerAdapter);
    }


}


