package com.android.personalbest.cloud;

import android.util.Log;

import com.android.personalbest.SignUpFriendPageActivity;

public class CloudstoreServiceFactory {
    private static final String TAG = "[cloudServiceFactory]";

    public static CloudstoreService create(SignUpFriendPageActivity signUpFriendPageActivity) {
        Log.i(TAG, "Creating CloudstoreService");
        return new FirestoreAdapter(signUpFriendPageActivity);
    }
}
