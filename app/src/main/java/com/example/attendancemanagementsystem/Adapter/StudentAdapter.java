package com.example.attendancemanagementsystem.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.attendancemanagementsystem.Model.StudentListItem;
import com.example.attendancemanagementsystem.R;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> {

    private List<StudentListItem> studentListItems;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, id, attendance;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.student_name);
            id = view.findViewById(R.id.student_id);
            attendance = view.findViewById(R.id.attendance);
        }
    }


    public StudentAdapter(Context context, List<StudentListItem> studentList) {
        this.studentListItems = studentList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final StudentListItem studentListItem = studentListItems.get(position);
        holder.name.setText(studentListItem.getName());
        holder.id.setText(studentListItem.getId());
        holder.attendance.setText(studentListItem.getAttendance());
    }

    @Override
    public int getItemCount() {
        return studentListItems.size();
    }
}