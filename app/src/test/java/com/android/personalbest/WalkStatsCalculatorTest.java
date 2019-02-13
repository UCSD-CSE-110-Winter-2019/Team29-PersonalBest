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

    //added to avoid empty test suite error
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
        double expectedMiles = 0.4;
        assertEquals(expectedMiles, walkStatsCalculator.calculateMiles(numStepsTaken, numStepsInMile), 0.05);
    }

    @Test
    public void testCalculateMilesWhenWalkedMoreThanMile() {
        int numStepsInMile = 2360;
        int numStepsTaken = 5000;
        double expectedMiles = 2.1;
        assertEquals(expectedMiles, walkStatsCalculator.calculateMiles(numStepsTaken, numStepsInMile), 0.05);
    }

    @Test
    public void testCalculateMilesPerHour() {
        double miles = 2.12;
        int minutes = 45;
        double expectedMilesPerHour = 2.8;
        assertEquals(expectedMilesPerHour, walkStatsCalculator.calculateMilesPerHour(miles, minutes), 0.05);
    }
}
