package com.android.personalbest;

import android.widget.TextView;

import com.android.personalbest.chatmessage.ChatActivity;
import com.android.personalbest.cloud.CloudstoreService;
import com.android.personalbest.cloud.CloudstoreServiceFactory;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class ChatMessageTest {
    private ChatActivity chatActivity;
    private CloudstoreService cloudstoreService;

    @BeforeClass
    public static void beforeClass(){
        ChatActivity.mock =true;
    }
    @Before
    public void setUp() {

        chatActivity = Robolectric.setupActivity(ChatActivity.class);
        cloudstoreService = CloudstoreServiceFactory.create(chatActivity, true);
    }


    @Test
    public void testInitChat(){

        cloudstoreService.initChat("from","to");
        assertEquals(true,cloudstoreService.getAppUserStatus());
    }


    @Test
    public void testSendMessage(){
        Map<String, String> newMessage = new HashMap<>();
        newMessage.put("from", "chl749@ucsd.edu");
        newMessage.put("", "Hello, I am testing SendMessage");
        StringBuilder sb = new StringBuilder();
        sb.append(newMessage);
        cloudstoreService.sendMessage(newMessage);
        TextView chatView = (TextView) chatActivity.findViewById(R.id.chat);
        assertEquals(sb.toString(), chatView.getText().toString());
    }

    @Test
    public void testInitMessageUpdateListener(){

        cloudstoreService.initMessageUpdateListener();
        assertEquals(true,cloudstoreService.getAppUserStatus());
    }





}
