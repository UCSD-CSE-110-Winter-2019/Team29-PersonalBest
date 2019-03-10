package com.android.personalbest;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;

public class MonthlyActivityLocalData {
    public static List<UserDayData> myMonthlyActivity;
    public static List<UserDayData> friendMonthlyActivity;

    public static boolean mock = false; //for unit testing purposes
    public static List<UserDayData> mockMyMonthlyActivity;

    public static void setFriendMonthlyActivity(List<UserDayData> monthlyActivity) {
        friendMonthlyActivity = monthlyActivity;
    }

    public static void setMyMonthlyActivity(List<UserDayData> monthlyActivity) {
        if (mock) {
            mockMyMonthlyActivity = monthlyActivity;
        }
        else {
            myMonthlyActivity = monthlyActivity;
        }
    }

    public static List<UserDayData> getMyMonthlyActivity() {
        if (mock) {
            return mockMyMonthlyActivity;
        }
        return myMonthlyActivity;
    }

    public static void storeMonthlyActivityForNewUser() {
        ArrayList<UserDayData> newUserMonthlyActivity = new ArrayList<UserDayData>();

        //Add empty data for past 28 days
        for (int i = 0; i < 28; i++) {
            newUserMonthlyActivity.add(new UserDayData());
        }
        MonthlyActivityLocalData.setMyMonthlyActivity(newUserMonthlyActivity);
    }

    public static void updateTodayData(Context context) {
        UserDayData todayData = getMyMonthlyActivity().get(27);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        int today = TimeMachine.getDayOfWeek();
        todayData.setIntentionalSteps(sharedPrefManager.getIntentionalStepsTaken(today));
        todayData.setIntentionalMph(sharedPrefManager.getIntentionalMilesPerHour(today));
        todayData.setIntentionalDistance(sharedPrefManager.getIntentionalDistanceInMiles(today));
        todayData.setTotalSteps(sharedPrefManager.getTotalSteps());
        todayData.setGoal(sharedPrefManager.getGoal());
    }

    public static void updateDataAtEndOfDay(Context context) {
        updateTodayData(context);
        getMyMonthlyActivity().remove(0); //remove oldest day
        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        getMyMonthlyActivity().add(new UserDayData(sharedPrefManager.getGoal())); //add new UserDayData for new day
    }
}
