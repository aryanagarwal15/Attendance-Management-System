package com.example.attendancemanagementsystem.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.attendancemanagementsystem.Model.StudentItem;
import com.example.attendancemanagementsystem.R;
import com.example.attendancemanagementsystem.Utils.AppContants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ScanActivity extends AppCompatActivity {

    private Button finishAttendance, submitAttendance;
    private EditText studentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        studentID = findViewById(R.id.student_id);
        finishAttendance = findViewById(R.id.finish_attendance);
        submitAttendance = findViewById(R.id.submit_id);
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
        final DatabaseReference mDatabase = mFirebaseInstance.getReference();

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy-HH");
        final String formattedDate = df.format(c);
        submitAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String studentIDS = studentID.getText().toString();

                mDatabase.child("students").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean flag = false, loopFinished = false;
                        int i = 0;
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            StudentItem studentItem = dataSnapshot1.getValue(StudentItem.class);
                            i++;
                            if (studentItem.getStudentId().equalsIgnoreCase(studentIDS)) {
                                mDatabase.child("classes").child(AppContants.classID).child("period").child(formattedDate).push().setValue(studentIDS);
                                mDatabase.child("classes").child(AppContants.classID).child("students").child(studentIDS).push().setValue(formattedDate);
                                flag = true;
                            }
                            if (dataSnapshot.getChildrenCount() == i)
                                loopFinished = true;
                        }
                        if (!flag && loopFinished) {
                            Intent intent = new Intent(ScanActivity.this, AddStudentActivity.class);
                            intent.putExtra("studentID", studentIDS);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("ScanActivity", "Failed to get firebase data");
                    }
                });

            }
        });


        finishAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
