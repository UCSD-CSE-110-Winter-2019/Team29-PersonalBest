package com.android.personalbest.cloud;

import android.util.Log;
import android.content.Context;

public class CloudstoreServiceFactory {
    private static final String TAG = "[cloudServiceFactory]";

    public static CloudstoreService create(Context context, boolean mock) {
        Log.i(TAG, "Creating CloudstoreService");

        if (mock){
            return new TestCloudService(context);
        }else{
            Log.i(TAG,"creating a real fireStoreAdapter");
            return new FirestoreAdapter(context);
        }
    }
}
