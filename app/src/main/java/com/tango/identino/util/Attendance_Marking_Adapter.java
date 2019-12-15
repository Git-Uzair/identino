package com.tango.identino.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tango.identino.R;
import com.tango.identino.model.Single_Attendance_status;

import java.util.List;

public class Attendance_Marking_Adapter extends RecyclerView.Adapter<Attendance_Marking_Adapter.ViewHolder> {

    List<Single_Attendance_status> record;
    Context context;

    public Attendance_Marking_Adapter(List<Single_Attendance_status> record, Context context) {
        this.record = record;
        this.context = context;
    }

    @NonNull
    @Override
    public Attendance_Marking_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.attendance_marked_summary_row, parent, false);

        return new ViewHolder(view, context);
    }

    public Attendance_Marking_Adapter() {
    }

    @Override
    public void onBindViewHolder(@NonNull Attendance_Marking_Adapter.ViewHolder holder, int position) {

        holder.status.setText(String.valueOf(record.get(position).getStatus()));
        holder.reg_no.setText(record.get(position).getRegno());


    }

    @Override
    public int getItemCount() {
        return record.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView reg_no, status;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            reg_no = itemView.findViewById(R.id.attendance_reg);
            status = itemView.findViewById(R.id.attendance_status);
            context=ctx;



        }
    }
}
