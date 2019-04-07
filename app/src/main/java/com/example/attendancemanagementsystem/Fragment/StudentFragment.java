package com.example.attendancemanagementsystem.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.attendancemanagementsystem.Adapter.StudentAdapter;
import com.example.attendancemanagementsystem.Model.StudentListItem;
import com.example.attendancemanagementsystem.R;
import com.example.attendancemanagementsystem.Utils.AppContants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class StudentFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<StudentListItem> studentItems = new ArrayList<>();
    private StudentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student, container, false);
        recyclerView = view.findViewById(R.id.student_recycler);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = mFirebaseInstance.getReference();

        mDatabase.child("classes").child(AppContants.classID).child("students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                studentItems.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                    dataSnapshot1.getValue(StudentListItem.class);
                    StudentListItem studentItem = new StudentListItem();
                    studentItem.setId(dataSnapshot1.getKey());
                    studentItems.add(studentItem);
                }
                adapter = new StudentAdapter(getContext(), studentItems);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("StudentFragment", "Failed to get firebase data");
            }
        });

        return view;
    }
}
