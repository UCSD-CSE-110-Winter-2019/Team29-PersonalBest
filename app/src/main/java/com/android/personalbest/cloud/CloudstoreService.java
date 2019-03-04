package com.android.personalbest.cloud;

import com.google.firebase.firestore.CollectionReference;

import java.util.Map;

public interface CloudstoreService {

    void setup();
    CollectionReference getCurrentAppUser();
    boolean appUserCheck(String friendEmail);
    void registerFriendsInCloud(String appUser, Map<String, Object> friend);
    boolean isUserFriend(String currentAppUserEmail, String friendEmail);
    boolean isFriendWithUser(String currentAppUserEmail, String friendEmail);
    boolean weAreBothFriendCheck(String currentAppUserEmail, String friendEmail);

}
