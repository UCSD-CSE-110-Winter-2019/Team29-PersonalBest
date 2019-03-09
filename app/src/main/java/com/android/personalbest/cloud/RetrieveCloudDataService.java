package com.android.personalbest.cloud;

import java.util.List;

public interface RetrieveCloudDataService {

    void onAppUserCheckCompleted();
    void onIsInUserPendingListCheckCompleted();
    void onIsInFriendListCheckCompleted();
    void onIsInFriendPendingListCheckCompleted();

}
