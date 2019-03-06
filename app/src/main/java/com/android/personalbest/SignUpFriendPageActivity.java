package com.android.personalbest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import static com.android.personalbest.cloud.FirestoreAdapter.addToSentRequestFriendList;
import static com.android.personalbest.cloud.FirestoreAdapter.appUserCheck;
import static com.android.personalbest.cloud.FirestoreAdapter.getAppUserStatus;

public class SignUpFriendPageActivity extends AppCompatActivity  {

    private Button returnFriendListBtn;
    private Button addFriendsBtn;
    private EditText friendEmail;
    private SharedPrefManager sharedPrefManager;

    private String TAG = "SignUpFriendPageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_friend_page);

        sharedPrefManager = new SharedPrefManager(this);



        returnFriendListBtn = findViewById(R.id.returnFriendBtn);
        addFriendsBtn =findViewById(R.id.addBtn);
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
                addFriendsBtn.setEnabled(false);
                returnFriendListBtn.setEnabled(false);
                sharedPrefManager.setFriendEmail(friendEmail.getText().toString());
                appUserCheck(SignUpFriendPageActivity.this,sharedPrefManager.getFriendEmail());
            }
        });
    }

   //Promt User if they enter a right email Address
    private void showAddUserPrompt(){

        if (getAppUserStatus() && !sharedPrefManager.getCurrentAppUserEmail().equals(sharedPrefManager.getFriendEmail())){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "FriendRequestSent (:",
                    Toast.LENGTH_LONG);
            toast.show();

        }else if(!getAppUserStatus() && !sharedPrefManager.getCurrentAppUserEmail().equals(sharedPrefManager.getFriendEmail())) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "The email address you just enter doesn't match our app user record. Please Try Again!!!" ,
                    Toast.LENGTH_LONG);
            toast.show();
        }else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "You are adding Yourself==! Find a Friend^^" ,
                    Toast.LENGTH_LONG);
            toast.show();
        }


    }


    private void SentRequestFriendList(){

        if(getAppUserStatus()){
           addToSentRequestFriendList(sharedPrefManager.getCurrentAppUserEmail(),friendEmail.getText().toString());
        }

    }


    public void onUserCheckCompleted() {
        showAddUserPrompt();
        SentRequestFriendList();
        addFriendsBtn.setEnabled(true);
        returnFriendListBtn.setEnabled(true);

    }




}
