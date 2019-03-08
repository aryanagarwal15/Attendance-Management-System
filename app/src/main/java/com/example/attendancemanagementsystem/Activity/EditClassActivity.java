package com.example.attendancemanagementsystem.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.attendancemanagementsystem.R;

public class EditClassActivity extends AppCompatActivity {

    private Button sumbitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class);
        getSupportActionBar().setTitle("Add/Edit Class");
        sumbitButton = findViewById(R.id.submit_button);

        sumbitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditClassActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
