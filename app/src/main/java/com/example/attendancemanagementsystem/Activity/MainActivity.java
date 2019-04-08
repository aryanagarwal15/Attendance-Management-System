package com.example.attendancemanagementsystem.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.attendancemanagementsystem.Adapter.ClassAdapter;
import com.example.attendancemanagementsystem.Model.ClassItem;
import com.example.attendancemanagementsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ClassAdapter adapter;
    private List<ClassItem> classList = new ArrayList<>();
    private FirebaseAuth mAuth;
    private String TAG = "MainActivity";
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //INIT VIEWS HERE
        RelativeLayout addClass = findViewById(R.id.add_class_but);
        recyclerView = findViewById(R.id.class_recycler_view);
        if (flag) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            flag = false;
        }
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = mFirebaseInstance.getReference();

        //INIT FUNCTIONS HERE
        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, EditClassActivity.class);
                startActivity(i);
            }
        });
        mAuth = FirebaseAuth.getInstance();


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mDatabase.child("class").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                classList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ClassItem classItem = dataSnapshot1.getValue(ClassItem.class);
                    classItem.setClassID(dataSnapshot1.getKey());
                    classList.add(classItem);
                }
                adapter = new ClassAdapter(MainActivity.this, classList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to get firebase data");
            }
        });

    }
}
