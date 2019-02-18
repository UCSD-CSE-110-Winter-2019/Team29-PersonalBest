package com.android.personalbest.walkData;

import android.content.Intent;

public interface WalkData {

    void displayStep();
    void displayMiles();
    void displayMPH();
    void chronometerStepUp();
    void setCurrentElapsedTime();
    int getCurrentElapsedTime();
    Intent returnElapsedTime();
    void updateWalkStepInRealTime();

}
