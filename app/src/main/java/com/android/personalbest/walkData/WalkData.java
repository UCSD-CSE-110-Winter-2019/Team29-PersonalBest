package com.android.personalbest.walkData;

import android.content.Intent;

public interface WalkData {

    void displayStep();
    void displayMiles();
    void displayMPH();
    void chronometerStepUp();
    int getCurrentElapsedTime();
    Intent returnElapsedTime();
    void updateWalkStepInRealTime();

}
