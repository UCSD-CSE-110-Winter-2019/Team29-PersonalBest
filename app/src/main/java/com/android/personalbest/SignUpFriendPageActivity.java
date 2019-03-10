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

public class SignUpFriendPageActivity extends AppCompatActivity implements RetriveClouldDataService {

    private Button returnFriendListBtn;
    private Button addFriendsBtn;
    private CloudstoreService couldstoreService;
    private EditText friendEmail;
    private SharedPrefManager sharedPrefManager;
    private String TAG = "SignUpFriendPageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_friend_page);

        sharedPrefManager = new SharedPrefManager(this);
        couldstoreService = CloudstoreServiceFactory.create(this,false);


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
                sharedPrefManager.setFriendEmail(friendEmail.getText().toString());
                disableUserInteraction();
                //check if the input email is in the appUserList
                couldstoreService.appUserCheck(SignUpFriendPageActivity.this,sharedPrefManager.getFriendEmail());

            }
        });
    }

    @Override
    public void onAppUserCheckCompleted() {

        //if the input email is in the appUserList check if the inputEmail is in the user pending friendList
        if(couldstoreService.getAppUserStatus()) {
            couldstoreService.isInUserPendingListCheck(sharedPrefManager.getCurrentAppUserEmail(),sharedPrefManager.getFriendEmail());
        }
        //If the input email is not in the appUserList prompt the user.
        else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "The email address you just enter doesn't match our app user record. Please Try Again!!!",
                    Toast.LENGTH_LONG);
            toast.show();
            couldstoreService.resetUserAddFriendProcess();
            enableUserInteraction();
        }

    }

    @Override
    public void onIsInUserPendingListCheckCompleted() {
        //check if the input email is in user friendList
        couldstoreService.isInFriendListCheck(sharedPrefManager.getCurrentAppUserEmail(),sharedPrefManager.getFriendEmail());
    }

    @Override
    public void onIsInFriendListCheckCompleted() {

        // Duplicate Check: Make user the input email not in userPendingList and userFriend
        // if the input email satisfy the dupliacate check condition, add to the user pendingFriendList
        if(!(couldstoreService.getUserPendingStatus() || couldstoreService.getFriendStatus())){

            couldstoreService.addToPendingFriendList(sharedPrefManager.getCurrentAppUserEmail(),sharedPrefManager.getFriendEmail());
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Add To User Pending FriendList (:",
                    Toast.LENGTH_SHORT);
            toast.show();
            //check if the user is in friend's pending list
            couldstoreService.isInFriendPendingListCheck(sharedPrefManager.getCurrentAppUserEmail(), sharedPrefManager.getFriendEmail());

        }
        Log.i(TAG,"getAppUserStatus() => "+ Boolean.toString(couldstoreService.getAppUserStatus()));
        Log.i(TAG,"getUserPendingStatus() => "+ Boolean.toString(couldstoreService.getUserPendingStatus()));
        Log.i(TAG,"getFriendStatus() => "+ Boolean.toString(couldstoreService.getFriendStatus()));
        Log.i(TAG,"getFriendPendingStatus() => "+ Boolean.toString(couldstoreService.getFriendPendingStatus()));
        enableUserInteraction();

    }

    @Override
    public void onIsInFriendPendingListCheckCompleted() {

        //if user is in friend pending list add them to each other friendlist and remove from each pending friend list
        if(couldstoreService.getFriendPendingStatus()){
            Log.i(TAG,"user is in friend pending list");
            couldstoreService.addToFriendList(sharedPrefManager.getCurrentAppUserEmail(),sharedPrefManager.getFriendEmail());
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Add To Friends List Successfully(:",
                    Toast.LENGTH_LONG);
            couldstoreService.removeFromPendingFriendList(sharedPrefManager.getCurrentAppUserEmail(),sharedPrefManager.getFriendEmail());
            couldstoreService.removeFromPendingFriendList(sharedPrefManager.getFriendEmail(),sharedPrefManager.getCurrentAppUserEmail());
            toast.show();
        }

        Log.i(TAG,"getAppUserStatus() => "+ Boolean.toString(couldstoreService.getAppUserStatus()));
        Log.i(TAG,"getUserPendingStatus() => "+ Boolean.toString(couldstoreService.getUserPendingStatus()));
        Log.i(TAG,"getFriendPendingStatus() => "+ Boolean.toString(couldstoreService.getFriendPendingStatus()));
        couldstoreService.resetUserAddFriendProcess();
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
