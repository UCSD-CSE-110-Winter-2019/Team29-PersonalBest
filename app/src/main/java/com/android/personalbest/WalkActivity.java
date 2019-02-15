package com.android.personalbest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

public class WalkActivity extends AppCompatActivity {

    private Button endWalk;
    private Chronometer chronometer;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk);
        sharedPrefManager = new SharedPrefManager(this.getApplicationContext());

        chronometer = findViewById(R.id.chronometer);
        //set the base of the chronometer to be the current system's clock
        chronometer.setBase(SystemClock.elapsedRealtime());
        //start timer
        chronometer.start();
        endWalk = (Button)findViewById(R.id.endButton);
        setEndWalkText();
        endWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK, returnElapsedTime());
                finish();
            }
        });

        //finish() destroys this activity and returns to main activity
        //To return data to main activity, use setResult()
        //so, when finish() is called, result is passed back on the onActivityResult
    }

    @Override
    protected void onStart(){
        super.onStart();
        SharedPreferences sharedPrefWalkRun = getSharedPreferences(getString(R.string.walker_or_runner), MODE_PRIVATE);
        boolean walker = sharedPrefWalkRun.getBoolean(getString(R.string.walker_option), true);
        Button endWalk = (Button)findViewById(R.id.endButton);
        if(walker == true){
            endWalk.setText(getString(R.string.end_walk));
        }
        else{
            endWalk.setText(getString(R.string.end_run));
        }
    }

    //method to be called when user clicks "end walk"
    //returns the time elapsed
    private Intent returnElapsedTime() {
        chronometer.stop();
        long elapsedTime = SystemClock.elapsedRealtime() - chronometer.getBase();
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.time_elapsed), elapsedTime);
        return intent;
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
}
