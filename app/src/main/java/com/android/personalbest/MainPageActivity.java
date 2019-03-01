package com.android.personalbest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.android.personalbest.fitness.FitnessService;
import com.android.personalbest.fitness.FitnessServiceFactory;
import com.android.personalbest.fitness.GoogleFitAdapter;


public class MainPageActivity extends AppCompatActivity {
    private Button startButton;
    private Button seeBarChart;
    private Button userSettings;
    private Button seeFriends;

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
        seeFriends = findViewById(R.id.goToFriBtn);

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

        goal.setText(String.valueOf(sharedPrefManager.getGoal()));
        checkWalkOrRun();

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
    protected void onStart(){
        super.onStart();
        goal.setText(String.valueOf(sharedPrefManager.getGoal()));
        checkWalkOrRun();
        //check for encouragement message
        if (sharedPrefManager.getNumSteps() > sharedPrefManager.getGoal() && !sharedPrefManager.getGoalChangedToday()) {
            launchNewGoalActivity();
            sharedPrefManager.setGoalChangedToday(true);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        //check for encouragement message
        if (sharedPrefManager.getNumSteps() > sharedPrefManager.getGoal() && !sharedPrefManager.getGoalChangedToday()) {
            launchNewGoalActivity();
            sharedPrefManager.setGoalChangedToday(true);
        }
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

    public void addToStepCount(int steps) {
        int completedSteps = Integer.parseInt(numStepDone.getText().toString());
        int totalSteps = completedSteps + steps;
        numStepDone.setText(String.valueOf(totalSteps));
        sharedPrefManager.editor.putInt(getString(R.string.totalStep), totalSteps);
        sharedPrefManager.editor.apply();
    }

}