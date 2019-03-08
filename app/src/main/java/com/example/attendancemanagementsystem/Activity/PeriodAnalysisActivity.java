package com.example.attendancemanagementsystem.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.example.attendancemanagementsystem.R;

public class PeriodAnalysisActivity extends AppCompatActivity {

    private CardView student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period_analysis);

        student = findViewById(R.id.student);

        getSupportActionBar().setTitle("15th Oct 2019 || 3PM-4PM");


        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PeriodAnalysisActivity.this, ChangeStudentAttendance.class);
                startActivity(i);
            }
        });
    }
}
