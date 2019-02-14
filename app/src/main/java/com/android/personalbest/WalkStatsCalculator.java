package com.android.personalbest;

import android.app.Activity;
import android.content.res.Resources;

public class WalkStatsCalculator {
    SharedPrefManager sharedPrefManager;
    Resources res;

    public static final double STRIDE_LENGTH_MULTIPLIER = 0.413;

    public WalkStatsCalculator(Activity activity) {
        sharedPrefManager = new SharedPrefManager(activity.getApplicationContext());
        res = activity.getResources();
    }

    //Reference for calculating miles: https://www.openfit.com/how-many-steps-walk-per-mile
    //Pass in height from Shared Pref (int heightInInches = sharedPrefManager.getHeight();)
    //Save output to Shared Pref to avoid repeated calculation (sharedPrefManager.storeNumStepsInMile(numStepsInMile);)
    public int calculateNumStepsInMile(int heightInInches) {
        double averageStrideLengthInInches = heightInInches * STRIDE_LENGTH_MULTIPLIER;
        double averageStrideLengthInFeet = averageStrideLengthInInches/res.getInteger(R.integer.num_inches_in_foot);
        int numStepsInMile = (int) (res.getInteger(R.integer.num_feet_in_mile)/averageStrideLengthInFeet);
        return numStepsInMile;
    }

    public long calculateMiles(int numStepsTaken, long numStepsInMile) {
        return ((long) numStepsTaken)/numStepsInMile;
    }

    public long calculateMilesPerHour(double miles, long millisecond) {

         long hours = ( millisecond)/res.getInteger(R.integer.num_milli_second_in_hour);
         if (hours == 0){
             return 0;
         }
        return (long)miles/hours;
    }
}
