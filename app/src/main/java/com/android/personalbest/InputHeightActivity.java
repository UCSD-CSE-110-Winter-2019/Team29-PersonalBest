package com.android.personalbest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InputHeightActivity extends AppCompatActivity {

    private Button doneButton;
    private EditText userHeight;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_height);

        sharedPrefManager = new SharedPrefManager(this.getApplicationContext());
        userHeight = findViewById(R.id.userHeight);
        doneButton = findViewById(R.id.done);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Check for invalid input
                String input = userHeight.getText().toString();

                int height;
                boolean validHeight = false;

                if (!input.matches("")) {
                    height = Integer.parseInt(userHeight.getText().toString());
                    //Invalid range
                    if (height < getResources().getInteger(R.integer.two_feet_in_inches)
                            || height > getResources().getInteger(R.integer.eight_feet_in_inches)) {
                        Toast.makeText(getApplicationContext(), R.string.invalid_input, Toast.LENGTH_SHORT).show();
                    } else {
                        validHeight = true;
                    }
                }
                //Empty input
                else {
                    Toast.makeText(getApplicationContext(), R.string.empty_input, Toast.LENGTH_SHORT).show();
                }

                if (validHeight) {
                    sharedPrefManager.setHeight(Integer.parseInt(userHeight.getText().toString()));
                    sharedPrefManager.setGoal(getResources().getInteger(R.integer.default_goal));
                    sharedPrefManager.setFirstTime(true);
                    sharedPrefManager.setIsWalker(true);

                    startActivity(new Intent(InputHeightActivity.this, MainPageActivity.class));
                }
            }
        });
    }
}

