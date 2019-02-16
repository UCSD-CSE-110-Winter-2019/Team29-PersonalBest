package com.android.personalbest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class NewGoalActivity extends AppCompatActivity {

    private Button yes;
    private Button no;
    private Button acceptSuggestedGoal;
    private Button acceptCustomGoal;
    private TextView suggestedSteps;
    private EditText customSteps;
    private TextView suggestedStepsPrompt;
    private TextView customStepsPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goal);

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
    }
}
