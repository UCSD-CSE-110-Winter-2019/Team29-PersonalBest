package com.android.personalbest.cloud;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreAdapter implements CouldstoreService{
    private String COLLECTION_KEY = "userInfo";

    public CollectionReference user;

    @Override
    public void setup(){
        user = FirebaseFirestore.getInstance()
                .collection(COLLECTION_KEY);

    }

    @Override
    public CollectionReference getUser() {
        return user;
    }
}
