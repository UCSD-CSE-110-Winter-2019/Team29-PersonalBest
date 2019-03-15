package com.android.personalbest.cloud;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.personalbest.FriendListActivity;
import com.android.personalbest.MonthlyDataList;
import com.android.personalbest.R;
import com.android.personalbest.SharedPrefManager;
import com.android.personalbest.SignUpFriendPageActivity;
import com.android.personalbest.chatmessage.ChatActivity;
import com.android.personalbest.chatmessage.ChatMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirestoreAdapter implements CloudstoreService {
    private String COLLECTION_KEY = "appUserList";
    private String TAG = " FirestoreAdapter ";
    private boolean userPendingStatus = false;
    private boolean friendPendingStatus = false;
    private boolean friendStatus = false;
    private CollectionReference currentAppUser;
    private CollectionReference chat;
    private boolean isAppUser = false;
    private Context context;
    private String CHAT_COLLECTION_KEY = "chats";
    private String CHAT_DOCUMENT_KEY;
    private String CHAT_MESSAGES_KEY = "messages";
    private String CHAT_TIMESTAMP_KEY = "timestamp";
    private String FROM_KEY = "from";
    private String TEXT_KEY = "text";
    private ChatMessage chatMessage;
    private SharedPrefManager sharedPrefManager;


    public FirestoreAdapter(Context context){
        this.context = context;
        this.sharedPrefManager = new SharedPrefManager(context);
        FirebaseApp.initializeApp(context);
        currentAppUser = FirebaseFirestore.getInstance().collection(COLLECTION_KEY);
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
        sharedPrefManager = new SharedPrefManager(this.context);
        currentAppUser.document(currentAppUserEmail).update(context.getString(R.string.friend_list), FieldValue.arrayUnion(friendEmail));
        currentAppUser.document(friendEmail).update(context.getString(R.string.friend_list), FieldValue.arrayUnion(currentAppUserEmail));
        currentAppUser.document(currentAppUserEmail);
    }

    @Override
    public void removeFromPendingFriendList(String currentAppUserEmail, String friendEmail) {
        currentAppUser.document(currentAppUserEmail).update(context.getString(R.string.pending_friend_list),
                FieldValue.arrayRemove(friendEmail));
        currentAppUser.document(friendEmail).update(context.getString(R.string.pending_friend_list),
                FieldValue.arrayRemove(currentAppUserEmail));
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

    @Override
    public void getFriendList(final FriendListActivity friendListActivity, String currentAppUserEmail) {
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
        currentAppUser.document(currentAppUserEmail).update("monthlyActivity", new MonthlyDataList());
    }

    //Called this method at end of day
    @Override
    public void updateMonthlyActivityEndOfDay(String currentAppUserEmail, MonthlyDataList dataList) {
        final DataStorageMediator dataStorageMediator = new DataStorageMediator();
        currentAppUser.document(currentAppUserEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                dataStorageMediator.setMonthlyActivity(document.get("monthlyActivity", MonthlyDataList.class));
                                dataList.setList(dataStorageMediator.getMonthlyActivity().getList());
                                onUpdateDataEndOfDay(currentAppUserEmail, dataList);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    private void onUpdateDataEndOfDay(String currentAppUserEmail, MonthlyDataList dataList) {
        dataList.updateDataAtEndOfDay(context);
        currentAppUser.document(currentAppUserEmail).update("monthlyActivity", dataList);
    }

    //Optionally call this method periodically to update today's data in the Cloud
    @Override
    public void updateTodayData(String currentAppUserEmail, MonthlyDataList dataList) {
        final DataStorageMediator dataStorageMediator = new DataStorageMediator();
        currentAppUser.document(currentAppUserEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                dataStorageMediator.setMonthlyActivity(document.get("monthlyActivity", MonthlyDataList.class));
                                dataList.setList(dataStorageMediator.getMonthlyActivity().getList());
                                onUpdateTodayData(currentAppUserEmail, dataList);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    private void onUpdateTodayData(String currentAppUserEmail, MonthlyDataList dataList) {
        dataList.updateTodayData(context);
        currentAppUser.document(currentAppUserEmail).update("monthlyActivity", dataList);
    }

    @Override
    public void setMockPastData(String currentAppUserEmail, MonthlyDataList dataList) {
        final DataStorageMediator dataStorageMediator = new DataStorageMediator();
        currentAppUser.document(currentAppUserEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                dataStorageMediator.setMonthlyActivity(document.get("monthlyActivity", MonthlyDataList.class));
                                dataList.setList(dataStorageMediator.getMonthlyActivity().getList());
                                onMockPastData(currentAppUserEmail, dataList);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }


    private void onMockPastData(String currentAppUserEmail, MonthlyDataList dataList) {
        dataList.mockPastData();
        setMonthlyActivityData(sharedPrefManager.getCurrentAppUserEmail(), dataList);
    }

    @Override
    public void setMonthlyActivityData(String currentAppUserEmail, MonthlyDataList dataList) {
        currentAppUser.document(currentAppUserEmail).update("monthlyActivity", dataList);
    }

    @Override
    public void initChat(String from, String to) {

        if(from.compareTo(to) < 0){
            CHAT_DOCUMENT_KEY = from + to;
        }
        else{
           CHAT_DOCUMENT_KEY = to + from;
        }
        chat = FirebaseFirestore.getInstance()
                .collection(CHAT_COLLECTION_KEY)
                .document(CHAT_DOCUMENT_KEY)
                .collection(CHAT_MESSAGES_KEY);
    }

    @Override
    public void initMessageUpdateListener() {
        chat.orderBy(CHAT_TIMESTAMP_KEY, Query.Direction.ASCENDING)
                .addSnapshotListener((newChatSnapShot, error) -> {
                    if (error != null) {
                        Log.e(TAG, error.getLocalizedMessage());
                        return;
                    }
                    if (newChatSnapShot != null && !newChatSnapShot.isEmpty() && !newChatSnapShot.getMetadata().hasPendingWrites()) {
                        StringBuilder sb = new StringBuilder();
                        List<DocumentChange> documentChanges = newChatSnapShot.getDocumentChanges();
                        documentChanges.forEach(change -> {
                            QueryDocumentSnapshot document = change.getDocument();
                            chatMessage = new ChatMessage((String) document.get(FROM_KEY),(String) document.get(TEXT_KEY));
                            sb.append(chatMessage.toString());
                        });
                        TextView chatView = ((Activity)context).findViewById(R.id.chat);
                        chatView.append(sb.toString());
                    }
                });
    }

    @Override
    public void subscribeToNotificationsTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(CHAT_DOCUMENT_KEY)
                .addOnCompleteListener(task -> {
                            String msg = "Subscribed to notifications";
                            if (!task.isSuccessful()) {
                                msg = "Subscribe to notifications failed";
                            }
                            Log.d(TAG, msg);
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        }
                );
    }

    @Override
    public void sendMessage(Map<String, String> newMessage) {
        EditText messageView = ((Activity)context).findViewById(R.id.text_message);

        chat.add(newMessage).addOnSuccessListener(result -> {
            messageView.setText("");
        }).addOnFailureListener(error -> {
            Log.e(TAG, error.getLocalizedMessage());
        });
    }
}

