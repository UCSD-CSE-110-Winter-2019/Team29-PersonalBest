package com.android.personalbest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.personalbest.fitness.FitnessService;
import com.android.personalbest.fitness.FitnessServiceFactory;
import com.android.personalbest.fitness.GoogleFitAdapter;

import java.util.Calendar;


public class MainPageActivity extends AppCompatActivity {
    private Button startButton;
    private Button seeBarChart;
    private Button userSettings;

    private FitnessService fitnessService;
    private GoogleFitAdapter googleFitAdapter;
    private int curStep;

    public TextView numStepDone;
    private TextView goal;
    private SharedPrefManager sharedPrefManager;

    public static boolean mock = false; //change to true for testing purposes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //initialize components
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        sharedPrefManager = new SharedPrefManager(this.getApplicationContext());
        goal = findViewById(R.id.goal);
        startButton = findViewById(R.id.startButton);
        seeBarChart = findViewById(R.id.seeBarChart);
        userSettings = findViewById(R.id.userSettings);
        numStepDone = findViewById(R.id.numStepDone);

        sharedPrefManager = new SharedPrefManager(this);

        fitnessService = FitnessServiceFactory.create(this, mock);

        goal = findViewById(R.id.goal);

        //update UI: steps, goal, walk/run status
        SharedPreferences sharedPrefWalkRun = getSharedPreferences(getString(R.string.walker_or_runner), MODE_PRIVATE);
        boolean walker = sharedPrefWalkRun.getBoolean(getString(R.string.walker_option), true);
        if(walker){
            startButton.setText(getString(R.string.start_walk));
        }
        else{
            startButton.setText(getString(R.string.start_run));
        }

        //set button listeners
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                curStep = fitnessService.getCurrentStep();
                sharedPrefManager.editor.putInt(getString(R.string.step_count_before_switch_to_start_walk_activity),curStep);
                sharedPrefManager.editor.apply();
                launchWalkActivity();
            }
        });

        seeBarChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPrefManager.storeTotalSteps(TimeMachine.getDay(), sharedPrefManager.getNumSteps());
                launchBarChartActivity();
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
        int storedDay = sharedPrefManager.getDayInStorage();
        sharedPrefManager.storeGoal(storedDay, sharedPrefManager.getGoal());
        sharedPrefManager.storeTotalSteps(storedDay, sharedPrefManager.getNumSteps());
        sharedPrefManager.storeTotalStepsFromYesterday(sharedPrefManager.getNumSteps());
        sharedPrefManager.setGoalExceededToday(false);
        sharedPrefManager.setSubGoalExceededToday(false);

        //check if it's a new week and we need to reset the bar chart
        if (storedDay == Calendar.SATURDAY) {
            sharedPrefManager.resetSharedPrefForWeek();
            sharedPrefManager.setIgnoreGoal(false);
        }

        sharedPrefManager.setDayInStorage(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
    }

    public void exceedsGoal() {
        sharedPrefManager.setGoalExceededToday(true);
        if (!sharedPrefManager.getIgnoreGoal()) {
            sharedPrefManager.setGoalMessageDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            sharedPrefManager.setGoalReached(true);
        }
    }

    public void exceedsSubGoal() {
        sharedPrefManager.setSubGoalExceededToday(true);
        sharedPrefManager.setSubGoalMessageDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        sharedPrefManager.setSubGoalReached(true);
    }

    private void checkGoalMet() {
        if (sharedPrefManager.getGoalReached() && !sharedPrefManager.getIgnoreGoal()) {
            int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            int goalDay = sharedPrefManager.getGoalMessageDay();

            if (today == goalDay || today == goalDay + 1) {
                sharedPrefManager.setGoalReached(false);
                launchNewGoalActivity();
            }
        }
    }

    private void checkSubGoalMet() {
        if (sharedPrefManager.getSubGoalReached()) {
            int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            long time = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int subGoalDay = sharedPrefManager.getSubGoalMessageDay();

            if ((today == subGoalDay + 1) || (today == subGoalDay && time > R.integer.eight_pm)) {
                sharedPrefManager.setSubGoalReached(false);
                showSubGoalMsg();
            }
        }
    }

    private void showSubGoalMsg() {
        int difference = sharedPrefManager.getNumSteps() - sharedPrefManager.getTotalStepsFromYesterday();
        int specificSubGoal = (difference / R.integer.subgoal) * 500;
        Toast.makeText(getApplicationContext(), R.string.subgoal_msg_p1 + specificSubGoal + R.string.subgoal_msg_p2, Toast.LENGTH_LONG).show();
    }

    public void addToStepCount(int steps) {
        int completedSteps = Integer.parseInt(numStepDone.getText().toString());
        int totalSteps = completedSteps + steps;
        numStepDone.setText(String.valueOf(totalSteps));
        sharedPrefManager.editor.putInt(getString(R.string.totalStep), totalSteps);
        sharedPrefManager.editor.apply();
    }
}