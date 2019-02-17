package com.android.personalbest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.android.personalbest.walkData.WalkDataAdapter;

import java.util.Calendar;


public class WalkActivity extends AppCompatActivity {


    public Chronometer chronometer;
    private SharedPrefManager sharedPrefManager;
    private WalkDataAdapter walkDataAdapter;
    public TextView intentionalStepTextView;
    public TextView milesTextView;
    public TextView MPHTextView;
    private Button endWalk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk);
        sharedPrefManager = new SharedPrefManager(this.getApplicationContext());

        initiateWalkDataTextView();

        walkDataAdapter = new WalkDataAdapter(this);
        endWalk = (Button)findViewById(R.id.endButton);
        endWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK, walkDataAdapter.returnElapsedTime());
                storeWalkStats();
                finish();
            }
        });

        setEndWalkText();
        walkDataAdapter.updateWalkStepInRealTime();
    }

    @Override
    protected void onStart(){
        super.onStart();
        setEndWalkText();
    }

    public void initiateWalkDataTextView() {
        intentionalStepTextView = findViewById(R.id.intentionalStep);
        milesTextView = findViewById(R.id.miles);
        MPHTextView = findViewById(R.id.MPH);
        chronometer = findViewById(R.id.chronometer);
    }

    private void setEndWalkText() {
        boolean walker = sharedPrefManager.getIsWalker();
        if(walker == true){
            endWalk.setText(getString(R.string.end_walk));
        }
        else{
            endWalk.setText(getString(R.string.end_run));
        }
    }

    private void storeWalkStats() {
        intentionalStepTextView = findViewById(R.id.intentionalStep);
        milesTextView = findViewById(R.id.miles);
        MPHTextView = findViewById(R.id.MPH);
        chronometer = findViewById(R.id.chronometer);

        int intentionalStepsTaken = Integer.parseInt(intentionalStepTextView.getText().toString());
        float intentionalDistanceInMiles = Float.parseFloat(milesTextView.getText().toString());
        float intentionalMilesPerHour = Float.parseFloat(MPHTextView.getText().toString());

        int elapsedTimeInMilliseconds = walkDataAdapter.getCurrentElapsedTime();
        int intentionalTimeElapsed = elapsedTimeInMilliseconds/60000;

        sharedPrefManager.storeIntentionalWalkStats(Calendar.DAY_OF_WEEK, intentionalStepsTaken, intentionalDistanceInMiles,
                intentionalMilesPerHour, intentionalTimeElapsed);
    }
}
