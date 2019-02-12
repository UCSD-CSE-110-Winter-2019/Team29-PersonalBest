package com.android.personalbest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.personalbest.fitness.GoogleFitAdapter;


import java.util.concurrent.TimeUnit;

public class MainPageActivity extends AppCompatActivity {
    private Button startButton;
    private Button seeBarChart;
    private Button userSettings;
    private GoogleFitAdapter googleFitAdapter;

    private static final String TAG = "MainPageActivity";

    public TextView numStepDone;

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        startButton = findViewById(R.id.startButton);
        seeBarChart = findViewById(R.id.seeBarChart);
        userSettings = findViewById(R.id.userSettings);
        numStepDone = findViewById(R.id.numStepDone);

        sharedPreferences = getSharedPreferences(getString(R.string.user_prefs),MODE_PRIVATE);

        googleFitAdapter = new GoogleFitAdapter(this);
        googleFitAdapter.setup();
        googleFitAdapter.updateStepInRealTime();



        Button startWalkActivity = (Button) findViewById(R.id.startButton);
        startWalkActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                launchWalkActivity();
            }
        });


    }

    public void launchWalkActivity() {
        Intent walk = new Intent(this, WalkActivity.class);
        startActivity(walk);
    }


}

