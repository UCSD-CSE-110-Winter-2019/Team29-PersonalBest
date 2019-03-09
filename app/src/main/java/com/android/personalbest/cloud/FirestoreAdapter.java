package com.android.personalbest.cloud;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.personalbest.FriendListActivity;
import com.android.personalbest.R;
import com.android.personalbest.SignUpFriendPageActivity;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class FirestoreAdapter implements CloudstoreService {
    private static String COLLECTION_KEY = "appUserList";
    private static String TAG = " FirestoreAdapter ";
    private boolean userAddAFriend = false;
    private boolean friendAddUser = false;
    public static CollectionReference currentAppUser = FirebaseFirestore.getInstance().collection(COLLECTION_KEY);;
    private static boolean isAppUser = false;
    private FriendListActivity friendListActivity;


    public FirestoreAdapter(FriendListActivity friendListActivity){
        this.friendListActivity = friendListActivity;

    }

    @Override
    public CollectionReference getCurrentAppUser() {
        return currentAppUser;
    }

    public static void appUserCheck(final SignUpFriendPageActivity signUpFriendPageActivity, final String friendEmail) {
        currentAppUser.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getId().equals(friendEmail) ){
                                    setAppUserStatus(true);
                                }
                                Log.i(TAG, "This will call later");
                            }
                            //  pass success message to observer
                            signUpFriendPageActivity.onUserCheckCompleted();

                        } else {
                            Log.i(TAG, "Error getting documents: ", task.getException());
                            //  pass failure message to observer
                        }
                    }

                });
        Log.i(TAG, "This will call first");
    }

    public static void addToSentRequestFriendList(String currentAppUserEmail, String friendEmail) {
            currentAppUser.document(currentAppUserEmail).update("sentRequestFriendList",
                    FieldValue.arrayUnion(friendEmail));
    }

    public void isUserAddFriendCheck(final String currentAppUserEmail, final String friendEmail) {

        currentAppUser.document(currentAppUserEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                List<Object> userSentRequestFriendList = (List<Object>)document.getData().get(friendListActivity.getString(R.string.sent_friend_request_list));
                                for(Object friend: userSentRequestFriendList){
                                    if (friend.equals(friendEmail)){
                                        setUserAddFriendStatus(true);
                                    }
                                }
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                            }
                            friendListActivity.onUserAddFriendCheckCompleted();
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

        Log.i(TAG,"isUserAddFriendCheck get call \n");

    }

    @Override
    public void isFriendAddUserCheck(final String currentAppUserEmail, final String friendEmail) {

        Log.i(TAG,"isFriendAddUserCheck get call \n");
        currentAppUser.document(friendEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                List<Object> friendListSentRequestFriendList = (List<Object>)document.getData().get(friendListActivity.getString(R.string.sent_friend_request_list));
                                for(Object friend: friendListSentRequestFriendList){
                                    if (friend.equals(currentAppUserEmail)){
                                        setFriendAddUserStatus(true);
                                    }
                                }
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                            }
                            friendListActivity.onFriendAddUserCheckCompleted();
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

    }

    @Override
    public boolean weAreBothFriendCheck(String currentAppUserEmail, String friendEmail) {

        return getUserAddFriendStatus() && getFriendAddUserStatus();
    }

    @Override
    public void upDateAppUserFriendList(String currentAppUserEmail, String friendEmail) {
        currentAppUser.document(currentAppUserEmail).update(friendListActivity.getString(R.string.friend_list), FieldValue.arrayUnion(friendEmail));
        currentAppUser.document(friendEmail).update(friendListActivity.getString(R.string.friend_list),FieldValue.arrayUnion(currentAppUserEmail));
    }

    @Override
    public void getFriendList(String currentAppUserEmail) {

        currentAppUser.document(currentAppUserEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                List<Object> userFriendList = (List<Object>)document.getData().get(friendListActivity.getString(R.string.friend_list));

                                friendListActivity.onUserAddFriendCheckCompleted();
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

    public static void setAppUserInCloud(String appUser, Map<String, Object> friend) {

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


    public static void setAppUserStatus(boolean appUserStatus) {
        isAppUser = appUserStatus;
    }



    public static boolean getAppUserStatus() {
        return isAppUser;
    }

    @Override
    public void setUserAddFriendStatus(boolean userAddFriendStatus) {
        userAddAFriend = userAddFriendStatus;
    }

    @Override
    public boolean getUserAddFriendStatus() {
        return userAddAFriend;
    }

    @Override
    public void setFriendAddUserStatus(boolean friendAddUserStatus) {
        friendAddUser = friendAddUserStatus;
    }

    @Override
    public boolean getFriendAddUserStatus() {
        return  friendAddUser;
    }

    @Override
    public void resetUserAddFriendProcess() {
        setUserAddFriendStatus(false);
        setFriendAddUserStatus(false);
    }

    private List<String> convertObjectToString(List<Object> userFriendList){
        List<String> userFriendListInString = new ArrayList<>();
        for(Object friend: userFriendList){
            userFriendListInString.add((String) friend);
        }
        return  userFriendListInString;
    }


}

