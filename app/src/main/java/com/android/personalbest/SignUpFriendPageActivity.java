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
import com.android.personalbest.cloud.RetriveClouldDataService;

import java.util.List;
import java.util.Set;

public class SignUpFriendPageActivity extends AppCompatActivity implements RetriveClouldDataService {

    private Button returnFriendListBtn;
    private Button addFriendsBtn;
    private CloudstoreService couldstoreService;
    private EditText friendEmail;
    private SharedPrefManager sharedPrefManager;
    private Toast toast;
    private String TAG = "SignUpFriendPageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_friend_page);

        sharedPrefManager = new SharedPrefManager(this);
        couldstoreService = CloudstoreServiceFactory.create(this);


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
                Log.i("currentAppUser",sharedPrefManager.getCurrentAppUserEmail());
                Log.i("InputEmail",friendEmail.getText().toString());
                addFriendProcess();
                couldstoreService.setAppUserStatus(false);
            }
        });
    }

   //Promt User if they enter a right email Address
    private void showAddUserPrompt(){

        if (couldstoreService.getAppUserStatus()){
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

    private void addFriendProcess(){
        couldstoreService.appUserCheck(SignUpFriendPageActivity.this,friendEmail.getText().toString());
    }

    private void addFriend(){

        if(couldstoreService.getAppUserStatus()){
            couldstoreService.addToSentRequestFriendList(sharedPrefManager.getCurrentAppUserEmail(),friendEmail.getText().toString());
        }

    }

    private void addToFriendList(){

        if(couldstoreService.getAppUserStatus() && couldstoreService.weAreBothFriendCheck(sharedPrefManager.getCurrentAppUserEmail(),friendEmail.getText().toString())){
            Set<String> localFriendList = sharedPrefManager.getFriendListSet();
            for(String friend: localFriendList){
                Log.i(TAG," => " + friend +" \n");
            }
            couldstoreService.upDateAppUserFriendList(sharedPrefManager.getCurrentAppUserEmail(),friendEmail.getText().toString());
            Log.i(TAG,friendEmail.getText().toString() + " add to currentAppUser => " + sharedPrefManager.getCurrentAppUserEmail());
        }

    }

    @Override
    public void onUserCheckCompleted() {
        showAddUserPrompt();
        addFriend();
        couldstoreService.isUserAddFriendCheck(sharedPrefManager.getCurrentAppUserEmail(),friendEmail.getText().toString());
    }

    @Override
    public void onUserAddFriendCheckCompleted() {
        couldstoreService.isFriendAddUserCheck(sharedPrefManager.getCurrentAppUserEmail(),friendEmail.getText().toString());
    }

    @Override
    public void onFriendAddUserCheckCompleted() {
        addToFriendList();
        couldstoreService.getFriendList(sharedPrefManager.getCurrentAppUserEmail());
    }

    @Override
    public void onGetFriendListCompleted(List<Object> userFriendList) {
        saveFriendListLocally(userFriendList);
    }

    private void  saveFriendListLocally(List<Object> userFriendList){
        sharedPrefManager.setFriendListSet(userFriendList);
    }
}
