package com.tango.identino;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tango.identino.model.Single_Attendance_status;
import com.tango.identino.model.instructor;
import com.tango.identino.util.Attendance_Marking_Adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Attendance_marking extends AppCompatActivity {

    private Map<String, Integer> attendance = new HashMap<>();
    private RecyclerView status_recycler;
    private Button commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_marking);
        status_recycler = findViewById(R.id.commit_recycle);
        commit = findViewById(R.id.commit_btn);


        status_recycler.setHasFixedSize(true);
        status_recycler.setLayoutManager(new LinearLayoutManager(this));

        attendance = (Map<String, Integer>) getIntent().getSerializableExtra("attendance");

        List<Single_Attendance_status> records = new ArrayList<>();

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
