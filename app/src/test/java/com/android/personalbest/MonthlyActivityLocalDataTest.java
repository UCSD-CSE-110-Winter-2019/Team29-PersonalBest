package com.android.personalbest;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.sql.Time;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class MonthlyActivityLocalDataTest {

    MainPageActivity mainPageActivity;
    SharedPrefManager sharedPrefManager;

    @BeforeClass
    public static void beforeClass(){
        MainPageActivity.mockCloud = true;
        MonthlyActivityLocalData.mock = true;
        TimeMachine.setDayOfWeek(Calendar.MONDAY);
    }

    @Before
    public void init() {
        mainPageActivity = Robolectric.setupActivity(MainPageActivity.class);
        sharedPrefManager = new SharedPrefManager(mainPageActivity);
    }

    @Test
    public void testNewUser() {
        MonthlyActivityLocalData.storeMonthlyActivityForNewUser();
        UserDayData dayData = MonthlyActivityLocalData.getMyMonthlyActivity().get(27);
        assertEquals(0, dayData.getTotalSteps());
        assertEquals(0, dayData.getIntentionalSteps());
        assertEquals(5000, dayData.getGoal());
        assertEquals(0.0, dayData.getIntentionalDistance(), 0.01);
        assertEquals(0.0, dayData.getIntentionalMph(), 0.01);
    }

    @Test
    public void testUpdateData() {

        MonthlyActivityLocalData.storeMonthlyActivityForNewUser();

        int dayOfWeek = TimeMachine.getDayOfWeek();
        int totalStepsTaken = 8000;
        int intentionalStepsTaken = 5000;
        float intentionalDistanceInMiles = 5.1f;
        float intentionalMilesPerHour = 3.3f;
        int intentionalTimeElapsed = 20;
        int goal = 7000;

        sharedPrefManager.storeIntentionalWalkStats(dayOfWeek, intentionalStepsTaken, intentionalDistanceInMiles,
                intentionalMilesPerHour, intentionalTimeElapsed);

        sharedPrefManager.storeGoalForDayOfWeek(dayOfWeek, goal);

        sharedPrefManager.storeTotalStepsForDayOfWeek(dayOfWeek, totalStepsTaken);

        MonthlyActivityLocalData.updateTodayData(mainPageActivity);

        UserDayData dayData = MonthlyActivityLocalData.getMyMonthlyActivity().get(27);
        assertEquals(totalStepsTaken, dayData.getTotalSteps());
        assertEquals(intentionalStepsTaken, dayData.getIntentionalSteps());
        assertEquals(goal, dayData.getGoal());
        assertEquals(intentionalDistanceInMiles, dayData.getIntentionalDistance(), 0.01);
        assertEquals(intentionalMilesPerHour, dayData.getIntentionalMph(), 0.01);
    }

    /*@Test
    public void testEndOfDay() {
        MonthlyActivityLocalData.updateDataAtEndOfDay(mainPageActivity);
    }*/

    @AfterClass
    public static void after() {
        MonthlyActivityLocalData.mock = false;
    }
}
