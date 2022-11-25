package com.a2tocsolutions.nispsasapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.a2tocsolutions.nispsasapp.Fragments.AlertFrag;
import com.a2tocsolutions.nispsasapp.activities.CovidActivity;
import com.a2tocsolutions.nispsasapp.activities.HowToActivity;
import com.a2tocsolutions.nispsasapp.activities.MyCustomPagerAdapter;
import com.a2tocsolutions.nispsasapp.adapter.ArticleslideAdapter;
import com.a2tocsolutions.nispsasapp.database.AppDatabase;
import com.a2tocsolutions.nispsasapp.database.ArticleEntry;
import com.a2tocsolutions.nispsasapp.model.Article;
import com.a2tocsolutions.nispsasapp.model.ArticleResponse;
import com.a2tocsolutions.nispsasapp.networking.api.Service;
import com.a2tocsolutions.nispsasapp.networking.generator.DataGenerator;
import com.a2tocsolutions.nispsasapp.service.NispsasLockService;
import com.a2tocsolutions.nispsasapp.utils.AppExecutors;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;
import java.util.Timer;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RAPE extends AppCompatActivity {
    private static final String TAG = "RAPE";
    private static final int RC_APP_UPDATE = 0;
    AlertFrag mActivity = new AlertFrag();
    private static int i = 0;
    private AppDatabase mDb;
    //private ArticleAdapter articleAdapter;
    private ArticleslideAdapter articleslideAdapter;
    private static final int REQUEST_PERMISSIONS = 100;
    private TabLayout tabLayout;

    private String swi;


    @BindView(R.id.menu_image)
    AppCompatImageView menu_image;

    @BindView(R.id.fire)
    AppCompatImageView fire;

    @BindView(R.id.crime)
    AppCompatImageView crime;


    @BindView(R.id.progress_bar3)
    ProgressBar progress_bar;

    @BindView(R.id.flood)
    AppCompatImageView flood;

    @BindView(R.id.progress3)
    RelativeLayout progress;

    @BindView(R.id.progress_text3)
    TextView progress_text;


    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;

    private String longitude, latitude, phonenumber, reporter;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    boolean boolean_permission;
    /**
     * Provides the entry point to the Fused Location Provider API.
     */
    //private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;
    private static final int REQUEST_CALL_PHONE = 878;
    private static final int REQUEST_READ_GPS = 565;
    private static final int REQUEST_READ_PHONE_STATE = 45;
    private TextView nam;
    private TextView namm;


    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;


    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private String InteriorLink = "http://interior.gov.ng";

    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;
    private AppUpdateManager mAppUpdateManager;
    private Activity thisActivity;
    private Timer timer;
    private int current_position = 0;
    private int Num = 3;
    ////testin the recycler slide
    public Handler handler = new Handler();
    final int duration = 6000;
    final int pixelsToMove = 1080;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    ViewPager viewPager;

    MyCustomPagerAdapter myCustomPagerAdapter;
    private boolean isCanceled;
    private boolean Gott;
    private int progressStatus = 0;

    public RAPE() {

    }
    private void CovidAlert() {
        startLocationUpdates();
        startLocation();


        Log.d(TAG, "this is lat and lon" + " " + latitude + " " + latitude);
        progress.setVisibility(View.VISIBLE);
        progress_text.setText("Rape Alert sending...");
        if (latitude == null) {
            progress.setVisibility(View.GONE);

            startLocation();
            return;
        }

        try {
            //Toast.makeText(this, "lat: " + latitude + " " + "lon: " + longitude, Toast.LENGTH_LONG).show();

            Service service = DataGenerator.createService(Service.class, "http://104.131.77.176/");
            Call<Void> call = service.medicalAlert(phonenumber, latitude, longitude);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        progress.setVisibility(View.GONE);
                        Toast toast = Toast.makeText(getApplicationContext(), "Rape Alert successfully sent", Toast.LENGTH_SHORT);
                    } else {
                        progress.setVisibility(View.GONE);
                        Toast toast = Toast.makeText(getApplicationContext(), "Rape Alert was not sent try again", Toast.LENGTH_SHORT);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    progress.setVisibility(View.GONE);
                    Toast toast = Toast.makeText(getApplicationContext(), "Rape Alert was not sent try again", Toast.LENGTH_SHORT);
                }
            });
        } catch (Exception e) {
            progress.setVisibility(View.GONE);
            Toast toast = Toast.makeText(getApplicationContext(), "Rape Alert was not sent try again", Toast.LENGTH_SHORT);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_a_p_e);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        phonenumber = PreferenceUtils.getPhoneNumber(getApplicationContext());
        reporter = PreferenceUtils.getPhoneNumber(getApplicationContext());
        progress = (RelativeLayout) findViewById(R.id.progress3);
        progress_text = (TextView) findViewById(R.id.progress_text3);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar3);

        ImageView rape_alert = findViewById(R.id.rape_alert);
        ImageView rape_alert2 = findViewById(R.id.rape_alert2);
        rape_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CovidAlert();
            }
        });
        rape_alert2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RAPEUPLOAD.class);
                startActivity(intent);
            }
        });





        mDb = AppDatabase.getInstance(getApplicationContext());

        startService(new Intent(getApplicationContext(), NispsasLockService.class));
        //fn_permission();
        init();

        // restore the values from saved instance state
        restoreValuesFromBundle(savedInstanceState);
        startLocation();

        AppUpdateManager mAppUpdateManager = AppUpdateManagerFactory.create(this);

        mAppUpdateManager.registerListener(installStateUpdatedListener);

        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)){

                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.FLEXIBLE, RAPE.this, RC_APP_UPDATE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }


            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED){
                popupSnackbarForCompleteUpdate();
            } else {
                Log.e(TAG, "checkForAppUpdateAvailability: something else");
            }
        });

    }

    InstallStateUpdatedListener installStateUpdatedListener = new
            InstallStateUpdatedListener() {
                @Override
                public void onStateUpdate(InstallState state) {
                    if (state.installStatus() == InstallStatus.DOWNLOADED){
                        popupSnackbarForCompleteUpdate();
                    } else if (state.installStatus() == InstallStatus.INSTALLED){
                        if (mAppUpdateManager != null){
                            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                        }

                    } else {
                        Log.i(TAG, "InstallStateUpdatedListener: state: " + state.installStatus());
                    }
                }
            };


    private void popupSnackbarForCompleteUpdate() {

        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.tvContent),
                        "New Update is ready!",
                        Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("Install", view -> {
            if (mAppUpdateManager != null){
                mAppUpdateManager.completeUpdate();
            }
        });


        snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }



    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();

                updateLocationUI();
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

    private void restoreValuesFromBundle(Bundle savedInstanceState) {
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

        updateLocationUI();
    }



    public void startLocation() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
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

    private void updateLocationUI() {
        if (mCurrentLocation != null) {

            latitude = mCurrentLocation.getLatitude() + "";
            longitude = mCurrentLocation.getLongitude() + "";
            PreferenceUtils.saveLocationLongitude(longitude, getApplicationContext());
            PreferenceUtils.saveLocationLatitude(latitude, getApplicationContext());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates);
        outState.putParcelable("last_known_location", mCurrentLocation);

    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, locationSettingsResponse -> {
                    Log.i(TAG, "All location settings are satisfied.");

                    //  Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                    //noinspection MissingPermission
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                            mLocationCallback, Looper.myLooper());

                    updateLocationUI();
                })
                .addOnFailureListener(this, e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                    "location settings ");
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                ResolvableApiException rae = (ResolvableApiException) e;
                                rae.startResolutionForResult(RAPE.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sie) {
                                Log.i(TAG, "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            String errorMessage = "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings.";
                            Log.e(TAG, errorMessage);

                            Toast.makeText(RAPE.this, errorMessage, Toast.LENGTH_LONG).show();
                    }

                    updateLocationUI();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    private void openSettings() {
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

        updateLocationUI();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }




    private void fetchArticle() {
        try{
            Service service = DataGenerator.createService(Service.class, "http://104.131.77.176/");
            Call<ArticleResponse> call = service.article("All");

            call.enqueue(new Callback<ArticleResponse>() {
                @Override
                public void onResponse(@NonNull Call<ArticleResponse> call, @NonNull Response<ArticleResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body()!= null) {
                            AppExecutors.getInstance().diskIO().execute(() -> mDb.nispsasDao().deleteAll());
                            List<Article> articleResponseList = response.body().getArticles();
                            if (articleResponseList != null) {
                                for (Article article : articleResponseList) {
                                    int id = article.getId();
                                    String articleid = article.getArticleid();
                                    String pic_name = article.getPicname();
                                    String post_title = article.getPostTitle();
                                    String post_notes = article.getPostNotes();
                                    String post_category = (String) article.getPostCategory();
                                    String article_type = article.getArticletype();
                                    String video_url = article.getVideourl();

                                    ArticleEntry articleEntry = new ArticleEntry(id, articleid, pic_name, post_title, post_notes, post_category, article_type, video_url);
                                    AppExecutors.getInstance().diskIO().execute(() ->mDb.nispsasDao().insertArticle(articleEntry));
                                }
                            }
                        }
                    } else {

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArticleResponse> call, @NonNull Throwable t) {

                }
            });
        } catch (Exception e) {

        }
    }

    private void launchMedia() {
        Intent intent = new Intent(this, CovidActivity.class);
        startActivity(intent);
    }

    private void launchChat() {
        Intent intent = new Intent(this, HowToActivity.class);
        startActivity(intent);
    }
}
