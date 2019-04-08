package com.example.attendancemanagementsystem.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.attendancemanagementsystem.Adapter.StudentAdapter;
import com.example.attendancemanagementsystem.Model.StudentItem;
import com.example.attendancemanagementsystem.Model.StudentListItem;
import com.example.attendancemanagementsystem.R;
import com.example.attendancemanagementsystem.Utils.AppConstants;
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
    private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student, container, false);
        recyclerView = view.findViewById(R.id.student_recycler);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabase = mFirebaseInstance.getReference();

        mDatabase.child("classes").child(AppConstants.classID).child("students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                studentItems.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                    dataSnapshot1.getValue(StudentListItem.class);
                    final StudentListItem studentItem = new StudentListItem();
                    studentItem.setId(dataSnapshot1.getKey());
                    studentItems.add(studentItem);
                    adapter = new StudentAdapter(getContext(), studentItems);
                    recyclerView.setAdapter(adapter);
                    mDatabase.child("students").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                StudentItem studentItem1 = dataSnapshot1.getValue(StudentItem.class);
                                if (studentItem1.getStudentId().equalsIgnoreCase(studentItem.getId())) {
                                    studentItem.setName(studentItem1.getStudentName());
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("Student Fragment", "Failed to get Firebase data");
                        }
                    });
                    mDatabase.child("classes").child(AppConstants.classID).child("students").child(dataSnapshot1.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            studentItem.setAttendance(String.valueOf(dataSnapshot.getChildrenCount()));
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("Student Fragment", "Failed to get Firebase data");
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("StudentFragment", "Failed to get firebase data");
            }
        });

        return view;
    }
}
