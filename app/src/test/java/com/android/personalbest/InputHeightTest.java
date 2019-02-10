package test;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.widget.Button;
import android.widget.EditText;

import com.android.personalbest.InputHeightActivity;
import com.android.personalbest.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowToast;

import static org.junit.Assert.assertEquals;


@RunWith(RobolectricTestRunner.class)
public class InputHeightTest {

    InputHeightActivity heightInputActivity;
    Resources res;

    //added to avoid empty test suite error
    public InputHeightTest() {}

    @Before
    public void init() {
        heightInputActivity = Robolectric.setupActivity(InputHeightActivity.class);
        res = heightInputActivity.getResources();
    }

    @Test
    public void testValidInputHeightStored() {
        //Set the height input text field manually
        EditText userHeight = heightInputActivity.findViewById(R.id.userHeight);
        userHeight.setText("55");

        //Press done button
        Button doneButton = heightInputActivity.findViewById(R.id.done);
        doneButton.performClick();

        //Check sharedPref value
        SharedPreferences sharedPref = heightInputActivity.getSharedPreferences(res.getString(R.string.user_prefs), Context.MODE_PRIVATE);
        assertEquals(55, sharedPref.getInt(res.getString(R.string.height), 0));
    }

    @Test
    public void testDefaultGoalStored() {
        //Given valid height input
        EditText userHeight = heightInputActivity.findViewById(R.id.userHeight);
        userHeight.setText("55");

        //Press done button
        Button doneButton = heightInputActivity.findViewById(R.id.done);
        doneButton.performClick();

        //Check sharedPref value
        SharedPreferences sharedPref = heightInputActivity.getSharedPreferences(res.getString(R.string.user_prefs), Context.MODE_PRIVATE);
        assertEquals(res.getInteger(R.integer.default_goal), sharedPref.getInt(res.getString(R.string.goal), 0));
    }

    //Check and handle invalid height inputs (empty, some thresholds, non-integers)
    @Test
    public void testEmptyInputHeight() {
        EditText userHeight = heightInputActivity.findViewById(R.id.userHeight);

        //Press done button
        Button doneButton = heightInputActivity.findViewById(R.id.done);
        doneButton.performClick();

        //Check Toast message value
        String latestToast = ShadowToast.getTextOfLatestToast();
        assertEquals(heightInputActivity.getResources().getString(R.string.empty_input), latestToast);
    }

    @Test
    public void testInvalidHeights() {
        EditText userHeight = heightInputActivity.findViewById(R.id.userHeight);
        userHeight.setText("2");

        //Press done button
        Button doneButton = heightInputActivity.findViewById(R.id.done);
        doneButton.performClick();

        //Check Toast message value
        String latestToast = ShadowToast.getTextOfLatestToast();
        assertEquals(heightInputActivity.getResources().getString(R.string.invalid_input), latestToast);

        userHeight.setText("500");
        doneButton.performClick();
        latestToast = ShadowToast.getTextOfLatestToast();
        assertEquals(heightInputActivity.getResources().getString(R.string.invalid_input), latestToast);
    }
}
