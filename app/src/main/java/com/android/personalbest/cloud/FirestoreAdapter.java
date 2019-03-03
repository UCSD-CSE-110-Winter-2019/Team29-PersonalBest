package com.android.personalbest.cloud;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreAdapter implements CloudstoreService {
    private String COLLECTION_KEY = "friendsList";

    public CollectionReference friendsList;

    @Override
    public void setup(){
        friendsList = FirebaseFirestore.getInstance()
                .collection(COLLECTION_KEY);

    }

    @Override
    public CollectionReference getFriendsList() {
        return friendsList;
    }
}
