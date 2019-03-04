package com.android.personalbest.cloud;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.personalbest.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class FirestoreAdapter implements CloudstoreService {
    private String COLLECTION_KEY = "appUserList";
    private String TAG = " FirestoreAdapter ";
    private boolean isAppUser;
    private boolean userHasAFriend;
    private boolean friendHasUser;
    public CollectionReference currentAppUser;
    private SharedPrefManager sharedPrefManager;


    public FirestoreAdapter(){

        setup();

    }


    @Override
    public void setup(){
        currentAppUser = FirebaseFirestore.getInstance()
                .collection(COLLECTION_KEY);

    }

    @Override
    public CollectionReference getCurrentAppUser() {
        return currentAppUser;
    }


    @Override
    public boolean appUserCheck(final String friendEmail) {
        isAppUser =false;
        currentAppUser.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getId() == friendEmail){
                                    isAppUser = true;
                                }
                                Log.i(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.i(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return isAppUser;
    }

    @Override
    public void registerFriendsInCloud(String appUser, Map<String, Object> friend) {
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
    public boolean isUserFriend(String currentAppUserEmail, final String friendEmail) {
        userHasAFriend =false;
        currentAppUser.whereEqualTo(currentAppUserEmail, true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getData().get(friendEmail).equals(true)){
                                    userHasAFriend = true;
                                }
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return userHasAFriend;

    }

    @Override
    public boolean isFriendWithUser(final String currentAppUserEmail, String friendEmail) {
        friendHasUser = false;
        currentAppUser.whereEqualTo(friendEmail, true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getData().get(currentAppUserEmail).equals(true)) {
                                    friendHasUser = true;
                                }
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return userHasAFriend;
    }

    @Override
    public boolean weAreBothFriendCheck(String currentAppUserEmail, String friendEmail) {
        return (isUserFriend(currentAppUserEmail,friendEmail) && isFriendWithUser(currentAppUserEmail,friendEmail));
    }
}
