package com.android.personalbest.walkData;

import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;

import com.android.personalbest.R;
import com.android.personalbest.SharedPrefManager;
import com.android.personalbest.WalkActivity;
import com.android.personalbest.WalkStatsCalculator;



public class WalkDataAdapter implements WalkData {

    private WalkStatsCalculator walkStatsCalculator;
    private WalkActivity walkActivity;
    private SharedPrefManager sharedPrefManager;

    private int totalStepBeforeSwitch = 0;
    private int totalStep = 0;
    private int intenionalStep = 0;
    private float miles;
    private float numberStepInMiles;
    private float MPH;

    private long elapsedTime;
    private long curElasedTime;
    private int upDateTimeInterval = 1000;

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
    public long getCurrentElapsedTime(){
        curElasedTime = SystemClock.elapsedRealtime() - walkActivity.chronometer.getBase();
        return curElasedTime;
    }

    @Override
    public Intent returnElapsedTime(){

        walkActivity.chronometer.stop();
        elapsedTime = SystemClock.elapsedRealtime() - walkActivity.chronometer.getBase();
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
        intenionalStep = totalStep - totalStepBeforeSwitch;
        walkActivity.intenionalStepTextView.setText(String.valueOf(intenionalStep));

        sharedPrefManager.editor.putInt(walkActivity.getString(R.string.intentionalStep),intenionalStep);
        sharedPrefManager.editor.apply();


    }

    @Override
    public void displayMiles() {

        intenionalStep = sharedPrefManager.sharedPref.getInt(walkActivity.getString(R.string.intentionalStep),intenionalStep);
        numberStepInMiles = walkStatsCalculator.calculateNumStepsInMile(sharedPrefManager.getHeight());
        miles = walkStatsCalculator.calculateMiles(intenionalStep,numberStepInMiles);
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
                getCurrentElapsedTime();
                displayStep();
                displayMiles();
                displayMPH();

                handler.postDelayed(this, upDateTimeInterval);
            }
        };

        handler.postDelayed(runnable, upDateTimeInterval);

    }




}
