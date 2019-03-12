package com.android.personalbest;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;

public class MonthlyActivityLocalData {
    public MonthlyDataList myMonthlyActivity;
    public MonthlyDataList friendMonthlyActivity;

    public MonthlyActivityLocalData() {
        //initialize monthly activity for new user
        storeMonthlyActivityForNewUser();
    }

    public void setFriendMonthlyActivity(MonthlyDataList monthlyActivity) {
        friendMonthlyActivity = monthlyActivity;
    }

    public void setMyMonthlyActivity(MonthlyDataList monthlyActivity) {
            myMonthlyActivity = monthlyActivity;
    }

    public MonthlyDataList getMyMonthlyActivity() {
        return myMonthlyActivity;
    }

    private void storeMonthlyActivityForNewUser() {
        setMyMonthlyActivity(new MonthlyDataList());
    }

    public void updateTodayData(Context context) {
        int dayOfWeek = TimeMachine.getDayOfWeek();
        UserDayData todayData = getMyMonthlyActivity().list.get(27);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        todayData.setIntentionalSteps(sharedPrefManager.getCurrIntentionalStep());
        todayData.setIntentionalMph(sharedPrefManager.getCurrMPH());
        todayData.setIntentionalDistance(sharedPrefManager.getCurrMile());
        todayData.setTotalSteps(sharedPrefManager.getTotalStepsForDayOfWeek(dayOfWeek)); //this should not need day of week anymore
        todayData.setGoal(sharedPrefManager.getGoal());
    }

    public void updateDataAtEndOfDay(Context context) {
        updateTodayData(context);
        getMyMonthlyActivity().removeOldestDay();
        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        getMyMonthlyActivity().addNewDay(sharedPrefManager.getGoal());
    }
}
