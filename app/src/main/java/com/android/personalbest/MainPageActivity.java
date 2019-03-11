package com.android.personalbest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.personalbest.cloud.CloudstoreService;
import com.android.personalbest.cloud.CloudstoreServiceFactory;
import com.android.personalbest.fitness.FitnessService;
import com.android.personalbest.fitness.FitnessServiceFactory;
import com.android.personalbest.notifications.GoalNotificationAdapter;
import com.google.firebase.FirebaseApp;

import java.util.Calendar;

public class MainPageActivity extends AppCompatActivity {
    private Button startButton;
    private Button seeBarChart;
    private Button userSettings;
    private Button seeFriends;

    private FitnessService fitnessService;
    private int curStep;

    public TextView numStepDone;
    public TextView goal;
    public SharedPrefManager sharedPrefManager;
    public CloudstoreService cloudstoreService;

    public static boolean mockSteps = false;
    public static boolean mockCloud = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //initialize components
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        if(BuildConfig.DEBUG){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        sharedPrefManager = new SharedPrefManager(this.getApplicationContext());
        goal = findViewById(R.id.goal);
        startButton = findViewById(R.id.startButton);
        seeBarChart = findViewById(R.id.seeBarChart);
        userSettings = findViewById(R.id.userSettings);
        numStepDone = findViewById(R.id.numStepDone);
        seeFriends = findViewById(R.id.goToFriBtn);

        FirebaseApp.initializeApp(this.getApplicationContext()); //added during testing, may need to be called each time created
        cloudstoreService = CloudstoreServiceFactory.create(this.getApplicationContext(), mockCloud);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.setSubGoalExceededToday(false);

        fitnessService = FitnessServiceFactory.create(this, mockSteps);

        //set button listeners
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                curStep = fitnessService.getCurrentStep();
                sharedPrefManager.setCountBeforeWalk(curStep);
                launchWalkActivity();
            }
        });

        seeBarChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchBarChartActivity();
            }
        });

        seeFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFriendListActivity();
            }
        });

        userSettings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchUserSettings();
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        goal.setText(String.valueOf(sharedPrefManager.getGoal()));
        checkWalkOrRun();
        checkGoalMet();
        checkSubGoalMet();
    }

    public void launchWalkActivity() {
        Intent walk = new Intent(this, WalkActivity.class);
        startActivity(walk);
    }

    public void launchUserSettings() {
        Intent settings = new Intent(this, UserSettingsActivity.class);
        startActivity(settings);
    }

    public void launchBarChartActivity() {
        Intent walk = new Intent(this, BarChartActivity.class);
        startActivity(walk);
    }

    public void launchNewGoalActivity() {
        Intent newGoal = new Intent(this, NewGoalActivity.class);
        startActivity(newGoal);
    }

    public void launchFriendListActivity(){
        Intent friendList = new Intent(this,FriendListActivity.class);
        startActivity(friendList);
    }

    private void checkWalkOrRun() {
        boolean walker = sharedPrefManager.getIsWalker();
        if(walker){
            startButton.setText(getString(R.string.start_walk));
        }
        else{
            startButton.setText(getString(R.string.start_run));
        }
    }

    public void newDay() {
        int storedDay = sharedPrefManager.getDayOfWeekInStorage();

        sharedPrefManager.storeGoalForDayOfWeek(storedDay, sharedPrefManager.getGoal());
        sharedPrefManager.storeTotalStepsFromYesterday(sharedPrefManager.getTotalStepsForDayOfWeek(storedDay));
        sharedPrefManager.setGoalExceededToday(false);
        sharedPrefManager.setSubGoalExceededToday(false);

        cloudstoreService.updateMonthlyActivityEndOfDay(sharedPrefManager.getCurrentAppUserEmail());

        //check if it's a new week and we need to reset the bar chart
        if (storedDay == Calendar.SATURDAY) {
            sharedPrefManager.resetSharedPrefForWeek();
            sharedPrefManager.setIgnoreGoal(false);
        }

        sharedPrefManager.setDayOfWeekInStorage(TimeMachine.getDayOfWeek());
        sharedPrefManager.setDayOfMonthInStorage(TimeMachine.getDayOfMonth());
        sharedPrefManager.setMonthInStorage(TimeMachine.getMonth());
    }

    public void exceedsGoal() {
        sharedPrefManager.setGoalExceededToday(true);
        if (!sharedPrefManager.getIgnoreGoal()) {
            sharedPrefManager.setGoalMessageDay(TimeMachine.getDayOfMonth());
            sharedPrefManager.setGoalReached(true);
            sendGoalNotification();
        }
    }

    public void sendGoalNotification() {
        GoalNotificationAdapter goalNotif = new GoalNotificationAdapter(this);
        goalNotif.sendNotif(getString(R.string.goalNotifTitle), getString(R.string.goalNotifBody));
    }

    public void exceedsSubGoal() {
        sharedPrefManager.setSubGoalExceededToday(true);
        sharedPrefManager.setSubGoalMessageDay(TimeMachine.getDayOfMonth());
        sharedPrefManager.setSubGoalReached(true);
    }

    private void checkGoalMet() {
        if (sharedPrefManager.getGoalReached() && !sharedPrefManager.getIgnoreGoal()) {
            int dayOfMonth = TimeMachine.getDayOfMonth();

            int goalDay = sharedPrefManager.getGoalMessageDay();

            if (dayOfMonth == goalDay || dayOfMonth == goalDay + 1) {
                sharedPrefManager.setGoalReached(false);
                launchNewGoalActivity();
            }
        }
    }

    private void checkSubGoalMet() {
        if (sharedPrefManager.getSubGoalReached()) {
            int dayOfMonth = TimeMachine.getDayOfMonth();
            long time = TimeMachine.getHourOfDay();
            int subGoalDay = sharedPrefManager.getSubGoalMessageDay();

            if ((dayOfMonth == subGoalDay + 1) || (dayOfMonth == subGoalDay && time > getResources().getInteger(R.integer.eight_pm))) {
                sharedPrefManager.setSubGoalReached(false);
                showSubGoalMsg();
            }
        }
    }

    private void showSubGoalMsg() {
        int difference = sharedPrefManager.getTotalStepsForDayOfWeek(TimeMachine.getDayOfWeek()) - sharedPrefManager.getTotalStepsFromYesterday();
        int specificSubGoal = (difference / getResources().getInteger(R.integer.subgoal)) * getResources().getInteger(R.integer.subgoal);
        Toast.makeText(getApplicationContext(), getString(R.string.subgoal_msg_p1) + specificSubGoal + getString(R.string.subgoal_msg_p2), Toast.LENGTH_LONG).show();
    }

    public void addToStepCount(int steps) {
        int completedSteps = Integer.parseInt(numStepDone.getText().toString());
        int totalSteps = completedSteps + steps;
        totalUpdated(totalSteps);
    }

    public void totalUpdated(int total) {
        numStepDone.setText(String.valueOf(total));
        if (total < sharedPrefManager.getTotalStepsForDayOfWeek(sharedPrefManager.getDayOfWeekInStorage())){
            //total was reset to 0, it's a new day
            newDay();
        }
        if (total > sharedPrefManager.getGoal() && !sharedPrefManager.getGoalExceededToday()) {
            exceedsGoal();
            Log.i(getString(R.string.mpa_tag), getString(R.string.goal_exceeded));
        } else if (total > (sharedPrefManager.getTotalStepsFromYesterday() + getResources().getInteger(R.integer.subgoal))
                && !sharedPrefManager.getSubGoalExceededToday()) {
            exceedsSubGoal();
            Log.i(getString(R.string.mpa_tag), getString(R.string.subgoal_exceeded));
        }
        sharedPrefManager.storeTotalStepsForDayOfWeek(TimeMachine.getDayOfWeek(), total);
    }

    //used for Espresso testing
    public void resetDisplayToDefault() {
        sharedPrefManager.resetSharedPrefToDefault();
        goal.setText(String.valueOf(sharedPrefManager.getGoal()));
        numStepDone.setText(getString(R.string._0));
        checkWalkOrRun();
    }
}