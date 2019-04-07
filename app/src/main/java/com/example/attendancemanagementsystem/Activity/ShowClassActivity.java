package com.example.attendancemanagementsystem.Activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.attendancemanagementsystem.Adapter.ClassTabAdapter;
import com.example.attendancemanagementsystem.Fragment.AnalyticsFragment;
import com.example.attendancemanagementsystem.Fragment.PeriodFragment;
import com.example.attendancemanagementsystem.Fragment.StudentFragment;
import com.example.attendancemanagementsystem.R;
import com.example.attendancemanagementsystem.Utils.AppContants;

public class ShowClassActivity extends AppCompatActivity {
    public ClassTabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton takeAttendance;
    public String className, classID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_class);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tab_layout);
        takeAttendance = findViewById(R.id.take_attendance);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            className = extras.getString("className");
            AppContants.classID = extras.getString("classID");
            classID = extras.getString("classID");
        } else {
            className = "null";
            AppContants.classID = "null";
            classID = "null";
        }
        getSupportActionBar().setTitle(className);

        adapter = new ClassTabAdapter(getSupportFragmentManager());
        adapter.addFragment(new PeriodFragment(), "Period");
        adapter.addFragment(new StudentFragment(), "Students");
        adapter.addFragment(new AnalyticsFragment(), "Analytics");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        takeAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShowClassActivity.this, ScanActivity.class);
                startActivity(i);
            }
        });
    }

    public String getClassID() {
        return classID;
    }
}
