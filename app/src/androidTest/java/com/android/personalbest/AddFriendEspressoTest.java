package com.android.personalbest;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
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

import static android.support.test.espresso.matcher.ViewMatchers.withId;


public class AddFriendEspressoTest {
    @BeforeClass
    public static void beforeClass() {
        MainPageActivity.mockSteps = true;
        MainPageActivity.mockCloud = true;
    }

    @Rule
    public ActivityTestRule<MainPageActivity> mActivityTestRule = new ActivityTestRule<>(MainPageActivity.class);

    @Before
    public void beforeTest(){
        final MainPageActivity mainPageActivity = mActivityTestRule.getActivity();
        mainPageActivity.runOnUiThread(new Runnable() {
            public void run() {
                mainPageActivity.resetDisplayToDefault();
                SharedPrefManager sharedPrefManager = new SharedPrefManager(mainPageActivity);
                sharedPrefManager.setMockSteps(true);
                sharedPrefManager.setMockCloud(true);
            }
        });
    }

    @Test
    public void addFriendEspressoTest() {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.goToFriBtn))
                .perform(ViewActions.click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.addFriBtn))
                .perform(ViewActions.click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //type an email
        Espresso.onView((withId(R.id.friendEmail)))
                .perform(ViewActions.typeText("sarah@gmail.com"));

        Espresso.closeSoftKeyboard();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //enter add friend
        Espresso.onView(withId(R.id.addBtn))
                .perform(ViewActions.click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //refresh the friend page
        Espresso.onView(withId(R.id.returnFriendBtn))
                .perform(ViewActions.click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.refreshFriendListBtn))
                .perform(ViewActions.click());

        //show updated UI
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
