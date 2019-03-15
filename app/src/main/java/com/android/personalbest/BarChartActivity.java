package com.android.personalbest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.personalbest.cloud.CloudstoreService;
import com.android.personalbest.cloud.CloudstoreServiceFactory;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
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
import java.util.List;

public class BarChartActivity extends AppCompatActivity {

    private Button homeButton;

    public CombinedChart chart;
    public ArrayList<BarEntry> entries;
    public ArrayList<Entry> line;
    private static List<UserDayData> monthlyActivity;
    private MonthlyDataList monthlyData;
    private CloudstoreService cloudstoreService;
    private SharedPrefManager sharedPrefManager;
    public static boolean mockCloud = false;
    private String userEmail;

    private TextView totalSteps;
    private TextView totalTime;
    private TextView averageMPH;
    private TextView totalDistance;
    private TextView goalText;

    private TextView stepsNumber;
    private TextView timeNumber;
    private TextView MPHNumber;
    private TextView distanceNumber;
    private TextView goalNumber;

    private BarEntry data;
    private int intentionalSteps;
    private int nonIntentionalSteps;
    private float mPH;
    private int time;
    private float miles;
    private int goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_bar_chart);

        sharedPrefManager = new SharedPrefManager(this.getApplicationContext());
        homeButton = findViewById(R.id.homebutton);
        homeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.i("MonthlyChart", "MActivity ended");
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
        chart = findViewById(R.id.barChart);
        chart.setDescription("");
        chart.setNoDataText("Loading Chart");

        //initializing stats textViews
        totalSteps = findViewById(R.id.totalsteps);
        totalTime = findViewById(R.id.totaltime);
        averageMPH = findViewById(R.id.averagemph);
        totalDistance = findViewById(R.id.totaldistance);
        goalText = findViewById(R.id.goaltext);
        stepsNumber = findViewById(R.id.stepsnumber);
        timeNumber = findViewById(R.id.timenumber);
        MPHNumber = findViewById(R.id.mphnumber);
        distanceNumber = findViewById(R.id.distancenumber);
        goalNumber = findViewById(R.id.goalnumber);

        //setting up bar chart
        userEmail = sharedPrefManager.getMonthlyEmail();
        cloudstoreService = CloudstoreServiceFactory.create(this.getApplicationContext(), mockCloud);
        monthlyData = new MonthlyDataList();
        Log.i("MAemail", "d "+monthlyData.getList().get(26).getTotalSteps());
        cloudstoreService.getWeeklyActivity(this, userEmail, monthlyData);
    }

    public void finishBarChartSetup() {
        chart = findViewById(R.id.barChart);
        chart.setDescription("");

        //removing default grid
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.setScaleEnabled(false);

        monthlyActivity = monthlyData.getList();
        Log.w("MAemail", "e "+monthlyActivity.get(27).getTotalSteps());

        //getting total number of steps for each day (intentional)
        entries = new ArrayList<>();
        line = new ArrayList<Entry>();

        for (int i = monthlyActivity.size() - 7; i < monthlyActivity.size(); i++) {
            UserDayData todayData = monthlyActivity.get(i);
            intentionalSteps = todayData.getIntentionalSteps();
            nonIntentionalSteps = todayData.getTotalSteps() - intentionalSteps;
            goal = todayData.getGoal();

            data = new BarEntry(i, new float[] { intentionalSteps, nonIntentionalSteps });
            entries.add(data);
            line.add(new Entry(i, goal));
        }

        LineDataSet lineDataSet = new LineDataSet(line, "");
        lineDataSet.setColor(Color.RED);
        lineDataSet.setDrawValues(false);
        LineData lineData = new LineData(lineDataSet);

        //creating dataset
        BarDataSet dataSet = new BarDataSet(entries, "Steps Taken");
        int[] colors = new int[]{Color.GREEN, Color.CYAN};
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.75f);
        CombinedData combinedData = new CombinedData();
        combinedData.setData(data);
        combinedData.setData(lineData);
        chart.setData(combinedData);
        chart.getXAxis().setAxisMaxValue(data.getXMax() + 0.25f);
        chart.getXAxis().setAxisMinValue(data.getXMin() - 0.25f);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        chart.getXAxis().setDrawLabels(false);
        chart.getAxisRight().setDrawLabels(false);

        //show the chart
        chart.setMaxVisibleValueCount(1);
        chart.setVisibility(View.VISIBLE);

        //onclicklistener for bar chart
        chart.setOnChartValueSelectedListener( new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                totalSteps.setVisibility(View.VISIBLE);
                totalTime.setVisibility(View.VISIBLE);
                averageMPH.setVisibility(View.VISIBLE);
                totalDistance.setVisibility(View.VISIBLE);
                goalText.setVisibility(View.VISIBLE);

                int day = (int) e.getX();

                UserDayData todayData = monthlyActivity.get(day);
                intentionalSteps = todayData.getIntentionalSteps();
                nonIntentionalSteps = todayData.getTotalSteps() - intentionalSteps;
                mPH = todayData.getIntentionalMph();
                time = todayData.getIntentionalTime();
                miles = todayData.getIntentionalDistance();
                goal = todayData.getGoal();

                String stepsStr = intentionalSteps + nonIntentionalSteps + getString(R.string.emptyString);
                String timeStr = time + getString(R.string.emptyString);
                String mphStr = mPH + getString(R.string.emptyString);
                String distanceStr = miles + getString(R.string.emptyString);
                String goalStr = goal + getString(R.string.emptyString);
                stepsNumber.setText(stepsStr);
                timeNumber.setText(timeStr);
                MPHNumber.setText(mphStr);
                distanceNumber.setText(distanceStr);
                goalNumber.setText(goalStr);
            }

            @Override
            public void onNothingSelected() {
                //do nothing
            }
        });
    }
}


