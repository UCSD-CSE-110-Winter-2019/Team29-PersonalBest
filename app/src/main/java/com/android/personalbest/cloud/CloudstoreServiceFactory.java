package com.android.personalbest.cloud;

import android.util.Log;

import com.android.personalbest.SignUpFriendPageActivity;

public class CloudstoreServiceFactory {
    private static final String TAG = "[cloudServiceFactory]";

    public static CloudstoreService create(SignUpFriendPageActivity signUpFriendPageActivity, boolean mock) {
        Log.i(TAG, "Creating CloudstoreService");

        if (mock){
            return new TestCloudService(signUpFriendPageActivity);
        }else{
            Log.i(TAG,"creating a real fireStoreAdaper");
            return new FirestoreAdapter(signUpFriendPageActivity);
        }

    }
}
