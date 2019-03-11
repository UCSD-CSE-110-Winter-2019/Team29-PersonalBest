package com.android.personalbest;

import android.widget.TextView;

import com.android.personalbest.walkData.WalkDataAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class WalkActivityUnitTest {

    private WalkActivity walkActivity;
    private TextView intentionalStepTextView;
    private SharedPrefManager sharedPrefManager;
    private int expectedStepCount;

    @Before
    public void setup(){

        walkActivity = Robolectric.setupActivity(WalkActivity.class);
        walkActivity.walkDataAdapter = new WalkDataAdapter(walkActivity);
        intentionalStepTextView = walkActivity.findViewById(R.id.intentionalStep);
        sharedPrefManager = new SharedPrefManager(walkActivity.getApplicationContext());
    }

    @Test
    public void testUpdateIntentionalStep() {

        walkActivity.walkDataAdapter.totalStepBeforeSwitch = 1000;
        walkActivity.walkDataAdapter.totalStep = 3000;
        walkActivity.walkDataAdapter.displayStep();
        expectedStepCount = walkActivity.walkDataAdapter.totalStep -  walkActivity.walkDataAdapter.totalStepBeforeSwitch;

        assertEquals(String.valueOf(expectedStepCount), intentionalStepTextView.getText().toString());
    }

    @Test
    public void testUpdateMiles() {

        sharedPrefManager.setCurrIntentionalStep(100);
        sharedPrefManager.setHeight(100);
        walkActivity.walkDataAdapter.displayMiles();

        assertEquals("0.1", walkActivity.milesTextView.getText().toString());
    }

    @Test
    public void testUpdateMPH() {

        sharedPrefManager.setCurrMile(7f);
        walkActivity.walkDataAdapter.curElapsedTime = 0;
        walkActivity.walkDataAdapter.displayMPH();

        assertEquals("0.0", walkActivity.MPHTextView.getText().toString());
    }
}
