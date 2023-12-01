package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class homescreen extends AppCompatActivity {

    Button route;
    Button help;
    ImageView setting;
    ImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        route=findViewById(R.id.start);
        help=findViewById(R.id.report);
        setting=findViewById(R.id.settings);
        profile=findViewById(R.id.pro);
    }

    public void onClick1(View v){
        Intent intent = new Intent(homescreen.this, settings.class);

        startActivity(intent);

    }

    public void onClick2(View v){
        Intent intent = new Intent(homescreen.this, profile.class);

        startActivity(intent);

    }

    public void onClick3(View v){
        Intent intent = new Intent(homescreen.this, routesearch.class);

        startActivity(intent);

    }

    public void onClick4(View v){
        Intent intent = new Intent(homescreen.this, helpchat.class);

        startActivity(intent);

    }
}