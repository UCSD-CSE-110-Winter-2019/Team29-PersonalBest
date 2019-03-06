package com.android.personalbest.cloud;

import android.util.Log;

import com.android.personalbest.FriendListActivity;



public class CloudstoreServiceFactory {
    private static final String TAG = "[cloudServiceFactory]";

    public static CloudstoreService create(FriendListActivity friendListActivity) {
        Log.i(TAG, "Creating CloudstoreService");
        return new FirestoreAdapter(friendListActivity);

    }


}
