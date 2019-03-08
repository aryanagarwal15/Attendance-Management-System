package com.example.attendancemanagementsystem.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.example.attendancemanagementsystem.R;

public class IndividualStudentActivity extends AppCompatActivity {

    private CardView period;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_student);

        period = findViewById(R.id.period);

        getSupportActionBar().setTitle("Aryan Agarwal");

        period.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IndividualStudentActivity.this, ChangeStudentAttendance.class);
                startActivity(i);
            }
        });
    }
}
