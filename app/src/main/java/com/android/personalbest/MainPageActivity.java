package com.android.personalbest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class MainPageActivity extends AppCompatActivity {
    private Button startButton;
    private Button seeBarChart;
    private Button userSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        startButton = findViewById(R.id.startBtn);
        seeBarChart = findViewById(R.id.seeBarChart);
        userSettings = findViewById(R.id.userSettings);

    }
}
