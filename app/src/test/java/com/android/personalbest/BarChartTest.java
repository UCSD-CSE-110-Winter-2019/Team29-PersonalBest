package com.android.personalbest;

import com.android.personalbest.cloud.CloudstoreService;
import com.android.personalbest.cloud.CloudstoreServiceFactory;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class BarChartTest {
    private BarChartActivity barChartActivity;
    private CloudstoreService cloudstoreService;
    private SharedPrefManager sharedPrefManager;

    @BeforeClass
    public static void beforeClass(){
        BarChartActivity.mockCloud = true;
    }

    @Before
    public void setUp() {
        cloudstoreService = CloudstoreServiceFactory.create(barChartActivity, true);
        cloudstoreService.setMockPastData("chl749@ucsd.edu", new MonthlyDataList());
        barChartActivity = Robolectric.setupActivity(BarChartActivity.class);
    }

    @Test
    public void testBarChartValues(){
        int intentionalSteps;
        int nonIntentionalSteps;
        int time;
        int goal;

        UserDayData todayData = barChartActivity.monthlyActivity.get(0);
        intentionalSteps = todayData.getIntentionalSteps();
        nonIntentionalSteps = todayData.getTotalSteps() - intentionalSteps;
        time = todayData.getIntentionalTime();
        goal = todayData.getGoal();
        assertEquals(0,intentionalSteps);
        assertEquals(0,nonIntentionalSteps);
        assertEquals(0,time);
        assertEquals(5000,goal);

        todayData = barChartActivity.monthlyActivity.get(5);
        intentionalSteps = todayData.getIntentionalSteps();
        nonIntentionalSteps = todayData.getTotalSteps() - intentionalSteps;
        time = todayData.getIntentionalTime();
        goal = todayData.getGoal();
        assertEquals(0,intentionalSteps);
        assertEquals(300,nonIntentionalSteps);
        assertEquals(0,time);
        assertEquals(500,goal);

        todayData = barChartActivity.monthlyActivity.get(15);
        intentionalSteps = todayData.getIntentionalSteps();
        nonIntentionalSteps = todayData.getTotalSteps() - intentionalSteps;
        time = todayData.getIntentionalTime();
        goal = todayData.getGoal();
        assertEquals(0,intentionalSteps);
        assertEquals(800,nonIntentionalSteps);
        assertEquals(0,time);
        assertEquals(1000,goal);
    }
}
