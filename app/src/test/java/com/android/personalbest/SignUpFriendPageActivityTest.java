package com.android.personalbest;

import com.android.personalbest.cloud.CloudstoreService;
import com.android.personalbest.cloud.CloudstoreServiceFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
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
    public void testIsInUserPendingListCheck(){
        String currentAppUserEmail = "";
        String friendEmail = "";

        cloudstoreService.isInUserPendingListCheck(signUpFriendPageActivity, currentAppUserEmail,friendEmail);
        assertEquals(true,cloudstoreService.getUserPendingStatus());
    }

    @Test
    public void testIsIsInFriendPendingListCheck(){
        String currentAppUserEmail = "";
        String friendEmail = "";

        cloudstoreService.isInFriendPendingListCheck(signUpFriendPageActivity, currentAppUserEmail,friendEmail);
        assertEquals(true,cloudstoreService.getFriendPendingStatus());
    }

    @Test
    public void testIsInFriendListCheck(){
        String currentAppUserEmail = "";
        String friendEmail = "";

        cloudstoreService.isInFriendListCheck(signUpFriendPageActivity, currentAppUserEmail,friendEmail);
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
        cloudstoreService.resetUserAddFriendProcess();
        assertEquals(true,cloudstoreService.getAppUserStatus());
    }
}




