package com.tango.identino.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tango.identino.R;

import java.util.ArrayList;
import java.util.List;

public class courseAdapter extends RecyclerView.Adapter<courseAdapter.ViewHolder>{
    private List<String> courseList=new ArrayList<>();
    private Context context;

    public courseAdapter(List<String> courseList, Context context) {
        this.courseList = courseList;
        this.context = context;
    }

    @NonNull
    @Override
    public courseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.courses_layout,parent,false);
        return new ViewHolder(view,context);
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
            context=ctx;
            courseName=itemView.findViewById(R.id.course_name_text_view);
            markAttendanceButton=itemView.findViewById(R.id.mark_attendance_courses_layout);
            showSummaryButton=itemView.findViewById(R.id.summary_courses_layout);
            markAttendanceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }

    }
}
