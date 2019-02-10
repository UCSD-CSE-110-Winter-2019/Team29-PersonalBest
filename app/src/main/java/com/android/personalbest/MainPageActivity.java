package com.android.personalbest;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;

import com.android.personalbest.fitness.FitnessService;


public class MainPageActivity extends AppCompatActivity {
    private FitnessService fitnessService;
    private static final String TAG = "MainPageActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);


//        String fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
//        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);

        Button startWalkActivity = (Button) findViewById(R.id.startButton);
        startWalkActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                launchWalkActivity();
            }
        });
    }

    public void launchWalkActivity() {
        Intent walk = new Intent(this, WalkActivity.class);
        startActivity(walk);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//       If authentication was required during google fit setup, this will be called after the user authenticates
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == fitnessService.getRequestCode()) {
                fitnessService.updateStepCount();
            }
        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
    }

}
