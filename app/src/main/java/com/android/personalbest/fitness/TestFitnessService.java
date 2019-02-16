package com.android.personalbest.fitness;

import com.android.personalbest.MainPageActivity;

public class TestFitnessService implements FitnessService {
    private MainPageActivity activity;

    //using MainPageActivity like StepCounterActivity
    public TestFitnessService(MainPageActivity activity) {
        this.activity = activity;
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
        activity.setStepCount(500);
    }
}
