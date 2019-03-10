package com.android.personalbest.walkData;

import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import com.android.personalbest.R;
import com.android.personalbest.SharedPrefManager;
import com.android.personalbest.TimeMachine;
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
    public int curElapsedTime;
    public int upDateTimeInterval = 1000;

    private Handler handler;
    private Runnable runnable;
    public boolean walkEnded;

    public WalkDataAdapter(WalkActivity walkActivity){

        this.walkActivity = walkActivity;
        sharedPrefManager = new SharedPrefManager(walkActivity.getApplicationContext());
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

        curElapsedTime = (int)SystemClock.elapsedRealtime() - (int)walkActivity.chronometer.getBase();
    }

    @Override
    public int getCurrentElapsedTime(){

        return curElapsedTime;
    }

    @Override
    public Intent returnElapsedTime(){

        walkActivity.chronometer.stop();
        elapsedTime = (int)SystemClock.elapsedRealtime() - (int)walkActivity.chronometer.getBase();
        Intent intent = new Intent();
        intent.putExtra(walkActivity.getString(R.string.elapsedTime), elapsedTime);
        return intent;
    }

    @Override
    public void displayStep(){

        totalStepBeforeSwitch = sharedPrefManager.getCountBeforeWalk();
        totalStep = sharedPrefManager.getTotalStepsForDayOfWeek(TimeMachine.getDayOfWeek());
        intentionalStep = totalStep - totalStepBeforeSwitch;
        walkActivity.intentionalStepTextView.setText(String.valueOf(intentionalStep));

        sharedPrefManager.setCurrIntentionalStep(intentionalStep);
    }

    @Override
    public void displayMiles() {

        intentionalStep = sharedPrefManager.getCurrIntentionalStep();
        numberStepInMiles = walkStatsCalculator.calculateNumStepsInMile(sharedPrefManager.getHeight());
        miles = walkStatsCalculator.calculateMiles(intentionalStep,numberStepInMiles);
        walkActivity.milesTextView.setText(String.valueOf(miles));

        sharedPrefManager.setCurrMile(miles);
    }

    @Override
    public void displayMPH(){
         miles = sharedPrefManager.getCurrMile();
         MPH = walkStatsCalculator.calculateMilesPerHour(miles,getCurrentElapsedTime());
         walkActivity.MPHTextView.setText(String.valueOf(MPH));

         sharedPrefManager.setCurrMPH(MPH);
    }

    //method to be called when user clicks "end walk"
    //returns the time elapsed
    @Override
    public void updateWalkStepInRealTime(){

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (!walkEnded) {
                    setCurrentElapsedTime();
                    displayStep();
                    displayMiles();
                    displayMPH();

                    handler.postDelayed(this, upDateTimeInterval);
                }
            }
        };

        handler.postDelayed(runnable, upDateTimeInterval);
    }
}
