package com.example.attendancemanagementsystem.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.attendancemanagementsystem.Adapter.PeriodAdapter;
import com.example.attendancemanagementsystem.Model.PeriodItem;
import com.example.attendancemanagementsystem.R;
import com.example.attendancemanagementsystem.Utils.AppContants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PeriodFragment extends Fragment {

    private CardView mainRelative;
    private RecyclerView recyclerView;
    private List<PeriodItem> periodItemList = new ArrayList<>();
    private PeriodAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_period, container, false);
        recyclerView = view.findViewById(R.id.period_recycler);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = mFirebaseInstance.getReference();

        mDatabase.child("classes").child(AppContants.classID).child("period").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                periodItemList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    PeriodItem periodItem = dataSnapshot1.getValue(PeriodItem.class);
                    periodItem.setDate(dataSnapshot1.getKey());
                    periodItemList.add(periodItem);
                    Log.d("dev123", "here");
                }
                adapter = new PeriodAdapter(getContext(), periodItemList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("PeriodFragment", "Failed to get firebase data");
            }
        });

        return view;
    }
}
