package com.tango.identino;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.tango.identino.util.courseAdapter;

import java.util.ArrayList;
import java.util.List;

public class Post_login extends AppCompatActivity {
    private RecyclerView recyclerView;
    private com.tango.identino.util.courseAdapter courseAdapter;
    private List<String> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);
        recyclerView=findViewById(R.id.courses_recycler_view);
        courseList=new ArrayList<>();
        courseList.add("Course 1");
        courseList.add("Course 2");
        courseList.add("Course 3");
        courseList.add("Course 4");
        courseList.add("Course 5");
        courseList.add("Course 6");
        courseList.add("Course 7");
        courseList.add("Course 8");
        courseList.add("Course 9");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        courseAdapter=new courseAdapter(courseList,Post_login.this);
        recyclerView.setAdapter(courseAdapter);

    }
}
