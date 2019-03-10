package com.android.personalbest;

import org.apache.tools.ant.taskdefs.Sync;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class MonthlyActivityLocalDataTest {

    MainPageActivity mainPageActivity;
    SharedPrefManager sharedPrefManager;

    @Before
    public void init() {
        mainPageActivity = Robolectric.setupActivity(MainPageActivity.class);
        sharedPrefManager = new SharedPrefManager(mainPageActivity);
        MonthlyActivityLocalData.mock = true; //allows us not to manipulate actual local data
    }

    @Test
    public void testNewUser() {

        MonthlyActivityLocalData.storeMonthlyActivityForNewUser();

    }

    @Test
    public void testUpdateData() {

        int dayOfWeek = Calendar.MONDAY;
        int intentionalStepsTaken = 5000;
        float intentionalDistanceInMiles = 5.1f;
        float intentionalMilesPerHour = 3.3f;
        int intentionalTimeElapsed = 20;

        sharedPrefManager.storeIntentionalWalkStats(dayOfWeek, intentionalStepsTaken, intentionalDistanceInMiles,
                intentionalMilesPerHour, intentionalTimeElapsed);

        MonthlyActivityLocalData.updateTodayData(mainPageActivity);

        assertEquals(intentionalStepsTaken, sharedPrefManager.getIntentionalStepsTaken(dayOfWeek));
        assertEquals(intentionalDistanceInMiles, sharedPrefManager.getIntentionalDistanceInMiles(dayOfWeek), 0.00);
        assertEquals(intentionalMilesPerHour, sharedPrefManager.getIntentionalMilesPerHour(dayOfWeek), 0.00);
        assertEquals(intentionalTimeElapsed, sharedPrefManager.getIntentionalTimeElapsed(dayOfWeek));
    }

    @Test
    public void testEndOfDay() {
        MonthlyActivityLocalData.updateDataAtEndOfDay(mainPageActivity);
    }

    @AfterClass
    public void after() {
        MonthlyActivityLocalData.mock = false;
    }
}
