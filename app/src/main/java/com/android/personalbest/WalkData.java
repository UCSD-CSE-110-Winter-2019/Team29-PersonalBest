package com.android.personalbest;

import android.content.Intent;

public interface WalkData {

    void displayStep();
    void displayMiles();
    void displayMPH();
    void chronometerStepUp();
    long getCurrentElapsedTime();
    Intent returnElapsedTime();
    void updateWalkStepInRealTime();


}
