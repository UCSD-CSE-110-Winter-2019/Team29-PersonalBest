package com.android.personalbest.walkData;

import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import com.android.personalbest.R;
import com.android.personalbest.SharedPrefManager;
import com.android.personalbest.WalkActivity;
import com.android.personalbest.WalkStatsCalculator;



public class WalkDataAdapter implements WalkData {

    private WalkStatsCalculator walkStatsCalculator;
    private WalkActivity walkActivity;
    private SharedPrefManager sharedPrefManager;

    public int totalStepBeforeSwitch = 0;
    public int totalStep = 0;
    public int intentionalStep = 0;
    public float miles;
    public int numberStepInMiles;
    public float MPH;

    public int elapsedTime;
    public int curElasedTime;
    public int upDateTimeInterval = 1000;

    private Handler handler;
    private Runnable runnable;

    public WalkDataAdapter(WalkActivity walkActivity){

        this.walkActivity = walkActivity;
        sharedPrefManager = new SharedPrefManager(walkActivity);
        walkStatsCalculator = new WalkStatsCalculator(walkActivity);
        chronometerStepUp();

    }


    @Override
    public void chronometerStepUp(){
        //set the base of the chronometer to be the current system's clock
        walkActivity.chronometer.setBase(SystemClock.elapsedRealtime());
        //start timer
        walkActivity.chronometer.start();
    }
    @Override
    public void  setCurrentElapsedTime(){
        curElasedTime = (int)SystemClock.elapsedRealtime() - (int)walkActivity.chronometer.getBase();

    }
    @Override
    public int getCurrentElapsedTime(){

        return curElasedTime;
    }

    @Override
    public Intent returnElapsedTime(){

        walkActivity.chronometer.stop();
        elapsedTime = (int)SystemClock.elapsedRealtime() - (int)walkActivity.chronometer.getBase();
        sharedPrefManager.editor.putLong(walkActivity.getString(R.string.elapsedTime),elapsedTime);
        sharedPrefManager.editor.apply();
        Intent intent = new Intent();
        intent.putExtra(walkActivity.getString(R.string.elapsedTime), elapsedTime);
        return intent;

    }



    @Override
    public void displayStep(){

        totalStepBeforeSwitch = sharedPrefManager.sharedPref.getInt(walkActivity.getString(R.string.step_count_before_switch_to_start_walk_activity),totalStepBeforeSwitch);
        totalStep = sharedPrefManager.sharedPref.getInt(walkActivity.getString(R.string.totalStep),totalStep);
        intentionalStep = totalStep - totalStepBeforeSwitch;
        walkActivity.intentionalStepTextView.setText(String.valueOf(intentionalStep));

        sharedPrefManager.editor.putInt(walkActivity.getString(R.string.intentionalStep),intentionalStep);
        sharedPrefManager.editor.apply();


    }

    @Override
    public void displayMiles() {

        intentionalStep = sharedPrefManager.sharedPref.getInt(walkActivity.getString(R.string.intentionalStep),intentionalStep);
        numberStepInMiles = walkStatsCalculator.calculateNumStepsInMile(sharedPrefManager.getHeight());
        miles = walkStatsCalculator.calculateMiles(intentionalStep,numberStepInMiles);
        walkActivity.milesTextView.setText(String.valueOf(miles));

        sharedPrefManager.editor.putFloat(walkActivity.getString(R.string.milesInDisplay),miles);
        sharedPrefManager.editor.apply();



    }

    @Override
    public void displayMPH(){

         miles = sharedPrefManager.sharedPref.getFloat(walkActivity.getString(R.string.milesInDisplay),miles);
         MPH = walkStatsCalculator.calculateMilesPerHour(miles,getCurrentElapsedTime());
         walkActivity.MPHTextView.setText(String.valueOf(MPH));

         sharedPrefManager.editor.putFloat(walkActivity.getString(R.string.MPH),MPH);
         sharedPrefManager.editor.apply();


    }

    //method to be called when user clicks "end walk"
    //returns the time elapsed
    @Override
    public void updateWalkStepInRealTime(){

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                setCurrentElapsedTime();
                displayStep();
                displayMiles();
                displayMPH();

                handler.postDelayed(this, upDateTimeInterval);
            }
        };

        handler.postDelayed(runnable, upDateTimeInterval);

    }




}
