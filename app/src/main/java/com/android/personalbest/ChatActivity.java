package com.android.personalbest;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.view.View;
import static com.android.personalbest.cloud.FirestoreAdapter.messageHandlerSetUp;
import static com.android.personalbest.cloud.FirestoreAdapter.initMessageUpdateListener;
import static com.android.personalbest.cloud.FirestoreAdapter.sendMessage;


public class ChatActivity extends AppCompatActivity {

    public SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPrefManager = new SharedPrefManager(this.getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageHandlerSetUp(this);
        initMessageUpdateListener(this);

        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                sendMessage(ChatActivity.this);
            }
        });

        findViewById(R.id.btn_go_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                finish();
            }
        });
    }

}
