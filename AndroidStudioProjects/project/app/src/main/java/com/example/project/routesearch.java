package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.ImageReader;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class routesearch extends AppCompatActivity {

    ImageView setting;
    ImageView profile;

    EditText starting;
    EditText destination;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routesearch);

        setting=findViewById(R.id.settings);
        profile=findViewById(R.id.pro);
        starting=findViewById(R.id.startingLocationEditText);
        destination=findViewById(R.id.destinationEditText);
        b=findViewById(R.id.showRouteButton);

        String start=starting.getText().toString();
        String dest=starting.getText().toString();

    }

    public void onClick1(View v){
        Intent intent = new Intent(routesearch.this, settings.class);

        startActivity(intent);

    }

    public void onClick2(View v){
        Intent intent = new Intent(routesearch.this, profile.class);

        startActivity(intent);

    }

    public void onClick3(View v){
        Intent intent = new Intent(routesearch.this, routeinfo.class);

        startActivity(intent);

    }
}