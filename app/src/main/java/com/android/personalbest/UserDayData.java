package com.android.personalbest;

public class UserDayData {

    //data for bar chart
    private int totalSteps;
    private int intentionalSteps;
    private int goal;
    private float intentionalDistance;
    private int intentionalTime;
    private float intentionalMph;

    public UserDayData() {
        //All default values
        totalSteps = 0;
        intentionalSteps = 0;
        goal = 5000;
        intentionalDistance = 0.0f;
        intentionalTime = 0;
        intentionalMph = 0.0f;
    }

    public UserDayData(int goal) {
        this();
        this.goal = goal;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    public int getIntentionalSteps() {
        return intentionalSteps;
    }

    public void setIntentionalSteps(int intentionalSteps) {
        this.intentionalSteps = intentionalSteps;
    }

    public int getIntentionalTime() {
        return intentionalTime;
    }

    public void setIntentionalTime(int intentionalTime) {
        this.intentionalTime = intentionalTime;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public float getIntentionalDistance() {
        return intentionalDistance;
    }

    public void setIntentionalDistance(float intentionalDistance) {
        this.intentionalDistance = intentionalDistance;
    }

    public float getIntentionalMph() {
        return intentionalMph;
    }

    public void setIntentionalMph(float intentionalMph) {
        this.intentionalMph = intentionalMph;
    }
}
