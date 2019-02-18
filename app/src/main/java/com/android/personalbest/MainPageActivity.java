package com.android.personalbest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.personalbest.fitness.FitnessService;
import com.android.personalbest.fitness.GoogleFitAdapter;

import java.util.Calendar;


public class MainPageActivity extends AppCompatActivity {
    private Button startButton;
    private Button seeBarChart;
    private Button userSettings;
    private GoogleFitAdapter googleFitAdapter;
    private int curStep;
    public static boolean mock = false;
    private FitnessService fitnessService;

    public TextView numStepDone;
    private TextView goal;

    private SharedPrefManager sharedPrefManager;

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
        //sharedPrefManager = new SharedPrefManager(this);
        goal = findViewById(R.id.goal);

        googleFitAdapter = new GoogleFitAdapter(this);

        //update UI: steps, goal, walk/run status
        googleFitAdapter.setup();
        googleFitAdapter.updateStepInRealTime();
        goal.setText(String.valueOf(sharedPrefManager.getGoal()));
        checkWalkOrRun();


        googleFitAdapter = new GoogleFitAdapter(this);
        googleFitAdapter.setup();
        googleFitAdapter.updateStepInRealTime();

        //set button listeners
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                curStep = googleFitAdapter.getCurrentStep();
                sharedPrefManager.editor.putInt(getString(R.string.step_count_before_switch_to_start_walk_activity),curStep);
                sharedPrefManager.editor.apply();
                launchWalkActivity();
            }
        });

        seeBarChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    private void checkWalkOrRun() {
        boolean walker = sharedPrefManager.getIsWalker();
        if(walker){
            startButton.setText(getString(R.string.start_walk));
        }
        else{
            startButton.setText(getString(R.string.start_run));
        }
    }
}

