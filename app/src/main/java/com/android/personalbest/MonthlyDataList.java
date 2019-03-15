package com.android.personalbest;

import android.content.Context;
import java.util.ArrayList;

public class MonthlyDataList {

    public ArrayList<UserDayData> list;

    public static int TODAY = 27;
    public static int NUMDAYS = 28;

    //Remove context from MonthlyDataList
    public MonthlyDataList() {
        list = new ArrayList<>();

        //Add empty data for past 28 days
        for (int i = 0; i < NUMDAYS; i++) {
            list.add(new UserDayData());
        }
    }

    public void removeOldestDay() {
        list.remove(0);
    }

    public void addNewDay(int goal) {
        list.add(new UserDayData(goal));
    }

    public ArrayList<UserDayData> getList() {
        return list;
    }

    public void setList(ArrayList<UserDayData> list) {
        this.list = list;
    }

    public void updateTodayData(Context context) {
        updateData(context, TODAY);
    }

    public void updateDataAtEndOfDay(Context context) {
        updateTodayData(context);
        removeOldestDay();
        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        addNewDay(sharedPrefManager.getGoal());
    }

    //dayIndex of today is 27, yesterday is 26, and 28 days ago is 0
    //can be used for changing data to test bar chart/subgoal
    public void updateData(Context context, int dayIndex) {
        UserDayData todayData = list.get(dayIndex);
        int dayOfWeek = TimeMachine.getDayOfWeek();
        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        todayData.setIntentionalSteps(sharedPrefManager.getIntentionalStepsTaken(dayOfWeek));
        todayData.setIntentionalMph(sharedPrefManager.getIntentionalMilesPerHour(dayOfWeek));
        todayData.setIntentionalDistance(sharedPrefManager.getIntentionalDistanceInMiles(dayOfWeek));
        todayData.setTotalSteps(sharedPrefManager.getTotalStepsForDayOfWeek(dayOfWeek));
        todayData.setGoal(sharedPrefManager.getGoal());
        todayData.setIntentionalTime(sharedPrefManager.getIntentionalTimeElapsed(dayOfWeek));
    }

    public void mockPastData() {
        UserDayData todayData;
        for (int i = 5; i < 15; i++) {
            todayData = list.get(i);
            todayData.setGoal(500);
            todayData.setTotalSteps(300);
            if (i == 15) {
                todayData.setTotalSteps(600);
            }
        }
        for (int i = 15; i < 27; i++) {
            todayData = list.get(i);
            todayData.setGoal(1000);
            todayData.setTotalSteps(800);
            if (i == 26) {
                todayData.setTotalSteps(1200);
            }
        }
        //leave today data the same
    }
}
