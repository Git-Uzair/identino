package com.tango.identino;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class Attendance_marking extends AppCompatActivity {

    private Map<String, Integer> attendance = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_marking);

        attendance = (Map<String, Integer>) getIntent().getSerializableExtra("attendance");



    }
}
