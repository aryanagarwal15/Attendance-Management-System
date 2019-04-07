package com.example.attendancemanagementsystem.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.attendancemanagementsystem.Activity.ShowClassActivity;
import com.example.attendancemanagementsystem.Model.ClassItem;
import com.example.attendancemanagementsystem.R;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.MyViewHolder> {

    private List<ClassItem> classItems;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView className, classStrength;
        public CardView card;

        public MyViewHolder(View view) {
            super(view);
            className = (TextView) view.findViewById(R.id.class_name);
            classStrength = view.findViewById(R.id.class_strength);
            card = view.findViewById(R.id.media_card_view);
        }
    }


    public ClassAdapter(Context context, List<ClassItem> classItems) {
        this.classItems = classItems;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_class, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ClassItem classItem = classItems.get(position);
        holder.className.setText(classItem.getClassName());
        holder.classStrength.setText(classItem.getStrength());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ShowClassActivity.class);
                i.putExtra("className", classItem.getClassName());
                i.putExtra("classID",classItem.getClassID());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return classItems.size();
    }
}