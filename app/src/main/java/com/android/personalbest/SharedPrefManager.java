package com.android.personalbest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

public class SharedPrefManager {

    public SharedPreferences sharedPref;
    public SharedPreferences.Editor editor;
    Resources res;
    Context context;

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

    public void setGoal(int goal) {
        editor.putInt(res.getString(R.string.goal), goal);
        editor.apply();
    }

    public int getGoal(){
        return sharedPref.getInt(res.getString(R.string.goal),0);
    }

    public void setIsWalker(boolean isWalker) {
        editor.putBoolean("isWalker", isWalker);
        editor.apply();
    }

    public boolean getIsWalker() {
        return sharedPref.getBoolean(res.getString(R.string.walker_option), true);
    }

    //TODO: is this ever used?
    public void storeNumStepsInMile(int numStepsInMile) {
        editor.putInt(res.getString(R.string.num_steps_in_mile), context.MODE_PRIVATE);
        editor.apply();
    }
}
