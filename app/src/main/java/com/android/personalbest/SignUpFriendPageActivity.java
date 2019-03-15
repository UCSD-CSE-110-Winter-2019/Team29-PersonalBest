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
import com.android.personalbest.cloud.RetrieveCloudDataService;

public class SignUpFriendPageActivity extends AppCompatActivity implements RetrieveCloudDataService {

    private Button returnFriendListBtn;
    private Button addFriendsBtn;
    private CloudstoreService cloudstoreService;
    private EditText friendEmail;
    private SharedPrefManager sharedPrefManager;
    private String TAG = "SignUpFriendPageActivity";
    public static boolean mock = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_friend_page);

        sharedPrefManager = new SharedPrefManager(this.getApplicationContext());
        cloudstoreService = CloudstoreServiceFactory.create(this, mock || sharedPrefManager.getMockCloud());

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
                disableUserInteraction();
                //check if the input email is in the appUserList
                cloudstoreService.appUserCheck(SignUpFriendPageActivity.this,sharedPrefManager.getFriendEmail());

            }
        });
    }

    @Override
    public void onAppUserCheckCompleted() {
        //if the input email is in the appUserList check if the inputEmail is in the user pending friendList
        if(cloudstoreService.getAppUserStatus()) {
            cloudstoreService.isInUserPendingListCheck(this, sharedPrefManager.getCurrentAppUserEmail(),sharedPrefManager.getFriendEmail());
        }
        //If the input email is not in the appUserList prompt the user.
        else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "The entered email address does not have a Personal Best account. Please try again.",
                    Toast.LENGTH_LONG);
            toast.show();
            cloudstoreService.resetUserAddFriendProcess();
            enableUserInteraction();
        }
    }

    @Override
    public void onIsInUserPendingListCheckCompleted() {
        //check if the input email is in user friendList
        cloudstoreService.isInFriendListCheck(this, sharedPrefManager.getCurrentAppUserEmail(),sharedPrefManager.getFriendEmail());
    }

    @Override
    public void onIsInFriendListCheckCompleted() {

        // Duplicate Check: Make user the input email not in userPendingList and userFriend
        // if the input email satisfy the dupliacate check condition, add to the user pendingFriendList
        if(!(cloudstoreService.getUserPendingStatus() || cloudstoreService.getFriendStatus())){

            cloudstoreService.addToPendingFriendList(sharedPrefManager.getCurrentAppUserEmail(),sharedPrefManager.getFriendEmail());
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Add To User Pending FriendList (:",
                    Toast.LENGTH_SHORT);
            toast.show();
            //check if the user is in friend's pending list
            cloudstoreService.isInFriendPendingListCheck(this, sharedPrefManager.getCurrentAppUserEmail(), sharedPrefManager.getFriendEmail());

        }
        Log.i(TAG,"getAppUserStatus() => "+ Boolean.toString(cloudstoreService.getAppUserStatus()));
        Log.i(TAG,"getUserPendingStatus() => "+ Boolean.toString(cloudstoreService.getUserPendingStatus()));
        Log.i(TAG,"getFriendStatus() => "+ Boolean.toString(cloudstoreService.getFriendStatus()));
        Log.i(TAG,"getFriendPendingStatus() => "+ Boolean.toString(cloudstoreService.getFriendPendingStatus()));
        enableUserInteraction();
    }

    @Override
    public void onIsInFriendPendingListCheckCompleted() {
        //if user is in friend pending list add them to each other friendlist and remove from each pending friend list
        if(cloudstoreService.getFriendPendingStatus()){
            Log.i(TAG,"user is in friend pending list");
            cloudstoreService.addToFriendList(sharedPrefManager.getCurrentAppUserEmail(),sharedPrefManager.getFriendEmail());
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Add To Friends List Successfully(:",
                    Toast.LENGTH_LONG);
            cloudstoreService.removeFromPendingFriendList(sharedPrefManager.getCurrentAppUserEmail(),sharedPrefManager.getFriendEmail());
            toast.show();
        }

        Log.i(TAG,"getAppUserStatus() => "+ Boolean.toString(cloudstoreService.getAppUserStatus()));
        Log.i(TAG,"getUserPendingStatus() => "+ Boolean.toString(cloudstoreService.getUserPendingStatus()));
        Log.i(TAG,"getFriendPendingStatus() => "+ Boolean.toString(cloudstoreService.getFriendPendingStatus()));
        cloudstoreService.resetUserAddFriendProcess();
    }

    private void disableUserInteraction(){
        friendEmail.setEnabled(false);
        addFriendsBtn.setEnabled(false);
        returnFriendListBtn.setEnabled(false);
    }

    public void enableUserInteraction(){
        friendEmail.setEnabled(true);
        addFriendsBtn.setEnabled(true);
        returnFriendListBtn.setEnabled(true);
    }
}
