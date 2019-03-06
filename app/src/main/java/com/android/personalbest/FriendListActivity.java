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

import com.android.personalbest.cloud.CloudstoreService;
import com.android.personalbest.cloud.CloudstoreServiceFactory;
import com.android.personalbest.cloud.RetriveClouldDataService;
import static com.android.personalbest.cloud.FirestoreAdapter.getAppUserStatus;
import static com.android.personalbest.cloud.FirestoreAdapter.setAppUserStatus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;

public class FriendListActivity extends AppCompatActivity implements RetriveClouldDataService {

    public ListView listView;
    private Button returnHomeBtn;
    private Button addFriendsBtn;
    private Button refreshFriendListBtn;
    private SharedPrefManager sharedPrefManager;
    private String TAG = "FriendListActivity";
    private CloudstoreService cloudstoreService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        sharedPrefManager = new SharedPrefManager(this);
        cloudstoreService = CloudstoreServiceFactory.create(this);

        listView = findViewById(R.id.friendListView);
        returnHomeBtn = findViewById(R.id.returnHomeBtn);
        addFriendsBtn = findViewById(R.id.addFriBtn);
        refreshFriendListBtn = findViewById(R.id.refreshBtn);

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

                upDateFriendListProcess();
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


    private void upDateFriendListProcess(){
        if(getAppUserStatus()) {
            cloudstoreService.isUserAddFriendCheck(sharedPrefManager.getCurrentAppUserEmail(), sharedPrefManager.getFriendEmail());
        }
    }

    @Override
    public void onUserAddFriendCheckCompleted() {
        cloudstoreService.isFriendAddUserCheck(sharedPrefManager.getCurrentAppUserEmail(),sharedPrefManager.getFriendEmail());
    }

    @Override
    public void onFriendAddUserCheckCompleted() {
        addToFriendList();
        cloudstoreService.getFriendList(sharedPrefManager.getCurrentAppUserEmail());
    }

    @Override
    public void onGetFriendListCompleted(List<String> userFriendList) {
        setFriendListUI();
        setAppUserStatus(false);
        cloudstoreService.resetUserAddFriendProcess();
    }


    private void addToFriendList(){

        if( cloudstoreService.weAreBothFriendCheck(sharedPrefManager.getCurrentAppUserEmail(),sharedPrefManager.getFriendEmail())){
            Set<String> localFriendList;
            if(sharedPrefManager.getFriendListSet() == null){
                localFriendList = new HashSet<>();
            }else {
                localFriendList = sharedPrefManager.getFriendListSet();
            }

            List<String> friendList = new ArrayList<>();

            for(String friend: localFriendList){
                friendList.add(friend);
            }
            friendList.add(sharedPrefManager.getFriendEmail());
            sharedPrefManager.setFriendListSet(friendList);
            cloudstoreService.upDateAppUserFriendList(sharedPrefManager.getCurrentAppUserEmail(),sharedPrefManager.getFriendEmail());

        }

    }
}
