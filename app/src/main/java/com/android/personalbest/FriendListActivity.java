package com.android.personalbest;

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

import java.util.List;

import java.util.Set;

import static com.android.personalbest.cloud.FirestoreAdapter.getFriendList;

public class FriendListActivity extends AppCompatActivity {

    public ListView listView;
    private Button returnHomeBtn;
    private Button addFriendsBtn;
    private Button refreshFriendListBtn;
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
        refreshFriendListBtn = findViewById(R.id.refreshFriendListBtn);

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
        refreshFriendListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFriendList(FriendListActivity.this,sharedPrefManager.getCurrentAppUserEmail());

            }
        });
    }

    private void launchSignUpFriendPageActivity(){
        Intent addFriends = new Intent(this,SignUpFriendPageActivity.class);
        startActivity(addFriends);
    }

    public void onGetFriendListCompleted(List<String> userFriendList){

        if (userFriendList.isEmpty()){
            Log.i(TAG,"database FriendList is empty");
        }else {
            Log.i(TAG,"database FriendList is not empty");
            sharedPrefManager.setFriendListSet(userFriendList);
        }

        setFriendListUI();

    }

    private void setFriendListUI(){

        ArrayList<String> friendList = new ArrayList<>();
        Set<String>friendListSet;
        if(sharedPrefManager.getFriendListSet() == null){
            Log.i(TAG,"local FriendList is empty");
        }else{
            friendListSet = sharedPrefManager.getFriendListSet();
            for(String friend: friendListSet){
                friendList.add(friend);
            }
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(FriendListActivity.this,android.R.layout.simple_list_item_1,friendList);
        listView.setAdapter(arrayAdapter);

    }





}
