package me.aryanagarwal.attendancemanagementsystem.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import me.aryanagarwal.attendancemanagementsystem.Adapter.StudentAdapter;
import me.aryanagarwal.attendancemanagementsystem.Model.StudentItem;
import me.aryanagarwal.attendancemanagementsystem.Model.StudentListItem;
import me.aryanagarwal.attendancemanagementsystem.R;
import me.aryanagarwal.attendancemanagementsystem.Utils.AppConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PeriodAnalysisActivity extends AppCompatActivity {


    private TextView className, present, absent;
    private RecyclerView recyclerView;
    private List<StudentListItem> studentItems = new ArrayList<>();
    private StudentAdapter adapter;
    private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period_analysis);

        recyclerView = findViewById(R.id.student_recycler);

        className = findViewById(R.id.class_name);
        present = findViewById(R.id.students_present);
        className.setText("Class: " + AppConstants.ClassName);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabase = mFirebaseInstance.getReference();


        getSupportActionBar().setTitle(AppConstants.Period.substring(0, 8) + " || " + AppConstants.Period.substring(9, 11) + ":00");

        mDatabase.child("classes").child(AppConstants.classID).child("students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                studentItems.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    final StudentListItem studentItem = new StudentListItem();
                    studentItem.setId(dataSnapshot1.getKey());
                    studentItem.setAttendance("No");
                    studentItems.add(studentItem);
                    adapter = new StudentAdapter(PeriodAnalysisActivity.this, studentItems);
                    recyclerView.setAdapter(adapter);

                    mDatabase.child("classes").child(AppConstants.classID).child("period").child(AppConstants.Period).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            present.setText("Students Present: " + String.valueOf(dataSnapshot.getChildrenCount()));
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                if (dataSnapshot1.getValue().toString().equalsIgnoreCase(studentItem.getId())) {
                                    studentItem.setAttendance("Yes");
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("PeriodAnalysis", "Failed to get Firebase data");
                        }
                    });

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
                            Log.e("PeriodAnalysis", "Failed to get Firebase data");
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("StudentFragment", "Failed to get firebase data");
            }
        });

    }
}
