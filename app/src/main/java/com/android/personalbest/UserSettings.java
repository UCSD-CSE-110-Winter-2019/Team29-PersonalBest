/**when I choose runner, go home, and go back, it still goes back to walker
 * end/start run/walk not switching properly/wrong string is showing???
 */

package com.android.personalbest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class UserSettings extends AppCompatActivity {
    private Button changeGoal;
    private Button goBackButton;
    private RadioButton walkerOpt;
    private RadioButton runnerOpt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        changeGoal = findViewById(R.id.changeGoal);
        goBackButton = findViewById(R.id.homeButton);
        walkerOpt = findViewById(R.id.walkerOption);
        runnerOpt = findViewById(R.id.runnerOption);

        SharedPreferences sharedPrefWalkRun = getSharedPreferences(getString(R.string.walker_or_runner), MODE_PRIVATE);
        boolean walker = sharedPrefWalkRun.getBoolean(getString(R.string.runner_option), true);

        if(walker == true){
            walkerOpt.setChecked(true);
            runnerOpt.setChecked(false);
        }
        else{
            runnerOpt.setChecked(true);
            walkerOpt.setChecked(false);
        }

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        SharedPreferences sharedPrefWalkRun = getSharedPreferences(getString(R.string.walker_or_runner), MODE_PRIVATE);
        boolean walker = sharedPrefWalkRun.getBoolean(getString(R.string.walker_option), true);
        if(walker == true){
            walkerOpt.setChecked(true);
            runnerOpt.setChecked(false);
        }
        else{
            runnerOpt.setChecked(true);
            walkerOpt.setChecked(false);
        }

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
            if(walkerOpt.isChecked()){
                SharedPreferences sharedPrefWalkerRunner = getSharedPreferences(getString(R.string.walker_or_runner), MODE_PRIVATE);
                SharedPreferences.Editor editorWalkerRunner = sharedPrefWalkerRunner.edit();
                editorWalkerRunner.putBoolean(getString(R.string.walker_option), true);
                editorWalkerRunner.apply();
            }
            else{
                SharedPreferences sharedPrefWalkerRunner = getSharedPreferences(getString(R.string.walker_or_runner), MODE_PRIVATE);
                SharedPreferences.Editor editorWalkerRunner = sharedPrefWalkerRunner.edit();
                editorWalkerRunner.putBoolean(getString(R.string.walker_option), false);
                editorWalkerRunner.apply();
            }
    }
}
