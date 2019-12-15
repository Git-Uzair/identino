package com.tango.identino;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.tango.identino.model.Attendance_record;
import com.tango.identino.model.Single_Attendance_status;
import com.tango.identino.model.instructor;
import com.tango.identino.util.Attendance_Marking_Adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Attendance_marking extends AppCompatActivity {

    private Map<String, Integer> attendance = new HashMap<>();
    private RecyclerView status_recycler;
    private Button commit;
    private ProgressBar bar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_marking);
        status_recycler = findViewById(R.id.commit_recycle);
        commit = findViewById(R.id.commit_btn);
        bar = findViewById(R.id.prog_bar_status);

        final String course_name = getIntent().getStringExtra("course_name");

        status_recycler.setHasFixedSize(true);
        status_recycler.setLayoutManager(new LinearLayoutManager(this));

        attendance = (Map<String, Integer>) getIntent().getSerializableExtra("attendance");

        final List<Single_Attendance_status> records = new ArrayList<>();

        for (String key : attendance.keySet()) {
            records.add(new Single_Attendance_status(key, attendance.get(key)));
        }

        Attendance_Marking_Adapter adapter = new Attendance_Marking_Adapter(records, Attendance_marking.this);
        status_recycler.setAdapter(adapter);

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(Attendance_marking.this);

                alert.setTitle("Commit");
                alert.setMessage("Verify your password");

                // Set an EditText view to get user input
                final EditText input = new EditText(getApplicationContext());
                input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();

                        if (value.equals(instructor.getPassword())) {

                            bar.setVisibility(View.VISIBLE);
                            //attenance
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            final Date date = new Date();
                            final String Date = dateFormat.format(date).toString();


                            //Mark Attendance from firebase here
                            //course_name is from the intent extra
                            //replace 2017494 with name variable when dataset is trained on reg_nos
                            //replace document TEST with todays date

                            for (final Single_Attendance_status e : records) {

                                //uncomment below to delete todays attendance from database for test
//                                db.collection("courses").document(course_name).collection("students").document(e.getRegno()).collection("attendance").document(Date).delete();


                                db.collection("courses").document(course_name).collection("students").document(e.getRegno()).collection("attendance").document(Date).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            //lag chuki hai

                                        } else {
                                            //lagani hai from the list
                                            Map<String, Integer> data = new HashMap<>();
                                            data.put("status", e.getStatus());
                                            db.collection("courses").document(course_name).collection("students").document(e.getRegno()).collection("attendance").document(Date).set(data);

                                            db.collection("courses").document(course_name).collection("students").document(e.getRegno()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                    Attendance_record record = documentSnapshot.toObject(Attendance_record.class);
                                                    if (e.getStatus() == 1) {
                                                        Map<String, Integer> present = new HashMap<>();
                                                        present.put("present", record.getPresent() + 1);
                                                        db.collection("courses").document(course_name).collection("students").document(e.getRegno()).set(present, SetOptions.merge());
                                                    } else {
                                                        Map<String, Integer> absent = new HashMap<>();
                                                        absent.put("absent", record.getAbsent() + 1);
                                                        db.collection("courses").document(course_name).collection("students").document(e.getRegno()).set(absent, SetOptions.merge());
                                                    }

                                                }
                                            });


                                        }

                                    }
                                });


                            }

                            bar.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(Attendance_marking.this, Post_login.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                            //attendance


                        }

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }
        });


    }
}
