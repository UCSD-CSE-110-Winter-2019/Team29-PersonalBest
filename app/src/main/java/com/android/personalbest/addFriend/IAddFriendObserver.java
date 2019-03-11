package com.android.personalbest.addFriend;

public interface IAddFriendObserver {
    void onAppUserCheckCompleted();
    void onIsInUserPendingListCheckCompleted();
    void onIsInFriendListCheckCompleted();
    void onIsInFriendPendingListCheckCompleted();
}
