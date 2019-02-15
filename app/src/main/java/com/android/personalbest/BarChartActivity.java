package com.android.personalbest;

import android.content.Intent;
import android.graphics.Color;
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
        /**int sundayIntentionalSteps = pastWeek.getIntentionalStepsTaken(Calendar.SUNDAY);
        int mondayIntentionalSteps = pastWeek.getIntentionalStepsTaken(Calendar.MONDAY);
        int tuesdayIntentionalSteps = pastWeek.getIntentionalStepsTaken(Calendar.TUESDAY);
        int wednesdayIntentionalSteps = pastWeek.getIntentionalStepsTaken(Calendar.WEDNESDAY);
        int thursdayIntentionalSteps = pastWeek.getIntentionalStepsTaken(Calendar.THURSDAY);
        int fridayIntentionalSteps = pastWeek.getIntentionalStepsTaken(Calendar.FRIDAY);
        int saturdayIntentionalSteps = pastWeek.getIntentionalStepsTaken(Calendar.SATURDAY);

        int sundayNonIntentional = pastWeek.getNonIntentionalStepsTaken(Calendar.SUNDAY);
        int mondayNonIntentional = pastWeek.getNonIntentionalStepsTaken(Calendar.MONDAY);
        int tuesdayNonIntentional = pastWeek.getNonIntentionalStepsTaken(Calendar.TUESDAY);
        int wednesdayNonIntentional = pastWeek.getNonIntentionalStepsTaken(Calendar.WEDNESDAY);
        int thursdayNonIntentional = pastWeek.getNonIntentionalStepsTaken(Calendar.THURSDAY);
        int fridayNonIntentional = pastWeek.getNonIntentionalStepsTaken(Calendar.FRIDAY);
        int saturdayNonIntentional = pastWeek.getNonIntentionalStepsTaken(Calendar.SATURDAY);**/



        int sundayIntentionalSteps = 1000;
        int mondayIntentionalSteps = 0;
        int tuesdayIntentionalSteps = 2450;
        int wednesdayIntentionalSteps = 3000;
        int thursdayIntentionalSteps = 500;
        int fridayIntentionalSteps = 10275;
        int saturdayIntentionalSteps = 8010;

        int sundayNonIntentional = 500;
        int mondayNonIntentional = 0;
        int tuesdayNonIntentional = 20;
        int wednesdayNonIntentional = 1000;
        int thursdayNonIntentional = 250;
        int fridayNonIntentional = 70;
        int saturdayNonIntentional = 2000;



        //setting up entries
        entries.add(new BarEntry(1f, new float[] { sundayIntentionalSteps, sundayNonIntentional }));
        entries.add(new BarEntry(2f, new float[] { mondayIntentionalSteps, mondayNonIntentional }));
        entries.add(new BarEntry(3f, new float[] { tuesdayIntentionalSteps, tuesdayNonIntentional }));
        entries.add(new BarEntry(4f, new float[] { wednesdayIntentionalSteps, wednesdayNonIntentional }));
        entries.add(new BarEntry(5f, new float[] { thursdayIntentionalSteps, thursdayNonIntentional }));
        entries.add(new BarEntry(6f, new float[] { fridayIntentionalSteps, fridayNonIntentional }));
        entries.add(new BarEntry(7f, new float[] { saturdayIntentionalSteps, saturdayNonIntentional }));

        //creating dataset
        BarDataSet dataSet = new BarDataSet(entries, "Steps Taken");
        int[] colors = new int[]{Color.CYAN, Color.GREEN};
        dataSet.setColors(colors);
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
        Intent settings = new Intent(this, UserSettingsActivity.class);
        startActivity(settings);
    }
}
