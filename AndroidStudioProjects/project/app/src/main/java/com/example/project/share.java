package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;

public class share extends AppCompatActivity {

    ImageView backk;
    ImageView homee;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        backk=findViewById(R.id.back);
        homee=findViewById(R.id.home);
    }

    public void onClick1(View v){
        finish();
    }

    public void onClick2(View v){
        Intent intent = new Intent(share.this, homescreen.class);

        startActivity(intent);

    }
    public void onClick3(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI);

        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        startActivity(intent);
    }

}