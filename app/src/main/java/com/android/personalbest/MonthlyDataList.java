package com.android.personalbest;

import android.content.Context;
import java.util.ArrayList;

public class MonthlyDataList {

    public ArrayList<UserDayData> list;
    public Context context;
    public SharedPrefManager sharedPrefManager;

    public static int TODAY = 27;
    public static int NUMDAYS = 28;



    public MonthlyDataList(Context context) {
        this.context = context;
        this.sharedPrefManager = new SharedPrefManager(context);
        list = new ArrayList<UserDayData>();

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

    public void updateTodayData() {
        updateData(TODAY);
    }

    public UserDayData getTodayData() {
        return list.get(TODAY);
    }

    public void updateDataAtEndOfDay() {
        updateTodayData();
        removeOldestDay();
        addNewDay(sharedPrefManager.getGoal());
    }

    //dayIndex of today is 27, yesterday is 26, and 28 days ago is 0
    //can be used for changing data to test bar chart/subgoal
    public void updateData(int dayIndex) {
        UserDayData todayData = list.get(dayIndex);
        int dayOfWeek = TimeMachine.getDayOfWeek();
        todayData.setIntentionalSteps(sharedPrefManager.getIntentionalStepsTaken(dayOfWeek));
        todayData.setIntentionalMph(sharedPrefManager.getIntentionalMilesPerHour(dayOfWeek));
        todayData.setIntentionalDistance(sharedPrefManager.getIntentionalDistanceInMiles(dayOfWeek));
        todayData.setTotalSteps(sharedPrefManager.getTotalStepsForDayOfWeek(dayOfWeek));
        todayData.setGoal(sharedPrefManager.getGoal());
    }
}
