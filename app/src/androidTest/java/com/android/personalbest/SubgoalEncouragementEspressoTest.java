package com.android.personalbest;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Time;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SubgoalEncouragementEspressoTest {

    SharedPrefManager sharedPrefManager;

    @BeforeClass
    public static void beforeClass(){
        MainPageActivity.mockSteps = true;
        MainPageActivity.mockCloud = true;
        TimeMachine.setHourOfDay(21);
    }

    @Rule
    public ActivityTestRule<MainPageActivity> mActivityTestRule = new ActivityTestRule<>(MainPageActivity.class);

    @Before
    public void beforeTest(){
        final MainPageActivity mainPageActivity = mActivityTestRule.getActivity();
        sharedPrefManager = new SharedPrefManager(mainPageActivity);
        mainPageActivity.runOnUiThread(new Runnable() {
            public void run() {
                mainPageActivity.resetDisplayToDefault();
                sharedPrefManager.storeTotalStepsFromYesterday(1000);
            }
        });
    }

    @Test
    public void meetSubgoalTest() {

        try {
            Thread.sleep(2000);
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
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Update steps again
        Espresso.onView(withId(R.id.updateSteps))
                .perform(ViewActions.click());

        //Check that the steps have increased to 1000
        Espresso.onView(withId(R.id.numStepDone))
                .check(matches(withText("1000")));

        //Update steps again
        Espresso.onView(withId(R.id.updateSteps))
                .perform(ViewActions.click());

        Espresso.onView(withId(R.id.numStepDone))
                .check(matches(withText("1500")));

        Espresso.onView(withId(R.id.updateSteps))
                .perform(ViewActions.click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.userSettings))
                .perform(ViewActions.click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Toast message appears
        Espresso.onView(withId(R.id.homeButton))
                .perform(ViewActions.click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
