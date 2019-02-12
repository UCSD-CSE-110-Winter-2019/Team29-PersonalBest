package com.android.personalbest;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

public class SharedPrefManager {

    SharedPreferences sharedPref;
    Resources res;
    Context context;

    public SharedPrefManager(Context context) {
        this.context = context;
        res = context.getResources();
        sharedPref = context.getSharedPreferences(res.getString(R.string.user_prefs), context.MODE_PRIVATE);
    }

    int getHeight() {
        return sharedPref.getInt(res.getString(R.string.height), context.MODE_PRIVATE);
    }

}
