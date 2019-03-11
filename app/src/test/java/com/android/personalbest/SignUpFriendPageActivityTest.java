package com.android.personalbest;


import android.util.Log;

import com.android.personalbest.cloud.CloudstoreService;
import com.android.personalbest.cloud.CloudstoreServiceFactory;
import com.android.personalbest.cloud.FirestoreAdapter;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static com.android.personalbest.cloud.CloudstoreServiceFactory.create;
import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class SignUpFriendPageActivityTest {

    private SignUpFriendPageActivity signUpFriendPageActivity;
    private CloudstoreService cloudstoreService;


    @Before
    public void setUp() {
        signUpFriendPageActivity = Robolectric.buildActivity(SignUpFriendPageActivity.class).get();
        cloudstoreService = CloudstoreServiceFactory.create(signUpFriendPageActivity, true);



    }

    @Test
    public void testAppUserCheck(){
        String friendEmail = "";
        cloudstoreService.appUserCheck(signUpFriendPageActivity,friendEmail);
        assertEquals(true,cloudstoreService.getAppUserStatus());

    }

    @Test
    public void testIsInUserPendingListCheck(){
        String currentAppUserEmail = "";
        String friendEmail = "";

        cloudstoreService.isInUserPendingListCheck(currentAppUserEmail,friendEmail);
        assertEquals(true,cloudstoreService.getUserPendingStatus());
    }

    @Test
    public void testIsIsInFriendPendingListCheck(){
        String currentAppUserEmail = "";
        String friendEmail = "";

        cloudstoreService.isInFriendPendingListCheck(currentAppUserEmail,friendEmail);
        assertEquals(true,cloudstoreService.getFriendPendingStatus());
    }

    @Test
    public void testIsInFriendListCheck(){
        String currentAppUserEmail = "";
        String friendEmail = "";

        cloudstoreService.isInFriendListCheck(currentAppUserEmail,friendEmail);
        assertEquals(true,cloudstoreService.getFriendStatus());
    }

    @Test
    public void testAddToPendingFriendList(){
        String currentAppUserEmail = "";
        String friendEmail = "";

        cloudstoreService.addToPendingFriendList(currentAppUserEmail,friendEmail);
        assertEquals(true,cloudstoreService.getFriendStatus());
    }

    @Test
    public void testAddToFriendList(){
        String currentAppUserEmail = "";
        String friendEmail = "";

        cloudstoreService.addToFriendList(currentAppUserEmail,friendEmail);
        assertEquals(true,cloudstoreService.getFriendStatus());
    }

    @Test
    public void testResetUserAddFriendProcess(){
        String currentAppUserEmail = "";
        String friendEmail = "";

        cloudstoreService.resetUserAddFriendProcess();
        assertEquals(true,cloudstoreService.getAppUserStatus());
    }













}




