package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class incidentHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_history);
    }

    public void onBack(View view)
    {
        finish();
}
    public void profile(View view)
    {
        Intent intent = new Intent(incidentHistory.this, profile.class);

        startActivity(intent);
    }
}