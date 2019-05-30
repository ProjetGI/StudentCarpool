package com.gi2.servicedecovoiturage;


import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


import com.gi2.servicedecovoiturage.logregform.R;

public class ReportDetail extends AppCompatActivity {
    TextView reporter,target,description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reportrapport);
        Intent intent = getIntent();
        String user = intent.getStringExtra("user");
        String reported = intent.getStringExtra("reported");
        String desc = intent.getStringExtra("description");
        String titre = intent.getStringExtra("titre");

        target= findViewById(R.id.reported);
        reporter = findViewById(R.id.reporter);
        description = findViewById(R.id.maxPlaces);

        target.setText(reported);
        reporter.setText(user);
        description.setText(desc);


    }
}
