package com.android.personalbest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.widget.Button;
import android.widget.EditText;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class ChangeGoalTest {
    private UserSettingsActivity userSettings;
    private MainPageActivity mainPage;
    SharedPrefManager pastWeek;


    public ChangeGoalTest() {}

    @BeforeClass
    public static void beforeClass(){
        MainPageActivity.mockCloud = true;
    }

    @Before
    public void init() {
        userSettings = Robolectric.setupActivity(UserSettingsActivity.class);
        mainPage = Robolectric.setupActivity(MainPageActivity.class);
        pastWeek = new SharedPrefManager(mainPage.getApplicationContext());
    }

    @Test
    public void testValidGoalUpdate() {
        Button changeGoal = userSettings.findViewById(R.id.changeGoal);
        changeGoal.performClick();
        EditText userInput = userSettings.edittext;
        userInput.setText("3000");

        AlertDialog dialog = userSettings.dialogBox;
        Button OKButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
        OKButton.performClick();

        Button goHome = userSettings.findViewById(R.id.homeButton);
        goHome.performClick();

        int goal = pastWeek.getGoal();
        assertEquals(3000, goal);
    }
}
