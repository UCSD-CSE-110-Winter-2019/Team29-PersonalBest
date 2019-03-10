package com.android.personalbest.fitness;

public interface FitnessService {
    void setup();
    void updateStepCount();
    int getCurrentStep();
    void updateStepInRealTime();
}
