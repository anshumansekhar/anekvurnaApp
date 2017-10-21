package com.cognichamp.CogniChamp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    TextView schoolName;
    TextView studentName;
    TextView classNameText;
    TextView schoolAddress;
    RecyclerView testsRecycler;
    int i;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    reportCardAdapter adapter;

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
        // image.scaleAbsolute(150f, 150f);
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.generated_report_card, container, false);
        schoolName = (TextView) v.findViewById(R.id.schoolNameReport);
        studentName = (TextView) v.findViewById(R.id.studentNameReport);
        classNameText = (TextView) v.findViewById(R.id.classReport);
        testsRecycler = (RecyclerView) v.findViewById(R.id.testsReport);
        schoolAddress = (TextView) v.findViewById(R.id.schoolAddressReport);
        testsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
//        adapter=new reportCardAdapter(toAdd,getActivity());
        testsRecycler.setAdapter(adapter);
        v.setDrawingCacheEnabled(true);
        Bitmap screen = getBitmapFromView(getActivity().getWindow().findViewById(R.id.scrollView));


        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {

        }
        File pdfDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "MyApp");
        if (!pdfDir.exists()) {
            pdfDir.mkdir();
        }


        File pdfFile = new File(pdfDir, "myPdfFile.pdf");

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
        firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("SchoolDetails")
                .child(EducationFragment.className)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            schoolAddress.setText("School Address:" + dataSnapshot.child("schoolAddress").getValue());
                            schoolName.setText("School Name:" + dataSnapshot.child("schoolName").getValue());
                            classNameText.setText(EducationFragment.className);
                            getStudentName();
                            getTests();
                        } else {
                            //TODO no details available
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        return v;
    }

    void getStudentName() {
        firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                .child("UserProfile")
                .child("name")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        studentName.setText("Name:" + dataSnapshot.getValue().toString());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    void getTests() {
        DatabaseReference reference = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                .child(EducationFragment.className)
                .child("tests");
        if (EducationFragment.className.contains("10") || EducationFragment.className.contains("12")) {
            final String[] tests = getActivity().getResources().getStringArray(R.array.testsBoard);
            for (i = 0; i < tests.length; i++) {
                reference.child(tests[i])
                        .child("subjects")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    long ght = dataSnapshot.getChildrenCount();
                                    while (ght != 0) {


                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        /*.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if(Float.parseFloat(dataSnapshot.child("totalMarks").toString())==0.0){
                                    toBeAdded.put(tests[i],false);
                                };
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                if(Float.parseFloat(dataSnapshot.child("totalMarks").toString())==0.0){
                                    toBeAdded.put(tests[i],false);
                                };
                                adapter.notifyDataSetChanged();

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
                if(toBeAdded.get(tests[i])){
                    toAdd.add(tests[i]);
                    adapter.notifyDataSetChanged();
                }

            }


        }else {
            final String[] tests=getActivity().getResources().getStringArray(R.array.testsNormal);
            for( i=0;i<tests.length;i++) {
                reference.child(tests[i])
                        .child("subjects")
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if (Float.parseFloat(dataSnapshot.child("totalMarks").toString()) == 0.0) {
                                    toBeAdded.put(tests[i], false);
                                }
                                ;
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                if (Float.parseFloat(dataSnapshot.child("totalMarks").toString()) == 0.0) {
                                    toBeAdded.put(tests[i], false);
                                }
                                ;

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
                if(toBeAdded.get(tests[i])){
                    toAdd.add(tests[i]);
                    adapter.notifyDataSetChanged();
                }
            }*/

            }

        }
    }
}
