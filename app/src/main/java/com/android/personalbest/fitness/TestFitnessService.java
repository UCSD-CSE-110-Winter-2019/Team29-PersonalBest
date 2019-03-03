package com.android.personalbest.fitness;

import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.android.personalbest.MainPageActivity;
import com.android.personalbest.R;
import com.android.personalbest.SharedPrefManager;
import com.android.personalbest.WalkActivity;

public class TestFitnessService implements FitnessService {
    private MainPageActivity mainPageActivity;
    private SharedPrefManager sharedPrefManager;
    private int total = 0;
    private int goal = 0;
    public static boolean seeUpdateStepsButton = false; //Set to true for manually updating steps, false for automatic update
    Button mainUpdateStepsButton;

    private Handler handler;
    private Runnable runnable;

    //using MainPageActivity like StepCounterActivity
    public TestFitnessService(MainPageActivity activity) {
        this.mainPageActivity = activity;
        sharedPrefManager = new SharedPrefManager(activity.getApplicationContext());
        total = sharedPrefManager.getNumSteps();
        goal = sharedPrefManager.getGoal();

        activity.numStepDone.setText(String.valueOf(total));
        activity.goal.setText(String.valueOf(goal));
        this.setup();

        //Button to quickly update/mock steps
        if (seeUpdateStepsButton) {
            mainUpdateStepsButton = activity.findViewById(R.id.updateSteps);
            mainUpdateStepsButton.setVisibility(View.VISIBLE);
            mainUpdateStepsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    updateStepCount();
                }
            });
        }
        //Automatic update of steps
        else {
            this.updateStepInRealTime();
        }
    }

    @Override
    public int getRequestCode() {
        return 0;
    }

    @Override
    public void setup() {
        System.out.println("setup");
    }

    @Override
    public void updateStepCount() {
        System.out.println("update steps");
        mainPageActivity.addToStepCount(500);
    }

    @Override
    public int getCurrentStep() {
        return sharedPrefManager.getNumSteps();
    }

    @Override
    public void updateStepInRealTime(){
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                updateStepCount();
                handler.postDelayed(this, 10000);
            }
        };
        handler.postDelayed(runnable, 10000);
    }
}
