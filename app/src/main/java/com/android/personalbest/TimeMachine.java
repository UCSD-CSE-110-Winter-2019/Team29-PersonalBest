package com.android.personalbest;

import java.util.Calendar;

//Used for changing day for testing and demo purposes
public class TimeMachine {

    private static int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

    public static int getDay() {
        return today;
    }

    public static void setDay(int dayOfWeek){
        today = dayOfWeek;
    }
}
