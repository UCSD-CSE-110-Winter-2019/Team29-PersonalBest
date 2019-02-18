package com.android.personalbest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EspressoTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espresso_test);
        Intent mainPage = new Intent(this, MainPageActivity.class);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);

        //clear shared pref
        sharedPrefManager.editor.clear();
        sharedPrefManager.editor.apply();

        //set values to default from input height
        sharedPrefManager.setHeight(65);
        sharedPrefManager.setGoal(getResources().getInteger(R.integer.default_goal));
        sharedPrefManager.storeGoal(TimeMachine.getDay(), getResources().getInteger(R.integer.default_goal));
        sharedPrefManager.setFirstTime(true);
        sharedPrefManager.setIsWalker(true);

        MainPageActivity.mock = true;
        startActivity(mainPage);
    }

}
