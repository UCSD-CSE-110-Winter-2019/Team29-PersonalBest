package com.android.personalbest.cloud;

import com.google.firebase.firestore.CollectionReference;

public interface CloudstoreService {

    void setup();
    CollectionReference getFriendsList();


}
