package com.android.personalbest;

import android.util.Log;
import android.widget.Button;

import com.github.mikephil.charting.data.BarEntry;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class BarChartTest {

    private MainPageActivity mainPage;
    private BarChartActivity barChartActivity;
    private SharedPrefManager sharedPrefManager;
    ArrayList<BarEntry> entriesTest = new ArrayList<>();
    public BarChartTest() {}

    @BeforeClass
    public static void beforeClass(){
        MainPageActivity.mockCloud = true;
    }

    @Before
    public void init() {
        mainPage = Robolectric.setupActivity(MainPageActivity.class);
        sharedPrefManager = new SharedPrefManager(mainPage.getApplicationContext());
        initBar();
        barChartActivity = Robolectric.setupActivity(BarChartActivity.class);
    }

    public void initBar() {
        int sundaySteps = 1000;
        int mondaySteps = 1500;
        int tuesdaySteps = 2000;
        int wednesdaySteps = 2500;
        int thursdaySteps = 3000;
        int fridaySteps = 3500;
        int saturdaySteps = 4000;

        sharedPrefManager.storeTotalStepsForDayOfWeek(Calendar.SUNDAY, sundaySteps);
        sharedPrefManager.storeTotalStepsForDayOfWeek(Calendar.MONDAY, mondaySteps);
        sharedPrefManager.storeTotalStepsForDayOfWeek(Calendar.TUESDAY, tuesdaySteps);
        sharedPrefManager.storeTotalStepsForDayOfWeek(Calendar.WEDNESDAY, wednesdaySteps);
        sharedPrefManager.storeTotalStepsForDayOfWeek(Calendar.THURSDAY, thursdaySteps);
        sharedPrefManager.storeTotalStepsForDayOfWeek(Calendar.FRIDAY, fridaySteps);
        sharedPrefManager.storeTotalStepsForDayOfWeek(Calendar.SATURDAY, saturdaySteps);

        BarEntry sundayEntry= new BarEntry(1f, new float[] { sharedPrefManager.getIntentionalStepsTaken(Calendar.SUNDAY), sharedPrefManager.getNonIntentionalStepsTaken(Calendar.SUNDAY)});
        BarEntry mondayEntry= new BarEntry(2f, new float[] { sharedPrefManager.getIntentionalStepsTaken(Calendar.MONDAY), sharedPrefManager.getNonIntentionalStepsTaken(Calendar.MONDAY)});
        BarEntry tuesdayEntry= new BarEntry(3f, new float[] { sharedPrefManager.getIntentionalStepsTaken(Calendar.TUESDAY), sharedPrefManager.getNonIntentionalStepsTaken(Calendar.TUESDAY)});
        BarEntry wednesdayEntry= new BarEntry(4f, new float[] { sharedPrefManager.getIntentionalStepsTaken(Calendar.WEDNESDAY), sharedPrefManager.getNonIntentionalStepsTaken(Calendar.WEDNESDAY)});
        BarEntry thursdayEntry= new BarEntry(5f, new float[] { sharedPrefManager.getIntentionalStepsTaken(Calendar.THURSDAY), sharedPrefManager.getNonIntentionalStepsTaken(Calendar.THURSDAY)});
        BarEntry fridayEntry= new BarEntry(6f, new float[] { sharedPrefManager.getIntentionalStepsTaken(Calendar.FRIDAY), sharedPrefManager.getNonIntentionalStepsTaken(Calendar.FRIDAY)});
        BarEntry saturdayEntry= new BarEntry(7f, new float[] { sharedPrefManager.getIntentionalStepsTaken(Calendar.SATURDAY), sharedPrefManager.getNonIntentionalStepsTaken(Calendar.SATURDAY)});

        entriesTest.add(sundayEntry);
        entriesTest.add(mondayEntry);
        entriesTest.add(tuesdayEntry);
        entriesTest.add(wednesdayEntry);
        entriesTest.add(thursdayEntry);
        entriesTest.add(fridayEntry);
        entriesTest.add(saturdayEntry);
    }

    @Test
    public void testBar(){
        Button goToBarChart = mainPage.findViewById(R.id.seeBarChart);
        goToBarChart.performClick();

        ArrayList<BarEntry> originalEntries = barChartActivity.entries;

        //making sure two arraylists of data are equal
        for(int i = 0; i < originalEntries.size(); i++){
            assert(originalEntries.get(i).getX() == entriesTest.get(i).getX());
            assert(originalEntries.get(i).getY() == entriesTest.get(i).getY());
        }
    }

    @Test
    public void testLine(){
        int goal = 1500;

        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        sharedPrefManager.resetSharedPrefForDay(today);
        sharedPrefManager.storeGoalForDayOfWeek(today, goal);
        sharedPrefManager.setGoal(1500);

        Button goToBarChart = mainPage.findViewById(R.id.seeBarChart);
        goToBarChart.performClick();

        //making sure both sharedpreferences stored the goal
        //which corresponds to a y-value of 1500 of the line on the chart
        assertEquals(sharedPrefManager.getGoal(), 1500);
    }

}
