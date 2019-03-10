package com.android.personalbest;

public class UserDayData {

    //data for bar chart
    private int totalSteps;
    private int intentionalSteps;
    private int goal;
    private float intentionalDistance;
    private float intentionalMph;

    //date of this data
    private int dayOfMonth;
    private int month;

    public UserDayData() {
        //All default values
        totalSteps = 0;
        intentionalSteps = 0;
        goal = 5000;
        intentionalDistance = 0.0f;
        intentionalMph = 0.0f;
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

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
