package com.android.personalbest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import java.util.*;


public class UserSettingsActivity extends AppCompatActivity {
    private Button changeGoal;
    private Button goBackButton;
    private RadioButton walkerOpt;
    private RadioButton runnerOpt;
    private CheckBox proposedGoals;
    private SharedPrefManager sharedPrefManager;

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
        changeGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(UserSettingsActivity.this);
                final EditText edittext = new EditText(UserSettingsActivity.this);
                //cannot enter any character except integers
                edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
                dialog.setTitle("Enter Your New Goal");
                dialog.setView(edittext);
                dialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        SharedPrefManager pastWeek = new SharedPrefManager(UserSettingsActivity.this.getApplicationContext());
                        int newGoal = Integer.parseInt(edittext.getText().toString());
                        if(newGoal <= 0){
                            Toast.makeText(getApplicationContext(), "Please enter a number greater than zero", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Calendar calendar = Calendar.getInstance();
                            int today = calendar.get(Calendar.DAY_OF_WEEK);
                            pastWeek.storeGoal(today, newGoal);
                            pastWeek.setGoal(newGoal);
                            Toast.makeText(getApplicationContext(), "Goal updated!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //do nothing
                    }
                });

                dialog.show();

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

    private void setWalkOrRunOption(){
        boolean walker = sharedPrefManager.getIsWalker();
        if(walker == true){
            walkerOpt.setChecked(true);
            runnerOpt.setChecked(false);
        }
        else{
            runnerOpt.setChecked(true);
            walkerOpt.setChecked(false);
        }

    }

   /** public void onRadioButtonClicked(View view) {
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
    }**/
}
