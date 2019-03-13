package com.android.personalbest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.personalbest.cloud.CloudstoreService;
import com.android.personalbest.cloud.CloudstoreServiceFactory;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private SignInButton signInButton;

    private int RC_SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient;

    //TAG for Log
    private String TAG = "MainActivity";

    //Firebase authentication
    private FirebaseAuth mAuth;

    private boolean login = false;

    //Determind if user enter his/her stride length
    private boolean haveInputtedHeight = false;

    private SharedPrefManager sharedPrefManager;

    private CloudstoreService cloudstoreService;


    //Resource In use:https://firebase.google.com/docs/auth/android/google-signin
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInButton = findViewById(R.id.sign_in_button);
        mAuth = FirebaseAuth.getInstance();
        sharedPrefManager = new SharedPrefManager(this.getApplicationContext());

        login = sharedPrefManager.getLogin();
        haveInputtedHeight = sharedPrefManager.getFirstTime();

        cloudstoreService = CloudstoreServiceFactory.create(this, false);

        //If first time signing in, ask user for height
        if (login && !haveInputtedHeight) {
            startActivity(new Intent(MainActivity.this, InputHeightActivity.class));
        }
        //If not first time signing in, go to main page
        else if (login) {
            startActivity(new Intent(MainActivity.this, MainPageActivity.class));
        }

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("757516144939-c7fh4aclkjoc6fc79ms6hpbulr9ft1sg.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"Log in button is pressed");
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, log error
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            sharedPrefManager.setLogin(true);
                            updateUI(user);

                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        signInButton.setVisibility(View.GONE);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        registerAppUserInCloud(acct);
        startActivity(new Intent(MainActivity.this, InputHeightActivity.class));

    }


    private void registerAppUserInCloud(GoogleSignInAccount acct){
        Map<String, Object> friend = new HashMap<>();

        if (acct != null) {
            sharedPrefManager.setCurrentAppUserEmail(acct.getEmail());
            friend.put(this.getString(R.string.current_user_email),acct.getEmail());
            friend.put(this.getString(R.string.pending_friend_list), Arrays.asList());
            friend.put(this.getString(R.string.friend_list), Arrays.asList());
            cloudstoreService.setAppUserInCloud(acct.getEmail(),friend);
            cloudstoreService.storeMonthlyActivityForNewUser(acct.getEmail());
        }
    }

}
