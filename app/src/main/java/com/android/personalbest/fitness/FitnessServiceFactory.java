package com.android.personalbest.fitness;

import android.util.Log;

import com.android.personalbest.MainPageActivity;

public class FitnessServiceFactory {

    private static final String TAG = "[FitnessServiceFactory]";

    public static FitnessService create(MainPageActivity mainPageActivity, boolean mock) {
        Log.i(TAG, "Creating FitnessService");
        if (mock) {
            return new TestFitnessService(mainPageActivity);
        }
        else {
            return new GoogleFitAdapter(mainPageActivity);
        }
    }

}
