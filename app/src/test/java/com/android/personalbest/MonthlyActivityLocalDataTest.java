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
        int goal = 7000;

        sharedPrefManager.storeTotalStepsForDayOfWeek(dayOfWeek, totalStepsTaken);
        sharedPrefManager.setCurrIntentionalStep(intentionalStepsTaken);
        sharedPrefManager.setCurrMile(intentionalDistanceInMiles);
        sharedPrefManager.setCurrMPH(intentionalMilesPerHour);
        sharedPrefManager.setGoal(goal);

        MonthlyActivityLocalData.updateTodayData(mainPageActivity);

        UserDayData dayData = MonthlyActivityLocalData.getMyMonthlyActivity().get(27);
        assertEquals(totalStepsTaken, dayData.getTotalSteps());
        assertEquals(intentionalStepsTaken, dayData.getIntentionalSteps());
        assertEquals(intentionalDistanceInMiles, dayData.getIntentionalDistance(), 0.01);
        assertEquals(intentionalMilesPerHour, dayData.getIntentionalMph(), 0.01);
        assertEquals(goal, dayData.getGoal());
    }

    @Test
    public void testEndOfDay() {

        MonthlyActivityLocalData.storeMonthlyActivityForNewUser();

        int dayOfWeek = TimeMachine.getDayOfWeek();
        int totalStepsTaken = 8000;
        int intentionalStepsTaken = 5000;
        float intentionalDistanceInMiles = 5.1f;
        float intentionalMilesPerHour = 3.3f;
        int goal = 7000;

        sharedPrefManager.storeTotalStepsForDayOfWeek(dayOfWeek, totalStepsTaken);
        sharedPrefManager.setCurrIntentionalStep(intentionalStepsTaken);
        sharedPrefManager.setCurrMile(intentionalDistanceInMiles);
        sharedPrefManager.setCurrMPH(intentionalMilesPerHour);
        sharedPrefManager.setGoal(goal);

        MonthlyActivityLocalData.updateDataAtEndOfDay(mainPageActivity);

        UserDayData dayData = MonthlyActivityLocalData.getMyMonthlyActivity().get(26);
        assertEquals(totalStepsTaken, dayData.getTotalSteps());
        assertEquals(intentionalStepsTaken, dayData.getIntentionalSteps());
        assertEquals(intentionalDistanceInMiles, dayData.getIntentionalDistance(), 0.01);
        assertEquals(intentionalMilesPerHour, dayData.getIntentionalMph(), 0.01);
        assertEquals(goal, dayData.getGoal());

        dayData = MonthlyActivityLocalData.getMyMonthlyActivity().get(27);
        assertEquals(0, dayData.getTotalSteps());
        assertEquals(0, dayData.getIntentionalSteps());
        assertEquals(0.0, dayData.getIntentionalDistance(), 0.01);
        assertEquals(0.0, dayData.getIntentionalMph(), 0.01);
        assertEquals(goal, dayData.getGoal());
    }

    @AfterClass
    public static void after() {
        MainPageActivity.mockCloud = false;
        MonthlyActivityLocalData.mock = false;
    }
}
