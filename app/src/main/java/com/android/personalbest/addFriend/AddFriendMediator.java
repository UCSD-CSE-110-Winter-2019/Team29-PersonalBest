package com.android.personalbest.addFriend;


import com.android.personalbest.SignUpFriendPageActivity;
import com.android.personalbest.cloud.CloudstoreService;


public class AddFriendMediator{

   private CloudstoreService cloudstoreService;
   private SignUpFriendPageActivity signUpFriendPageActivity;
   private AddFriend addFriend;

   public AddFriendMediator(AddFriend addFriend,SignUpFriendPageActivity signUpFriendPageActivity){
       addFriend = new AddFriend(signUpFriendPageActivity);

   }

    public void onAddFriendUpdate(){
       addFriend.addFriendUpdate();
    }

}
