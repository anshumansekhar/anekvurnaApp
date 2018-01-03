package com.cognichamp.CogniChamp;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Anshuman-HP on 17-10-2017.
 */

public class generatedReportFragment extends Fragment {
    private static final int EXTERNAL_STORAGE_PERMISSION = 456;
    TextView schoolName;
    TextView studentName;
    TextView classNameText;
    TextView schoolAddress;
    // TextView testsList;
    RecyclerView subjectList;
    int i;
    File pdfDir;
    ImageButton saveReport;
    ImageView schoolLogo;
    Bitmap screen;
    String test;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    ReportCardAdapter adapter;

    String returnString = "";

    School school;


    View v;


    private static void addImage(Document document, byte[] byteArray) {
        Image image = null;
        try {
            image = Image.getInstance(byteArray);
        } catch (BadElementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            document.add(image);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmapFromView(Activity activity, View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        view.draw(canvas);


        return returnedBitmap;

    }

    public static void detailsNotAvailable() {
        //TODO when details not available
        Log.e("sfhd", "details not available");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.generated_report_card, container, false);
        v.setDrawingCacheEnabled(true);
        schoolName = (TextView) v.findViewById(R.id.schoolNameReport);
        studentName = (TextView) v.findViewById(R.id.studentNameReport);
        classNameText = (TextView) v.findViewById(R.id.classReport);
        schoolAddress = (TextView) v.findViewById(R.id.schoolAddressReport);
        saveReport = (ImageButton) v.findViewById(R.id.saveReport);
        schoolLogo = (ImageView) v.findViewById(R.id.schoolLogo);
        //testsList = (TextView) v.findViewById(R.id.subjectsList);
        subjectList = (RecyclerView) v.findViewById(R.id.subjectsList);
        subjectList.setLayoutManager(new LinearLayoutManager(getActivity()));
        v.setDrawingCacheEnabled(true);

        adapter = new ReportCardAdapter(getActivity());
        subjectList.setAdapter(adapter);
        adapter.list.clear();

        saveReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.print(pdfDir.getAbsolutePath());
                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE);
                int permissionCheck1 = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED && permissionCheck1 == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        pdfDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "CogniChamp");
                    } else {
                        pdfDir = new File(Environment.getExternalStorageDirectory() + "/Documents/CogniChamp");
                    }
                    if (!pdfDir.exists()) {
                        pdfDir.mkdirs();
                        pdfDir.setWritable(true);
                        pdfDir.setReadable(true);
                        Log.e("skgj", pdfDir.getAbsolutePath());
                    } else {
                        Log.e("skgj", "Skipping making directory");
                    }
                    createPDfFile();
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            EXTERNAL_STORAGE_PERMISSION);
                }
            }
        });
        firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("UserProfile")
                .child("name")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        studentName.setText(dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        classNameText.setText(EducationFragment.className);

        firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("SchoolDetails")
                .child(EducationFragment.className)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            school = dataSnapshot.getValue(School.class);
                            schoolName.setText(school.getSchoolName());
                            schoolAddress.setText(school.getSchoolAddress());
                            Glide.with(getActivity())
                                    .load(school.getSchoolLogo())
                                    .into(schoolLogo);
                            adapter.list.clear();
                            fillTheList();
                            adapter.notifyDataSetChanged();
                            //testsList.setText(getTests());
                        } else {
                            detailsNotAvailable();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return v;
    }

    public void update() {
        if (isAdded()) {
            firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                    .child("UserProfile")
                    .child("name")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            studentName.setText(dataSnapshot.getValue().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            classNameText.setText(EducationFragment.className);
            firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                    .child("SchoolDetails")
                    .child(EducationFragment.className)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                school = dataSnapshot.getValue(School.class);
                                schoolName.setText(school.getSchoolName());
                                schoolAddress.setText(school.getSchoolAddress());
                                adapter.list.clear();
                                fillTheList();
                                adapter.notifyDataSetChanged();
                                //testsList.setText(getTests());
                            } else {
                                detailsNotAvailable();
                                adapter.list.clear();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

    }

    private void createPDfFile() {
        screen = getBitmapFromView(getActivity(), getActivity().getWindow().findViewById(R.id.containerLayout));
        File pdfFile = new File(pdfDir, EducationFragment.className + ".pdf");
        Image image;
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            screen.compress(Bitmap.CompressFormat.PNG, 100, stream);
            image = Image.getInstance(stream.toByteArray());
            image.setAbsolutePosition(0, 0);
            Document document = new Document(image);
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
            document.add(image);
            byte[] byteArray = stream.toByteArray();
            //addImage(document, byteArray);
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View layout = inflater.inflate(R.layout.toastlayout,
                    (ViewGroup) getActivity().findViewById(R.id.toastContainer));

            TextView text = (TextView) layout.findViewById(R.id.toastText);
            text.setText("Report Card Saved in " + pdfFile.getPath());

            Toast toast = new Toast(getActivity());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pdfDir.mkdir();
                createPDfFile();
            }
        }
    }

    public void fillTheList() {
        final DatabaseReference databaseReference;
        adapter.list.clear();
        if (EducationFragment.className.contains("Age")) {
            databaseReference = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                    .child("ClassDetails")
                    .child(EducationFragment.className)
                    .child("Grades");

        } else {
            databaseReference = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                    .child("ClassDetails")
                    .child(EducationFragment.className)
                    .child("tests");
        }
        if (EducationFragment.className.contains("10") || EducationFragment.className.contains("12")) {
            final String[] tests = getActivity().getResources().getStringArray(R.array.testsBoard);
            for (int i = 0; i < tests.length; i++) {
                final String test = tests[i];
                databaseReference.child(tests[i])
                        .child("percentage")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    if (dataSnapshot.getValue().toString().equals("0.0") || dataSnapshot.getValue().toString().equals("NaN")) {
                                        //TODO not to be taken

                                    } else {
                                        String percentage = dataSnapshot.getValue().toString();
                                        adapter.addToMap(test, percentage);
                                        adapter.notifyDataSetChanged();
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

            }
        } else if (EducationFragment.className.contains("Age")) {
            final String[] tests = getActivity().getResources().getStringArray(R.array.gradesTestType);
            for (int i = 0; i < tests.length; i++) {
                final String test = tests[i];
                databaseReference.child(tests[i])
                        .child("topics")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    adapter.addToMap(test, "Grade");
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
            }
        } else {
            Log.e("sfb", "normal not boards");
            final String[] tests = getActivity().getResources().getStringArray(R.array.testsNormal);
            for (int i = 0; i < tests.length; i++) {
                final String test = tests[i];
                databaseReference.child(tests[i])
                        .child("percentage")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    if (dataSnapshot.getValue().toString().equals("0.0") || dataSnapshot.getValue().toString().equals("NaN")) {
                                        //TODO not to be taken
                                    } else {
                                        String percentage = dataSnapshot.getValue().toString();
                                        adapter.addToMap(test, percentage);
                                        adapter.notifyDataSetChanged();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        }
    }

}
