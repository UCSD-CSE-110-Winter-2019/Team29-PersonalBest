package com.android.personalbest.cloud;

import com.android.personalbest.FriendListActivity;
import com.android.personalbest.SignUpFriendPageActivity;

import java.util.Map;

public interface CloudstoreService {

    void setAppUserInCloud(String appUser, Map<String, Object> friend);

    void appUserCheck(SignUpFriendPageActivity signUpFriendPageActivity, String friendEmail);

    void isInUserPendingListCheck(SignUpFriendPageActivity signUpFriendPageActivity, final String currentAppUserEmail, final String friendEmail);
    void isInFriendListCheck(SignUpFriendPageActivity signUpFriendPageActivity, final String currentAppUserEmail, final String friendEmail);

    void isInFriendPendingListCheck(SignUpFriendPageActivity signUpFriendPageActivity, final String currentAppUserEmail, final String friendEmail);
    void addToPendingFriendList(String currentAppUserEmail, String friendEmail);

    void addToFriendList(String currentAppUserEmail,String friendEmail);
    void removeFromPendingFriendList(String currentAppUserEmail, String friendEmail);

    void setAppUserStatus(boolean appUserStatus);
    boolean getAppUserStatus();

    void setFriendStatus(boolean friendStatus);
    boolean getFriendStatus();

    void setUserPendingStatus(boolean userPendingStatus);
    boolean getUserPendingStatus();

    void setFriendPendingStatus(boolean friendPendingStatus);
    boolean getFriendPendingStatus();

    void resetUserAddFriendProcess();

    void storeMonthlyActivityForNewUser(String currentAppUserEmail);
    void updateMonthlyActivityEndOfDay(String currentAppUserEmail);
    void updateMonthlyActivityData(String currentAppUserEmail);
    void getFriendMonthlyActivity(String friendEmail);
}
