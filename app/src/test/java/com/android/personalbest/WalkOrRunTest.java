package com.android.personalbest;

import android.content.res.Resources;
import android.widget.RadioButton;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class WalkOrRunTest {
    UserSettingsActivity usersettings;
    InputHeightActivity inputHeight;
    MainPageActivity mainPage;
    WalkActivity walkActivity;
    Resources res;
    private SharedPrefManager sharedPrefManager;

    public WalkOrRunTest() {}

    @BeforeClass
    public static void beforeClass(){
        MainPageActivity.mockCloud = true;
    }

    @Before
    public void init() {
        usersettings = Robolectric.setupActivity(UserSettingsActivity.class);
        inputHeight = Robolectric.setupActivity(InputHeightActivity.class);
        mainPage = Robolectric.setupActivity(MainPageActivity.class);
        walkActivity = Robolectric.setupActivity(WalkActivity.class);
        res = usersettings.getResources();
        sharedPrefManager = new SharedPrefManager(usersettings.getApplicationContext());
    }

    @Test
    public void testWalkOption() {
        //grabbing the walker option and checking it
        RadioButton walkerOpt = usersettings.findViewById(R.id.walkerOption);
        walkerOpt.performClick();

        boolean walker = sharedPrefManager.getIsWalker();
        assertEquals(true, walker);
    }

    @Test
    public void testRunOption() {
        //grabbing the walker option and checking it
        RadioButton runnerOpt = usersettings.findViewById(R.id.runnerOption);
        runnerOpt.performClick();

        boolean walker = sharedPrefManager.getIsWalker();
        assertEquals(false, walker);

    }



}