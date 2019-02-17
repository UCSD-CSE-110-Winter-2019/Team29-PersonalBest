package com.android.personalbest.fitness;

import android.view.View;
import android.widget.Button;

import com.android.personalbest.MainPageActivity;
import com.android.personalbest.R;
import com.android.personalbest.SharedPrefManager;

public class TestFitnessService implements FitnessService {
    private MainPageActivity activity;
    private SharedPrefManager sharedPrefManager;
    private int goal = 0;
    Button updateSteps;

    //using MainPageActivity like StepCounterActivity
    public TestFitnessService(MainPageActivity activity) {
        this.activity = activity;
        sharedPrefManager = new SharedPrefManager(activity.getApplicationContext());
        goal = sharedPrefManager.getGoal();
        activity.numStepDone.setText("0");
        this.setup();

        //Button to quickly update/mock steps
        /*
        updateSteps = activity.findViewById(R.id.updateSteps);
        updateSteps.setVisibility(View.VISIBLE);
        updateSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                updateStepCount();
            }
        });*/
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
        activity.addToStepCount(500);
    }

    @Override
    public int getCurrentStep() {
        return Integer.parseInt(activity.numStepDone.getText().toString());
    }
}
