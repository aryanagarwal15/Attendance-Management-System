package me.aryanagarwal.attendancemanagementsystem.Activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import me.aryanagarwal.attendancemanagementsystem.Adapter.ClassTabAdapter;
import me.aryanagarwal.attendancemanagementsystem.Fragment.PeriodFragment;
import me.aryanagarwal.attendancemanagementsystem.Fragment.StudentFragment;
import me.aryanagarwal.attendancemanagementsystem.R;
import me.aryanagarwal.attendancemanagementsystem.Utils.AppConstants;

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
            AppConstants.ClassName = className;
            AppConstants.classID = extras.getString("classID");
            classID = extras.getString("classID");
        } else {
            className = "null";
            AppConstants.classID = "null";
            AppConstants.ClassName = className;
            classID = "null";
        }
        getSupportActionBar().setTitle(className);

        adapter = new ClassTabAdapter(getSupportFragmentManager());
        adapter.addFragment(new PeriodFragment(), "Period");
        adapter.addFragment(new StudentFragment(), "Students");
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
