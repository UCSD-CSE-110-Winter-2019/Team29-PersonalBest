package com.android.personalbest.cloud;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.personalbest.FriendListActivity;
import com.android.personalbest.MonthlyActivityLocalData;
import com.android.personalbest.R;
import com.android.personalbest.SharedPrefManager;
import com.android.personalbest.SignUpFriendPageActivity;
import com.android.personalbest.UserDayData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirestoreAdapter implements CloudstoreService {
    private static String COLLECTION_KEY = "appUserList";
    private static String TAG = " FirestoreAdapter ";
    private boolean userPendingStatus = false;
    private boolean friendPendingStatus = false;
    private boolean friendStatus = false;
    private static CollectionReference currentAppUser = FirebaseFirestore.getInstance().collection(COLLECTION_KEY);
    private boolean isAppUser = false;
    private Context context;
    private SharedPrefManager sharedPrefManager;

    public FirestoreAdapter(Context context){
        this.context = context;
    }

    @Override
    public void appUserCheck(final SignUpFriendPageActivity signUpFriendPageActivity, final String friendEmail) {

        currentAppUser.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getId().equals(friendEmail)){
                                    setAppUserStatus(true);
                                }
                                //Log.i(TAG, document.getId() + " => " + document.getData());
                            }
                            //  pass success message to observer
                            signUpFriendPageActivity.onAppUserCheckCompleted();
                        } else {
                            Log.i(TAG, "Error getting documents: ", task.getException());
                            //  pass failure message to observer
                        }
                    }

                });
    }

    @Override
    public void isInUserPendingListCheck(final SignUpFriendPageActivity signUpFriendPageActivity, final String currentAppUserEmail, final String friendEmail) {

        currentAppUser.document(currentAppUserEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                List<Object> userPendingFriendList = (List<Object>)document.getData().get(context.getString(R.string.pending_friend_list));
                                for(Object pendingFriend: userPendingFriendList){
                                    if (pendingFriend.equals(friendEmail)){
                                        setUserPendingStatus(true);
                                    }
                                }
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                signUpFriendPageActivity.onIsInUserPendingListCheckCompleted();
                            } else {
                                Log.d(TAG, "No such document");
                            }

                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void isInFriendListCheck(final SignUpFriendPageActivity signUpFriendPageActivity, final String currentAppUserEmail, final String friendEmail) {

        currentAppUser.document(currentAppUserEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                List<Object> userFriendList = (List<Object>)document.getData().get(context.getString(R.string.friend_list));
                                for(Object friend: userFriendList){
                                    if (friend.equals(friendEmail)){
                                        setFriendStatus(true);
                                    }
                                }
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                signUpFriendPageActivity.onIsInFriendListCheckCompleted();
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void isInFriendPendingListCheck(final SignUpFriendPageActivity signUpFriendPageActivity, final String currentAppUserEmail, final String friendEmail) {

        currentAppUser.document(friendEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                List<Object> friendPendingFriendList = (List<Object>)document.getData().get(context.getString(R.string.pending_friend_list));
                                for(Object pendingFriend: friendPendingFriendList){
                                    if (pendingFriend.equals(currentAppUserEmail)){
                                        setFriendPendingStatus(true);
                                    }
                                }
                                Log.i(TAG, "friendPendingCheck call!!!");
                                signUpFriendPageActivity.onIsInFriendPendingListCheckCompleted();
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void addToPendingFriendList(String currentAppUserEmail, String friendEmail) {
        currentAppUser.document(currentAppUserEmail).update(context.getString(R.string.pending_friend_list),
                FieldValue.arrayUnion(friendEmail));
    }

    @Override
    public void addToFriendList(String currentAppUserEmail, String friendEmail) {
        currentAppUser.document(currentAppUserEmail).update(context.getString(R.string.friend_list), FieldValue.arrayUnion(friendEmail));
        currentAppUser.document(friendEmail).update(context.getString(R.string.friend_list), FieldValue.arrayUnion(currentAppUserEmail));
    }

    @Override
    public void removeFromPendingFriendList(String currentAppUserEmail, String friendEmail) {
        currentAppUser.document(currentAppUserEmail).update(context.getString(R.string.pending_friend_list),
                FieldValue.arrayRemove(friendEmail));
    }

    @Override
    public void setAppUserInCloud(String appUser, Map<String, Object> friend) {

       currentAppUser.document(appUser).set(friend)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    public static void getFriendList(final FriendListActivity friendListActivity, String currentAppUserEmail) {

        currentAppUser.document(currentAppUserEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                List<Object> userFriendList = (List<Object>)document.getData().get(friendListActivity.getString(R.string.friend_list));
                                friendListActivity.onGetFriendListCompleted(convertObjectToString(userFriendList));
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void setAppUserStatus(boolean appUserStatus) {
        isAppUser = appUserStatus;
    }

    @Override
    public boolean getAppUserStatus() {
        return isAppUser;
    }

    public void setUserPendingStatus(boolean userPendingStatus) {
        this.userPendingStatus = userPendingStatus;
    }

    @Override
    public boolean getUserPendingStatus() {
        return this.userPendingStatus;
    }

    @Override
    public void setFriendPendingStatus(boolean friendPendingStatus) {
        this.friendPendingStatus = friendPendingStatus;
    }

    @Override
    public boolean getFriendPendingStatus() {
        return this.friendPendingStatus;
    }

    @Override
    public void setFriendStatus(boolean friendStatus) {
        this.friendStatus = friendStatus;
    }

    @Override
    public boolean getFriendStatus() {
        return friendStatus;
    }

    @Override
    public void resetUserAddFriendProcess() {
        setAppUserStatus(false);
        setFriendStatus(false);
        setFriendPendingStatus(false);
        setUserPendingStatus(false);
    }

    private static List<String> convertObjectToString(List<Object> userFriendList){
        List<String> userFriendListInString = new ArrayList<>();
        for(Object friend: userFriendList){
            userFriendListInString.add((String) friend);
        }
        return userFriendListInString;
    }


    /* Cloud storage for monthly activity */
    @Override
    public void storeMonthlyActivityForNewUser(String currentAppUserEmail) {
        MonthlyActivityLocalData.storeMonthlyActivityForNewUser();
        currentAppUser.document(currentAppUserEmail).update("monthlyActivity", MonthlyActivityLocalData.myMonthlyActivity);
    }

    //Called this method at end of day
    @Override
    public void updateMonthlyActivityEndOfDay(String currentAppUserEmail) {
        MonthlyActivityLocalData.updateDataAtEndOfDay(context);
        currentAppUser.document(currentAppUserEmail).update("monthlyActivity", MonthlyActivityLocalData.myMonthlyActivity);
    }

    //Optionally call this method periodically to update today's data in the Cloud
    @Override
    public void updateMonthlyActivityData(String currentAppUserEmail) {
        MonthlyActivityLocalData.updateTodayData(context);
        currentAppUser.document(currentAppUserEmail).update("monthlyActivity", MonthlyActivityLocalData.myMonthlyActivity);
    }

    //TODO: Call this method when clicking on friends monthly activity, Then access data at MonthlyActivityLocalData.friendMonthlyActivity
    @Override
    public void getFriendMonthlyActivity(String friendEmail) {
        currentAppUser.document(friendEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                List<Object> retrievedData = (List<Object>)document.getData().get("monthlyActivity");
                                MonthlyActivityLocalData.setFriendMonthlyActivity(convertObjectToUserDayData(retrievedData));
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    private static List<UserDayData> convertObjectToUserDayData(List<Object> friendMonthlyActivity){
        List<UserDayData> friendUserDayDataList = new ArrayList<>();
        for(Object day: friendMonthlyActivity){
            friendUserDayDataList.add((UserDayData) day);
        }
        return friendUserDayDataList;
    }
}

