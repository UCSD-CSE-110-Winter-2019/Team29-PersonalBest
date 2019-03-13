package com.android.personalbest;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDateTime;

public class TestSubGoal {
    @Rule
    public ActivityTestRule<MainPageActivity> mainPageActivity = new ActivityTestRule<MainPageActivity>(MainPageActivity.class);


    @Test
    public void testTriggerSubGoal(){
        LocalDateTime dummyTime = LocalDateTime.of(2018,3,11,12,00);
        TimeMachine.useFixedClockAt(dummyTime);

    }

}
