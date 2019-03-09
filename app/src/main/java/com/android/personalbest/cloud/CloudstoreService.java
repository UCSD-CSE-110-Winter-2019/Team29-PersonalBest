package com.android.personalbest.cloud;

import com.android.personalbest.SignUpFriendPageActivity;
import com.google.firebase.firestore.CollectionReference;

public interface CloudstoreService {


    CollectionReference getCurrentAppUser();
    void appUserCheck(SignUpFriendPageActivity signUpFriendPageActivity, String friendEmail);

    void isInUserPendingListCheck(final String currentAppUserEmail, final String friendEmail);
    void isInFriendPendingListCheck(final String currentAppUserEmail, final String friendEmail);

    void isInFriendListCheck(final String currentAppUserEmail, final String friendEmail);


    void addToPendingFriendList(String currentAppUserEmail, String friendEmail);
    void removeFromPendingFriendList(String currentAppUserEmail, String friendEmail);
    void addToFriendList(String currentAppUserEmail,String friendEmail);



    void setAppUserStatus(boolean appUserStatus);
    boolean getAppUserStatus();



    void setUserPendingStatus(boolean userPendingStatus);
    boolean getUserPendingStatus();

    void setFriendPendingStatus(boolean friendPendingStatus);
    boolean getFriendPendingStatus();

    void setFriendStatus(boolean friendStatus);
    boolean getFriendStatus();


    void resetUserAddFriendProcess();

}
