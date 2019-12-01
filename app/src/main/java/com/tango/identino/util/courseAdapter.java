package com.tango.identino.util;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tango.identino.R;
import com.tango.identino.model.Attendance_record;

import java.util.ArrayList;
import java.util.List;

public class courseAdapter extends RecyclerView.Adapter<courseAdapter.ViewHolder> {
    private List<String> courseList;
    private Context context;


    public courseAdapter(List<String> courseList, Context context) {
        this.courseList = courseList;
        this.context = context;
    }

    @NonNull
    @Override
    public courseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.courses_layout, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull courseAdapter.ViewHolder holder, int position) {
        holder.courseName.setText(courseList.get(position));


    }


    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView courseName;
        public Button markAttendanceButton;
        public Button showSummaryButton;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            courseName = itemView.findViewById(R.id.course_name_text_view);
            markAttendanceButton = itemView.findViewById(R.id.mark_attendance_courses_layout);
            showSummaryButton = itemView.findViewById(R.id.summary_courses_layout);
            showSummaryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popUP();

                }
            });


        }

        private void popUP() {
            View View = LayoutInflater.from(context).inflate(R.layout.summary_popup, null);
            final PopupWindow pop_window = new PopupWindow(View, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            RecyclerView summary_recycler = View.findViewById(R.id.summary_recycler);
            List<Attendance_record> records = new ArrayList<>();
            records.add(new Attendance_record("2017494", 2, 3));
            records.add(new Attendance_record("2017494", 2, 3));
            records.add(new Attendance_record("2017494", 2, 3));
            records.add(new Attendance_record("2017494", 2, 3));
            records.add(new Attendance_record("2017494", 2, 3));
            records.add(new Attendance_record("2017494", 2, 3));
            SummaryAdapter summaryAdapter = new SummaryAdapter(context, records);
            summary_recycler.setAdapter(summaryAdapter);
            summary_recycler.setHasFixedSize(true);
            summary_recycler.setLayoutManager(new LinearLayoutManager(context));

            pop_window.showAtLocation(View, Gravity.CENTER, 0, 0);

            Button close=View.findViewById(R.id.pop_close_button);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(android.view.View view) {
                    pop_window.dismiss();
                }
            });

        }



    }
}
