package com.android.personalbest.cloud;

public interface RetrieveCloudDataService {
    //onCompleted check
    void onAppUserCheckCompleted();
    void onIsInUserPendingListCheckCompleted();
    void onIsInFriendListCheckCompleted();
    void onIsInFriendPendingListCheckCompleted();
}
