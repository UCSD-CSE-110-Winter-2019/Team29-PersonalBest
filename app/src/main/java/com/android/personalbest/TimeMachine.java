package com.android.personalbest;

import java.util.Calendar;

//Used for changing day for testing and demo purposes
public class TimeMachine {

    private static long hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    private static int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    private static int dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    private static int month = Calendar.getInstance().get(Calendar.MONTH);

    public static int getDayOfWeek() {
        return dayOfWeek;
    }

    public static void setDayOfWeek(int dayOfWeek){
        TimeMachine.dayOfWeek = dayOfWeek;
    }

    public static int getDayOfMonth() {
        return dayOfMonth;
    }

    public static void setDayOfMonth(int dayOfMonth) {
        TimeMachine.dayOfMonth = dayOfMonth;
    }

    public static long getHourOfDay() {
        return hourOfDay;
    }

    public static void setHourOfDay(long hourOfDay) {
        TimeMachine.hourOfDay = hourOfDay;
    }

    public static int getMonth() {
        return month;
    }

    public static void setMonth(int month) {
        TimeMachine.month = month;
    }
}
