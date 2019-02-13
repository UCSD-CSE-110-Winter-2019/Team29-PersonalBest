package com.android.personalbest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {

    private SignInButton signInButton;

    private int RC_SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient;
    private String TAG = "MainActivity";
    private FirebaseAuth mAuth;

    private String LogInStatus = "LogInStatus";
    private boolean login = false;

    private boolean haveInputtedHeight = false;

    public String numStepDone = "numStepDone";

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    //Resource In use:https://firebase.google.com/docs/auth/android/google-signin
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInButton = findViewById(R.id.sign_in_button);
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(getString(R.string.user_prefs),MODE_PRIVATE);

        //update log in status here
        login = sharedPreferences.getBoolean(LogInStatus,login);
        //update haveInputtedHeight here
        haveInputtedHeight = sharedPreferences.getBoolean(getString(R.string.first_time), haveInputtedHeight);

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
                .requestIdToken( "83289949414-ud0dn53urfr46sp3aib7ta8su7nct94v.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            editor = sharedPreferences.edit();
                            editor.putBoolean(LogInStatus,true);
                            editor.apply();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this,"You are not able to log in to Google",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        signInButton.setVisibility(View.GONE);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        startActivity(new Intent(MainActivity.this, InputHeightActivity.class));

    }

}
