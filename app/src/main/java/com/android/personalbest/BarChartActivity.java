package com.android.personalbest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;


public class BarChartActivity extends AppCompatActivity {

    private Button userSettingsButton;
    private Button homeButton;

    public CombinedChart chart;
    public ArrayList<BarEntry> entries;
    public ArrayList<Entry> line;
    public SharedPrefManager pastWeek;

    private TextView thisWeek;
    private TextView stats;

    private TextView totalSteps;
    private TextView totalTime;
    private TextView averageMPH;
    private TextView totalDistance;

    private TextView stepsNumber;
    private TextView timeNumber;
    private TextView MPHNumber;
    private TextView distanceNumber;

    private BarEntry sundayData;
    private BarEntry mondayData;
    private BarEntry tuesdayData;
    private BarEntry wednesdayData;
    private BarEntry thursdayData;
    private BarEntry fridayData;
    private BarEntry saturdayData;

    private int sundayIntentionalSteps;
    private int mondayIntentionalSteps;
    private int tuesdayIntentionalSteps;
    private int wednesdayIntentionalSteps;
    private int thursdayIntentionalSteps;
    private int fridayIntentionalSteps;
    private int saturdayIntentionalSteps;

    private int sundayNonIntentional;
    private int mondayNonIntentional;
    private int tuesdayNonIntentional;
    private int wednesdayNonIntentional;
    private int thursdayNonIntentional;
    private int fridayNonIntentional;
    private int saturdayNonIntentional;

    private float sunMPH;
    private float monMPH;
    private float tuesMPH;
    private float wedMPH;
    private float thursMPH;
    private float friMPH;
    private float satMPH;

    private int sunTime;
    private int monTime;
    private int tuesTime;
    private int wedTime;
    private int thursTime;
    private int friTime;
    private int satTime;

    private float sunMiles;
    private float monMiles;
    private float tuesMiles;
    private float wedMiles;
    private float thursMiles;
    private float friMiles;
    private float satMiles;

    private int sunGoal;
    private int monGoal;
    private int tuesGoal;
    private int wedGoal;
    private int thursGoal;
    private int friGoal;
    private int satGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        userSettingsButton = findViewById(R.id.usersettings);
        homeButton = findViewById(R.id.homebutton);

        setUpBarChart();

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
    protected void onStart(){
        super.onStart();
        //reset bar chart every time this activity is opened
        setUpBarChart();
    }

    public void setUpBarChart(){

        //initializing buttons
        userSettingsButton = findViewById(R.id.usersettings);
        homeButton = findViewById(R.id.homebutton);

        //initializing stats textViews
        thisWeek = findViewById(R.id.thisweek);
        thisWeek.setVisibility(View.VISIBLE);
        stats = findViewById(R.id.stats);
        totalSteps = findViewById(R.id.totalsteps);
        totalTime = findViewById(R.id.totaltime);
        averageMPH = findViewById(R.id.averagemph);
        totalDistance = findViewById(R.id.totaldistance);
        stepsNumber = findViewById(R.id.stepsnumber);
        timeNumber = findViewById(R.id.timenumber);
        MPHNumber = findViewById(R.id.mphnumber);
        distanceNumber = findViewById(R.id.distancenumber);

        //setting up bar chart
        pastWeek = new SharedPrefManager(this.getApplicationContext());
        chart = findViewById(R.id.barChart);
        chart.setDescription("");

        //removing default grid
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.setScaleEnabled(false);

        //getting total number of steps for each day (intentional)
        entries = new ArrayList<>();
        sundayIntentionalSteps = pastWeek.getIntentionalStepsTaken(Calendar.SUNDAY);
        mondayIntentionalSteps = pastWeek.getIntentionalStepsTaken(Calendar.MONDAY);
        tuesdayIntentionalSteps = pastWeek.getIntentionalStepsTaken(Calendar.TUESDAY);
        wednesdayIntentionalSteps = pastWeek.getIntentionalStepsTaken(Calendar.WEDNESDAY);
        thursdayIntentionalSteps = pastWeek.getIntentionalStepsTaken(Calendar.THURSDAY);
        fridayIntentionalSteps = pastWeek.getIntentionalStepsTaken(Calendar.FRIDAY);
        saturdayIntentionalSteps = pastWeek.getIntentionalStepsTaken(Calendar.SATURDAY);

        //getting total number of steps for each day (nonintentional)
        sundayNonIntentional = pastWeek.getNonIntentionalStepsTaken(Calendar.SUNDAY);
        mondayNonIntentional = pastWeek.getNonIntentionalStepsTaken(Calendar.MONDAY);
        tuesdayNonIntentional = pastWeek.getNonIntentionalStepsTaken(Calendar.TUESDAY);
        wednesdayNonIntentional = pastWeek.getNonIntentionalStepsTaken(Calendar.WEDNESDAY);
        thursdayNonIntentional = pastWeek.getNonIntentionalStepsTaken(Calendar.THURSDAY);
        fridayNonIntentional = pastWeek.getNonIntentionalStepsTaken(Calendar.FRIDAY);
        saturdayNonIntentional = pastWeek.getNonIntentionalStepsTaken(Calendar.SATURDAY);

        //getting total MPH for each day
        sunMPH = pastWeek.getIntentionalMilesPerHour(Calendar.SUNDAY);
        monMPH = pastWeek.getIntentionalMilesPerHour(Calendar.MONDAY);
        tuesMPH = pastWeek.getIntentionalMilesPerHour(Calendar.TUESDAY);
        wedMPH = pastWeek.getIntentionalMilesPerHour(Calendar.WEDNESDAY);
        thursMPH = pastWeek.getIntentionalMilesPerHour(Calendar.THURSDAY);
        friMPH = pastWeek.getIntentionalMilesPerHour(Calendar.FRIDAY);
        satMPH = pastWeek.getIntentionalMilesPerHour(Calendar.SATURDAY);

        //getting time elapsed for each day
        sunTime = pastWeek.getIntentionalTimeElapsed(Calendar.SUNDAY);
        monTime = pastWeek.getIntentionalTimeElapsed(Calendar.MONDAY);
        tuesTime = pastWeek.getIntentionalTimeElapsed(Calendar.TUESDAY);
        wedTime = pastWeek.getIntentionalTimeElapsed(Calendar.WEDNESDAY);
        thursTime = pastWeek.getIntentionalTimeElapsed(Calendar.THURSDAY);
        friTime = pastWeek.getIntentionalTimeElapsed(Calendar.FRIDAY);
        satTime = pastWeek.getIntentionalTimeElapsed(Calendar.SATURDAY);

        //getting total miles for each day
        sunMiles = pastWeek.getIntentionalDistanceInMiles(Calendar.SUNDAY);
        monMiles = pastWeek.getIntentionalDistanceInMiles(Calendar.MONDAY);
        tuesMiles = pastWeek.getIntentionalDistanceInMiles(Calendar.TUESDAY);
        wedMiles = pastWeek.getIntentionalDistanceInMiles(Calendar.WEDNESDAY);
        thursMiles = pastWeek.getIntentionalDistanceInMiles(Calendar.THURSDAY);
        friMiles = pastWeek.getIntentionalDistanceInMiles(Calendar.FRIDAY);
        satMiles = pastWeek.getIntentionalDistanceInMiles(Calendar.SATURDAY);

        //getting total number of goals for each day
        sunGoal = pastWeek.getGoalForCertainDay(Calendar.SUNDAY);
        monGoal = pastWeek.getGoalForCertainDay(Calendar.MONDAY);
        tuesGoal = pastWeek.getGoalForCertainDay(Calendar.TUESDAY);
        wedGoal = pastWeek.getGoalForCertainDay(Calendar.WEDNESDAY);
        thursGoal = pastWeek.getGoalForCertainDay(Calendar.THURSDAY);
        friGoal = pastWeek.getGoalForCertainDay(Calendar.FRIDAY);
        satGoal = pastWeek.getGoalForCertainDay(Calendar.SATURDAY);

        //putting together steps for bar chart
        sundayData = new BarEntry(1f, new float[] { sundayIntentionalSteps, sundayNonIntentional });
        mondayData = new BarEntry(2f, new float[] { mondayIntentionalSteps, mondayNonIntentional });
        tuesdayData = new BarEntry(3f, new float[] { tuesdayIntentionalSteps, tuesdayNonIntentional });
        wednesdayData = new BarEntry(4f, new float[] { wednesdayIntentionalSteps, wednesdayNonIntentional });
        thursdayData = new BarEntry(5f, new float[] { thursdayIntentionalSteps, thursdayNonIntentional });
        fridayData = new BarEntry(6f, new float[] { fridayIntentionalSteps, fridayNonIntentional });
        saturdayData = new BarEntry(7f, new float[] { saturdayIntentionalSteps, saturdayNonIntentional });

        //setting up entries
        entries.add(sundayData);
        entries.add(mondayData);
        entries.add(tuesdayData);
        entries.add(wednesdayData);
        entries.add(thursdayData);
        entries.add(fridayData);
        entries.add(saturdayData);

        line = new ArrayList<Entry>();
        line.add(new Entry(1f, sunGoal));
        line.add(new Entry(2f, monGoal));
        line.add(new Entry(3f, tuesGoal));
        line.add(new Entry(4f, wedGoal));
        line.add(new Entry(5f, thursGoal));
        line.add(new Entry(6f, friGoal));
        line.add(new Entry(7f, satGoal));
        LineDataSet lineDataSet = new LineDataSet(line, "");
        lineDataSet.setColor(Color.RED);
        LineData lineData = new LineData(lineDataSet);

        //creating dataset
        BarDataSet dataSet = new BarDataSet(entries, "Steps Taken");
        int[] colors = new int[]{Color.CYAN, Color.GREEN};
        dataSet.setColors(colors);
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.75f);
        CombinedData combinedData = new CombinedData();
        combinedData.setData(data);
        combinedData.setData(lineData);
        chart.setData(combinedData);
        chart.getXAxis().setAxisMaxValue(data.getXMax() + 0.25f);
        chart.getXAxis().setAxisMinValue(data.getXMin() - 0.25f);


        //onclicklistener for bar chart
        chart.setOnChartValueSelectedListener( new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                thisWeek.setVisibility(View.INVISIBLE);
                totalSteps.setVisibility(View.VISIBLE);
                totalTime.setVisibility(View.VISIBLE);
                averageMPH.setVisibility(View.VISIBLE);
                totalDistance.setVisibility(View.VISIBLE);

                if(e.equals(sundayData)){
                    stats.setText(getString(R.string.sundaystats));
                    stepsNumber.setText((sundayIntentionalSteps + sundayNonIntentional) + getString(R.string.emptyString));
                    timeNumber.setText(sunTime + getString(R.string.emptyString));
                    MPHNumber.setText(sunMPH + getString(R.string.emptyString));
                    distanceNumber.setText(sunMiles + getString(R.string.emptyString));
                }
                else if(e.equals(mondayData)){
                    stats.setText(getString(R.string.mondaystats));
                    stepsNumber.setText((mondayIntentionalSteps + mondayNonIntentional) + getString(R.string.emptyString));
                    timeNumber.setText(monTime + getString(R.string.emptyString));
                    MPHNumber.setText(monMPH + getString(R.string.emptyString));
                    distanceNumber.setText(monMiles + getString(R.string.emptyString));
                }
                else if(e.equals(tuesdayData)){
                    stats.setText(getString(R.string.tuesdaystats));
                    stepsNumber.setText((tuesdayIntentionalSteps + tuesdayNonIntentional) + getString(R.string.emptyString));
                    timeNumber.setText(tuesTime + getString(R.string.emptyString));
                    MPHNumber.setText(tuesMPH + getString(R.string.emptyString));
                    distanceNumber.setText(tuesMiles + getString(R.string.emptyString));

                }
                else if(e.equals(wednesdayData)){
                    stats.setText(getString(R.string.wednesdaystats));
                    stepsNumber.setText((wednesdayIntentionalSteps + wednesdayNonIntentional) + getString(R.string.emptyString));
                    timeNumber.setText(wedTime + getString(R.string.emptyString));
                    MPHNumber.setText(wedMPH + getString(R.string.emptyString));
                    distanceNumber.setText(wedMiles + getString(R.string.emptyString));
                }
                else if(e.equals(thursdayData)){
                    stats.setText(getString(R.string.thursdaystats));
                    stepsNumber.setText((thursdayIntentionalSteps + thursdayNonIntentional) + getString(R.string.emptyString));
                    timeNumber.setText(thursTime + getString(R.string.emptyString));
                    MPHNumber.setText(thursMPH + getString(R.string.emptyString));
                    distanceNumber.setText(thursMiles + getString(R.string.emptyString));

                }
                else if(e.equals(fridayData)){
                    stats.setText(getString(R.string.fridaystats));
                    stepsNumber.setText((fridayIntentionalSteps + fridayNonIntentional) + "");
                    timeNumber.setText(friTime + getString(R.string.emptyString));
                    MPHNumber.setText(friMPH + getString(R.string.emptyString));
                    distanceNumber.setText(friMiles + getString(R.string.emptyString));
                }
                else{ //if e.equals(saturdayData))
                    stats.setText(getString(R.string.saturdaystats));
                    stepsNumber.setText((saturdayIntentionalSteps + saturdayNonIntentional) + "");
                    timeNumber.setText(satTime + getString(R.string.emptyString));
                    MPHNumber.setText(satMPH + getString(R.string.emptyString));
                    distanceNumber.setText(satMiles + getString(R.string.emptyString));
                }

            }

            @Override
            public void onNothingSelected() {
                //do nothing
            }
        });

    }
    public void launchUserSettings() {
        Intent settings = new Intent(this, UserSettingsActivity.class);
        startActivity(settings);
    }

}