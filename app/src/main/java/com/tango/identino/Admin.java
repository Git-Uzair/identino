package com.tango.identino;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.tango.identino.model.instructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Admin extends AppCompatActivity implements View.OnClickListener {

    private Button addInstructor, removeInstructor, addStudent, removeStudent;
    private TextView adminName;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        addInstructor = findViewById(R.id.admin_add_instructor_button);
        addStudent = findViewById(R.id.admin_add_student_button);
        removeInstructor = findViewById(R.id.admin_remove_instructor_button);
        removeStudent = findViewById(R.id.admin_remove_student_button);
        adminName = findViewById(R.id.admin_name_text_view);
        Intent intent = getIntent();
        adminName.setText(intent.getExtras().get("name").toString());
        addInstructor.setOnClickListener(this);
        removeInstructor.setOnClickListener(this);
        removeStudent.setOnClickListener(this);
        addStudent.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.admin_add_instructor_button: {
                addInstructorFunc();
            }
            break;
            case R.id.admin_add_student_button: {
                addStudentFunc();
            }
            break;
            case R.id.admin_remove_instructor_button: {
                removeInstructorFunc();
            }
            break;
            case R.id.admin_remove_student_button: {
                removeStudentFunc();
            }
            break;
        }

    }

    public void addInstructorFunc() {
            addInstructorMakeDialog();
    }

    public void addStudentFunc() {
        addStudentMakeDialog();

    }

    public void removeInstructorFunc() {
        removeInstructorMakeDialog();
    }

    public void removeStudentFunc() {
        removeStudentMakeDialog();
    }

    public void removeInstructorMakeDialog() {
        final EditText email = new EditText(Admin.this);
        email.setHint("Email");
        AlertDialog.Builder builder1 = new AlertDialog.Builder(Admin.this);
        builder1.setTitle("Remove Instructor");
        builder1.setMessage("Fill the field below to delete Instructor.");
        builder1.setView(email);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Remove Instructor",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final String text = email.getText().toString().trim();
                        db.collection("instructor").document(text).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (!documentSnapshot.exists() || (Boolean) documentSnapshot.get("admin") == true) {
                                    Toast.makeText(Admin.this, "No such Instructor exists", Toast.LENGTH_LONG).show();
                                } else {
                                    db.collection("instructor").document(text).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Admin.this, "Instructor Removed Successfully", Toast.LENGTH_LONG).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void removeStudentMakeDialog() {
        final LinearLayout layout = new LinearLayout(Admin.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final TextView coursesLabel = new TextView(Admin.this);
        final EditText regNo = new EditText(Admin.this);
        final Spinner dropdown = new Spinner(Admin.this);
        final List<String> list=new ArrayList<>();
        // dropdown.setDropDownWidth(30);
       //  final String[] items = new String[]{"1", "2", "three"};
        coursesLabel.setText("Select Course: ");
        regNo.setHint("Reg No.");
        AlertDialog.Builder builder1 = new AlertDialog.Builder(Admin.this);
        builder1.setTitle("Remove Student");
        builder1.setMessage("Fill the field below to delete Student.");
        layout.addView(regNo);
        layout.addView(coursesLabel);
        layout.addView(dropdown);
        builder1.setView(layout);
        builder1.setCancelable(true);
        final String[] courseName = new String[1];

        db.collection("courses").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String tmp = "";

                if (queryDocumentSnapshots.isEmpty()) {
                    Toast.makeText(Admin.this, "No such document", Toast.LENGTH_LONG).show();
                } else {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        tmp = (documentSnapshot.getId());
                        list.add(tmp);
                    }
                    if(list.isEmpty())
                    {
                        list.add("No Courses in Database");
                        list.add("2 exit please");

                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Admin.this, android.R.layout.simple_spinner_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dropdown.setAdapter(adapter);
                    
                }

            }
        });

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                courseName[0]=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        builder1.setPositiveButton(
                "Remove Student",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final String text = regNo.getText().toString().trim();
                        db.collection("courses").document(courseName[0]).collection("students").document(text).get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            db.collection("courses").document(courseName[0]).collection("students")
                                                    .document(text).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(Admin.this, "Student Deleted Successfully", Toast.LENGTH_LONG).show();

                                                }
                                            });

                                        } else {
                                            Toast.makeText(Admin.this, "No such Student Exists in this course", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    public void addStudentMakeDialog()
    {
        final LinearLayout layout = new LinearLayout(Admin.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final TextView coursesLabel = new TextView(Admin.this);
        final EditText regNo = new EditText(Admin.this);
        final Spinner dropdown = new Spinner(Admin.this);
        final EditText stuName=new EditText(Admin.this);
        final List<String> list=new ArrayList<>();
        // dropdown.setDropDownWidth(30);
        //  final String[] items = new String[]{"1", "2", "three"};
        coursesLabel.setText("Select Course: ");
        regNo.setHint("Reg No.");
        stuName.setHint("Name");
        AlertDialog.Builder builder1 = new AlertDialog.Builder(Admin.this);
        builder1.setTitle("Add Student");
        builder1.setMessage("Fill the field below to add Student.");
        layout.addView(regNo);
        layout.addView(stuName);
        layout.addView(coursesLabel);
        layout.addView(dropdown);
        builder1.setView(layout);
        builder1.setCancelable(false);
        final String[] courseName = new String[1];

        db.collection("courses").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String tmp = "";

                if (queryDocumentSnapshots.isEmpty()) {
                    Toast.makeText(Admin.this, "No courses in database.", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        tmp = (documentSnapshot.getId());
                        list.add(tmp);
                    }
                    if(list.isEmpty())
                    {
                        Toast.makeText(Admin.this, "No courses in database.", Toast.LENGTH_LONG).show();
                        return;

                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Admin.this, android.R.layout.simple_spinner_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dropdown.setAdapter(adapter);

                }

            }
        });

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                courseName[0]=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        builder1.setPositiveButton(
                "Add Student",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final String text = regNo.getText().toString().trim();
                        final String name=stuName.getText().toString().trim();
                        db.collection("courses").document(courseName[0]).collection("students").document(text).get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                        if (documentSnapshot.exists())
                                        {
                                            Toast.makeText(Admin.this,"Student already exists in this course",Toast.LENGTH_LONG).show();
                                        }
                                        else
                                        {
                                            final Map<String, Object> data = new HashMap<>();
                                            data.put("regno",text);
                                            data.put("name",name);
                                            data.put("absent",0);
                                            data.put("present",0);
                                            db.collection("courses").document(courseName[0]).collection("students").document(text)
                                                    .set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    Toast.makeText(Admin.this,"Student Added successfully",Toast.LENGTH_LONG).show();
                                                    data.clear();
                                                    data.put("status",0);
                                                    db.collection("courses").document(courseName[0]).collection("students").document(text)
                                                            .collection("attendance").document("initial").set(data,SetOptions.merge())
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText(Admin.this,"Student Added successfully",Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                }
                                            });
                                        }
                                    }
                                });

                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }
    public void addInstructorMakeDialog()
    {
        final LinearLayout layout = new LinearLayout(Admin.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText email = new EditText(Admin.this);
        email.setHint("Email");
        AlertDialog.Builder builder1 = new AlertDialog.Builder(Admin.this);
        builder1.setTitle("Add Instructor");
        builder1.setMessage("Fill the field below to add Instructor.");
        layout.addView(email);
        builder1.setView(layout);
        builder1.setCancelable(false);


        builder1.setPositiveButton(
                "Add Instructor",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final String text = email.getText().toString().trim();
                                db.collection("instructor").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if (queryDocumentSnapshots.isEmpty())
                                        {
                                            Toast.makeText(Admin.this,"No instructor exists in the database",Toast.LENGTH_LONG).show();
                                        }
                                        else
                                        {
                                            for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots)
                                            {
                                                if (documentSnapshot.getId()==text && documentSnapshot.get("admin")==(Boolean)false)
                                                {
                                                    Toast.makeText(Admin.this,"Instructor Already Exists",Toast.LENGTH_LONG).show();
                                                    return;
                                                }
                                            }
                                           //Query for add instructor will be added here.//
                                        }
                                    }
                                });
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

}
