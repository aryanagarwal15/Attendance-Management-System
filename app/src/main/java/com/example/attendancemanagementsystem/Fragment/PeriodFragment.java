package com.example.attendancemanagementsystem.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.attendancemanagementsystem.Activity.PeriodAnalysisActivity;
import com.example.attendancemanagementsystem.R;

public class PeriodFragment extends Fragment {

    private CardView mainRelative;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_period, container, false);
        mainRelative = view.findViewById(R.id.period);

        mainRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PeriodAnalysisActivity.class);
                startActivity(i);
            }
        });

        return view;
    }
}
