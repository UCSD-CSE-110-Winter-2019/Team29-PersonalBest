package com.android.personalbest.cloud;

import com.android.personalbest.MonthlyDataList;

public class DataStorageMediator {
    public MonthlyDataList monthlyActivity;

    public MonthlyDataList getMonthlyActivity() { return monthlyActivity; }

    public void setMonthlyActivity(MonthlyDataList monthlyActivity) {
        this.monthlyActivity = monthlyActivity;
    }
}
