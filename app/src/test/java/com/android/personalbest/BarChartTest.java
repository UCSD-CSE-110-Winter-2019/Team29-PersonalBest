package com.android.personalbest;

import android.widget.Button;

import com.github.mikephil.charting.data.BarEntry;

import org.junit.Before;
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
    public BarChartTest() {}

    @Before
    public void init() {
        mainPage = Robolectric.setupActivity(MainPageActivity.class);
        barChartActivity = Robolectric.setupActivity(BarChartActivity.class);
        sharedPrefManager = new SharedPrefManager(mainPage.getApplicationContext());
    }

    @Test
    public void testBar(){
        int sundaySteps = 1000;
        int mondaySteps = 1500;
        int tuesdaySteps = 2000;
        int wednesdaySteps = 2500;
        int thursdaySteps = 3000;
        int fridaySteps = 3500;
        int saturdaySteps = 4000;

        sharedPrefManager.storeTotalSteps(Calendar.SUNDAY, sundaySteps);
        sharedPrefManager.storeTotalSteps(Calendar.MONDAY, mondaySteps);
        sharedPrefManager.storeTotalSteps(Calendar.TUESDAY, tuesdaySteps);
        sharedPrefManager.storeTotalSteps(Calendar.WEDNESDAY, wednesdaySteps);
        sharedPrefManager.storeTotalSteps(Calendar.THURSDAY, thursdaySteps);
        sharedPrefManager.storeTotalSteps(Calendar.FRIDAY, fridaySteps);
        sharedPrefManager.storeTotalSteps(Calendar.SATURDAY, saturdaySteps);

        ArrayList<BarEntry> entriesTest = new ArrayList<>();
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
        sharedPrefManager.storeGoal(today, goal);
        sharedPrefManager.setGoal(1500);

        Button goToBarChart = mainPage.findViewById(R.id.seeBarChart);
        goToBarChart.performClick();

        barChartActivity.pastWeek.resetSharedPrefForDay(today);
        barChartActivity.pastWeek.storeGoal(today, goal);
        sharedPrefManager.setGoal(1500);

        //making sure both sharedpreferences stored the goal
        //which corresponds to a y-value of 1500 of the line on the chart
        assertEquals(sharedPrefManager.getGoal(), barChartActivity.pastWeek.getGoal());
    }

}