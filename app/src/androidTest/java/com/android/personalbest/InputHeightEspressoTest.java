package com.android.personalbest;

import android.app.Activity;
import android.renderscript.ScriptGroup;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.WindowManager;

import com.android.personalbest.fitness.FitnessService;
import com.android.personalbest.fitness.FitnessServiceFactory;
import com.android.personalbest.fitness.TestFitnessService;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class InputHeightEspressoTest extends FitnessServiceFactory {

    @BeforeClass
    public static void beforeClass(){
        MainPageActivity.mockSteps = true;
        MainPageActivity.mockCloud = true;
    }

    @Rule
    public ActivityTestRule<InputHeightActivity> mActivityTestRule = new ActivityTestRule<>(InputHeightActivity.class);

    private String height = "65";
    private String startWalkText = "Start Walk";
    private String defaultGoal = "5000";

    @Test
    public void inputHeightTest() {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView((withId(R.id.userHeight)))
                .perform(ViewActions.typeText(height));

        Espresso.closeSoftKeyboard();

        Espresso.onView((withId(R.id.userHeight)))
                .check(matches(withText(height)));

        Espresso.onView(withId(R.id.done))
                .perform(ViewActions.click());

        //Check for the green "Start Walk" button
        Espresso.onView(withId(R.id.startButton))
                .check(matches(withText(startWalkText)));

        //Check for default goal
        Espresso.onView(withId(R.id.goal))
                .check(matches(withText(defaultGoal)));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}