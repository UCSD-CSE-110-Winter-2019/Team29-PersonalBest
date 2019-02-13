package com.android.personalbest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainPageActivity extends AppCompatActivity {
    private Button startButton;
    private Button seeBarChart;
    private Button userSettings;
    private Button startWalkActivity;
    private Button endWalkActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        startButton = findViewById(R.id.startButton);
        seeBarChart = findViewById(R.id.seeBarChart);
        userSettings = findViewById(R.id.userSettings);
        startWalkActivity = findViewById(R.id.startButton);

        SharedPreferences sharedPrefWalkRun = getSharedPreferences("walkerOrRunner", MODE_PRIVATE);
        boolean walker = sharedPrefWalkRun.getBoolean("isWalker", true);
        if(walker){
            startWalkActivity.setText("Start Walk");
        }
        else{
            startWalkActivity.setText("Start Run");
        }

        startWalkActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
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
        startWalkActivity = (Button)findViewById(R.id.startButton);
        SharedPreferences sharedPrefWalkRun = getSharedPreferences("walkerOrRunner", MODE_PRIVATE);
        boolean walker = sharedPrefWalkRun.getBoolean("isWalker", true);
        if(walker == true){
            startWalkActivity.setText("Start Walk");
        }
        else{
            startWalkActivity.setText("Start Run");
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
