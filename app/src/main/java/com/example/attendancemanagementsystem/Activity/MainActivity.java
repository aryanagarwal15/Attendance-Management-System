package com.example.attendancemanagementsystem.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.attendancemanagementsystem.R;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout addClass;
    private CardView subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addClass = findViewById(R.id.add_class_but);
        subject = findViewById(R.id.media_card_view);

        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, EditClassActivity.class);
                startActivity(i);
            }
        });

        subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ShowClassActivity.class);
                startActivity(i);
            }
        });

    }
}
