package com.a2tocsolutions.nispsasapp;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.a2tocsolutions.nispsasapp.Fragments.AlertFrag;
import com.a2tocsolutions.nispsasapp.adapter.ArticleslideAdapter;
import com.a2tocsolutions.nispsasapp.database.AppDatabase;
import com.a2tocsolutions.nispsasapp.database.ArticleEntry;
import com.a2tocsolutions.nispsasapp.model.Article;
import com.a2tocsolutions.nispsasapp.model.ArticleResponse;
import com.a2tocsolutions.nispsasapp.networking.api.Service;
import com.a2tocsolutions.nispsasapp.networking.generator.DataGenerator;
import com.a2tocsolutions.nispsasapp.service.NispsasLockService;
import com.a2tocsolutions.nispsasapp.utils.AppExecutors;
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
import com.google.android.material.snackbar.Snackbar;
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

public class Gbv_form_Activity extends AppCompatActivity{

    private static final String TAG = "GBV";
    private static final int RC_APP_UPDATE = 0;
    AlertFrag mActivity = new AlertFrag();
    private static int i = 0;
    private AppDatabase mDb;
    //private ArticleAdapter articleAdapter;
    private ArticleslideAdapter articleslideAdapter;
    private static final int REQUEST_PERMISSIONS = 100;
    private Spinner gbv_rep_type_option;
    private Spinner gbv_rep_type_option_physical_selected;
    private Spinner gbv_rep_type_option_sex_selected;
    private Spinner gbv_rep_type_option_emotion_selected;
    private Spinner gbv_rep_type_option_socio_selected;
    private Spinner gbv_rep_type_option_harmful_tradition_selected;
    private String swi;


    @BindView(R.id.menu_image)
    AppCompatImageView menu_image;


    @BindView(R.id.crime)
    AppCompatImageView crime;




    @BindView(R.id.progress_bar2)
    ProgressBar progress_bar;

    @BindView(R.id.flood)
    AppCompatImageView flood;

    @BindView(R.id.progress2)
    RelativeLayout progress;

    @BindView(R.id.progress_text2)
    TextView progress_text;


    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;

    private String longitude, latitude, phonenumber, reporter, repname, repstate, replga;
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
    private boolean isCanceled;
    private boolean Gott;
    private int progressStatus = 0;
    private View SwitchButton;

    public Gbv_form_Activity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gbv_form_activity);
        String user_image = (PreferenceUtils.getUserImage(getApplicationContext()));
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        phonenumber = PreferenceUtils.getPhoneNumber(getApplicationContext());
        reporter = PreferenceUtils.getPhoneNumber(getApplicationContext());
        repname = PreferenceUtils.getUsername(getApplicationContext());
        repstate = PreferenceUtils.getState(getApplicationContext());
        replga = PreferenceUtils.getLga(getApplicationContext());
        gbv_rep_type_option = (Spinner) findViewById(R.id.rep_type);
        gbv_rep_type_option.setOnItemSelectedListener(new Gbv_form_Activity.ItemSelectedListener());

        gbv_rep_type_option_physical_selected = (Spinner) findViewById(R.id.option_physical);
        gbv_rep_type_option_physical_selected.setOnItemSelectedListener(new Gbv_form_Activity.ItemRepSelectedListener());

        gbv_rep_type_option_sex_selected = (Spinner) findViewById(R.id.option_sexual);
        gbv_rep_type_option_sex_selected.setOnItemSelectedListener(new Gbv_form_Activity.ItemRepSelectedListener());

        gbv_rep_type_option_emotion_selected = (Spinner) findViewById(R.id.option_emotional);
        gbv_rep_type_option_emotion_selected.setOnItemSelectedListener(new Gbv_form_Activity.ItemRepSelectedListener());

        gbv_rep_type_option_socio_selected = (Spinner) findViewById(R.id.option_socio);
        gbv_rep_type_option_socio_selected.setOnItemSelectedListener(new Gbv_form_Activity.ItemRepSelectedListener());

        gbv_rep_type_option_harmful_tradition_selected = (Spinner) findViewById(R.id.option_harmful);
        gbv_rep_type_option_harmful_tradition_selected.setOnItemSelectedListener(new Gbv_form_Activity.ItemRepSelectedListener());

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
                            appUpdateInfo, AppUpdateType.FLEXIBLE, Gbv_form_Activity.this, RC_APP_UPDATE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }


            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED){
                popupSnackbarForCompleteUpdate();
            } else {
                Log.e(TAG, "checkForAppUpdateAvailability: something else");
            }
        });
        Button submit = findViewById(R.id.button_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floodAlert();
            }
        });

        TextView close = findViewById(R.id.close_form);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void floodAlert() {

        String type="GbvForm";
        startLocationUpdates();
        startLocation();

        if (latitude == null) {

            startLocation();
            return;
        }
        try{
            //Toast.makeText(this, "lat: " + latitude + " " + "lon: " + longitude, Toast.LENGTH_LONG).show();

            Service service = DataGenerator.createService(Service.class, "http://104.131.77.176/");
            String mreptype = gbv_rep_type_option.getSelectedItem().toString();
            String physicaltype = gbv_rep_type_option_physical_selected.getSelectedItem().toString();
            String sextype = gbv_rep_type_option_sex_selected.getSelectedItem().toString();
            String emotiontype = gbv_rep_type_option_emotion_selected.getSelectedItem().toString();
            String sociotype = gbv_rep_type_option_socio_selected.getSelectedItem().toString();
            String harmfultype = gbv_rep_type_option_harmful_tradition_selected.getSelectedItem().toString();
            EditText input_witness = findViewById(R.id.witness);
            String witness = input_witness.getText().toString().trim();
            EditText input_victname = findViewById(R.id.victim);
            String victim = input_victname.getText().toString().trim();
            EditText input_victimadd = findViewById(R.id.add_of_victim);
            String address = input_victimadd.getText().toString().trim();
            EditText input_victimphoe = findViewById(R.id.victim_phone);
            String victphone = input_victimphoe.getText().toString().trim();
            EditText input_gencomment = findViewById(R.id.gen_comment);
            String comment = input_gencomment.getText().toString().trim();
            Call<Void> call = service.SubmitGbvForm(reporter, latitude, longitude, repname, repstate, replga, mreptype, comment, type, physicaltype, sextype, emotiontype, sociotype, harmfultype, witness, victim, address, victphone);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        FancyToast.makeText(getApplicationContext(), "Your Short Video Post was successfull", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        //emptyInputEditText();
                    } else {

                        FancyToast.makeText(getApplicationContext(), "Short Video Post Failed", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    FancyToast.makeText(getApplicationContext(), "No Internet Connection", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
            });
        } catch (Exception e) {
            FancyToast.makeText(getApplicationContext(), "Short Video Post Failed", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

        }
    }

    private void requestPermissions(List<String> missingPermissions, int code) {
        mInPermissionRequest = true;
        String[] permissions = missingPermissions.toArray(new String[missingPermissions.size()]);
        try {
            getClass().getMethod("requestPermissions", String[].class, Integer.TYPE).invoke(this, permissions, code);
        } catch (Exception ignored) {}
    }

    public class ItemSelectedListener implements AdapterView.OnItemSelectedListener{
        //get first string in the array
        String firstItem = String.valueOf(gbv_rep_type_option.getSelectedItem());
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            firstItem.equals(String.valueOf(gbv_rep_type_option.getSelectedItem()));
            if (gbv_rep_type_option.getSelectedItem().toString().equals("Physical")){
                gbv_rep_type_option_physical_selected.setVisibility(View.VISIBLE);
            }
            if (gbv_rep_type_option.getSelectedItem().toString().equals("Sexual")){
                gbv_rep_type_option_sex_selected.setVisibility(View.VISIBLE);
            }
            if (gbv_rep_type_option.getSelectedItem().toString().equals("Emotional and psychological")){
                gbv_rep_type_option_emotion_selected.setVisibility(View.VISIBLE);
            }
            if (gbv_rep_type_option.getSelectedItem().toString().equals("Socio-economic")){
                gbv_rep_type_option_socio_selected.setVisibility(View.VISIBLE);
            }
            if (gbv_rep_type_option.getSelectedItem().toString().equals("Harmful traditional practices")){
                gbv_rep_type_option_harmful_tradition_selected.setVisibility(View.VISIBLE);
            }

        }
        public void onNothingSelected(AdapterView<?> arg){

        }

    }

    public class ItemRepSelectedListener implements AdapterView.OnItemSelectedListener{
        //get first string in the array
        String firstItem = String.valueOf(gbv_rep_type_option.getSelectedItem());
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {


        }
        public void onNothingSelected(AdapterView<?> arg){

        }

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
                .withPermission(permission.ACCESS_FINE_LOCATION)
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
                                rae.startResolutionForResult(Gbv_form_Activity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sie) {
                                Log.i(TAG, "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            String errorMessage = "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings.";
                            Log.e(TAG, errorMessage);

                            Toast.makeText(Gbv_form_Activity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }

                    updateLocationUI();
                });
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


    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                permission.ACCESS_FINE_LOCATION);
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

    @Override
    public void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        }

        updateLocationUI();
    }

    private boolean hasPermission(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean mInPermissionRequest = false;
    private boolean mUploading = false;
    private AlertDialog mUploadDialog;
    private long mLastUploadStatusUpdateTime = 0;

    private void emptyInputEditText() {
        EditText input_post_comment = findViewById(R.id.say_comment);
        input_post_comment.setText("");
        refreshActivity();
    }

    private void refreshActivity(){
        recreate();
    }

}
