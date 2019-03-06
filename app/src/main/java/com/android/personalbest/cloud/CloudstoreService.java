package com.android.personalbest.cloud;

import com.android.personalbest.SignUpFriendPageActivity;
import com.google.firebase.firestore.CollectionReference;

public interface CloudstoreService {


    CollectionReference getCurrentAppUser();


    void isUserAddFriendCheck(final String currentAppUserEmail, final String friendEmail);
    void isFriendAddUserCheck(String currentAppUserEmail, String friendEmail);
    boolean weAreBothFriendCheck(String currentAppUserEmail, String friendEmail);

    void upDateAppUserFriendList(String appUser,String friendEmail);

    void getFriendList(String currentAppUserEmail);
    void setUserAddFriendStatus(boolean userAddFriendStatus);
    boolean getUserAddFriendStatus();
    void setFriendAddUserStatus(boolean friendAddUserStatus);
    boolean getFriendAddUserStatus();
    void resetUserAddFriendProcess();

}
