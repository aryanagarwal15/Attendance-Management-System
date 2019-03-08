package com.example.attendancemanagementsystem.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.attendancemanagementsystem.Adapter.ClassTabAdapter;
import com.example.attendancemanagementsystem.Fragment.AnalyticsFragment;
import com.example.attendancemanagementsystem.Fragment.PeriodFragment;
import com.example.attendancemanagementsystem.Fragment.StudentFragment;
import com.example.attendancemanagementsystem.R;

public class ShowClassActivity extends AppCompatActivity {
    public ClassTabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_class);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tab_layout);

        getSupportActionBar().setTitle("SDPD");

        adapter = new ClassTabAdapter(getSupportFragmentManager());
        adapter.addFragment(new PeriodFragment(), "Period");
        adapter.addFragment(new StudentFragment(), "Students");
        adapter.addFragment(new AnalyticsFragment(), "Analytics");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
