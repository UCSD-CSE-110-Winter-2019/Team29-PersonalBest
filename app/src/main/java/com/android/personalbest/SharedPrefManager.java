package com.android.personalbest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import java.util.Calendar;

public class SharedPrefManager {

    public SharedPreferences sharedPref;
    public SharedPreferences.Editor editor;
    public Resources res;
    public Context context;


    public SharedPrefManager(Context context) {
        this.context = context;
        res = context.getResources();
        sharedPref = context.getSharedPreferences(res.getString(R.string.user_prefs), context.MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.apply();
    }

    public int getHeight() {

        return sharedPref.getInt(res.getString(R.string.height), context.MODE_PRIVATE);

    }

    void storeNumStepsInMile(int numStepsInMile) {
        editor.putInt(res.getString(R.string.num_steps_in_mile), numStepsInMile);
        editor.apply();
    }

    public int getGoal(){
        return sharedPref.getInt(res.getString(R.string.goal),0);
    }

    //Called when "end walk" button is pressed
    void storeIntentionalWalkStats(int dayOfWeek, int intentionalStepsTaken, float intentionalDistanceInMiles,
                                   float intentionalMilesPerHour, int intentionalTimeElapsed) {

        String today = getDayOfWeekAsString(dayOfWeek);

        //aggregate data if multiple walks taken
        int previousIntentionalStepsTaken = sharedPref.getInt(res.getString(R.string.intentionalStepsTaken) + today, 0);
        float previousIntentionalDistanceInMiles = sharedPref.getFloat(res.getString(R.string.intentionalDistanceInMiles) + today, 0);
        float previousIntentionalMilesPerHour = sharedPref.getFloat(res.getString(R.string.intentionalMilesPerHour) + today, 0);
        int previousIntentionalTimeElapsed = sharedPref.getInt(res.getString(R.string.intentionalTimeElapsed) + today, 0);

        editor.putInt(res.getString(R.string.intentionalStepsTaken) + today, previousIntentionalStepsTaken + intentionalStepsTaken);
        editor.putInt(res.getString(R.string.intentionalTimeElapsed) + today, previousIntentionalTimeElapsed + intentionalTimeElapsed);
        editor.putFloat(res.getString(R.string.intentionalDistanceInMiles) + today,previousIntentionalDistanceInMiles + intentionalDistanceInMiles);

        //take average miles per hour, ensure rounding to nearest tenth
        if (previousIntentionalMilesPerHour > 0) {
            editor.putFloat(res.getString(R.string.intentionalMilesPerHour) + today, Math.round(((previousIntentionalMilesPerHour + intentionalMilesPerHour)/2.0f)*10.0f)/10.0f);
        }
        else {
            editor.putFloat(res.getString(R.string.intentionalMilesPerHour) + today, intentionalMilesPerHour);
        }

        editor.apply();
    }

    //Called at end of day and every time bar chart is displayed (when the "see bar chart" is clicked)
    void storeTotalSteps(int dayOfWeek, int totalStepsTaken) {
        String today = getDayOfWeekAsString(dayOfWeek);
        editor.putInt(res.getString(R.string.totalStepsTaken) + today, totalStepsTaken);
    }

    //Called every time the goal is changed (and when the default goal is set)
    void storeGoal(int dayOfWeek, int goal) {
        String today = getDayOfWeekAsString(dayOfWeek);
        editor.putInt(res.getString(R.string.goal) + today, goal);
    }

    //used to check if subgoal has been met
    //Called at end of day (store today's step total in yesterday var)
    void storeTotalStepsFromTodayAsYesterday(int dayOfWeek, int totalStepsTaken) {
        String today = getDayOfWeekAsString(dayOfWeek);
        editor.putInt(res.getString(R.string.totalStepsTakenYesterday) + today, totalStepsTaken);
    }

    //called on Saturday end of day so that Sunday starts a new week with an empty bar chart
    //make sure you have already stored total steps from today as yesterday before resetting
    void resetSharedPrefForWeek() {
        for (int dayOfWeek = 1; dayOfWeek < 8; dayOfWeek++) {
            resetSharedPrefForDay(dayOfWeek);
        }
    }

    //helper method for resetting week's shared pref values
    void resetSharedPrefForDay(int dayOfWeek) {
        String today = getDayOfWeekAsString(dayOfWeek);
        editor.putInt(res.getString(R.string.totalStepsTaken) + today, 0); //remove this hard code
        editor.putInt(res.getString(R.string.intentionalStepsTaken) + today, 0);
        editor.putFloat(res.getString(R.string.intentionalDistanceInMiles) + today, 0);
        editor.putFloat(res.getString(R.string.intentionalMilesPerHour) + today, 0);
        editor.putInt(res.getString(R.string.intentionalTimeElapsed) + today, 0);
        editor.putInt(res.getString(R.string.goal) + today, 0);
        editor.apply();
    }

    //For getting step data for bar chart
    int getTotalStepsTaken(int dayOfWeek) {
        String today = getDayOfWeekAsString(dayOfWeek);
        return sharedPref.getInt(res.getString(R.string.totalStepsTaken) + today, 0);
    }

    int getIntentionalStepsTaken(int dayOfWeek) {
        String today = getDayOfWeekAsString(dayOfWeek);
        return sharedPref.getInt(res.getString(R.string.intentionalStepsTaken) + today, 0);
    }

    //For testing
    float getIntentionalDistanceInMiles(int dayOfWeek) {
        String today = getDayOfWeekAsString(dayOfWeek);
        return sharedPref.getFloat(res.getString(R.string.intentionalDistanceInMiles) + today, 0);
    }

    float getIntentionalMilesPerHour(int dayOfWeek) {
        String today = getDayOfWeekAsString(dayOfWeek);
        return sharedPref.getFloat(res.getString(R.string.intentionalMilesPerHour) + today, 0);
    }

    int getIntentionalTimeElapsed(int dayOfWeek) {
        String today = getDayOfWeekAsString(dayOfWeek);
        return sharedPref.getInt(res.getString(R.string.intentionalTimeElapsed) + today, 0);
    }

    //helper method for SharedPreference keys
    String getDayOfWeekAsString(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return res.getString(R.string.Sunday);
            case Calendar.MONDAY:
                return res.getString(R.string.Monday);
            case Calendar.TUESDAY:
                return res.getString(R.string.Tuesday);
            case Calendar.WEDNESDAY:
                return res.getString(R.string.Wednesday);
            case Calendar.THURSDAY:
                return res.getString(R.string.Thursday);
            case Calendar.FRIDAY:
                return res.getString(R.string.Friday);
            case Calendar.SATURDAY:
                return res.getString(R.string.Saturday);
            default:
                return "";
        }
    }
}
