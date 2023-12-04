package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.R;

public class helpchat extends AppCompatActivity {
    EditText input;
    TextView t1;
    TextView t2;
    TextView t3;
    TextView t4;
    TextView t5;
    TextView t6;

    String andar ;
    ImageView t1p;
    ImageView t2p;
    ImageView t3p;
    ImageView t4p;
    ImageView t5p;
    ImageView t6p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpchat);
        t1 = findViewById(R.id.text1);
        t2 = findViewById(R.id.text2);
        t3 = findViewById(R.id.text3);
        t4 = findViewById(R.id.text4);
        t1p= findViewById(R.id.imageView);
        t2p= findViewById(R.id.imageView2);
        t3p= findViewById(R.id.imageView3);
        t4p= findViewById(R.id.imageView4);
        t5 = findViewById(R.id.text5);
        t6 = findViewById(R.id.text6);
        t5p =  findViewById(R.id.imageView5);
        t6p = findViewById(R.id.imageView6);
        input = findViewById(R.id.editTextTextMultiLine);
        //t5 here with delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Code to be executed after 2 seconds
                // For example, displaying a TextView

                t5.setText("Hi How Can i help you ?");
                t5.setBackgroundResource(R.drawable.chat_bubble2);
                t5p.setImageResource(R.drawable.profile_icon);

            }
        }, 2500);


    }
    public void USERTEXTED(View view){
        andar = input.getText().toString();

        input.setText("");
        if(andar.length() == 0){

        }
        else {
            if (t1.getText().toString().length() == 0) {
                t1.setText("  " + andar);
                t1.setBackgroundResource(R.drawable.chat_bubble);
                t1p.setImageResource(R.drawable.profile_icon);
            } else if (t2.getText().toString().length() == 0) {
                t2.setText("  " + andar);
                t2.setBackgroundResource(R.drawable.chat_bubble);
                t2p.setImageResource(R.drawable.profile_icon);

            } else if (t3.getText().toString().length() == 0) {
                t3.setText("  " + andar);
                t3.setBackgroundResource(R.drawable.chat_bubble);
                t3p.setImageResource(R.drawable.profile_icon);
            } else {
                t4.setText("  " + andar);
                t4.setBackgroundResource(R.drawable.chat_bubble);
                t4p.setImageResource(R.drawable.profile_icon);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Code to be executed after 2 seconds
                        // For example, displaying a TextView

                        t6.setText("Call 911 please !");
                        t6.setBackgroundResource(R.drawable.chat_bubble2);
                        t6p.setImageResource(R.drawable.profile_icon);

                    }
                }, 2500);
            }
        }
    }
}