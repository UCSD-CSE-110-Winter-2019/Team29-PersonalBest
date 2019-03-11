package com.android.personalbest.addFriend;

import android.util.Log;
import android.widget.Toast;

import com.android.personalbest.SignUpFriendPageActivity;
import com.android.personalbest.cloud.CloudstoreService;
import com.android.personalbest.cloud.CloudstoreServiceFactory;

public class AddFriendMediator implements IAddFriendObserver{

   private CloudstoreService cloudstoreService;
   private SignUpFriendPageActivity signUpFriendPageActivity;
   private AddFriend addFriend;

   public AddFriendMediator(AddFriend addFriend,SignUpFriendPageActivity signUpFriendPageActivity){
       cloudstoreService = CloudstoreServiceFactory.create(signUpFriendPageActivity,false);
       addFriend = new AddFriend();
       addFriend.register(this);

   }


    @Override
    public void onAppUserCheckCompleted() {
        //if the input email is in the appUserList check if the inputEmail is in the user pending friendList
        if(cloudstoreService.getAppUserStatus()){
            cloudstoreService.isInUserPendingListCheck(signUpFriendPageActivity.sharedPrefManager.getCurrentAppUserEmail(),
                                                        signUpFriendPageActivity.sharedPrefManager.getFriendEmail());
        }
    }


    @Override
    public void onIsInUserPendingListCheckCompleted() {
        //check if the input email is in user friendList
        cloudstoreService.isInFriendListCheck(signUpFriendPageActivity.sharedPrefManager.getCurrentAppUserEmail(),
                signUpFriendPageActivity.sharedPrefManager.getFriendEmail());

    }

    @Override
    public void onIsInFriendListCheckCompleted() {
        // Duplicate Check: Make user the input email not in userPendingList and userFriend
        // if the input email satisfy the dupliacate check condition, add to the user pendingFriendList
        if(!(cloudstoreService.getUserPendingStatus() || cloudstoreService.getFriendStatus())){

            cloudstoreService.addToPendingFriendList(signUpFriendPageActivity.sharedPrefManager.getCurrentAppUserEmail(),signUpFriendPageActivity.sharedPrefManager.getFriendEmail());

            //check if the user is in friend's pending list
            cloudstoreService.isInFriendPendingListCheck(signUpFriendPageActivity.sharedPrefManager.getCurrentAppUserEmail(),signUpFriendPageActivity.sharedPrefManager.getFriendEmail());

        }
    }

    @Override
    public void onIsInFriendPendingListCheckCompleted() {
        //if user is in friend pending list add them to each other friendlist and remove from each pending friend list
        if(cloudstoreService.getFriendPendingStatus()){

            cloudstoreService.addToFriendList(signUpFriendPageActivity.sharedPrefManager.getCurrentAppUserEmail(),signUpFriendPageActivity.sharedPrefManager.getFriendEmail());
            cloudstoreService.removeFromPendingFriendList(signUpFriendPageActivity.sharedPrefManager.getCurrentAppUserEmail(),signUpFriendPageActivity.sharedPrefManager.getFriendEmail());

        }
    }

    public void onAddFriendUpdate(){
       addFriend.addFriendUpdate();
    }

}
