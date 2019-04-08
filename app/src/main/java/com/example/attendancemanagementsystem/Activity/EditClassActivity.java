package com.example.attendancemanagementsystem.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.attendancemanagementsystem.Model.ClassItem;
import com.example.attendancemanagementsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditClassActivity extends AppCompatActivity {

    private Button sumbitButton;
    private FirebaseAuth mAuth;
    private EditText className, strength;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class);
        getSupportActionBar().setTitle("Add/Edit Class");
        sumbitButton = findViewById(R.id.submit_button);
        className = findViewById(R.id.class_name);
        strength = findViewById(R.id.class_strength);
        mAuth = FirebaseAuth.getInstance();


        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
        final DatabaseReference mDatabase = mFirebaseInstance.getReference();

        sumbitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassItem classItem = new ClassItem();
                classItem.setClassName(className.getText().toString());
                classItem.setStrength(strength.getText().toString());
                mDatabase.child("class").child(mAuth.getUid()).push().setValue(classItem);
                finish();
            }
        });
    }
}
