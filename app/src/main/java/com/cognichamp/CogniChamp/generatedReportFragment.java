package com.cognichamp.CogniChamp;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    TextView testsList;
    int i;
    File pdfDir;
    ImageButton saveReport;
    Bitmap screen;
    String test;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    String returnString = "";


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

    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
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
        View v = inflater.inflate(R.layout.generated_report_card, container, false);
        schoolName = (TextView) v.findViewById(R.id.schoolNameReport);
        studentName = (TextView) v.findViewById(R.id.studentNameReport);
        classNameText = (TextView) v.findViewById(R.id.classReport);
        schoolAddress = (TextView) v.findViewById(R.id.schoolAddressReport);
        saveReport = (ImageButton) v.findViewById(R.id.saveReport);
        testsList = (TextView) v.findViewById(R.id.subjectsList);
        System.out.print("f" + getTests());
        Log.e("fsh", " jgg" + getTests());
        v.setDrawingCacheEnabled(true);

        saveReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print(pdfDir.getAbsolutePath());
                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE);
                int permissionCheck1 = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED && permissionCheck1 == PackageManager.PERMISSION_GRANTED) {
                    pdfDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "CogniChamp");
                    if (!pdfDir.exists()) {
                        pdfDir.mkdirs();
                        pdfDir.setWritable(true);
                        pdfDir.setReadable(true);
                        System.out.print(pdfDir.getAbsolutePath());
                    } else {
                        System.out.print("Skipping making directory");
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
                        studentName.setText("Student Name:" + dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        classNameText.setText(EducationFragment.className);

        firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("SchoolDetails")
                .child(EducationFragment.className)
                .child("schoolName")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            schoolName.setText("School Name:" + dataSnapshot.getValue().toString());
                            testsList.setText(getTests());
                            System.out.print("f" + getTests());
                        } else {
                            detailsNotAvailable();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("SchoolDetails")
                .child(EducationFragment.className)
                .child("schoolAddress")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            schoolAddress.setText("School Address:" + dataSnapshot.getValue().toString());
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

    private void createPDfFile() {
        screen = getBitmapFromView(getActivity().getWindow().findViewById(R.id.scrollView));
        File pdfFile = new File(pdfDir, EducationFragment.className + ".pdf");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            screen.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            addImage(document, byteArray);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
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

    public String getTests() {
        String toReturn = "";
        if (EducationFragment.className.contains("10") || EducationFragment.className.contains("12")) {
            String[] tests = getActivity().getResources().getStringArray(R.array.testsBoard);
            for (int i = 0; i < tests.length; i++) {
                toReturn = toReturn.concat(getSubjectsList(tests[i]));
            }
        } else {
            Log.e("sfb", "normal not boards");
            String[] tests = getActivity().getResources().getStringArray(R.array.testsNormal);
            for (int i = 0; i < tests.length; i++) {
                toReturn = toReturn.concat(getSubjectsList(tests[i]));
            }

        }
        return toReturn;
    }

    public String getSubjectsList(String testType) {
        Log.e("Xnv", testType);
        Log.e("sfnd", "inside getSubjects");
        returnString = "";
        test = testType;
        firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("ClassDetails")
                .child(EducationFragment.className)
                .child("tests")
                .child("tests")
                .child(testType)
                .child("subjects")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                            subject subject = dataSnapshot.child("" + i).getValue(com.cognichamp.CogniChamp.subject.class);
                            if (!returnString.contains(subject.getSubjectName())) {
                                if (!returnString.contains(test)) {
                                    returnString = "\n" + test + "\n";
                                }
                                Log.e("ncg", test + subject.getSubjectName().toString());
                                returnString = returnString.concat(subject.getSubjectName() + ":" + subject.getSubMarks() + "/" + subject.getTotalMarks() + "\n");
                                testsList.setText(returnString);
                            } else {
                                Log.e("amf", test + subject.getSubjectName().toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("ClassDetails")
                .child(EducationFragment.className)
                .child("tests")
                .child("tests")
                .child(testType)
                .child("percentage")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            returnString = returnString.concat("Percentage:" + dataSnapshot.getValue());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return returnString;

    }

}
