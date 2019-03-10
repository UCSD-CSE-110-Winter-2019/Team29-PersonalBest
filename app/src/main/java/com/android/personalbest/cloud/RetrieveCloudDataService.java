package com.android.personalbest.cloud;

public interface RetrieveCloudDataService {

    void onAppUserCheckCompleted();
    void onIsInUserPendingListCheckCompleted();
    void onIsInFriendListCheckCompleted();
    void onIsInFriendPendingListCheckCompleted();
}
