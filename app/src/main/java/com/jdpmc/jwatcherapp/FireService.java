package com.jdpmc.jwatcherapp;

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

import com.jdpmc.jwatcherapp.Fragments.AlertFrag;
import com.jdpmc.jwatcherapp.activities.CovidActivity;
import com.jdpmc.jwatcherapp.activities.HowToActivity;
import com.jdpmc.jwatcherapp.activities.MyCustomPagerAdapter;
import com.jdpmc.jwatcherapp.activities.VerifyExtinguisher;
import com.jdpmc.jwatcherapp.adapter.ArticleslideAdapter;
import com.jdpmc.jwatcherapp.adapter.EmergencyAdapter;
import com.jdpmc.jwatcherapp.database.AppDatabase;
import com.jdpmc.jwatcherapp.database.ArticleEntry;
import com.jdpmc.jwatcherapp.model.Article;
import com.jdpmc.jwatcherapp.model.ArticleResponse;
import com.jdpmc.jwatcherapp.networking.api.Service;
import com.jdpmc.jwatcherapp.networking.generator.DataGenerator;
import com.jdpmc.jwatcherapp.service.NispsasLockService;
import com.jdpmc.jwatcherapp.utils.AppExecutors;
import com.jdpmc.jwatcherapp.utils.PreferenceUtils;
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

public class FireService extends AppCompatActivity implements EmergencyAdapter.ItemClickListener{
    private static final String TAG = "FireService";
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



    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;

    @BindView(R.id.flood)
    AppCompatImageView flood;

    @BindView(R.id.progress)
    RelativeLayout progress;

    @BindView(R.id.progress_text)
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

    public FireService() {

    }

    private void fireAlert() {
        startLocationUpdates();
        startLocation();
        Log.d(TAG, "this is lat and lon" + " " + latitude + " " + latitude);
        progress.setVisibility(View.VISIBLE);
        progress_text.setText("Fire Alert sending...");
        if (latitude == null) {
            progress.setVisibility(View.GONE);

            startLocation();
            return;
        }

        try {
            //Toast.makeText(this, "lat: " + latitude + " " + "lon: " + longitude, Toast.LENGTH_LONG).show();

            Service service = DataGenerator.createService(Service.class, "http://104.131.77.176/");
            Call<Void> call = service.fireAlert(phonenumber, latitude, longitude);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Fire Alert successfully sent", Toast.LENGTH_SHORT).show();
                    } else {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Fire Alert was not sent try again", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Fire Alert was not sent try again", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            progress.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Fire Alert was not sent try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClickListener(int itemId) {
        Gott = false;
        isCanceled = false;
        ProgressDialog myDialog = new ProgressDialog(mActivity.getContext());
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

                            if (itemId == 1) {
                                Gott = false;
                                if (progressStatus == myDialog.getMax()) {
                                    myDialog.dismiss();
                                    Gott = true;
                                }
                                if (Gott == true) {
                                    fireAlert();
                                }
                            }


                        }
                    });
                }
            }
        }).start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_service);
        setTitle("Fire Service");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        phonenumber = PreferenceUtils.getPhoneNumber(getApplicationContext());
        reporter = PreferenceUtils.getPhoneNumber(getApplicationContext());
        progress = (RelativeLayout) findViewById(R.id.progress);
        progress_text = (TextView) findViewById(R.id.progress_text);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);



        ImageView fire = findViewById(R.id.fire_alert);
        ImageView buy = findViewById(R.id.buy_extinquisher);
        ImageView verify = findViewById(R.id.verify_extinquisher);

        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(getApplicationContext(), FireVideo.class);
                startActivity(Intent);
            }
        });
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://206.189.174.195"));
                startActivity(browserIntent);
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(getApplicationContext(), VerifyExtinguisher.class);
                startActivity(Intent);
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
                            appUpdateInfo, AppUpdateType.FLEXIBLE, FireService.this, RC_APP_UPDATE);
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
                                rae.startResolutionForResult(FireService.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sie) {
                                Log.i(TAG, "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            String errorMessage = "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings.";
                            Log.e(TAG, errorMessage);

                            Toast.makeText(FireService.this, errorMessage, Toast.LENGTH_LONG).show();
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
