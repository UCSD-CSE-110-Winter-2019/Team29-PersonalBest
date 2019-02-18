/**
 * Really basic espresso test
 * Right now it crashes if the phone requires sign in or input height, it has to start off on mainpageactivity to work
 * This is what it does:
 * - Clicks user settings
 * - Switches to Runner
 * - Goes to home screen, should display "Start Run"
 * - Clicks "Start Run", should display "End Run"
 * - Clicks "End Run"
 * - Clicks user settings
 * - Switches to Walker
 *  - Goes to home screen, should display "Start Walk"
 *  - Clicks "Start Walk", should display "End Walk"
 *  - Clicks "End Walk"
 *  - Clicks user settings
 *  - Clicks "update goal"
 *  - Input new goal of 1000
 *  - Goes back to home screen, should display new goal of 1000
 *  - Clicks "see bar chart"
 *  - Line on bar chart for today should update to 1000
 *
 * Doesn't assert anything right now (i.e. check if text is equal or if button exists)
 */


package com.android.personalbest;


import android.content.SharedPreferences;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
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
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RunWalkEspressoTest {

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

        Espresso.onView(withId(R.id.startButton))
                .check(matches(withText(R.string.start_walk)));

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.userSettings), withText("User Settings"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatRadioButton = onView(
                allOf(withId(R.id.runnerOption), withText("Runner"),
                        childAtPosition(
                                allOf(withId(R.id.radioChoices),
                                        childAtPosition(
                                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                                5)),
                                1),
                        isDisplayed()));
        appCompatRadioButton.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.homeButton), withText("Home"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatButton3.perform(click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.startButton))
                .check(matches(withText(R.string.start_run)));

        Espresso.onView(withId(R.id.startButton))
                .perform(ViewActions.click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.endButton), withText("End Run"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));

        Espresso.onView(withId(R.id.endButton))
                .check(matches(withText(R.string.end_run)));

        appCompatButton5.perform(click());

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
