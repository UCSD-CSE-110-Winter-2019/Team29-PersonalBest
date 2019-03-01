package com.android.personalbest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class FriendListActivity extends AppCompatActivity {

    public ListView listView;
    private Button returnHomeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        listView = findViewById(R.id.friendListView);
        returnHomeBtn = findViewById(R.id.returnHomeBtn);

        ArrayList<String> friendList = new ArrayList<>();
        friendList.add("Lu");
        friendList.add("Nidi");
        friendList.add("Sarah");
        friendList.add("Lavanya");
        friendList.add("Lu");
        friendList.add("Nidi");
        friendList.add("Sarah");
        friendList.add("Lavanya");
        friendList.add("Lu");
        friendList.add("Nidi");
        friendList.add("Sarah");
        friendList.add("Lavanya");

        friendList.add("Lu");
        friendList.add("Nidi");
        friendList.add("Sarah");
        friendList.add("Lavanya");

        friendList.add("Lu");
        friendList.add("Nidi");
        friendList.add("Sarah");
        friendList.add("Lavanya");


        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,friendList);
        listView.setAdapter(arrayAdapter);


        returnHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
