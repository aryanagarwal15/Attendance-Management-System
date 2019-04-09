package me.aryanagarwal.attendancemanagementsystem.Activity;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import me.aryanagarwal.attendancemanagementsystem.Adapter.PeriodAdapter;
import me.aryanagarwal.attendancemanagementsystem.Model.PeriodItem;
import me.aryanagarwal.attendancemanagementsystem.R;
import me.aryanagarwal.attendancemanagementsystem.Utils.AppConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class IndividualStudentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<PeriodItem> periodItemList = new ArrayList<>();
    private PeriodAdapter adapter;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mDatabase;
    private int totalClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_student);
        final TextView name, id, attendance, percentage;
        name = findViewById(R.id.student_name);
        id = findViewById(R.id.student_id);
        attendance = findViewById(R.id.class_attended);
        percentage = findViewById(R.id.percentage);
        recyclerView = findViewById(R.id.period_student);

        name.setText("Name: " + AppConstants.StudentName);
        id.setText("ID: " + AppConstants.StudentID);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabase = mFirebaseInstance.getReference();

        getSupportActionBar().setTitle(AppConstants.StudentName);

        mDatabase.child("classes").child(AppConstants.classID).child("period").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                periodItemList.clear();
                totalClasses = (int) dataSnapshot.getChildrenCount();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    final PeriodItem periodItem = new PeriodItem();
                    periodItem.setDate(dataSnapshot1.getKey().substring(0, 8));
                    periodItem.setTime(dataSnapshot1.getKey().substring(9, 11) + ":00");
                    periodItem.setAttendees("No");
                    mDatabase.child("classes").child(AppConstants.classID).child("students").child(AppConstants.StudentID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            attendance.setText("Class Attended: " + String.valueOf(dataSnapshot.getChildrenCount()));
                            percentage.setText(String.valueOf(dataSnapshot.getChildrenCount() / totalClasses * 100));
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                if (dataSnapshot1.getValue().toString().equalsIgnoreCase(periodItem.getDate() + "-" + periodItem.getTime().substring(0, 2))) {
                                    periodItem.setAttendees("Yes");
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("IndividualStudent", "Failed to get firebase data");
                        }
                    });

                    periodItemList.add(periodItem);
                    adapter = new PeriodAdapter(IndividualStudentActivity.this, periodItemList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("IndividualStudent", "Failed to get firebase data");
            }
        });


    }
}
