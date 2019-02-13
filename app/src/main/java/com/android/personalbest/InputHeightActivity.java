package com.android.personalbest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InputHeightActivity extends AppCompatActivity {

    private Button doneButton;
    private EditText userHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_height);

        userHeight = findViewById(R.id.userHeight);
        doneButton = findViewById(R.id.done);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Check for invalid input
                String input = userHeight.getText().toString();

                int height;
                boolean validHeight = false;

                //Check for invalid input
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

                //Save valid height input
                if (validHeight) {
                    SharedPreferences sharedPref = getSharedPreferences(getString(R.string.user_prefs), MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();

                    //Save height and default step goal in shared pref
                    editor.putInt(getString(R.string.height), Integer.parseInt(userHeight.getText().toString()));
                    editor.putInt(getString(R.string.goal), getResources().getInteger(R.integer.default_goal));
                    editor.putBoolean(getString(R.string.first_time), true);
                    editor.apply();

                    //creating sharedpreference to keep track of whether user is a walker runner
                    SharedPreferences sharedPrefWalkerRunner = getSharedPreferences("walkerOrRunner", MODE_PRIVATE);
                    SharedPreferences.Editor editorWalkerRunner = sharedPrefWalkerRunner.edit();
                    //default is walker
                    editorWalkerRunner.putBoolean("isWalker", true);
                    editorWalkerRunner.apply();

                    startActivity(new Intent(InputHeightActivity.this, MainPageActivity.class));
                }
            }
        });
    }
}

