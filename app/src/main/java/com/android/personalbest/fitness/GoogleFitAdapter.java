package com.android.personalbest.fitness;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.personalbest.MainPageActivity;
import com.android.personalbest.R;
import com.android.personalbest.SharedPrefManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class GoogleFitAdapter implements FitnessService {
    private final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = System.identityHashCode(this) & 0xFFFF;
    private final String TAG = "GoogleFitAdapter";

    private MainPageActivity activity;
    private SharedPrefManager sharedPrefManager;
    private int total = 0;

    private Handler handler;
    private Runnable runnable;
    private GoogleSignInAccount lastSignedInAccount;

    public GoogleFitAdapter(MainPageActivity activity) {

        this.activity = activity;
        sharedPrefManager = new SharedPrefManager(activity.getApplicationContext());
        this.setup();
        this.updateStepInRealTime();
        lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(activity);
    }

    public void setup() {

        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();

        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(activity), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    activity, // your activity
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(activity),
                    fitnessOptions);
        } else {
            Log.i(TAG,"ELSE get called()");
            updateStepCount();
            startRecording();
        }
    }

    private void startRecording() {

        if (lastSignedInAccount == null) {
            return;
        }

        Fitness.getRecordingClient(activity,lastSignedInAccount)
                .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Successfully subscribed!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "There was a problem subscribing.");
                    }
                });
    }


    /**
     * Reads the current daily step total, computed from midnight of the current day on the device's
     * current timezone.
     */
    public void updateStepCount() {
        Log.i(TAG, "updateStepCount()  get call");
        if (lastSignedInAccount == null) {
            Log.i(TAG, "lastSignedInAccount == null");
            return;
        }

        Fitness.getHistoryClient(activity, lastSignedInAccount)
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(
                        new OnSuccessListener<DataSet>() {
                            @Override
                            public void onSuccess(DataSet dataSet) {
                                Log.i(TAG, "dataset:"+dataSet.toString());
                                total =
                                        dataSet.isEmpty()
                                                ? 0
                                                : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();

                                activity.numStepDone.setText(String.valueOf(total));
                                if (total < sharedPrefManager.getNumSteps()) {
                                    //total was reset to 0, it's a new day
                                    activity.newDay();
                                }
                                if (total > sharedPrefManager.getGoal() && !sharedPrefManager.getGoalExceededToday()) {
                                    activity.exceedsGoal();
                                    Log.i(TAG, "Goal Exceeded");
                                }
                                if (total > (sharedPrefManager.getTotalStepsFromYesterday() + activity.getResources().getInteger(R.integer.subgoal))
                                    && !sharedPrefManager.getSubGoalExceededToday()) {
                                    activity.exceedsSubGoal();
                                    Log.i(TAG, "Subgoal Exceeded");
                                }
                                sharedPrefManager.editor.putInt(activity.getString(R.string.totalStep),total);
                                sharedPrefManager.editor.apply();

                                Log.i(TAG, "Total steps: " + total);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "There was a problem getting the step count.", e);
                            }
                        });
    }




    @Override
    public void updateStepInRealTime(){
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                updateStepCount();
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);

    }

    public int getCurrentStep(){
        return total;
    }
}