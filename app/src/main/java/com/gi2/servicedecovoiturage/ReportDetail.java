package com.gi2.servicedecovoiturage;


import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

        target=(TextView)findViewById(R.id.reported);
        reporter = (TextView)findViewById(R.id.reporter);
        description = (TextView) findViewById(R.id.description);

        target.setText(reported);
        reporter.setText(user);
        description.setText(desc);


    }
}
