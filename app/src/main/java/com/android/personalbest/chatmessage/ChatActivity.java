package com.android.personalbest.chatmessage;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.personalbest.MainActivity;
import com.android.personalbest.R;
import com.android.personalbest.SharedPrefManager;
import com.android.personalbest.cloud.CloudstoreService;
import com.android.personalbest.cloud.CloudstoreServiceFactory;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    String TAG = MainActivity.class.getSimpleName();

    public SharedPrefManager sharedPrefManager;
    private CloudstoreService cloudstoreService;
    private ChatMessage chatMessage;
    public static boolean mock = false;
    private String from;
    private String to;
    private String FROM_KEY = "from";
    private String TEXT_KEY = "text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPrefManager = new SharedPrefManager(this.getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        from = sharedPrefManager.getCurrentAppUserEmail();
        to = sharedPrefManager.getCurrentChatFriend();

        cloudstoreService = CloudstoreServiceFactory.create(this.getApplicationContext(),mock);
        cloudstoreService.initChat(from,to);
        cloudstoreService.initMessageUpdateListener();

        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                cloudstoreService.sendMessage(buildMessage());
            }
        });

        findViewById(R.id.btn_go_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                finish();
            }
        });
    }

    private Map<String, String> buildMessage(){
        EditText messageView = findViewById(R.id.text_message);
        Map<String, String> newMessage = new HashMap<>();
        newMessage.put(FROM_KEY, from);
        newMessage.put(TEXT_KEY, messageView.getText().toString());
        return newMessage;
    }

}
