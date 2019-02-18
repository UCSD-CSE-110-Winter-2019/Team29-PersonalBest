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

    /* Google Fit API Sign in */

    public void setLogin(boolean login){
        editor.putBoolean(res.getString(R.string.login), login);
        editor.apply();
    }

    public boolean getLogin(){
        return sharedPref.getBoolean(res.getString(R.string.login), false);
    }

    public void setFirstTime(boolean firstTime) {
        editor.putBoolean(res.getString(R.string.first_time), firstTime);
        editor.apply();
    }

    public boolean getFirstTime() {
        return sharedPref.getBoolean(res.getString(R.string.first_time),false);
    }

    /* User settings */

    public void setHeight(int height) {
        editor.putInt(res.getString(R.string.height), height);
        editor.apply();
    }

    public int getHeight() {
        return sharedPref.getInt(res.getString(R.string.height), 0);
    }

    //called every time goal is set
    public void setGoal(int goal) {
        setGoalReached(false);
        editor.putInt(res.getString(R.string.goal), goal);
        editor.apply();
    }

    public int getGoal(){
        return sharedPref.getInt(res.getString(R.string.goal),0);
    }

    public void setIsWalker(boolean isWalker) {
        editor.putBoolean(res.getString(R.string.walker_option), isWalker);
        editor.apply();
    }

    public boolean getIsWalker() {
        return sharedPref.getBoolean(res.getString(R.string.walker_option), true);
    }

    /* For encouragement messages */

    public int getNumSteps() {
       return sharedPref.getInt(res.getString(R.string.totalStep), 0);
    }

    public void setGoalExceededToday(boolean goalExceededToday) {
        editor.putBoolean(res.getString(R.string.goal_exceeded_today), goalExceededToday);
        editor.apply();
    }

    public boolean getGoalExceededToday() {
        return sharedPref.getBoolean(res.getString(R.string.goal_exceeded_today), false);
    }

    public void setGoalMessageDay(int day) {
        editor.putInt(res.getString(R.string.goal_msg_expires), day);
        editor.apply();
    }

    public int getGoalMessageDay() {
        return sharedPref.getInt(res.getString(R.string.goal_msg_expires), -1);
    }

    public void setIgnoreGoal(boolean ignoreGoal) {
        editor.putBoolean(res.getString(R.string.goal_msg_shown), ignoreGoal);
        editor.apply();
    }

    public boolean getIgnoreGoal() {
        return sharedPref.getBoolean(res.getString(R.string.goal_msg_shown), false);
    }

    public void setGoalReached(boolean goalReached) {
        editor.putBoolean(res.getString(R.string.goal_reached), goalReached);
        editor.apply();
    }

    public boolean getGoalReached() {
        return sharedPref.getBoolean(res.getString(R.string.goal_reached), false);
    }

    public void setDayInStorage(int dayInStorage) {
        editor.putInt(res.getString(R.string.day_storage), dayInStorage);
        editor.apply();
    }

    public int getDayInStorage() {
        return sharedPref.getInt(res.getString(R.string.day_storage),-1);
    }

    /* Calculating intentional walk stats */

    public void storeNumStepsInMile(int numStepsInMile) {
        editor.putInt(res.getString(R.string.num_steps_in_mile), 0);
        editor.apply();
    }

    //Called when "end walk" button is pressed
    public void storeIntentionalWalkStats(int dayOfWeek, int intentionalStepsTaken, float intentionalDistanceInMiles,
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

    //TODO: Called at end of day
    // Called every time bar chart is displayed (when the "see bar chart" is clicked)
    public void storeTotalSteps(int dayOfWeek, int totalStepsTaken) {
        String today = getDayOfWeekAsString(dayOfWeek);
        editor.putInt(res.getString(R.string.totalStepsTaken) + today, totalStepsTaken);
    }

    //Called at end of day
    //Called when the default goal is set
    public void storeGoal(int dayOfWeek, int goal) {
        String today = getDayOfWeekAsString(dayOfWeek);
        editor.putInt(res.getString(R.string.goal) + today, goal);
    }

    public int getGoalForCertainDay(int dayOfWeek){
        String today = getDayOfWeekAsString(dayOfWeek);
        return sharedPref.getInt(res.getString(R.string.goal) + today,0 );
    }

    //used to check if subgoal has been met
    //TODO: Called at end of day (store today's step total in yesterday var)
    public void storeTotalStepsFromYesterday(int totalStepsTaken) {
        editor.putInt(res.getString(R.string.totalStepsTakenYesterday), totalStepsTaken);
    }

    public void getTotalStepsFromYesterday() {
        sharedPref.getInt(res.getString(R.string.totalStepsTakenYesterday), 0);
    }

    //called on Saturday end of day so that Sunday starts a new week with an empty bar chart
    //make sure you have already stored total steps from today as yesterday before resetting
    public void resetSharedPrefForWeek() {
        for (int dayOfWeek = 1; dayOfWeek < 8; dayOfWeek++) {
            resetSharedPrefForDay(dayOfWeek);
        }
    }

    //helper method for resetting week's shared pref values
    public void resetSharedPrefForDay(int dayOfWeek) {
        String today = getDayOfWeekAsString(dayOfWeek);
        editor.putInt(res.getString(R.string.totalStepsTaken) + today, 0); //remove this hard code
        editor.putInt(res.getString(R.string.intentionalStepsTaken) + today, 0);
        editor.putFloat(res.getString(R.string.intentionalDistanceInMiles) + today, 0);
        editor.putFloat(res.getString(R.string.intentionalMilesPerHour) + today, 0);
        editor.putInt(res.getString(R.string.intentionalTimeElapsed) + today, 0);
        editor.putInt(res.getString(R.string.goal) + today, 0);
        editor.apply();
    }

    public int getTotalStepsTaken(int dayOfWeek) {
        String today = getDayOfWeekAsString(dayOfWeek);
        return sharedPref.getInt(res.getString(R.string.totalStepsTaken) + today, 0);
    }

    public int getIntentionalStepsTaken(int dayOfWeek) {
        String today = getDayOfWeekAsString(dayOfWeek);
        return sharedPref.getInt(res.getString(R.string.intentionalStepsTaken) + today, 0);
    }

    public int getNonIntentionalStepsTaken(int dayOfWeek){
        return getTotalStepsTaken(dayOfWeek) - getIntentionalStepsTaken(dayOfWeek);
    }

    public float getIntentionalDistanceInMiles(int dayOfWeek) {
        String today = getDayOfWeekAsString(dayOfWeek);
        return sharedPref.getFloat(res.getString(R.string.intentionalDistanceInMiles) + today, 0);
    }

    public float getIntentionalMilesPerHour(int dayOfWeek) {
        String today = getDayOfWeekAsString(dayOfWeek);
        return sharedPref.getFloat(res.getString(R.string.intentionalMilesPerHour) + today, 0);
    }

    public int getIntentionalTimeElapsed(int dayOfWeek) {
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

    void resetSharedPrefToDefault() {
        //clear shared pref
        editor.clear();
        editor.apply();

        //set values to default from input height
        setHeight(65);
        setGoal(res.getInteger(R.integer.default_goal));
        storeGoal(TimeMachine.getDay(), res.getInteger(R.integer.default_goal));
        setFirstTime(true);
        setIsWalker(true);
    }
}
