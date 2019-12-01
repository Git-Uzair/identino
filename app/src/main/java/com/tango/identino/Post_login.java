package com.tango.identino;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tango.identino.model.courses;
import com.tango.identino.util.courseAdapter;

import java.util.ArrayList;
import java.util.List;

public class Post_login extends AppCompatActivity {
    private RecyclerView recyclerView;
    private com.tango.identino.util.courseAdapter courseAdapter;
    private List<String> courseList;
    private TextView Ins_name;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private FirebaseAuth mauth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);
        recyclerView=findViewById(R.id.courses_recycler_view);
        Ins_name = findViewById(R.id.post_login_username_textView);
        Ins_name.setText(getIntent().getStringExtra("name"));
        courseList=new ArrayList<>();
        mauth=FirebaseAuth.getInstance();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        currentUser=mauth.getCurrentUser();
        displayCourses();


    }

    private void displayCourses()
    {
        String email=currentUser.getEmail();
        db.collection("instructor").document(email).collection("timetable").document("monday")
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                courses Courses=documentSnapshot.toObject(courses.class);
                List<String> coursesList=Courses.getCourses();
                courseAdapter=new courseAdapter(coursesList,Post_login.this);
                recyclerView.setAdapter(courseAdapter);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
