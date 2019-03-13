package com.android.personalbest.cloud;

import com.android.personalbest.MonthlyDataList;
import com.android.personalbest.SignUpFriendPageActivity;
import android.content.Context;

import java.util.Map;

public class TestCloudService implements CloudstoreService{

    private boolean userPendingStatus = false;
    private boolean friendPendingStatus = false;
    private boolean friendStatus = false;
    private boolean isAppUser = false;

    private Context context;

    public TestCloudService(Context context){
        this.context = context;
    }

    @Override
    public void setAppUserInCloud(String appUser, Map<String, Object> friend) { }

    @Override
    public void appUserCheck(SignUpFriendPageActivity signUpFriendPageActivity, String friendEmail) {
        setAppUserStatus(true);
    }

    @Override
    public void isInUserPendingListCheck(SignUpFriendPageActivity signUpFriendPageActivity, String currentAppUserEmail, String friendEmail) {
        setUserPendingStatus(true);
    }

    @Override
    public void isInFriendListCheck(SignUpFriendPageActivity signUpFriendPageActivity, String currentAppUserEmail, String friendEmail) {
        setFriendStatus(true);
    }

    @Override
    public void isInFriendPendingListCheck(SignUpFriendPageActivity signUpFriendPageActivity, String currentAppUserEmail, String friendEmail) {
        setFriendPendingStatus(true);
    }

    @Override
    public void addToPendingFriendList(String currentAppUserEmail, String friendEmail) {
        setFriendStatus(true);
    }

    @Override
    public void addToFriendList(String currentAppUserEmail, String friendEmail) {
        setFriendStatus(true);
    }

    @Override
    public void removeFromPendingFriendList(String currentAppUserEmail, String friendEmail) {
        setFriendStatus(true);
    }

    @Override
    public void setAppUserStatus(boolean appUserStatus) {
        this.isAppUser = appUserStatus;
    }

    @Override
    public boolean getAppUserStatus() {
        return this.isAppUser;
    }

    @Override
    public void setFriendStatus(boolean friendStatus) {
        this.friendStatus = friendStatus;
    }

    @Override
    public boolean getFriendStatus() {
        return this.friendStatus;
    }

    @Override
    public void setUserPendingStatus(boolean userPendingStatus) {
        this.userPendingStatus = userPendingStatus;
    }

    @Override
    public boolean getUserPendingStatus() {
        return this.userPendingStatus;
    }

    @Override
    public void setFriendPendingStatus(boolean friendPendingStatus) {
        this.friendPendingStatus = friendPendingStatus;
    }

    @Override
    public boolean getFriendPendingStatus() {
        return  this.friendPendingStatus;
    }

    @Override
    public void resetUserAddFriendProcess() {
        setAppUserStatus(true);
    }

    //For testing, can add mock implementations here
    public void storeMonthlyActivityForNewUser(String currentAppUserEmail) {}
    public void updateMonthlyActivityEndOfDay(String currentAppUserEmail) {}
    public void updateMonthlyActivityData(String currentAppUserEmail, int dayIndex) {}
    public void updateTodayData(String currentAppUserEmail) {}
    public void getMonthlyActivity(String userEmail, MonthlyDataList dataList) {}
}
