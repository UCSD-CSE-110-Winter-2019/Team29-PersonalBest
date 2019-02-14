package com.android.personalbest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.widget.Button;
import android.widget.RadioButton;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class WalkOrRunTest {
    UserSettings usersettings;
    InputHeightActivity inputHeight;
    MainPageActivity mainPage;
    WalkActivity walkActivity;
    Resources res;

    public WalkOrRunTest() {}

    @Before
    public void init() {
        usersettings = Robolectric.setupActivity(UserSettings.class);
        inputHeight = Robolectric.setupActivity(InputHeightActivity.class);
        mainPage = Robolectric.setupActivity(MainPageActivity.class);
        walkActivity = Robolectric.setupActivity(WalkActivity.class);
        res = usersettings.getResources();
    }

    @Test
    public void testWalkOption() {
        //grabbing the walker option and checking it
        RadioButton walkerOpt = usersettings.findViewById(R.id.walkerOption);
        walkerOpt.performClick();

        SharedPreferences sharedPref = usersettings.getSharedPreferences("walkerOrRunner", Context.MODE_PRIVATE);
        boolean walker = sharedPref.getBoolean("isWalker", false);
        assertEquals(true, walker);

        //going back home
        Button goHome = usersettings.findViewById(R.id.homeButton);
        goHome.performClick();

        //checking that start button says "start walk"
        /**Button startButton = mainPage.findViewById(R.id.startButton);
        String startButtonText = startButton.getText().toString();
        assertEquals("Start Walk", startButtonText);

        //checking end button says "end walk"
        startButton.performClick();
        Button endButton = walkActivity.findViewById(R.id.endButton);
        String endButtonText = endButton.getText().toString();
        assertEquals("End Walk", endButtonText);**/
    }

    @Test
    public void testRunOption() {
        //grabbing the walker option and checking it
        RadioButton runnerOpt = usersettings.findViewById(R.id.runnerOption);
        runnerOpt.performClick();

        SharedPreferences sharedPref = usersettings.getSharedPreferences("walkerOrRunner", Context.MODE_PRIVATE);
        boolean walker = sharedPref.getBoolean("isWalker", true);
        assertEquals(false, walker);

        //going back home
        Button goHome = usersettings.findViewById(R.id.homeButton);
        goHome.performClick();

        //checking that start button says "start run"
       /** Button startButton = mainPage.findViewById(R.id.startButton);
        String startButtonText = startButton.getText().toString();
        assertEquals("Start Run", startButtonText);

        //checking end button says "end run"
        startButton.performClick();
        Button endButton = walkActivity.findViewById(R.id.endButton);
        String endButtonText = endButton.getText().toString();
        assertEquals("End Run", endButtonText);**/
    }



}
