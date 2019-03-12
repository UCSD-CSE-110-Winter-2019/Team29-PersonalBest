package com.android.personalbest;

import java.util.ArrayList;

public class MonthlyDataList {

    ArrayList<UserDayData> list;

    public MonthlyDataList() {
        list = new ArrayList<UserDayData>();

        //Add empty data for past 28 days
        for (int i = 0; i < 28; i++) {
            list.add(new UserDayData());
        }
    }

    public void removeOldestDay() {
        list.remove(0);
    }

    public void addNewDay(int goal) {
        list.add(new UserDayData(goal));
    }
}
