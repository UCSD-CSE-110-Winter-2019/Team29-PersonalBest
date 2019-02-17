package com.android.personalbest.fitness;

import android.view.View;
import android.widget.Button;

import com.android.personalbest.MainPageActivity;
import com.android.personalbest.R;
import com.android.personalbest.SharedPrefManager;
import com.android.personalbest.WalkActivity;

public class TestFitnessService implements FitnessService {
    private MainPageActivity mainPageActivity;
    private SharedPrefManager sharedPrefManager;
    private int goal = 0;
    private boolean seeUpdateStepsButton = true; //Set to true for manually updating steps
    Button mainUpdateStepsButton;
    Button walkUpdateStepsButton;

    //using MainPageActivity like StepCounterActivity
    public TestFitnessService(MainPageActivity activity) {
        this.mainPageActivity = activity;
        sharedPrefManager = new SharedPrefManager(activity.getApplicationContext());
        goal = sharedPrefManager.getGoal();
        activity.numStepDone.setText("0");
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
}
