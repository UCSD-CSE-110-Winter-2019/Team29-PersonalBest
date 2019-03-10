package com.android.personalbest.cloud;

import android.util.Log;
import android.content.Context;

public class CloudstoreServiceFactory {
    private static final String TAG = "[cloudServiceFactory]";

    public static CloudstoreService create(Context context) {
        Log.i(TAG, "Creating CloudstoreService");
        return new FirestoreAdapter(context);
    }
}
