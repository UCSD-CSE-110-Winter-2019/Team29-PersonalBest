package com.android.personalbest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


    /*User Information*/
    //store current app UserEmail
    public void setCurrentAppUserEmail(String curUserEmail) {
        editor.putString(res.getString(R.string.current_user_email),curUserEmail);
        editor.apply();
    }

    //return current app user email
    public String getCurrentAppUserEmail(){
        return sharedPref.getString(res.getString(R.string.current_user_email),"");
    }

    //store current app UserEmail
    public void setFriendEmail(String friendEmail) {
        editor.putString(res.getString(R.string.friend_email),friendEmail);
        editor.apply();
    }

    //return current app user email
    public String getFriendEmail(){
        return sharedPref.getString(res.getString(R.string.friend_email),"");
    }


    public void setFriendListSet(List<String> friendList){

        Set<String>friendListSet = new HashSet<>();

        for(Object friend: friendList){
            friendListSet.add((String) friend);
        }

        editor.putStringSet(res.getString(R.string.friend_list_set),friendListSet);
        editor.apply();;
    }

    //return friendListSet
    public Set<String> getFriendListSet(){
        return sharedPref.getStringSet(res.getString(R.string.friend_list_set),null);
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

    public void setTotalSteps(int numSteps) {
        editor.putInt(res.getString(R.string.totalStep), numSteps);
        editor.apply();
    }
    public int getTotalSteps() {
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

    public void setSubGoalExceededToday(boolean subgoalExceededToday) {
        editor.putBoolean(res.getString(R.string.subgoal_exceeded_today), subgoalExceededToday);
        editor.apply();
    }

    public boolean getSubGoalExceededToday() {
        return sharedPref.getBoolean(res.getString(R.string.subgoal_exceeded_today), false);
    }

    public void setSubGoalMessageDay(int day) {
        editor.putInt(res.getString(R.string.subgoal_msg_expires), day);
        editor.apply();
    }

    public int getSubGoalMessageDay() {
        return sharedPref.getInt(res.getString(R.string.subgoal_msg_expires), -1);
    }


    public void setSubGoalReached(boolean subGoalReached) {
        editor.putBoolean(res.getString(R.string.subgoal_reached), subGoalReached);
        editor.apply();
    }

    public boolean getSubGoalReached() {
        return sharedPref.getBoolean(res.getString(R.string.subgoal_reached), false);
    }

    public void setDayOfWeekInStorage(int dayInStorage) {
        editor.putInt(res.getString(R.string.day_storage), dayInStorage);
        editor.apply();
    }

    public int getDayOfWeekInStorage() {
        return sharedPref.getInt(res.getString(R.string.day_storage),-1);
    }

    public void setDayOfMonthInStorage(int dayInStorage) {
        editor.putInt(res.getString(R.string.day_month_storage), dayInStorage);
        editor.apply();
    }

    public int getDayOfMonthInStorage() {
        return sharedPref.getInt(context.getString(R.string.day_month_storage),-1);
    }

    public void setMonthInStorage(int dayInStorage) {
        editor.putInt(context.getString(R.string.month_storage), dayInStorage);
        editor.apply();
    }

    public int getMonthInStorage() {
        return sharedPref.getInt(res.getString(R.string.month_storage),-1);
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

    // Called every time bar chart is displayed (when the "see bar chart" is clicked)
    public void storeTotalStepsForDayOfWeek(int dayOfWeek, int totalStepsTaken) {
        String today = getDayOfWeekAsString(dayOfWeek);
        editor.putInt(res.getString(R.string.totalStepsTaken) + today, totalStepsTaken);
    }

    //Called at end of day
    //Called when the default goal is set
    public void storeGoalForDayOfWeek(int dayOfWeek, int goal) {
        String today = getDayOfWeekAsString(dayOfWeek);
        editor.putInt(res.getString(R.string.goal) + today, goal);
    }

    public int getGoalForCertainDay(int dayOfWeek){
        String today = getDayOfWeekAsString(dayOfWeek);
        return sharedPref.getInt(res.getString(R.string.goal) + today,0 );
    }

    //used to check if subgoal has been met
    //Called at end of day (store today's step total in yesterday var)
    public void storeTotalStepsFromYesterday(int totalStepsTaken) {
        editor.putInt(res.getString(R.string.totalStepsTakenYesterday), totalStepsTaken);
    }

    public int getTotalStepsFromYesterday() {
        return sharedPref.getInt(res.getString(R.string.totalStepsTakenYesterday), 0);
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
        storeGoalForDayOfWeek(TimeMachine.getDayOfWeek(), res.getInteger(R.integer.default_goal));
        setFirstTime(true);
        setIsWalker(true);
    }
}
