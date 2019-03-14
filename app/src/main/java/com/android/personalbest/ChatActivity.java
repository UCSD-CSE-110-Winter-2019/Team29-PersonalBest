package com.android.personalbest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    String TAG = MainActivity.class.getSimpleName();
    String COLLECTION_KEY = "chats";
    String DOCUMENT_KEY;
    String MESSAGES_KEY = "messages";
    String FROM_KEY = "from";
    String TEXT_KEY = "text";
    String TIMESTAMP_KEY = "timestamp";

    CollectionReference chat;
    SharedPrefManager sharedPrefManager;
    String from;
    String to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPrefManager = new SharedPrefManager(this.getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        from = sharedPrefManager.getCurrentAppUserEmail();
        to = sharedPrefManager.getCurrentChatFriend();
        if(from.compareTo(to) < 0){
            DOCUMENT_KEY = from + to;
        }
        else{
            DOCUMENT_KEY = to + from;
        }

        chat = FirebaseFirestore.getInstance()
                .collection(COLLECTION_KEY)
                .document(DOCUMENT_KEY)
                .collection(MESSAGES_KEY);

        initMessageUpdateListener();

        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                sendMessage();
            }
        });

        findViewById(R.id.btn_go_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                finish();
            }
        });
    }

    private void sendMessage() {
        EditText messageView = findViewById(R.id.text_message);
        Map<String, String> newMessage = new HashMap<>();
        newMessage.put(FROM_KEY, from);
        newMessage.put(TEXT_KEY, messageView.getText().toString());

        chat.add(newMessage).addOnSuccessListener(result -> {
            messageView.setText("");
        }).addOnFailureListener(error -> {
            Log.e(TAG, error.getLocalizedMessage());
        });
    }

    private void initMessageUpdateListener(){
        chat.orderBy(TIMESTAMP_KEY, Query.Direction.ASCENDING)
            .addSnapshotListener((newChatSnapShot, error) -> {
                if (error != null) {
                    Log.e(TAG, error.getLocalizedMessage());
                    return;
                }
                if (newChatSnapShot != null && !newChatSnapShot.isEmpty() && !newChatSnapShot.getMetadata().hasPendingWrites()) {
                    StringBuilder sb = new StringBuilder();
                    List<DocumentChange> documentChanges = newChatSnapShot.getDocumentChanges();
                    documentChanges.forEach(change -> {
                        QueryDocumentSnapshot document = change.getDocument();
                        sb.append(document.get(FROM_KEY));
                        sb.append(":\n");
                        sb.append(document.get(TEXT_KEY));
                        sb.append("\n");
                        sb.append("---\n");
                    });
                    TextView chatView = findViewById(R.id.chat);
                    chatView.append(sb.toString());
                }
            });
    }
}
