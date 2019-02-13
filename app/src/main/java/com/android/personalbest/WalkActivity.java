package com.android.personalbest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;


public class WalkActivity extends AppCompatActivity {

    public Chronometer chronometer;
    private SharedPrefManager sharedPrefManager;
    private WalkDataAdapter walkDataAdapter;


    public TextView intenionalStepTextView;
    public TextView milesTextView;
    public TextView MPHTextView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk);

        initiateWalkDataTextView();
        walkDataAdapter = new WalkDataAdapter(this);


        Button endWalk = (Button)findViewById(R.id.endButton);
        SharedPreferences sharedPrefWalkRun = getSharedPreferences("walkerOrRunner", MODE_PRIVATE);
        boolean walker = sharedPrefWalkRun.getBoolean("isWalker", true);
        sharedPrefManager = new SharedPrefManager(this);



        if(walker == true){
            endWalk.setText("End Walk");
        }
        else{
            endWalk.setText("End Run");
        }
        endWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK, walkDataAdapter.returnElapsedTime());
                finish();
            }
        });

        walkDataAdapter.updateWalkStepInRealTime();




        //finish() destroys this activity and returns to main activity
        //To return data to main activity, use setResult()
        //so, when finish() is called, result is passed back on the onActivityResult
    }


    @Override
    protected void onStart(){
        super.onStart();
        SharedPreferences sharedPrefWalkRun = getSharedPreferences("walkerOrRunner", MODE_PRIVATE);
        boolean walker = sharedPrefWalkRun.getBoolean("isWalker", true);
        Button endWalk = (Button)findViewById(R.id.endButton);
        if(walker == true){
            endWalk.setText("End Walk");
        }
        else{
            endWalk.setText("End Run");
        }

    }



    public void initiateWalkDataTextView(){

        intenionalStepTextView = findViewById(R.id.intenionalStep);
        milesTextView = findViewById(R.id.miles);
        MPHTextView = findViewById(R.id.MPH);
        chronometer = findViewById(R.id.chronometer);

    }

}
