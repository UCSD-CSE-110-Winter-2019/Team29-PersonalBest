package com.android.personalbest;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.personalbest.fitness.FitnessService;
import com.android.personalbest.fitness.FitnessServiceFactory;
import com.android.personalbest.fitness.GoogleFitAdapter;

import java.util.Calendar;


public class MainPageActivity extends AppCompatActivity {
    private Button startButton;
    private Button seeBarChart;
    private Button userSettings;

    private FitnessService fitnessService;
    private int curStep;

    public TextView numStepDone;
    public static boolean mock = false;
    private TextView goal;
    private SharedPrefManager sharedPrefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        sharedPrefManager = new SharedPrefManager(this.getApplicationContext());

        startButton = findViewById(R.id.startButton);
        seeBarChart = findViewById(R.id.seeBarChart);
        userSettings = findViewById(R.id.userSettings);


        //Total Step done
        numStepDone = findViewById(R.id.numStepDone);
        sharedPrefManager = new SharedPrefManager(this);
        goal = findViewById(R.id.goal);

        /*
        googleFitAdapter = new GoogleFitAdapter(this);
        googleFitAdapter.setup();
        googleFitAdapter.updateStepInRealTime();
        */

        fitnessService = FitnessServiceFactory.create(this, mock);


        goal.setText(String.valueOf(sharedPrefManager.getGoal()));

        checkWalkOrRun();

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
                sharedPrefManager.storeTotalSteps(Calendar.DAY_OF_WEEK, Integer.parseInt(numStepDone.getText().toString()));
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
    }

}

