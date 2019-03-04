package com.android.personalbest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.personalbest.cloud.CloudstoreService;
import com.android.personalbest.cloud.CloudstoreServiceFactory;

import java.util.HashMap;
import java.util.Map;

public class SignUpFriendPageActivity extends AppCompatActivity {

    private Button returnFriendListBtn;
    private Button addFriendsBtn;
    private CloudstoreService couldstoreService;
    private EditText friendEmail;
    private boolean isFriendAdd;
    private SharedPrefManager sharedPrefManager;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_friend_page);

        sharedPrefManager = new SharedPrefManager(this);
        couldstoreService = CloudstoreServiceFactory.create();


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
                Log.i("InputEmail",friendEmail.getText().toString());
                isFriendAdd = couldstoreService.appUserCheck(friendEmail.getText().toString());
                addUserPrompt();


            }
        });
    }

    private void addUserPrompt(){

        if (isFriendAdd){
            Map<String, Object> friend = new HashMap<>();
            friend.put(friendEmail.getText().toString(),true);
            couldstoreService.registerFriendsInCloud(sharedPrefManager.getCurrentAppUserEmail(),friend);
            toast = Toast.makeText(getApplicationContext(),
                    "FriendRequestSent (:",
                    Toast.LENGTH_LONG);

        }else{
            toast = Toast.makeText(getApplicationContext(),
                    "The email address you just enter doesn't match our app user record. Please Try Again!!!" ,
                    Toast.LENGTH_LONG);
        }


        toast.show();
    }

    private void upDataFriendListUI(String currentAppUserEmail, String friendEmail){

        if(couldstoreService.weAreBothFriendCheck(currentAppUserEmail,friendEmail)){

        }

    }




}
