package com.example.attendancemanagementsystem.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.attendancemanagementsystem.Model.StudentItem;
import com.example.attendancemanagementsystem.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddStudentActivity extends AppCompatActivity {

    private EditText studentId, studentName;
    private Button addStudent;
    private String student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        studentId = findViewById(R.id.student_id);
        studentName = findViewById(R.id.student_name);
        addStudent = findViewById(R.id.add_student);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            student = extras.getString("studentID");
        } else {
            student = "";
        }
        studentId.setText(student);

        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
        final DatabaseReference mDatabase = mFirebaseInstance.getReference();

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentItem studentItem = new StudentItem();
                studentItem.setStudentName(studentName.getText().toString());
                studentItem.setStudentId(studentId.getText().toString());
                mDatabase.child("students").push().setValue(studentItem);
                finish();
            }
        });
    }
}
