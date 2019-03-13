package com.android.personalbest;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class MonthlyDataListTest {

    MainPageActivity mainPageActivity;
    SharedPrefManager sharedPrefManager;
    MonthlyDataList monthlyDataList;

    @BeforeClass
    public static void beforeClass(){
        MainPageActivity.mockCloud = true;
        TimeMachine.setDayOfWeek(Calendar.MONDAY);
    }

    @Before
    public void init() {
        mainPageActivity = Robolectric.setupActivity(MainPageActivity.class);
        sharedPrefManager = new SharedPrefManager(mainPageActivity);
        monthlyDataList = new MonthlyDataList(mainPageActivity);
    }

    @Test
    public void testNewUser() {
        UserDayData dayData = monthlyDataList.getTodayData();
        assertEquals(0, dayData.getTotalSteps());
        assertEquals(0, dayData.getIntentionalSteps());
        assertEquals(5000, dayData.getGoal());
        assertEquals(0.0, dayData.getIntentionalDistance(), 0.01);
        assertEquals(0.0, dayData.getIntentionalMph(), 0.01);
    }

    @Test
    public void testUpdateData() {
        int dayOfWeek = TimeMachine.getDayOfWeek();
        int totalStepsTaken = 8000;
        int intentionalStepsTaken = 5000;
        float intentionalDistanceInMiles = 5.1f;
        float intentionalMilesPerHour = 3.3f;
        int goal = 7000;
        int intentionalTimeElapsed = 20;

        sharedPrefManager.storeTotalStepsForDayOfWeek(dayOfWeek, totalStepsTaken);
        sharedPrefManager.storeIntentionalWalkStats(dayOfWeek, intentionalStepsTaken, intentionalDistanceInMiles, intentionalMilesPerHour, intentionalTimeElapsed);
        sharedPrefManager.setGoal(goal);

        monthlyDataList.updateTodayData();

        UserDayData dayData = monthlyDataList.getTodayData();
        assertEquals(totalStepsTaken, dayData.getTotalSteps());
        assertEquals(intentionalStepsTaken, dayData.getIntentionalSteps());
        assertEquals(intentionalDistanceInMiles, dayData.getIntentionalDistance(), 0.01);
        assertEquals(intentionalMilesPerHour, dayData.getIntentionalMph(), 0.01);
        assertEquals(goal, dayData.getGoal());
    }

    @Test
    public void testEndOfDay() {
        int dayOfWeek = TimeMachine.getDayOfWeek();
        int totalStepsTaken = 8000;
        int intentionalStepsTaken = 5000;
        float intentionalDistanceInMiles = 5.1f;
        float intentionalMilesPerHour = 3.3f;
        int goal = 7000;
        int intentionalTimeElapsed = 20;

        sharedPrefManager.storeTotalStepsForDayOfWeek(dayOfWeek, totalStepsTaken);
        sharedPrefManager.storeIntentionalWalkStats(dayOfWeek, intentionalStepsTaken, intentionalDistanceInMiles, intentionalMilesPerHour, intentionalTimeElapsed);
        sharedPrefManager.setGoal(goal);

        monthlyDataList.updateDataAtEndOfDay();

        UserDayData dayData = monthlyDataList.list.get(26);
        assertEquals(totalStepsTaken, dayData.getTotalSteps());
        assertEquals(intentionalStepsTaken, dayData.getIntentionalSteps());
        assertEquals(intentionalDistanceInMiles, dayData.getIntentionalDistance(), 0.01);
        assertEquals(intentionalMilesPerHour, dayData.getIntentionalMph(), 0.01);
        assertEquals(goal, dayData.getGoal());

        dayData = monthlyDataList.getTodayData();
        assertEquals(0, dayData.getTotalSteps());
        assertEquals(0, dayData.getIntentionalSteps());
        assertEquals(0.0, dayData.getIntentionalDistance(), 0.01);
        assertEquals(0.0, dayData.getIntentionalMph(), 0.01);
        assertEquals(goal, dayData.getGoal());
    }

    @AfterClass
    public static void after() {
        MainPageActivity.mockCloud = false;
    }
}
