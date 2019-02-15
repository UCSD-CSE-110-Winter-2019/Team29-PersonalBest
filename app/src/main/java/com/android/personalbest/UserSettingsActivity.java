/**when I choose runner, go home, and go back, it still goes back to walker
 * end/start run/walk not switching properly/wrong string is showing???
 */

package com.android.personalbest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;

public class UserSettingsActivity extends AppCompatActivity {
    private Button changeGoal;
    private Button goBackButton;
    private RadioButton walkerOpt;
    private RadioButton runnerOpt;
<<<<<<< HEAD:app/src/main/java/com/android/personalbest/UserSettings.java
    private CheckBox proposedGoals;
=======
    private SharedPrefManager sharedPrefManager;
>>>>>>> 964bcbbaaa700220f959458b4e917f196d9fc535:app/src/main/java/com/android/personalbest/UserSettingsActivity.java

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        sharedPrefManager = new SharedPrefManager(this.getApplicationContext());

        changeGoal = findViewById(R.id.changeGoal);
        goBackButton = findViewById(R.id.homeButton);
        walkerOpt = findViewById(R.id.walkerOption);
        runnerOpt = findViewById(R.id.runnerOption);
        proposedGoals = findViewById(R.id.appProposedGoals);

        setWalkOrRunOption();
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMainActivity();
            }
        });
    }

    public void launchMainActivity() {
        Intent walk = new Intent(this, MainPageActivity.class);
        startActivity(walk);
    }

    @Override
    protected void onStart(){
        super.onStart();
        setWalkOrRunOption();
    }

    public void onRadioButtonClicked(View view) {
        if(walkerOpt.isChecked()){
            sharedPrefManager.setIsWalker(true);
        }
        else{
            sharedPrefManager.setIsWalker(false);
        }
    }

    private void setWalkOrRunOption() {
        boolean walker = sharedPrefManager.getIsWalker();
        if(walker == true){
            walkerOpt.setChecked(true);
            runnerOpt.setChecked(false);
        }
        else{
            runnerOpt.setChecked(true);
            walkerOpt.setChecked(false);
        }
<<<<<<< HEAD:app/src/main/java/com/android/personalbest/UserSettings.java

    }

    public void onRadioButtonClicked(View view) {
        if(walkerOpt.isChecked()){
                SharedPreferences sharedPrefWalkerRunner = getSharedPreferences("walkerOrRunner", MODE_PRIVATE);
                SharedPreferences.Editor editorWalkerRunner = sharedPrefWalkerRunner.edit();
                editorWalkerRunner.putBoolean("isWalker", true);
                editorWalkerRunner.apply();
            }
            else{
                SharedPreferences sharedPrefWalkerRunner = getSharedPreferences("walkerOrRunner", MODE_PRIVATE);
                SharedPreferences.Editor editorWalkerRunner = sharedPrefWalkerRunner.edit();
                editorWalkerRunner.putBoolean("isWalker", false);
                editorWalkerRunner.apply();
            }
=======
>>>>>>> 964bcbbaaa700220f959458b4e917f196d9fc535:app/src/main/java/com/android/personalbest/UserSettingsActivity.java
    }
}
