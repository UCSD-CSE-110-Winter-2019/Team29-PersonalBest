package com.android.personalbest;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;

public class FriendListActivity extends AppCompatActivity {

    public ListView listView;
    private Button returnHomeBtn;
    private Button addFriendsBtn;
    private SharedPrefManager sharedPrefManager;
    private String TAG = "FriendListActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        sharedPrefManager = new SharedPrefManager(this);

        listView = findViewById(R.id.friendListView);
        returnHomeBtn = findViewById(R.id.returnHomeBtn);
        addFriendsBtn = findViewById(R.id.addFriBtn);

        Log.i(TAG,"onCreate setFriendListUI() Get Call");
        setFriendListUI();

        returnHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addFriendsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSignUpFriendPageActivity();
            }
        });
    }

    private void launchSignUpFriendPageActivity(){
        Intent addFriends = new Intent(this,SignUpFriendPageActivity.class);
        startActivity(addFriends);
    }

    //get friendList from sharePreManager, pass data and display the friendlist in listView
    private void setFriendListUI(){
        Set<String>friendListSet;
        if(sharedPrefManager.getFriendListSet() == null){
            friendListSet = new HashSet<>();

        }else {
            friendListSet = sharedPrefManager.getFriendListSet();
        }
        ArrayList<String> friendList = new ArrayList<String>();
        if(friendListSet.isEmpty()){
            Log.i(TAG,"friendListSet is empty:");
        }
        for(String friend: friendListSet){

                friendList.add(friend);
                Log.i(TAG,"I am here in the friendList:"+ friend + "\n");

        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,friendList);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"onResume: setFriendListUI() Get Call");
        setFriendListUI();
    }


}
