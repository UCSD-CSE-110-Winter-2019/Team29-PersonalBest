package com.android.personalbest;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class BarChartEspressoTest {
    @BeforeClass
    public static void beforeClass(){
        MainPageActivity.mock = true;
    }

    @Rule
    public ActivityTestRule<MainPageActivity> mActivityTestRule = new ActivityTestRule<>(MainPageActivity.class);

    @Before
    public void beforeTest(){
        MainPageActivity mainPageActivity = mActivityTestRule.getActivity();
        SharedPrefManager sharedPrefManager = new SharedPrefManager(mainPageActivity);

        //clear shared pref
        sharedPrefManager.editor.clear();
        sharedPrefManager.editor.apply();

        //set values to default from input height
        sharedPrefManager.setHeight(65);
        sharedPrefManager.setGoal(mainPageActivity.getResources().getInteger(R.integer.default_goal));
        sharedPrefManager.storeGoal(TimeMachine.getDay(), mainPageActivity.getResources().getInteger(R.integer.default_goal));
        sharedPrefManager.setFirstTime(true);
        sharedPrefManager.setIsWalker(true);
    }

    @Test
    public void runWalkEspressoTest() {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Update the steps once
        Espresso.onView(withId(R.id.updateSteps))
                .perform(ViewActions.click());

        //Check that the steps have increased to 500
        Espresso.onView(withId(R.id.numStepDone))
                .check(matches(withText("500")));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Update steps again
        Espresso.onView(withId(R.id.updateSteps))
                .perform(ViewActions.click());

        //Check that the steps have increased to 1000
        Espresso.onView(withId(R.id.numStepDone))
                .check(matches(withText("1000")));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.seeBarChart))
                .perform(ViewActions.click());

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
