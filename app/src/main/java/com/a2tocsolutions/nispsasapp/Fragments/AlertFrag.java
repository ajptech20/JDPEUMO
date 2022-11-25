package com.a2tocsolutions.nispsasapp.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.a2tocsolutions.nispsasapp.R;
import com.a2tocsolutions.nispsasapp.adapter.EmergencyAdapter;
import com.a2tocsolutions.nispsasapp.model.Emergency;
import com.a2tocsolutions.nispsasapp.networking.api.Service;
import com.a2tocsolutions.nispsasapp.networking.generator.DataGenerator;
import com.a2tocsolutions.nispsasapp.service.NispsasLockService;
import com.a2tocsolutions.nispsasapp.utils.FancyToast;
import com.a2tocsolutions.nispsasapp.utils.PreferenceUtils;
import com.afollestad.materialdialogs.BuildConfig;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.a2tocsolutions.nispsasapp.utils.Constants.BASE_URL;

public class AlertFrag extends Fragment implements EmergencyAdapter.ItemClickListener {
    public List <Emergency> emergencyList;
    public static final int MY_REQUEST_CALL_PHONE = 1;
    public EmergencyAdapter mAdapter;
    public RecyclerView myrecyclerview;
    public static final String TAG = "AlertFrag";
    public static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    boolean boolean_permission;

    protected Location mLastLocation;
    public static final int REQUEST_CALL_PHONE = 878;
    public static final int REQUEST_READ_GPS = 565;
    public static final int REQUEST_READ_PHONE_STATE = 45;



    // location updates interval - 10sec
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    public static final int REQUEST_CHECK_SETTINGS = 100;


    // bunch of location related apis
    public FusedLocationProviderClient mFusedLocationClient;
    public SettingsClient mSettingsClient;
    public LocationRequest mLocationRequest;
    public LocationSettingsRequest mLocationSettingsRequest;
    public LocationCallback mLocationCallback;
    public Location mCurrentLocation;
    // boolean flag to toggle the ui
    public Boolean mRequestingLocationUpdates;
    public AppUpdateManager mAppUpdateManager;


    public String longitude, latitude, phonenumber;
    @BindView(R.id.progress)
    RelativeLayout progress;


    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;

    @BindView(R.id.progress_text)
    TextView progress_text;

    View v;
    public FloatingActionButton floatbut;
    public TextView tv;

    public int progressStatus = 0;
    public boolean isCanceled;
    public boolean Gott;
    public Handler handler = new Handler();
    //The number of milliseconds in the future from the
    //call to start() until the count down is done
    public long millisInFuture = 5000; //5 seconds (make it dividable by 1000)
    //The interval along the way to receive onTick() callbacks
    public long countDownInterval = 1000; //1 second (don't change this value)





    public AlertFrag(){

    }
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.alertfrag, container, false);
        myrecyclerview = (RecyclerView) v.findViewById(R.id.emergency_recyclerview);
        mAdapter = new EmergencyAdapter(this, emergencyList, this);

        myrecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        myrecyclerview.setItemAnimator(new DefaultItemAnimator());
        myrecyclerview.setAdapter(mAdapter);
        prepareEmergency();

       floatbut = (FloatingActionButton) v.findViewById(R.id.floatb);
       floatbut.setOnClickListener(v -> distressCall());
        progress = (RelativeLayout) v.findViewById(R.id.progress);
        progress_text = (TextView) v.findViewById(R.id.progress_text);
        progress_bar = (ProgressBar) v.findViewById(R.id.progress_bar);

        tv=(TextView) v.findViewById(R.id.flytext);
        tv.setSelected(true);

        int progressBarMaximumValue = (int)(millisInFuture/countDownInterval);
        progress_bar.setMax(progressBarMaximumValue);


        startService(new Intent(Objects.requireNonNull(getContext()).getApplicationContext(), NispsasLockService.class));
        //fn_permission();
        init();

        // restore the values from saved instance state
        restoreValuesFromBundle(savedInstanceState);
        startLocation();


    return v;
    }


    public void startService(Intent intent) {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    emergencyList = new ArrayList <>();


    }



    public void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getActivity()));
        mSettingsClient = LocationServices.getSettingsClient(getActivity());

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();

            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setSmallestDisplacement(10);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    public void restoreValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates");
            }

            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location");
            }

            if (savedInstanceState.containsKey("last_updated_on")) {
            }
        }

    }



    public void startLocation() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates);
        outState.putParcelable("last_known_location", mCurrentLocation);

    }

    @SuppressLint("MissingPermission")
    public void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(Objects.requireNonNull(getActivity()), locationSettingsResponse -> {
                    Log.i(TAG, "All location settings are satisfied.");

                    //  Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                    //noinspection MissingPermission
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                            mLocationCallback, Looper.myLooper());

                })
                .addOnFailureListener(getActivity(), e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                    "location settings ");
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                ResolvableApiException rae = (ResolvableApiException) e;
                                rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sie) {
                                Log.i(TAG, "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            String errorMessage = "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings.";
                            Log.e(TAG, errorMessage);

                            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                    }

                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }

    public void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Resuming location updates depending on button state and
        // allowed permissions
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        }

    }

    public boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }




    public void prepareEmergency() {
        int[] covers = new int[]{
                R.drawable.crime,
                R.drawable.fire,
                R.drawable.rape,
                R.drawable.floo

        };

        Emergency a = new Emergency(1,"CRIME", "", covers[0]);
        emergencyList.add(a);

        a = new Emergency(2, "FIRE", "", covers[1]);
        emergencyList.add(a);

        a = new Emergency(3, "RAPE", "", covers[2]);
        emergencyList.add(a);

        a = new Emergency(4, "FLOOD", "", covers[3]);
        emergencyList.add(a);


        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClickListener(int itemId) {
            Gott=false;
            isCanceled = false;
            ProgressDialog myDialog = new ProgressDialog(getContext());
            myDialog.setTitle("Cancel Alert");
            myDialog.setMessage(new StringBuilder().append("Sending Alert in ").append(progress_bar.getMax()).append(" Seconds...").toString());
            myDialog.setCancelable(false);
            myDialog.setMax(progress_bar.getMax());
            myDialog.setIndeterminate(false);
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
            myDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            myDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    isCanceled = true;
                    dialog.dismiss();
                }

            });
            myDialog.show();
            progressStatus = 0;

            // Start the lengthy operation in a background thread
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (progressStatus < myDialog.getMax()) {
                        // If user's click the cancel button from progress dialog
                        if (isCanceled) {
                            // Stop the operation/loop
                            Thread.currentThread().interrupt();
                            break;
                        }
                        // Update the progress status
                        progressStatus += 1;

                        // Try to sleep the thread for 200 milliseconds
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Update the progress bar
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                myDialog.setProgress(progressStatus);

                                if( itemId == 1) {
                                    Gott = false;
                                    if (progressStatus == myDialog.getMax()) {
                                        myDialog.dismiss();
                                        Gott = true;
                                    }
                                    if (Gott == true) {
                                        crimeAlert();
                                    }
                                }else if(itemId == 2){
                                    Gott = false;
                                    if (progressStatus == myDialog.getMax()) {
                                        myDialog.dismiss();
                                        Gott = true;
                                    }
                                    if (Gott  == true) {
                                        fireAlert();
                                    }
                                }else if(itemId == 3){
                                    Gott = false;
                                    if (progressStatus == myDialog.getMax()) {
                                        myDialog.dismiss();
                                        Gott = true;
                                    }
                                    if (Gott == true) {
                                        CovidAlert();
                                    }

                                }else if(itemId == 4){
                                    Gott = false;
                                    if (progressStatus == myDialog.getMax()) {
                                        myDialog.dismiss();
                                        Gott = true;
                                    }
                                    if (Gott == true) {
                                      floodAlert();
                                    }

                                }



                            }
                        });
                    }
                }
            }).start();



        /*
            //Initialize a new CountDownTimer instance
            new CountDownTimer(millisInFuture, countDownInterval){
                public void onTick(long millisUntilFinished){

                    //Another one second passed
                    //  progress_text.setText(millisUntilFinished/1000 + "  Seconds...");
                    //Each second ProgressBar progress counter added one
                    progressStatus +=1;
                    progress_bar.setProgress(progressStatus);
                    progress_text.setText(new StringBuilder().append("Sending Crime Alert in ").append(progressStatus - progress_bar.getMax()).append(" Seconds...").toString());
                    progress_bar.setVisibility(View.VISIBLE);


                }

                public void onFinish(){
                    //Do something when count down end.
                    crimeAlert();
                }
            }.start();*/





    }


    private void fireAlert () {
        startLocationUpdates();
        startLocation();
         String longitude = PreferenceUtils.getLocationLongitude(Objects.requireNonNull(getActivity()).getApplicationContext());
         String latitude = PreferenceUtils.getLocationLatitude(Objects.requireNonNull(getActivity()).getApplicationContext());
         String phonenumber = PreferenceUtils.getPhoneNumber(Objects.requireNonNull(getActivity()).getApplicationContext());


        progress.setVisibility(View.VISIBLE);
        progress_text.setText("Fire Alert sending...");

       if (latitude == null) {
            progress.setVisibility(View.GONE);
            Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), R.string.no_location_detected, Toast.LENGTH_LONG).show();
           startLocation();
            return;
        }
        try {
            //Toast.makeText(this, "lat: " + lat + " " + "lon: " + lon, Toast.LENGTH_LONG).show();

            Service service = DataGenerator.createService(Service.class, BASE_URL);
            Call <Void> call = service.fireAlert(phonenumber, latitude, longitude);

            call.enqueue(new Callback <Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response <Void> response) {
                    if (response.isSuccessful()) {
                        progress.setVisibility(View.GONE);
                        FancyToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Fire Alert successfully sent", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                    } else {
                        progress.setVisibility(View.GONE);
                        FancyToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Fire Alert was not sent try again", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    progress.setVisibility(View.GONE);
                    FancyToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Fire Alert was not sent try again", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
            });
        } catch (Exception e) {
            progress.setVisibility(View.GONE);
            FancyToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Fire Alert was not sent try again", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
        }

    }

    private void CovidAlert () {
        startLocationUpdates();
        startLocation();
        String longitude = PreferenceUtils.getLocationLongitude(Objects.requireNonNull(getActivity()).getApplicationContext());
        String latitude = PreferenceUtils.getLocationLatitude(Objects.requireNonNull(getActivity()).getApplicationContext());
        String phonenumber = PreferenceUtils.getPhoneNumber(Objects.requireNonNull(getActivity()).getApplicationContext());


        Log.d(TAG, "this is lat and lon" + " " + latitude + " " + latitude);
        progress.setVisibility(View.VISIBLE);
        progress_text.setText("Medical Alert sending...");
        if (latitude == null) {
            progress.setVisibility(View.GONE);
            Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), R.string.no_location_detected, Toast.LENGTH_LONG).show();
            startLocation();
            return;
        }

        try {
            //Toast.makeText(this, "lat: " + latitude + " " + "lon: " + longitude, Toast.LENGTH_LONG).show();

            Service service = DataGenerator.createService(Service.class, BASE_URL);
            Call<Void> call = service.medicalAlert(phonenumber, latitude, longitude);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        progress.setVisibility(View.GONE);
                        FancyToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Medical Alert successfully sent", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                    } else {
                        progress.setVisibility(View.GONE);
                        FancyToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Medical Alert was not sent try again", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    progress.setVisibility(View.GONE);
                    FancyToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Medical Alert was not sent try again", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
            });
        } catch (Exception e) {
            progress.setVisibility(View.GONE);
            FancyToast.makeText(getActivity().getApplicationContext(), "Medical Alert was not sent try again", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
        }
    }

    private void floodAlert() {
        String longitude = PreferenceUtils.getLocationLongitude(Objects.requireNonNull(getActivity()).getApplicationContext());
        String latitude = PreferenceUtils.getLocationLatitude(Objects.requireNonNull(getActivity()).getApplicationContext());
        String reporter = PreferenceUtils.getPhoneNumber(Objects.requireNonNull(getActivity()).getApplicationContext());
        String type="Flood";
        startLocationUpdates();
        startLocation();
        progress.setVisibility(View.VISIBLE);
        progress_text.setText("Flood Alert sending...");
        if (latitude == null) {
            progress.setVisibility(View.GONE);
            Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), R.string.no_location_detected, Toast.LENGTH_LONG).show();
           startLocation();
            return;
        }
        try{
            //Toast.makeText(this, "lat: " + latitude + " " + "lon: " + longitude, Toast.LENGTH_LONG).show();

            Service service = DataGenerator.createService(Service.class, BASE_URL);
            Call<Void> call = service.floodAlert(reporter, latitude, longitude, type);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        progress.setVisibility(View.GONE);
                        FancyToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Flood Alert successfully sent", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                    } else {
                        progress.setVisibility(View.GONE);
                        FancyToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Flood Alert was not sent try again", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    progress.setVisibility(View.GONE);
                    FancyToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Flood Alert was not sent try again", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
            });
        } catch (Exception e) {
            progress.setVisibility(View.GONE);
            FancyToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Flood Alert was not sent try again", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
        }
    }

    private void crimeAlert() {
        startLocationUpdates();
        startLocation();
        String id = UUID.randomUUID().toString();
        String longitude = PreferenceUtils.getLocationLongitude(Objects.requireNonNull(getActivity()).getApplicationContext());
        String latitude = PreferenceUtils.getLocationLatitude(Objects.requireNonNull(getActivity()).getApplicationContext());
        String phonenumber = PreferenceUtils.getPhoneNumber(Objects.requireNonNull(getActivity()).getApplicationContext());
        String type="Crime";

        progress.setVisibility(View.VISIBLE);
        progress_text.setText("Crime Alert sending...");
        if (latitude== null) {
            progress.setVisibility(View.GONE);
            Toast.makeText(getActivity(), R.string.no_location_detected, Toast.LENGTH_LONG).show();
         startLocation();
            return;
        }
        try{
            //Toast.makeText(this, "lat: " + lat + " " + "lon: " + lon, Toast.LENGTH_LONG).show();
            startLocationUpdates();
            Service service = DataGenerator.createService(Service.class, BASE_URL);
            Call<Void> call = service.crimeAlert(phonenumber, latitude, longitude, type, id);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        progress.setVisibility(View.GONE);
                        FancyToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Crime Alert successfully sent", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                    } else {
                        progress.setVisibility(View.GONE);
                        FancyToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Crime Alert was not sent try again", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    progress.setVisibility(View.GONE);
                    FancyToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Crime Alert was not sent try again", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
            });
        } catch (Exception e) {
            progress.setVisibility(View.GONE);
            FancyToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Crime Alert was not sent try again", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
        }
    }



    private void distressCall(){

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_REQUEST_CALL_PHONE);
        }else{
            Intent call = new Intent(Intent.ACTION_CALL);
            call.setData(Uri.parse("tel:112"));
            getActivity().getApplicationContext().startActivity(call);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_REQUEST_CALL_PHONE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                distressCall();
            }else {
                Toast.makeText(getActivity(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }















}
