package com.android.personalbest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.android.personalbest.walkData.WalkDataAdapter;


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

        sharedPrefManager = new SharedPrefManager(this);

        endWalk = (Button)findViewById(R.id.endButton);
        endWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK, walkDataAdapter.returnElapsedTime());
                finish();
            }
        });
        setEndWalkText();

        walkDataAdapter.updateWalkStepInRealTime();

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
}
