package com.tango.identino.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tango.identino.R;
import com.tango.identino.model.Attendance_record;

import java.util.List;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.ViewHolder> {

    private Context context;
    private List<Attendance_record> recordList;

    public SummaryAdapter(Context context, List<Attendance_record> recordList) {
        this.context = context;
        this.recordList = recordList;
    }

    @NonNull
    @Override
    public SummaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.summary_row_presabs,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryAdapter.ViewHolder holder, int position) {

        holder.regno.setText( recordList.get(position).getRegno());
        holder.present.setText( String.valueOf(recordList.get(position).getPresent()));
        holder.absent.setText( String.valueOf(recordList.get(position).getAbsent()));



    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView regno,present,absent;
        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            regno= itemView.findViewById(R.id.summary_regno);
            present= itemView.findViewById(R.id.summary_present);
            absent= itemView.findViewById(R.id.summary_absent);
            context = ctx;


        }
    }
}
