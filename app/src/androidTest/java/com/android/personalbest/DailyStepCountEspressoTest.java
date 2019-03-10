package com.android.personalbest;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.android.personalbest.fitness.TestFitnessService;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class DailyStepCountEspressoTest {

    @BeforeClass
    public static void beforeClass(){
        MainPageActivity.mock = true;
    }

    @Rule
    public ActivityTestRule<MainPageActivity> mActivityTestRule = new ActivityTestRule<>(MainPageActivity.class);

    @Before
    public void beforeTest(){
        final MainPageActivity mainPageActivity = mActivityTestRule.getActivity();
        mainPageActivity.runOnUiThread(new Runnable() {
            public void run() {
                mainPageActivity.resetDisplayToDefault();
            }
        });
    }

    @Test
    public void dailyStepCountEspressoTest() {

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
