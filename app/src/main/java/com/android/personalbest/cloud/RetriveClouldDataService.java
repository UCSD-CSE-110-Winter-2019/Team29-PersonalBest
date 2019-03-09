package com.android.personalbest.cloud;

import java.util.List;

public interface RetriveClouldDataService {

    void onAppUserCheckCompleted();
    void onIsInUserPendingListCheckCompleted();
    void onIsInFriendListCheckCompleted();
    void onIsInFriendPendingListCheckCompleted();

}
