package com.android.personalbest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import java.util.Calendar;
import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class StoreIntentionalWalkStatsTest {
    WalkActivity walkActivity;
    SharedPrefManager sharedPrefManager;

    public StoreIntentionalWalkStatsTest() {}

    @Before
    public void init() {
        walkActivity = Robolectric.setupActivity(WalkActivity.class);
        sharedPrefManager = new SharedPrefManager(walkActivity);
    }

    @Test
    public void testStoreWalkStatsSingleWalk() {
        int dayOfWeek = Calendar.MONDAY;
        int intentionalStepsTaken = 5000;
        float intentionalDistanceInMiles = 5.1f;
        float intentionalMilesPerHour = 3.3f;
        int intentionalTimeElapsed = 20;

        sharedPrefManager.storeIntentionalWalkStats(dayOfWeek, intentionalStepsTaken, intentionalDistanceInMiles,
        intentionalMilesPerHour, intentionalTimeElapsed);

        assertEquals(intentionalStepsTaken, sharedPrefManager.getIntentionalStepsTaken(dayOfWeek));
        assertEquals(intentionalDistanceInMiles, sharedPrefManager.getIntentionalDistanceInMiles(dayOfWeek), 0.00);
        assertEquals(intentionalMilesPerHour, sharedPrefManager.getIntentionalMilesPerHour(dayOfWeek), 0.00);
        assertEquals(intentionalTimeElapsed, sharedPrefManager.getIntentionalTimeElapsed(dayOfWeek));
    }

    @Test
    public void testStoreWalkStatsMultipleWalks() {
        //test aggregating data when multiple walks taken
        int dayOfWeek = Calendar.MONDAY;
        int intentionalStepsTaken = 5000;
        float intentionalDistanceInMiles = 5.1f;
        float intentionalMilesPerHour = 3.3f;
        int intentionalTimeElapsed = 20;

        sharedPrefManager.storeIntentionalWalkStats(dayOfWeek, intentionalStepsTaken, intentionalDistanceInMiles,
                intentionalMilesPerHour, intentionalTimeElapsed);

        int moreIntentionalStepsTaken = 3000;
        float moreIntentionalDistanceInMiles = 1.2f;
        float moreIntentionalMilesPerHour = 2.8f;
        int moreIntentionalTimeElapsed = 12;

        int expectedIntentionalStepsTaken = 8000;
        float expectedIntentionalDistanceInMiles = 6.3f;
        float expectedIntentionalMilesPerHour = 3.1f;
        int expectedIntentionalTimeElapsed = 32;

        sharedPrefManager.storeIntentionalWalkStats(dayOfWeek, moreIntentionalStepsTaken, moreIntentionalDistanceInMiles,
                moreIntentionalMilesPerHour, moreIntentionalTimeElapsed);

        assertEquals(expectedIntentionalStepsTaken, sharedPrefManager.getIntentionalStepsTaken(dayOfWeek));
        assertEquals(expectedIntentionalDistanceInMiles, sharedPrefManager.getIntentionalDistanceInMiles(dayOfWeek), 0.00);
        assertEquals(expectedIntentionalMilesPerHour, sharedPrefManager.getIntentionalMilesPerHour(dayOfWeek), 0.00);
        assertEquals(expectedIntentionalTimeElapsed, sharedPrefManager.getIntentionalTimeElapsed(dayOfWeek));
    }

    @Test
    public void testGetWalkStatsFromDifferentDay() {
        int dayOfWeek = Calendar.MONDAY;
        int intentionalStepsTaken = 5000;
        float intentionalDistanceInMiles = 5.1f;
        float intentionalMilesPerHour = 3.3f;
        int intentionalTimeElapsed = 20;

        sharedPrefManager.storeIntentionalWalkStats(dayOfWeek, intentionalStepsTaken, intentionalDistanceInMiles,
                intentionalMilesPerHour, intentionalTimeElapsed);


        dayOfWeek = Calendar.TUESDAY;
        assertEquals(0, sharedPrefManager.getIntentionalStepsTaken(dayOfWeek));
        assertEquals(0, sharedPrefManager.getIntentionalDistanceInMiles(dayOfWeek), 0.00);
        assertEquals(0, sharedPrefManager.getIntentionalMilesPerHour(dayOfWeek), 0.00);
        assertEquals(0, sharedPrefManager.getIntentionalTimeElapsed(dayOfWeek));
    }

    @Test
    public void testResetSharedPrefForDay() {
        int dayOfWeek = Calendar.MONDAY;
        int intentionalStepsTaken = 5000;
        float intentionalDistanceInMiles = 5.1f;
        float intentionalMilesPerHour = 3.3f;
        int intentionalTimeElapsed = 20;

        sharedPrefManager.storeIntentionalWalkStats(dayOfWeek, intentionalStepsTaken, intentionalDistanceInMiles,
                intentionalMilesPerHour, intentionalTimeElapsed);

        assertEquals(intentionalStepsTaken, sharedPrefManager.getIntentionalStepsTaken(dayOfWeek));
        assertEquals(intentionalDistanceInMiles, sharedPrefManager.getIntentionalDistanceInMiles(dayOfWeek), 0.00);
        assertEquals(intentionalMilesPerHour, sharedPrefManager.getIntentionalMilesPerHour(dayOfWeek), 0.00);
        assertEquals(intentionalTimeElapsed, sharedPrefManager.getIntentionalTimeElapsed(dayOfWeek));

        sharedPrefManager.resetSharedPrefForDay(dayOfWeek);
        assertEquals(0, sharedPrefManager.getIntentionalStepsTaken(dayOfWeek));
        assertEquals(0, sharedPrefManager.getIntentionalDistanceInMiles(dayOfWeek), 0.00);
        assertEquals(0, sharedPrefManager.getIntentionalMilesPerHour(dayOfWeek), 0.00);
        assertEquals(0, sharedPrefManager.getIntentionalTimeElapsed(dayOfWeek));

    }
}