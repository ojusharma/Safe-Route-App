package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.ImageReader;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class routesearch extends AppCompatActivity {

    ImageView setting;ImageView profile;

    EditText starting;
    EditText destination;
    Button b;
    String start;
    String dest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routesearch);

        setting=findViewById(R.id.settings);
        profile=findViewById(R.id.pro);
        starting=findViewById(R.id.startingLocationEditText);
        destination=findViewById(R.id.destinationEditText);
        b=findViewById(R.id.showRouteButton);

    }

    public void onClick1(View v){
        Intent intent = new Intent(routesearch.this, settings.class);

        startActivity(intent);

    }

    public void back(View view)
    {
        finish();
    }

    public void onClick2(View v){
        Intent intent = new Intent(routesearch.this, profile.class);

        startActivity(intent);

    }

    public void onClick3(View v){
        start=starting.getText().toString();
        dest=starting.getText().toString();

        if(start.isEmpty()){

            Toast toast = Toast.makeText(getApplicationContext(),"Please Enter your Starting Destination ", Toast.LENGTH_SHORT );
            toast.show();
        }
        else if( dest.length() == 0){
            Toast toast = Toast.makeText(getApplicationContext(),"Please Enter your Final Destination", Toast.LENGTH_SHORT );
            toast.show();

        }
        else {
            Intent intent = new Intent(routesearch.this, routeinfo.class);
            intent.putExtra("Start", start);
            intent.putExtra("End", dest);
            startActivity(intent);
        }



    }
}