package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class helpchat extends AppCompatActivity {

    ImageView backk;
    ImageView homee;
    RecyclerView recyclerViewChat;
    EditText editTextText;
    Button buttonSend;
    ChatAdapter chatAdapter;
    TextView initialMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpchat);

        backk = findViewById(R.id.back);
        homee = findViewById(R.id.home);
        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        editTextText = findViewById(R.id.editTextText);
        buttonSend = findViewById(R.id.buttonSend);
        initialMessage = findViewById(R.id.initialMessage);

        // Display initial message
        initialMessage.setText("What do you want help with?");

        // Initialize RecyclerView and ChatAdapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewChat.setLayoutManager(layoutManager);

        List<Message> messages = new ArrayList<>();
        chatAdapter = new ChatAdapter(messages);
        recyclerViewChat.setAdapter(chatAdapter);
    }

    public void onClick1(View v) {
        finish();
    }

    public void onClick2(View v) {
        Intent intent = new Intent(helpchat.this, homescreen.class);
        startActivity(intent);
    }

    // Handle the send button click
    public void onSendButtonClick(View view) {
        String userMessage = editTextText.getText().toString().trim();
        if (!userMessage.isEmpty()) {
            // Display user's message in RecyclerView
            chatAdapter.addMessage(new Message(userMessage, true));

            // Simulate a response (replace with your actual logic)
            String response = "Thank you for your message! I'll get back to you shortly.";
            chatAdapter.addMessage(new Message(response, false));

            // Clear the message input
            editTextText.setText("");

            // Scroll to the last message
            recyclerViewChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
        }
    }
}
