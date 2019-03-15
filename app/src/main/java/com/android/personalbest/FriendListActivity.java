package com.android.personalbest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
    public AlertDialog.Builder dialog;
    public AlertDialog dialogBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        sharedPrefManager = new SharedPrefManager(this.getApplicationContext());
        listView = findViewById(R.id.friendListView);
        returnHomeBtn = findViewById(R.id.returnHomeBtn);
        addFriendsBtn = findViewById(R.id.addFriBtn);
        refreshFriendListBtn = findViewById(R.id.refreshFriendListBtn);
        Log.i(TAG,"onCreate setFriendListUI() Get Call");
        setFriendListUI();

        //initializing window with options after clicking on friend
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                final String friendEmail = (String)parent.getItemAtPosition(position);
                Log.d("blah", "friend's email is " + friendEmail);
                dialog = new AlertDialog.Builder(FriendListActivity.this);
                dialog.setItems(new CharSequence[]
                                {"Chat", "See Activity", "Cancel"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int index) {
                                // The 'index' argument contains the index position of the selected item
                                switch (index) {
                                    case 0:
                                        sharedPrefManager.setCurrentChatFriend(friendEmail);
                                        launchChatActivity();
                                    case 1:
                                        sharedPrefManager.setMonthlyEmail(friendEmail);
                                        launchMonthlyActivity();
                                    case 2:
                                        dialog.dismiss();
                                }
                            }
                        });
                dialogBox = dialog.create();
                dialogBox.show();
            }
        });

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

    private void launchChatActivity(){
        Intent chat = new Intent(this, ChatActivity.class);
        startActivity(chat);
    }

    private void launchMonthlyActivity() {
        Intent monthlyActivity = new Intent(this, MonthlyBarChartActivity.class);
        startActivity(monthlyActivity);
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
