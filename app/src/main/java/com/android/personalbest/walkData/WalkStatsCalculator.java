package com.android.personalbest.walkData;

import android.app.Activity;
import android.content.res.Resources;

import com.android.personalbest.R;
import com.android.personalbest.SharedPrefManager;

public class WalkStatsCalculator {
    SharedPrefManager sharedPrefManager;
    Resources res;

    public static final float STRIDE_LENGTH_MULTIPLIER = 0.413f;

    public WalkStatsCalculator(Activity activity) {
        sharedPrefManager = new SharedPrefManager(activity.getApplicationContext());
        res = activity.getResources();
    }

    //Reference for calculating miles: https://www.openfit.com/how-many-steps-walk-per-mile
    //Pass in height from Shared Pref (int heightInInches = sharedPrefManager.getHeight();)
    //Save output to Shared Pref to avoid repeated calculation (sharedPrefManager.storeNumStepsInMile(numStepsInMile);)

    public int calculateNumStepsInMile(int heightInInches) {
        float averageStrideLengthInInches = heightInInches * STRIDE_LENGTH_MULTIPLIER;
        float averageStrideLengthInFeet = averageStrideLengthInInches/res.getInteger(R.integer.num_inches_in_foot);
        int numStepsInMile = (int) (res.getInteger(R.integer.num_feet_in_mile)/averageStrideLengthInFeet);
        return numStepsInMile;
    }


    public float calculateMiles(int numStepsTaken, int numStepsInMile) {
        float miles = (float) numStepsTaken/numStepsInMile;
        return Math.round(miles*10.0f)/10.0f; //ensures rounding to tenths place
    }

    public float calculateMilesPerHour(float miles, int milliseconds) {

        if (milliseconds == 0){
            return 0.0f;
        }

        float hours = ((float)milliseconds)/res.getInteger(R.integer.num_milli_second_in_hour);

        return Math.round((miles/hours)*10.0f)/10.0f; //ensures rounding to tenths place

    }
}
