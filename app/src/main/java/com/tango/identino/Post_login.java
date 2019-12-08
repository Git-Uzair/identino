package com.tango.identino;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tango.identino.model.Attendance_record;
import com.tango.identino.model.courses;
import com.tango.identino.util.SummaryAdapter;
import com.tango.identino.util.courseAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post_login extends AppCompatActivity {
    private RecyclerView recyclerView;
    private com.tango.identino.util.courseAdapter courseAdapter;
    private List<String> courseList;
    private TextView Ins_name;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mauth;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);
        recyclerView = findViewById(R.id.courses_recycler_view);
        Ins_name = findViewById(R.id.post_login_username_textView);
        Ins_name.setText(getIntent().getStringExtra("name"));
        courseList = new ArrayList<>();
        mauth = FirebaseAuth.getInstance();
        currentUser = mauth.getCurrentUser();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayCourses();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sign_out_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.sign_out_menu_button:
                if(currentUser!=null && mauth!=null)
                {
                    mauth.signOut();
                    startActivity(new Intent(Post_login.this,MainActivity.class));
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayCourses() {
        String email = currentUser.getEmail();
        Date now  = new Date();
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");

        db.collection("instructor").document(email).collection("timetable").document(simpleDateformat.format(now).toLowerCase())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                courses Courses = documentSnapshot.toObject(courses.class);
                List<String> coursesList = Courses.getCourses();
                courseAdapter = new courseAdapter(coursesList, Post_login.this);
                recyclerView.setAdapter(courseAdapter);}
                else
                {
                    Toast.makeText(Post_login.this,"Data not found",Toast.LENGTH_LONG).show();
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Post_login.this,"method failed",Toast.LENGTH_LONG).show();

            }
        });
    }
}
