package com.android.personalbest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;

public class NewGoalActivity extends AppCompatActivity {

    private Button yes;
    private Button no;
    private Button acceptSuggestedGoal;
    private Button acceptCustomGoal;
    private TextView suggestedSteps;
    private EditText customSteps;
    private TextView suggestedStepsPrompt;
    private TextView customStepsPrompt;
    private SharedPrefManager sharedPrefManager;
    int today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goal);

        sharedPrefManager = new SharedPrefManager(this.getApplicationContext());
        yes = (Button) findViewById(R.id.yesButton);
        no = (Button) findViewById(R.id.noButton);
        acceptSuggestedGoal = (Button) findViewById(R.id.accept_suggested_goal);
        acceptCustomGoal = (Button) findViewById(R.id.accept_custom_goal);
        suggestedSteps = (TextView) findViewById(R.id.suggested_steps);
        customSteps = (EditText) findViewById(R.id.custom_steps);
        suggestedStepsPrompt = (TextView) findViewById(R.id.textView13);
        customStepsPrompt = (TextView) findViewById(R.id.textView16);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                showGoalPrompts();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                finish();
            }
        });
    }

    private void showGoalPrompts() {
        suggestedStepsPrompt.setVisibility(View.VISIBLE);
        customStepsPrompt.setVisibility(View.VISIBLE);
        suggestedSteps.setVisibility(View.VISIBLE);
        customSteps.setVisibility(View.VISIBLE);
        acceptSuggestedGoal.setVisibility(View.VISIBLE);
        acceptCustomGoal.setVisibility(View.VISIBLE);
        no.setVisibility(View.INVISIBLE);

        suggestedSteps.setText(String.valueOf(sharedPrefManager.getGoal() + 500));
        Calendar calendar = Calendar.getInstance();
        today = calendar.get(TimeMachine.getDay());

        activateSuggestedButton();
        activateCustomButton();
    }

    private void activateSuggestedButton() {
        acceptSuggestedGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                int newGoal = sharedPrefManager.getGoal() + 500;
                sharedPrefManager.setGoal(newGoal);
                sharedPrefManager.storeGoal(today, newGoal);
                finish();
            }
        });
    }

    private void activateCustomButton() {
        acceptCustomGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if (customSteps.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.goal_input_empty, Toast.LENGTH_SHORT).show();
                } else{
                    int newGoal = Integer.parseInt(customSteps.getText().toString().trim());
                    if (newGoal <= 0) {
                        Toast.makeText(getApplicationContext(), R.string.input_zero, Toast.LENGTH_SHORT).show();
                    } else {
                        sharedPrefManager.setGoal(newGoal);
                        sharedPrefManager.storeGoal(today, newGoal);
                        finish();
                    }
                }
            }
        });
    }
}
