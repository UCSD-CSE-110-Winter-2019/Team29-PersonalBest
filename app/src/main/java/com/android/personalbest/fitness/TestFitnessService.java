package com.android.personalbest.fitness;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.android.personalbest.MainPageActivity;
import com.android.personalbest.R;
import com.android.personalbest.SharedPrefManager;
import com.android.personalbest.TimeMachine;
import com.android.personalbest.WalkActivity;

import java.util.Calendar;

public class TestFitnessService implements FitnessService {
    private Activity activity;
    private SharedPrefManager sharedPrefManager;
    private int total = 0;
    private int goal = 0;
    public static boolean seeUpdateStepsButton = true; //Set to true for manually updating steps, false for automatic update
    Button mainUpdateStepsButton;

    private Handler handler;
    private Runnable runnable;

    //using MainPageActivity like StepCounterActivity
    public TestFitnessService(Activity activity) {
        this.activity = activity;
        sharedPrefManager = new SharedPrefManager(activity.getApplicationContext());
        total = sharedPrefManager.getTotalStepsForDayOfWeek(TimeMachine.getDayOfWeek());
        goal = sharedPrefManager.getGoal();

        if (activity instanceof MainPageActivity) {
            ((MainPageActivity) activity).numStepDone.setText(String.valueOf(total));
            ((MainPageActivity) activity).goal.setText(String.valueOf(goal));
        }

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
            updateStepInRealTime();
        }
    }

    @Override
    public void setup() {
        System.out.println("setup");
    }

    @Override
    public void updateStepCount() {
        System.out.println("update steps");
        if (activity instanceof MainPageActivity) {
            ((MainPageActivity) activity).addToStepCount(500);
        }
        else if (activity instanceof WalkActivity) {
            ((WalkActivity) activity).addToStepCount(500);
        }
    }

    @Override
    public int getCurrentStep() {
        return sharedPrefManager.getTotalStepsForDayOfWeek(TimeMachine.getDayOfWeek());
    }

    @Override
    public void updateStepInRealTime(){
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                updateStepCount();
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(runnable, 10000);
    }
}
