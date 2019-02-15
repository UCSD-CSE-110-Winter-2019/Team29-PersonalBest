package com.android.personalbest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class BarChartActivity extends AppCompatActivity {

    private Button userSettingsButton;
    private Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        userSettingsButton = findViewById(R.id.usersettings);
        homeButton = findViewById(R.id.homebutton);

        userSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                launchUserSettings();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        ArrayList<BarEntry> entries = new ArrayList<>();
        /**entries.add(new BarEntry(4f, 0));
        entries.add(new BarEntry(8f, 1));
        entries.add(new BarEntry(6f, 2));
        entries.add(new BarEntry(12f, 3));
        entries.add(new BarEntry(18f, 4));
        entries.add(new BarEntry(9f, 5));
        entries.add(new BarEntry(9f, 6));**/





    }

    @Override
    protected void onStart() {
        super.onStart();
        BarChart chart = (BarChart) findViewById(R.id.barChart);

    }

    public void launchUserSettings() {
        Intent settings = new Intent(this, UserSettings.class);
        startActivity(settings);
    }
}
