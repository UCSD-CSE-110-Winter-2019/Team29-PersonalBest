package com.android.personalbest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InputHeightActivity extends AppCompatActivity {

    private Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_height);
        doneButton = (Button)findViewById(R.id.done);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InputHeightActivity.this,MainPageActivity.class));
            }
        });
    }
}
