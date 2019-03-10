package com.android.personalbest;

import java.util.List;

public class MonthlyActivityLocalData {
    public static List<UserDayData> myMonthlyActivity;
    public static List<UserDayData> friendMonthlyActivity;

    public static void setMyMonthlyActivity(List<UserDayData> monthlyActivity) {
        myMonthlyActivity = monthlyActivity;
    }

    public static void setFriendMonthlyActivity(List<UserDayData> monthlyActivity) {
        friendMonthlyActivity = monthlyActivity;
    }
}
