package com.android.personalbest;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import java.util.Calendar;

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
    }

    public int getHeight() {
        return sharedPref.getInt(res.getString(R.string.height), 0);
    }

    public void storeNumStepsInMile(int numStepsInMile) {
        editor.putInt(res.getString(R.string.num_steps_in_mile), context.MODE_PRIVATE);
        editor.apply();
    }

    public int getGoal(){
        return sharedPref.getInt(res.getString(R.string.goal),0);
    }

}
