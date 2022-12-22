package com.jdpmc.jwatcherapp.activities;

import static com.jdpmc.jwatcherapp.utils.Constants.BASE_URL;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
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

import com.jdpmc.jwatcherapp.Activity_home_backup;
import com.jdpmc.jwatcherapp.DisasterPage;
import com.jdpmc.jwatcherapp.Fragments.AlertFrag;
import com.jdpmc.jwatcherapp.NEMA;
import com.jdpmc.jwatcherapp.R;
import com.jdpmc.jwatcherapp.adapter.ArticleslideAdapter;
import com.jdpmc.jwatcherapp.database.AppDatabase;
import com.jdpmc.jwatcherapp.database.ArticleEntry;
import com.jdpmc.jwatcherapp.model.Article;
import com.jdpmc.jwatcherapp.model.ArticleResponse;
import com.jdpmc.jwatcherapp.networking.api.Service;
import com.jdpmc.jwatcherapp.networking.generator.DataGenerator;
import com.jdpmc.jwatcherapp.pg;
import com.jdpmc.jwatcherapp.service.NispsasLockService;
import com.jdpmc.jwatcherapp.utils.AppExecutors;
import com.jdpmc.jwatcherapp.utils.PreferenceUtils;
import com.afollestad.materialdialogs.BuildConfig;
import com.bambuser.broadcaster.BroadcastStatus;
import com.bambuser.broadcaster.Broadcaster;
import com.bambuser.broadcaster.CameraError;
import com.bambuser.broadcaster.ConnectionError;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityBackup extends AppCompatActivity {

    private static final String TAG = "MainActivity";
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


    @BindView(R.id.prog)
    ProgressBar progress_bar;

    @BindView(R.id.flood)
    AppCompatImageView flood;

    @BindView(R.id.s)
    RelativeLayout progress;

    @BindView(R.id.prog_text)
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
    private static final String LOGTAG = "Mybroadcastingapp";

    private static final String APPLICATION_ID = "mOQq8sbExROCWxFjbkGoaA";




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
    int[] image1 = {R.drawable.eye, R.drawable.algon};
    int[] image2 = {R.drawable.verify_driver, R.drawable.report};
    int[] image3 = {R.drawable.bank_fraud, R.drawable.verifyvp};
    String id = UUID.randomUUID().toString();
    private void distressCall() {


        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:112"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }

    public MainActivityBackup() {

    }


    private void crimeAlert() {
        startLocationUpdates();
        startLocation();
        String type = "Crime";

        progress.setVisibility(View.VISIBLE);
        progress_text.setText("Crime Alert sending...");
        if (latitude == null) {
            progress.setVisibility(View.GONE);
            Toast toast = Toast.makeText(getApplicationContext(), R.string.no_location_detected, Toast.LENGTH_SHORT);
            startLocation();
            return;
        }
        try {
            //Toast.makeText(this, "lat: " + lat + " " + "lon: " + lon, Toast.LENGTH_LONG).show();
            startLocationUpdates();
            Service service = DataGenerator.createService(Service.class, "http://104.131.77.176/");
            Call<Void> call = service.crimeAlert(phonenumber, latitude, longitude, type, id);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Crime Alert successfully sent", Toast.LENGTH_SHORT).show();

                        Toast.makeText(getApplicationContext(), "Virtual Incidence Report Successfully Initiated", Toast.LENGTH_SHORT).show();
                    } else {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Crime Alert was not sent try again", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    progress.setVisibility(View.GONE);
                   Toast.makeText(getApplicationContext(), "Crime Alert was not sent try again", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            progress.setVisibility(View.GONE);
           Toast.makeText(getApplicationContext(), "Crime Alert was not sent try again", Toast.LENGTH_SHORT).show();
        }
    }





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





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActivityCompat.requestPermissions(MainActivityBackup.this,
                new String[] {Manifest.permission.CALL_PHONE,Manifest.permission.RECORD_AUDIO,Manifest.permission.CAMERA,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CAPTURE_AUDIO_OUTPUT,Manifest.permission.MODIFY_AUDIO_SETTINGS}, 1);

        setContentView(R.layout.activity_main_bk);

        mute();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //what you want to do
                final MediaPlayer mp =  MediaPlayer.create(getApplicationContext(), R.raw.tts);
                mp.start();
            }
        }, 0, 7200000);//wait 0 ms before doing the action and do it evry 1000ms (1second)

        timer.cancel();
        int requestCode = 0;

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, requestCode);
        viewPager = (ViewPager) findViewById(R.id.sndviewpager);

        ImageAdapter viewPagerAdapter = new ImageAdapter(this);
        mBroadcaster = new Broadcaster(this, APPLICATION_ID, mBroadcasterObserver);
        mBroadcaster.setRotation(getWindowManager().getDefaultDisplay().getRotation());



        mPreviewSurface = findViewById(R.id.PreviewSurfaceView);


        viewPager.setAdapter(viewPagerAdapter);

        ButterKnife.bind(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        phonenumber = PreferenceUtils.getPhoneNumber(getApplicationContext());
        reporter = PreferenceUtils.getPhoneNumber(getApplicationContext());

        ImageView seesay = findViewById(R.id.seesay);
        ImageView complaint = findViewById(R.id.complain);
        nam = (TextView) findViewById(R.id.name);
        nam.setSelected(true);
        namm = (TextView) findViewById(R.id.namn);
        namm.setSelected(true);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        mPreviewSurface = findViewById(R.id.PreviewSurfaceView);
        mBroadcaster = new Broadcaster(this, APPLICATION_ID, mBroadcasterObserver);
        mBroadcaster.setRotation(getWindowManager().getDefaultDisplay().getRotation());
        mBroadcaster.setTitle("NISPSAS-VIR");
        mBroadcaster.setAuthor(id);
        mBroadcaster.setSendPosition(true);

        seesay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(getApplicationContext(), com.jdpmc.jwatcherapp.seesay.class);
                startActivity(Intent);
            }
        });
        complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(getApplicationContext(), pg.class);
                startActivity(Intent);
            }
        });


        crime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(getApplicationContext(), DisasterPage.class);
                startActivity(Intent);

            }


        });


        flood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NEMA.class);
                startActivity(intent);
            }
        });

        /*menu_image.setOnClickListener(v -> {
            Intent intent = new Intent(this, MoreActivity.class);
            startActivity(intent);
        });*/

        menu_image.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_home_backup.class);
            startActivity(intent);
            finish();
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
                            appUpdateInfo, AppUpdateType.FLEXIBLE, MainActivityBackup.this, RC_APP_UPDATE);
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


    private void mute() {
        //mute audio
        AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
    }

    public void unmute() {
        //unmute audio
        AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
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
                                rae.startResolutionForResult(MainActivityBackup.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sie) {
                                Log.i(TAG, "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            String errorMessage = "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings.";
                            Log.e(TAG, errorMessage);

                            Toast.makeText(MainActivityBackup.this, errorMessage, Toast.LENGTH_LONG).show();
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
    public void onDestroy() {
        super.onDestroy();
        mBroadcaster.onActivityDestroy();
    }
    @Override
    public void onPause() {
        super.onPause();
        mBroadcaster.onActivityPause();
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
        if (!hasPermission(Manifest.permission.CAMERA)
                && !hasPermission(Manifest.permission.RECORD_AUDIO))
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO}, 1);
        else if (!hasPermission(Manifest.permission.RECORD_AUDIO))
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, 1);
        else if (!hasPermission(Manifest.permission.CAMERA))
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 1);
        mBroadcaster.setCameraSurface(mPreviewSurface);
        mBroadcaster.onActivityResume();
        mBroadcaster.setRotation(getWindowManager().getDefaultDisplay().getRotation());
    }


    private boolean hasPermission(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }




    private void fetchArticle() {
        try{
            Service service = DataGenerator.createService(Service.class, BASE_URL);
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
                                    String userimage = article.getUserimage();
                                    String username = article.getUsername();
                                    String postcomment = article.getPostcomment();
                                    String postsate = article.getPoststate();
                                    String postsatus = article.getPoststatus();

                                    String posttype = article.getPosttype();
                                    String postarea = article.getPostarea();
                                    String likscount = article.getLikscount();
                                    String commentcount = article.getCommentcount();
                                    String preview = article.getPreview();
                                    String vidrscurl = article.getVidrscurl();
                                    String postdate = article.getPostdate();

                                    ArticleEntry articleEntry = new ArticleEntry(id, userimage, username, postcomment, postsate, postsatus, posttype,
                                            postarea, likscount, commentcount, preview, vidrscurl, postdate);
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

    public class MyTimerTask extends TimerTask{

        @Override
        public void run(){
            MainActivityBackup.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(viewPager.getCurrentItem() == 1){
                        viewPager.setCurrentItem(0);
                    } else if(viewPager.getCurrentItem() == 0){
                        viewPager.setCurrentItem(0);
                    }else if(viewPager.getCurrentItem() == 2){
                        viewPager.setCurrentItem(0);
                    }

                }
            });
        }

    }
    SurfaceView mPreviewSurface;
    Broadcaster mBroadcaster;
    private Broadcaster.Observer mBroadcasterObserver = new Broadcaster.Observer() {
        @Override
        public void onConnectionStatusChange(BroadcastStatus broadcastStatus) {
            Log.i(LOGTAG, "Received status change: " + broadcastStatus);
            if (broadcastStatus == BroadcastStatus.STARTING)
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            if (broadcastStatus == BroadcastStatus.IDLE)
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        @Override
        public void onStreamHealthUpdate(int i) {
        }
        @Override
        public void onConnectionError(ConnectionError connectionError, String s) {
            Log.w(LOGTAG, "Received connection error: " + connectionError + ", " + s);
        }
        @Override
        public void onCameraError(CameraError cameraError) {
            Log.w(LOGTAG, "Received camera error: " + cameraError);
        }
        @Override
        public void onChatMessage(String s) {
        }
        @Override
        public void onResolutionsScanned() {
        }
        @Override
        public void onCameraPreviewStateChanged() {
        }
        @Override
        public void onBroadcastInfoAvailable(String s, String s1) {
        }
        @Override
        public void onBroadcastIdAvailable(String s) {
        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            key = keyCode;
            keyEvent = KeyEvent.KEYCODE_VOLUME_UP;
            new CountDownTimer(5000, 1000) {

                public void onTick(long millisUntilFinished) {
                    if (key == keyEvent) {
                        count++;
                    } else {
                        count = 0;
                    }
                }

                public void onFinish() {
                    if (count == 5) {
                        crimeAlert();
                        if (mBroadcaster.canStartBroadcasting())
                            mBroadcaster.startBroadcast();
                        else
                            mBroadcaster.stopBroadcast();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mBroadcaster.stopBroadcast();
                            }
                        }, 60000);
                    }
                    count = 0;
                }
            }.start();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    int count = 0;
    int key, keyEvent;

}
