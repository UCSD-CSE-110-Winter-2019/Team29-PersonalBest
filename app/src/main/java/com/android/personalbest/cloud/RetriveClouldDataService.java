package com.android.personalbest.cloud;

import java.util.List;

public interface RetriveClouldDataService {


    void onUserAddFriendCheckCompleted();
    void onFriendAddUserCheckCompleted();
    void onGetFriendListCompleted(List<String> userFriendList);

}
