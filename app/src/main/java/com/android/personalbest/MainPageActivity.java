package com.android.personalbest;



import android.Manifest;
import android.app.Activity;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.personalbest.fitness.FitnessService;
import com.android.personalbest.fitness.GoogleFitAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.fitness.result.DataSourcesResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.common.api.GoogleApiClient;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainPageActivity extends AppCompatActivity {

    private Button startButton;
    private Button seeBarChart;
    private Button userSettings;
    private TextView numStepDone;
    private final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = System.identityHashCode(this) & 0xFFFF;
    private final int REQUEST_LOCATION = System.identityHashCode(this) & 0xFFFF;
    private OnDataPointListener mListener;
    private GoogleApiClient mClient = null;

//    public static CountStepAsynTask runner;

    private static final String TAG = "MainPageActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        startButton = findViewById(R.id.startButton);
        seeBarChart = findViewById(R.id.seeBarChart);
        userSettings = findViewById(R.id.userSettings);

        numStepDone = findViewById(R.id.numStepDone);


        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_ACTIVITY_SEGMENT, FitnessOptions.ACCESS_WRITE)
                .build();

        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this, // your activity
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(this),
                    fitnessOptions);
        } else {
            connectFitness();
            startRecording();

        }


        Button startWalkActivity = (Button) findViewById(R.id.startButton);
        startWalkActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                launchWalkActivity();
            }
        });

    }




    public void launchWalkActivity() {
        Intent walk = new Intent(this, WalkActivity.class);
        startActivity(walk);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

                Log.i(TAG,"resultCode == Activity.RESULT_OK");


        }
    }


    private void startRecording() {
        GoogleSignInAccount lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (lastSignedInAccount == null) {
            return;
        }

        Fitness.getRecordingClient(this, GoogleSignIn.getLastSignedInAccount(this))

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



    private void connectFitness(){

        if( mClient == null) {
            Log.i(TAG, "connectFitness get call: " );
            mClient = new GoogleApiClient.Builder(this)
                    .addApi(Fitness.HISTORY_API)
                    .addApi(Fitness.RECORDING_API)
                    .addScope(new Scope(Scopes.FITNESS_BODY_READ_WRITE))
                    .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                    .addScope(new Scope(Scopes.FITNESS_LOCATION_READ))
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {

                        @Override
                        public void onConnected(@Nullable Bundle bundle) {
                            Log.i(TAG, "Connectied!!!");
                            findFitnessDataSources();
                            ResultCallback<DataSourcesResult> dataSourcesResultCallback = new ResultCallback<DataSourcesResult>() {
                                @Override
                                public void onResult(DataSourcesResult dataSourcesResult) {
                                    for( DataSource dataSource : dataSourcesResult.getDataSources() ) {
                                        if( DataType.TYPE_STEP_COUNT_CUMULATIVE.equals( dataSource.getDataType() ) ) {
                                            registerFitnessDataListener(dataSource, DataType.TYPE_STEP_COUNT_CUMULATIVE);
                                        }
                                    }
                                }
                            };
                        }

                        @Override
                        public void onConnectionSuspended(int i) {

                            if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                Log.i(TAG, "Connection lost.  Cause: Network Lost.");
                            } else if (i
                                    == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                Log.i(TAG,
                                        "Connection lost.  Reason: Service Disconnected");
                            }

                        }

                    })
                    .enableAutoManage(this, 0, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult result) {
                            Log.e(TAG, "!_@@ERROR :: Google Play services connection failed. Cause: " + result.toString());
                        }
                    })
                    .build();
        }
    }

    private void findFitnessDataSources(){
        Log.i(TAG, "Step up sensor get call: " );
        // Note: Fitness.SensorsApi.findDataSources() requires the ACCESS_FINE_LOCATION permission.
        Fitness.getSensorsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .findDataSources(
                        new DataSourcesRequest.Builder()
                                .setDataTypes(DataType.TYPE_STEP_COUNT_DELTA)
                                .setDataSourceTypes(DataSource.TYPE_DERIVED)
                                .build())
                .addOnSuccessListener(
                        new OnSuccessListener<List<DataSource>>() {
                            @Override
                            public void onSuccess(List<DataSource> dataSources) {
                                Log.i(TAG, "onSuccess(List<DataSource> dataSources) " +dataSources.toString());
                                for (DataSource dataSource : dataSources) {
                                    Log.i(TAG, "Data source found: " + dataSource.toString());
                                    Log.i(TAG, "Data Source type: " + dataSource.getDataType().getName());


                                    // Let's register a listener to receive Activity data!
                                    if (dataSource.getDataType().equals(DataType.TYPE_STEP_COUNT_DELTA)
                                            && mListener == null) {
                                        Log.i(TAG, "Data source for LOCATION_SAMPLE found!  Registering.");
                                        registerFitnessDataListener(dataSource, DataType.TYPE_STEP_COUNT_DELTA);
                                    }
                                }
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "failed", e);
                            }
                        });
    }

    private void registerFitnessDataListener(DataSource dataSource,DataType dataType){

        mListener =
                new OnDataPointListener() {
                    @Override
                    public void onDataPoint(DataPoint dataPoint) {
                        Log.i(TAG, "Detected Point: " + dataPoint.toString());
                        for (Field field : dataPoint.getDataType().getFields()) {
                            final Value val = dataPoint.getValue(field);
                            Log.i(TAG, "Detected DataPoint field: " + field.getName());
                            Log.i(TAG, "Detected DataPoint value: " + val);
                            if (field.getName().compareToIgnoreCase("steps") == 0) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        numStepDone.setText(String.valueOf(val));
                                        Log.i("Step",String.valueOf(val));
                                    }
                                });
                            }
                        }


                    }
                };

        Fitness.getSensorsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .add(
                        new SensorRequest.Builder()
                                .setDataSource(dataSource) // Optional but recommended for custom data sets.
                                .setDataType(dataType) // Can't be omitted.
                                .setSamplingRate(1, TimeUnit.SECONDS)
                                .build(),
                        mListener)
                .addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.i(TAG, "Listener registered!");
                                } else {
                                    Log.e(TAG, "Listener not registered."+"Here", task.getException());
                                }
                            }
                        });

    }



//    private class CountStepAsynTask extends AsyncTask<Void, Long,Long> {
//
//        @Override
//        protected Long doInBackground(Void... params) {
//
//
//            Log.i(TAG, "doInBackground() get call " );
//            long total = 0;
//            for (int i = 0; i < 10; ++i) {
//                try{
//
//                    PendingResult<DailyTotalResult> result = Fitness.HistoryApi.readDailyTotal(mClient, DataType.TYPE_STEP_COUNT_DELTA);
//                    DailyTotalResult totalResult = result.await(1, TimeUnit.SECONDS);
//                    if (totalResult.getStatus().isSuccess()) {
//                        DataSet totalSet = totalResult.getTotal();
//                        total = totalSet.isEmpty()
//                                ? 0
//                                : totalSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
//                        publishProgress(total);
//                    } else {
//                        Log.i(TAG, "There was a problem getting the step count.");
//                    }
//
//                    Log.i(TAG, "Total steps 11111: " + total);
//                        Thread.sleep(1000);
//                }catch (InterruptedException e){
//                    e.printStackTrace();
//                }
//                if (isCancelled()) break;
//
//            }
//
//            return total;
//        }
//
//        @Override
//        protected void onProgressUpdate (Long... count) {
//
//            Log.i(TAG,"current Step" + String.valueOf(count));
//
//        }


//    }
//
//    private void findData(){
//        final DataSource ds = new DataSource.Builder()
//                .setAppPackageName("com.google.android.gms")
//                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
//                .setType(DataSource.TYPE_DERIVED)
//                .setStreamName("estimated_steps")
//                .build();
//
//        final DataReadRequest req = new DataReadRequest.Builder()
//                .aggregate(ds, DataType.AGGREGATE_STEP_COUNT_DELTA)
//                .bucketByTime(1, TimeUnit.DAYS)
//                .setTimeRange(1, 10, TimeUnit.MILLISECONDS)
//                .build();
//    }






}

//    private void findFitnessDataSources(){
//        Log.i(TAG, "Step up sensor get call: " );
//        // Note: Fitness.SensorsApi.findDataSources() requires the ACCESS_FINE_LOCATION permission.
//        Fitness.getSensorsClient(this, GoogleSignIn.getLastSignedInAccount(this))
//                .findDataSources(
//                        new DataSourcesRequest.Builder()
//                                .setDataTypes(DataType.TYPE_STEP_COUNT_DELTA)
//                                .setDataSourceTypes(DataSource.TYPE_DERIVED)
//                                .build())
//                .addOnSuccessListener(
//                        new OnSuccessListener<List<DataSource>>() {
//                            @Override
//                            public void onSuccess(List<DataSource> dataSources) {
//                                Log.i(TAG, "onSuccess(List<DataSource> dataSources) " +dataSources.toString());
//                                for (DataSource dataSource : dataSources) {
//                                    Log.i(TAG, "Data source found: " + dataSource.toString());
//                                    Log.i(TAG, "Data Source type: " + dataSource.getDataType().getName());
//
//
//                                    // Let's register a listener to receive Activity data!
//                                    if (dataSource.getDataType().equals(DataType.TYPE_STEP_COUNT_DELTA)
//                                            && mListener == null) {
//                                        Log.i(TAG, "Data source for LOCATION_SAMPLE found!  Registering.");
//                                        registerFitnessDataListener(dataSource, DataType.TYPE_STEP_COUNT_DELTA);
//                                    }
//                                }
//                            }
//                        })
//                .addOnFailureListener(
//                        new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.e(TAG, "failed", e);
//                            }
//                        });
//    }
//
//    private void registerFitnessDataListener(DataSource dataSource,DataType dataType){
//
//        mListener =
//                new OnDataPointListener() {
//                    @Override
//                    public void onDataPoint(DataPoint dataPoint) {
//                        Log.i(TAG, "Detected Point: " + dataPoint.toString());
//                        for (Field field : dataPoint.getDataType().getFields()) {
//                            Value val = dataPoint.getValue(field);
//                            Log.i(TAG, "Detected DataPoint field: " + field.getName());
//                            Log.i(TAG, "Detected DataPoint value: " + val);
//                        }
//                    }
//                };
//
//        Fitness.getSensorsClient(this, GoogleSignIn.getLastSignedInAccount(this))
//                .add(
//                        new SensorRequest.Builder()
//                                .setDataSource(dataSource) // Optional but recommended for custom data sets.
//                                .setDataType(dataType) // Can't be omitted.
//                                .setSamplingRate(1, TimeUnit.SECONDS)
//                                .build(),
//                        mListener)
//                .addOnCompleteListener(
//                        new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    Log.i(TAG, "Listener registered!");
//                                } else {
//                                    Log.e(TAG, "Listener not registered."+"Here", task.getException());
//                                }
//                            }
//                        });
//
//    }
//
//
//    private void getLocationPermission(){
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Check Permissions Now
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    REQUEST_LOCATION);
//            Log.i(TAG,"REQUEST_LOCATION" + String.valueOf(REQUEST_LOCATION));
//        } else {
//            // permission has been granted, continue as usual
//            Log.i(TAG,"permission has been granted, continue as usual");
//
//        }
//    }