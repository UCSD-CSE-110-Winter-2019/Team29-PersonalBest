package com.android.personalbest;

import android.app.Activity;

public class WalkStatsCalculator {
    SharedPrefManager sharedPrefManager;

    public WalkStatsCalculator(Activity activity) {
        sharedPrefManager = new SharedPrefManager(activity.getApplicationContext());
    }

    //Use this as helper method to store numStepsInMile in shared pref
    //Reference for calculating miles: https://www.openfit.com/how-many-steps-walk-per-mile
    protected double calculateNumStepsInMile() {
        int heightInInches = sharedPrefManager.getHeight();
        double averageStrideLengthInInches = heightInInches * 0.413; //get rid of hard code
        double averageStrideLengthInFeet = averageStrideLengthInInches/12; //get rid of hard code
        double numStepsInMile = averageStrideLengthInFeet/5280; //get rid of hard code
        return numStepsInMile;
    }

    protected double calculateMiles(int numSteps, double numStepsInMile) {
        return numSteps/numStepsInMile;
    }

    protected double calculateMilesPerHour(double miles, double hours) {
        return miles/hours;
    }
}
