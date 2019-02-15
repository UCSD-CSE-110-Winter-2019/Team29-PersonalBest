package com.android.personalbest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class WalkStatsCalculatorTest {
    WalkActivity walkActivity;
    WalkStatsCalculator walkStatsCalculator;

    public WalkStatsCalculatorTest() {}

    @Before
    public void init() {
        walkActivity = Robolectric.setupActivity(WalkActivity.class);
        walkStatsCalculator = new WalkStatsCalculator(walkActivity);
    }

    @Test
    public void testCalculateNumStepsInMile1() {
        int heightInInches = 65; // 5.5 feet
        int expectedNumStepsInMile = 2360; // calculation used: 5280/((65 * 0.413)/12)
        assertEquals(expectedNumStepsInMile, walkStatsCalculator.calculateNumStepsInMile(heightInInches));
    }

    @Test
    public void testCalculateNumStepsInMile2() {
        int heightInInches = 74; // 6.2 feet
        int expectedNumStepsInMile = 2073; // calculation used: 5280/((74 * 0.413)/12)
        assertEquals(expectedNumStepsInMile, walkStatsCalculator.calculateNumStepsInMile(heightInInches));
    }

    @Test
    public void testCalculateMilesWhenWalkedLessThanMile() {
        int numStepsInMile = 2360;
        int numStepsTaken = 1000;
        float expectedMiles = 0.4f;
        assertEquals(expectedMiles, walkStatsCalculator.calculateMiles(numStepsTaken, numStepsInMile), 0.00);
    }

    @Test
    public void testCalculateMilesWhenWalkedMoreThanMile() {
        int numStepsInMile = 2360;
        int numStepsTaken = 5000;
        float expectedMiles = 2.1f;
        assertEquals(expectedMiles, walkStatsCalculator.calculateMiles(numStepsTaken, numStepsInMile), 0.00);
    }

    @Test
    public void testCalculateMilesPerHour() {
        float miles = 2.12f;
        int milliseconds  = 2700000;
        float expectedMilesPerHour = 2.8f;
        assertEquals(expectedMilesPerHour, walkStatsCalculator.calculateMilesPerHour(miles, milliseconds), 0.00);
    }
}
