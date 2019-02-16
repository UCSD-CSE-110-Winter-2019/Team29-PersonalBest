package com.android.personalbest;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class InputHeightEspressoTest {
    @Rule
    public ActivityTestRule<InputHeightActivity> mActivityTestRule = new ActivityTestRule<>(InputHeightActivity.class);

    private String height = "65";
    private String startText = "Start Walk";
    private String defaultGoal = "5000";

    @Test
    public void inputHeightTest() {

        Espresso.onView((withId(R.id.userHeight)))
                .perform(ViewActions.typeText(height));

        Espresso.closeSoftKeyboard();

        Espresso.onView((withId(R.id.userHeight)))
                .check(matches(withText(height)));

        Espresso.onView(withId(R.id.done))
                .perform(ViewActions.click());

        //Check for the green "Start Walk" button
        Espresso.onView(withId(R.id.startButton))
                .check(matches(withText(startText)));

        //Check for default goal
        Espresso.onView(withId(R.id.goal))
                .check(matches(withText(defaultGoal)));

    }
}


