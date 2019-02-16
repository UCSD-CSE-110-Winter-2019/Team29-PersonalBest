package com.android.personalbest;

import android.app.Activity;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.android.personalbest.fitness.FitnessService;

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

    private class TestFitnessService implements FitnessService {
        private static final String TAG = "[TestFitnessService]: ";
        private MainPageActivity activity;

        //using MainPageActivity like StepCounterActivity
        public TestFitnessService(MainPageActivity activity) {
            this.activity = activity;
        }

        @Override
        public int getRequestCode() {
            return 0;
        }

        @Override
        public void setup() {
            System.out.println("setup");
        }

        @Override
        public void updateStepCount() {
            System.out.println("update steps");

        }
    }
}


