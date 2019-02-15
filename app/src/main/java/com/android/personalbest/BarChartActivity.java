package com.android.personalbest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;


import java.util.ArrayList;

public class BarChartActivity extends AppCompatActivity {

    private Button userSettingsButton;
    private Button homeButton;
    private BarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        userSettingsButton = findViewById(R.id.usersettings);
        homeButton = findViewById(R.id.homebutton);

        //setting up bar chart
        SharedPrefManager pastWeek = new SharedPrefManager(this.getApplicationContext());
        chart = (BarChart) findViewById(R.id.barChart);
        chart.setDescription("");
        //removing default grid
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.setScaleEnabled(false);

        //getting total number of steps for each day
        ArrayList<BarEntry> entries = new ArrayList<>();
        /**int sundaySteps = pastWeek.getTotalStepsTaken(Calendar.SUNDAY);
        int mondaySteps = pastWeek.getTotalStepsTaken(Calendar.MONDAY);
        int tuesdaySteps = pastWeek.getTotalStepsTaken(Calendar.TUESDAY);
        int wednesdaySteps = pastWeek.getTotalStepsTaken(Calendar.WEDNESDAY);
        int thursdaySteps = pastWeek.getTotalStepsTaken(Calendar.THURSDAY);
        int fridaySteps = pastWeek.getTotalStepsTaken(Calendar.FRIDAY);
        int saturdaySteps = pastWeek.getTotalStepsTaken(Calendar.SATURDAY);**/

        int sundaySteps = 1000;
        int mondaySteps = 0;
        int tuesdaySteps = 2450;
        int wednesdaySteps = 3000;
        int thursdaySteps = 500;
        int fridaySteps = 10275;
        int saturdaySteps = 8010;

        //setting up entries
        entries.add(new BarEntry(1f, sundaySteps));
        entries.add(new BarEntry(2f, mondaySteps));
        entries.add(new BarEntry(3f, tuesdaySteps));
        entries.add(new BarEntry(4f, wednesdaySteps));
        entries.add(new BarEntry(5f, thursdaySteps));
        entries.add(new BarEntry(6f, fridaySteps));
        entries.add(new BarEntry(7f, saturdaySteps));

        //creating labels for days of the week
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Sunday");
        labels.add("Monday");
        labels.add("Tuesday");
        labels.add("Wednesday");
        labels.add("Thursday");
        labels.add("Friday");
        labels.add("Saturday");

        //creating dataset
        BarDataSet dataSet = new BarDataSet(entries, "Steps Taken");
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.75f);
        chart.setData(data);
        chart.getXAxis().setAxisMaxValue(data.getXMax() + 0.25f);
        chart.getXAxis().setAxisMinValue(data.getXMin() - 0.25f);

        //onclicklisteners for buttons
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

    }

    @Override
    protected void onStart() {
        super.onStart();
        chart = (BarChart) findViewById(R.id.barChart);

    }

    public void launchUserSettings() {
        Intent settings = new Intent(this, UserSettings.class);
        startActivity(settings);
    }
}
