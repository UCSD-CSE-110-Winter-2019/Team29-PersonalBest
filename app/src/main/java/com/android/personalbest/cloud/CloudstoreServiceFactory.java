package com.android.personalbest.cloud;

import android.util.Log;



public class CloudstoreServiceFactory {
    private static final String TAG = "[cloudServiceFactory]";

    public static CouldstoreService create() {
        Log.i(TAG, "Creating CloudstoreService");

            return new FirestoreAdapter();

    }
}
