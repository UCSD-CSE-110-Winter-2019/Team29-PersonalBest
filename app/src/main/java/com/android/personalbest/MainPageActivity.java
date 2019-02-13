package com.android.personalbest;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.personalbest.fitness.GoogleFitAdapter;


public class MainPageActivity extends AppCompatActivity {
    private Button startButton;
    private Button seeBarChart;
    private Button userSettings;
    private GoogleFitAdapter googleFitAdapter;
    private int curStep;

    public TextView numStepDone;

    public SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        startButton = findViewById(R.id.startButton);
        seeBarChart = findViewById(R.id.seeBarChart);
        userSettings = findViewById(R.id.userSettings);

        //Total Step done
        numStepDone = findViewById(R.id.numStepDone);

        sharedPrefManager = new SharedPrefManager(this);


        googleFitAdapter = new GoogleFitAdapter(this);
        googleFitAdapter.setup();
        googleFitAdapter.updateStepInRealTime();

        SharedPreferences sharedPrefWalkRun = getSharedPreferences("walkerOrRunner", MODE_PRIVATE);
        boolean walker = sharedPrefWalkRun.getBoolean("isWalker", true);
        if(walker){
            startButton.setText("Start Walk");
        }
        else{
            startButton.setText("Start Run");
        }

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
        SharedPreferences sharedPrefWalkRun = getSharedPreferences("walkerOrRunner", MODE_PRIVATE);
        boolean walker = sharedPrefWalkRun.getBoolean("isWalker", true);
        if(walker == true){
            startButton.setText("Start Walk");
        }
        else{
            startButton.setText("Start Run");
        }

    }

    public void launchWalkActivity() {
        Intent walk = new Intent(this, WalkActivity.class);

        startActivity(walk);
    }


    public void launchUserSettings() {
        Intent settings = new Intent(this, UserSettings.class);
        startActivity(settings);
    }

    public void launchBarChartActivity() {
        Intent walk = new Intent(this, BarChartActivity.class);
        startActivity(walk);
    }

}

