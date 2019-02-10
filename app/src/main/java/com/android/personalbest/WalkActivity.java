package com.android.personalbest;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

public class WalkActivity extends AppCompatActivity {

    private Chronometer chronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk);

        chronometer = findViewById(R.id.chronometer);
        //set the base of the chronometer to be the current system's clock
        chronometer.setBase(SystemClock.elapsedRealtime());
        //start timer
        chronometer.start();
        Button endWalk = (Button) findViewById(R.id.endButton);
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

    //method to be called when user clicks "end walk"
    //returns the time elapsed
    public Intent returnElapsedTime() {
        chronometer.stop();
        long elapsedTime = SystemClock.elapsedRealtime() - chronometer.getBase();
        Intent intent = new Intent();
        intent.putExtra("time elapsed", elapsedTime);
        return intent;
    }

}
