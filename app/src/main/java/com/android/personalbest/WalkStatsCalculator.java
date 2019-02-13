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
    protected int calculateNumStepsInMile(int heightInInches) {
        double averageStrideLengthInInches = heightInInches * STRIDE_LENGTH_MULTIPLIER;
        double averageStrideLengthInFeet = averageStrideLengthInInches/res.getInteger(R.integer.num_inches_in_foot);
        int numStepsInMile = (int) (res.getInteger(R.integer.num_feet_in_mile)/averageStrideLengthInFeet);
        return numStepsInMile;
    }

    protected double calculateMiles(int numStepsTaken, int numStepsInMile) {
        return ((double) numStepsTaken)/numStepsInMile;
    }

    protected double calculateMilesPerHour(double miles, int minutes) {
        double hours = ((double) minutes)/res.getInteger(R.integer.num_mins_in_hour);
        return miles/hours;
    }
}
