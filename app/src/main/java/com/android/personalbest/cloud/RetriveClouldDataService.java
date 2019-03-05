package com.android.personalbest.cloud;

import java.util.List;

public interface RetriveClouldDataService {

    void onUserCheckCompleted();
    void onUserAddFriendCheckCompleted();
    void onFriendAddUserCheckCompleted();
    void onGetFriendListCompleted(List<Object> userFriendList);

}
