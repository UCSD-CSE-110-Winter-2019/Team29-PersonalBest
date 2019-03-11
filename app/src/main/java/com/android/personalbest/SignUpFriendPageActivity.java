package com.android.personalbest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.personalbest.addFriend.AddFriend;
import com.android.personalbest.addFriend.AddFriendMediator;
import com.android.personalbest.cloud.CloudstoreService;
import com.android.personalbest.cloud.CloudstoreServiceFactory;


public class SignUpFriendPageActivity extends AppCompatActivity {

    private Button returnFriendListBtn;
    private Button addFriendsBtn;
    private CloudstoreService cloudstoreService;
    private EditText friendEmail;
    public SharedPrefManager sharedPrefManager;
    private String TAG = "SignUpFriendPageActivity";
    private AddFriendMediator addFriendMediator;
    private AddFriend addFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_friend_page);

        addFriendMediator = new AddFriendMediator(addFriend,this);
        sharedPrefManager = new SharedPrefManager(this);


        returnFriendListBtn = findViewById(R.id.returnFriendBtn);
        addFriendsBtn = findViewById(R.id.addBtn);
        friendEmail = findViewById(R.id.friendEmail);

        returnFriendListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addFriendsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefManager.setFriendEmail(friendEmail.getText().toString());
                addFriendMediator.onAddFriendUpdate();
            }
        });
    }


    private void disableUserInteraction(){
        friendEmail.setEnabled(false);
        addFriendsBtn.setEnabled(false);
        returnFriendListBtn.setEnabled(false);
    }

    private void enableUserInteraction(){
        friendEmail.setEnabled(true);
        addFriendsBtn.setEnabled(true);
        returnFriendListBtn.setEnabled(true);
    }
}
