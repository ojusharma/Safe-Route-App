package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.content.*;
import androidx.appcompat.app.AppCompatActivity;

public class settings extends AppCompatActivity {


    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        backButton = findViewById(R.id.button3);
    }

        public void back(View v){
            Intent intent = new Intent(settings.this, homescreen.class);

            startActivity(intent);

        }
    public void GOTOUSER(View v){
        Intent intent = new Intent(settings.this, profile.class);

        startActivity(intent);

    }

}