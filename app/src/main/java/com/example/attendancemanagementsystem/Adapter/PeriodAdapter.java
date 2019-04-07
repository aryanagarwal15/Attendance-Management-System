package com.example.attendancemanagementsystem.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.attendancemanagementsystem.Model.PeriodItem;
import com.example.attendancemanagementsystem.R;

import java.util.List;

public class PeriodAdapter extends RecyclerView.Adapter<PeriodAdapter.MyViewHolder> {

    private List<PeriodItem> periodItemList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, time, attendees;

        public MyViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.default_date);
            time = view.findViewById(R.id.default_time);
            attendees = view.findViewById(R.id.default_attendees);
        }
    }


    public PeriodAdapter(Context context, List<PeriodItem> periodItemList) {
        this.periodItemList = periodItemList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_period_attendance, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final PeriodItem periodItem = periodItemList.get(position);
        holder.date.setText(periodItem.getDate());
        holder.attendees.setText(periodItem.getAttendees());
        holder.time.setText(periodItem.getTime());
    }

    @Override
    public int getItemCount() {
        return periodItemList.size();
    }
}