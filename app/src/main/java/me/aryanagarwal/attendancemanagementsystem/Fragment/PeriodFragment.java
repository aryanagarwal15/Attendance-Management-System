package me.aryanagarwal.attendancemanagementsystem.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class PeriodFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<PeriodItem> periodItemList = new ArrayList<>();
    private PeriodAdapter adapter;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mDatabase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_period, container, false);
        recyclerView = view.findViewById(R.id.period_recycler);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabase = mFirebaseInstance.getReference();

        mDatabase.child("classes").child(AppConstants.classID).child("period").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                periodItemList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    final PeriodItem periodItem = dataSnapshot1.getValue(PeriodItem.class);
                    periodItem.setDate(dataSnapshot1.getKey().substring(0, 8));
                    periodItem.setTime(dataSnapshot1.getKey().substring(9, 11) + ":00");
                    periodItemList.add(periodItem);
                    adapter = new PeriodAdapter(getContext(), periodItemList);
                    recyclerView.setAdapter(adapter);
                    mDatabase.child("classes").child(AppConstants.classID).child("period").child(dataSnapshot1.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            periodItem.setAttendees(String.valueOf(dataSnapshot.getChildrenCount()));
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("PeriodFragment", "Failed to get Firebase data");
                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("PeriodFragment", "Failed to get firebase data");
            }
        });

        return view;
    }
}
