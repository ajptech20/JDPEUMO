package com.a2tocsolutions.nispsasapp;

import static android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;

import android.Manifest;
import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import com.a2tocsolutions.nispsasapp.activities.CovidActivity;
import com.a2tocsolutions.nispsasapp.activities.HowToActivity;
import com.a2tocsolutions.nispsasapp.activities.MyCustomPagerAdapter;
//import com.a2tocsolutions.nispsasapp.adapter.ArticleAdapter;
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
import com.bambuser.broadcaster.BroadcastStatus;
import com.bambuser.broadcaster.Broadcaster;
import com.bambuser.broadcaster.CameraError;
import com.bambuser.broadcaster.ConnectionError;
import com.bambuser.broadcaster.Resolution;
import com.bumptech.glide.Glide;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class picture_uploader extends AppCompatActivity implements UploadHelper.ProgressCallback {
    private static final String LOGTAG = "Mybroadcastingapp";
    private static final int FILE_CHOOSER_CODE = 1;
    private static final int START_PERMISSIONS_CODE = 2;
    private static final int PHOTO_PERMISSIONS_CODE = 4;
    private static final int TALKBACK_DIALOG = 1;
    private static final String TALKBACK_DIALOG_CALLER = "caller";
    private static final String TALKBACK_DIALOG_REQUEST = "request";
    private static final String TALKBACK_DIALOG_SESSION_ID = "session_id";
    private static final int UPLOAD_PROGRESS_DIALOG = 2;
    private static final String STATE_IN_PERMISSION_REQUEST = "in_permission_request";
    private static final String APPLICATION_ID = "mOQq8sbExROCWxFjbkGoaA";

    private static final String TAG = "NEMA";
    private static final int RC_APP_UPDATE = 0;
    AlertFrag mActivity = new AlertFrag();
    private static int i = 0;
    private AppDatabase mDb;
    //private ArticleAdapter articleAdapter;
    private ArticleslideAdapter articleslideAdapter;
    private static final int REQUEST_PERMISSIONS = 100;
    private TabLayout tabLayout;
    private Spinner live_stream_types;
    private String swi;


    @BindView(R.id.menu_image)
    AppCompatImageView menu_image;

    @BindView(R.id.NEMAVIDSurfaceView)
    AppCompatImageView image_preview;

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
    private View SwitchButton;

    public picture_uploader() {

    }
    private void floodAlert() {

        String type="Flood";
        startLocationUpdates();
        startLocation();

        if (latitude == null) {

            startLocation();
            return;
        }
        try{
            //Toast.makeText(this, "lat: " + latitude + " " + "lon: " + longitude, Toast.LENGTH_LONG).show();

            Service service = DataGenerator.createService(Service.class, "http://104.131.77.176/");
            Call<Void> call = service.floodAlert(reporter, latitude, longitude, type);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {

                        Toast.makeText(getApplicationContext(), "Flood Alert Sent Successfully",
                                Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(getApplicationContext(), "Flood Alert Failed",
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

                    Toast.makeText(getApplicationContext(), "Flood Alert Failed",
                            Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Flood Alert Failed",
                    Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_post);
        String user_image = (PreferenceUtils.getUserImage(getApplicationContext()));
        ImageView imageView = (ImageView) findViewById(R.id.user_image);
        Glide.with(picture_uploader.this)
                .load(user_image)
                .into(imageView);
        TextView username = findViewById(R.id.user_name);
        username.setText(PreferenceUtils.getUsername(getApplicationContext()));
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        phonenumber = PreferenceUtils.getPhoneNumber(getApplicationContext());
        reporter = PreferenceUtils.getPhoneNumber(getApplicationContext());
        mPreviewSurface = findViewById(R.id.image_upload_page);
        mBroadcaster = new Broadcaster(this, APPLICATION_ID, mBroadcasterObserver);
        mBroadcaster.setRotation(getWindowManager().getDefaultDisplay().getRotation());
        mBroadcaster.setTitle("FLOOD-VIR");
        mBroadcaster.setAuthor("NISPSAS-VIR");
        mBroadcaster.setSendPosition(true);
        //mBroadcastButton = findViewById(R.id.NEMAVIDButton);
        mBroadcastButton2 = findViewById(R.id.mich_on);
        mBroadcastsatus = findViewById(R.id.status_live);
        mBroadcastButton3 = findViewById(R.id.video_on);
        mBroadcastclose = findViewById(R.id.stop_close_live3);
        mBroadcastcamchange = findViewById(R.id.change_camera);
        SwitchButton = findViewById(R.id.SwitchCameraButton);
        mPhotoButton = findViewById(R.id.PhotoButton);
        mChosePhotoButton = findViewById(R.id.select_from_gala);
        /*mBroadcastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int viewId = v.getId();
                if (mBroadcaster.canStartBroadcasting()){
                    mBroadcaster.startBroadcast();
                //floodAlert();
                }else{
                    mBroadcaster.stopBroadcast();
                } if (viewId == R.id.SwitchCameraButton) {
                    mBroadcaster.switchCamera();
                    Log.e(TAG, "Button Cliked: something else");
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBroadcaster.stopBroadcast();
                    }
                }, 60000);
            }
        });*/
        live_stream_types = (Spinner) findViewById(R.id.live_type);
        live_stream_types.setOnItemSelectedListener(new picture_uploader.ItemSelectedListener());

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
                            appUpdateInfo, AppUpdateType.FLEXIBLE, picture_uploader.this, RC_APP_UPDATE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }


            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED){
                popupSnackbarForCompleteUpdate();
            } else {
                Log.e(TAG, "checkForAppUpdateAvailability: something else");
            }
        });
        ImageView switchcam = findViewById(R.id.SwitchCameraButton);
        switchcam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBroadcaster.switchCamera();
                Log.e(TAG, "Button Cliked: something else");
            }
        });

        ImageView take_picture = findViewById(R.id.PhotoButton);
        take_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
                //Log.e(TAG, "Button Cliked: something else");
            }
        });

        ImageView select_picture = findViewById(R.id.select_from_gala);
        select_picture.setOnClickListener(new View.OnClickListener() {
            private int result;
            private int code;

            @Override
            public void onClick(View v) {
                chooseFile();
                //Log.e(TAG, "Button Cliked: Select Image");
            }
        });

        ImageView close = findViewById(R.id.stop_close_live3);
        close.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(picture_uploader.this, Activity_home.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
            }
        });

        TextView ImageUp = findViewById(R.id.live_activity);
        ImageUp.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(picture_uploader.this, Go_New_live.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                finish();
            }
        });

        TextView shorts = findViewById(R.id.shorts_up);
        shorts.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(picture_uploader.this, shortvideo_uploader.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                finish();
            }
        });

    }

    private void requestPermissions(List<String> missingPermissions, int code) {
        mInPermissionRequest = true;
        String[] permissions = missingPermissions.toArray(new String[missingPermissions.size()]);
        try {
            getClass().getMethod("requestPermissions", String[].class, Integer.TYPE).invoke(this, permissions, code);
        } catch (Exception ignored) {}
    }


    private void takePhoto() {
        List<String> missingPermissions = new ArrayList<>();
        if (!hasPermission(permission.CAMERA))
            missingPermissions.add(permission.CAMERA);
        if (!hasPermission(relevantStoragePermission()))
            missingPermissions.add(relevantStoragePermission());
        if (missingPermissions.size() > 0) {
            requestPermissions(missingPermissions, PHOTO_PERMISSIONS_CODE);
            return;
        }
        List<Resolution> resolutions = mBroadcaster.getSupportedPictureResolutions();
        if (resolutions.isEmpty())
            return;
        Resolution maxRes = resolutions.get(resolutions.size()-1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSS", Locale.US);
        String fileName = sdf.format(new Date()) + ".jpg";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            // Use classic File objects and paths on old Android versions
            File storageDir = getStorageDir();
            if (storageDir == null) {
                Toast.makeText(getApplicationContext(), "Can't store picture, external storage unavailable", Toast.LENGTH_LONG).show();
                return;
            }
            File file = new File(storageDir, fileName);
            Broadcaster.PictureObserver observer = new Broadcaster.PictureObserver() {
                @Override
                public void onPictureStored(boolean success) {
                    if (success) {
                        Toast.makeText(getApplicationContext(), "Stored " + file.getName(), Toast.LENGTH_SHORT).show();
                        MediaScannerConnection.scanFile(getApplicationContext(), new String[]{file.getAbsolutePath()}, null, null);
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to store " + file.getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            };
            mBroadcaster.takePicture(file, maxRes, observer);
        } else {
            // Create an image placeholder in shared storage using MediaStore on modern Android versions.
            // mark the image placeholder as pending while the SDK is writing to it
            ContentResolver cr = getApplicationContext().getContentResolver();
            ContentValues cv = new ContentValues();
            cv.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            cv.put(MediaStore.MediaColumns.IS_PENDING, 1);
            final Uri imageUri = cr.insert(MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY), cv);
            ParcelFileDescriptor imageFD;
            try {
                imageFD = cr.openFileDescriptor(imageUri, "rwt");
            }  catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to create image file", Toast.LENGTH_SHORT).show();
                cr.delete(imageUri, null, null);
                return;
            }
            File storageDir = getStorageDir();
            File file = new File(storageDir, fileName);
            Broadcaster.PictureObserver observer = new Broadcaster.PictureObserver() {
                @Override
                public void onPictureStored(boolean success) {
                    if (success) {
                        // mark the image as done so it appears in the MediaStore
                        ContentValues cv = new ContentValues();
                        cv.put(MediaStore.MediaColumns.IS_PENDING, 0);
                        cr.update(imageUri, cv, null, null);
                        //Toast.makeText(getApplicationContext(), "Stored " + imageUri, Toast.LENGTH_SHORT).show();
                    } else {
                        // delete the image placeholder from MediaStore
                        cr.delete(imageUri, null, null);
                        //Toast.makeText(getApplicationContext(), "Failed to store " + imageUri, Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getApplicationContext(), "Image Photo Stored Select Photo to Upload" + file.getName(), Toast.LENGTH_SHORT).show();
                    MediaScannerConnection.scanFile(getApplicationContext(), new String[]{file.getAbsolutePath()}, null, null);
                    chooseFile();
                }
            };
            mBroadcaster.takePicture(imageFD, maxRes, observer);
        }
    }

    private void initLocalRecording() {
        if (mBroadcaster == null || !mBroadcaster.hasLocalMediaCapability())
            return;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);
        String fileName = sdf.format(new Date()) + ".mp4";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            File storageDir = getStorageDir();
            if (storageDir == null) {
                Toast.makeText(getApplicationContext(), "Can't store local copy, external storage unavailable", Toast.LENGTH_SHORT).show();
                return;
            }
            File file = new File(storageDir, fileName);
            Broadcaster.LocalMediaObserver obs = new Broadcaster.LocalMediaObserver() {
                public void onLocalMediaClosed(boolean success) {
                    if (success) {
                        Toast.makeText(getApplicationContext(), "Local copy of broadcast stored", Toast.LENGTH_SHORT).show();
                        MediaScannerConnection.scanFile(getApplicationContext(), new String[]{file.getAbsolutePath()}, null, null);
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to write to video file. Storage/memory full?", Toast.LENGTH_LONG).show();
                    }
                }
            };
            boolean success = mBroadcaster.storeLocalMedia(file, obs);
            Toast.makeText(getApplicationContext(), "Writing to " + file.getAbsolutePath() + (success ? "" : " failed"), Toast.LENGTH_SHORT).show();
        } else {
            // Create a video placeholder in shared storage using MediaStore on modern Android versions.
            // mark the video placeholder as pending while the SDK is writing to it
            ContentResolver cr = getApplicationContext().getContentResolver();
            ContentValues cv = new ContentValues();
            cv.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            cv.put(MediaStore.MediaColumns.IS_PENDING, 1);
            final Uri videoUri = cr.insert(MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY), cv);
            ParcelFileDescriptor videoFD;
            try {
                videoFD = cr.openFileDescriptor(videoUri, "rwt");
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to create local video file", Toast.LENGTH_SHORT).show();
                cr.delete(videoUri, null, null);
                return;
            }
            Broadcaster.LocalMediaObserver obs = new Broadcaster.LocalMediaObserver() {
                public void onLocalMediaClosed(boolean success) {
                    if (success) {
                        // mark the video as done so it appears in the MediaStore
                        ContentValues cv = new ContentValues();
                        cv.put(MediaStore.MediaColumns.IS_PENDING, 0);
                        cr.update(videoUri, cv, null, null);
                        Toast.makeText(getApplicationContext(), "Local copy of broadcast stored", Toast.LENGTH_SHORT).show();
                    } else {
                        // delete the image placeholder from MediaStore
                        cr.delete(videoUri, null, null);
                        Toast.makeText(getApplicationContext(), "Failed to store " + videoUri + " Storage full?", Toast.LENGTH_SHORT).show();
                    }
                }
            };
            boolean success = mBroadcaster.storeLocalMedia(videoFD, obs);
            if (!success) {
                // delete the image placeholder from MediaStore
                cr.delete(videoUri, null, null);
                Toast.makeText(getApplicationContext(), "Writing to " + videoUri + " failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File getStorageDir() {
        if (hasPermission(permission.WRITE_EXTERNAL_STORAGE) && Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            final File externalDir = new File(Environment.getExternalStorageDirectory(), "LibBambuser");
            externalDir.mkdirs();
            if (externalDir.exists() && externalDir.canWrite())
                return externalDir;
        }
        return null;
    }

    private static String relevantStoragePermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ?
                permission.READ_EXTERNAL_STORAGE :
                permission.WRITE_EXTERNAL_STORAGE;
    }

    private void chooseFile() {
        Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFileIntent.setType("*/*");
        chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(chooseFileIntent, FILE_CHOOSER_CODE);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "No activity that could choose file", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int code, int result, Intent data) {
        if (code == FILE_CHOOSER_CODE) {
            if (result == Activity.RESULT_OK && data != null && data.getData() != null)
                startUpload(data.getData());
            else
                Toast.makeText(getApplicationContext(), "no file chosen", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(code, result, data);
    }


    private void startUpload(Uri uri) {
        getWindow().addFlags(FLAG_KEEP_SCREEN_ON);
        mUploading = true;
        showDialog(UPLOAD_PROGRESS_DIALOG);
        UploadHelper.upload(this, uri, APPLICATION_ID, "", "Uploaded Photos", null, this);
    }

    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        if (id == TALKBACK_DIALOG) {
            return new AlertDialog.Builder(this).setTitle("Talkback request pending")
                    .setCancelable(false)
                    .setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {
                            mBroadcaster.stopTalkback();
                        }
                    })
                    .setPositiveButton("Accept", null)
                    .setMessage("Incoming talkback call")
                    .create();
        } else if (id == UPLOAD_PROGRESS_DIALOG) {
            mUploadDialog = new AlertDialog.Builder(this).setTitle("Uploading")
                    .setView(getLayoutInflater().inflate(R.layout.upload_progress_dialog, null))
                    .setCancelable(false)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {
                            mUploading = false;
                        }
                    })
                    .create();
            return mUploadDialog;
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(final int id, final Dialog dialog, final Bundle args) {
        if (id == TALKBACK_DIALOG) {
            final String caller = args.getString(TALKBACK_DIALOG_CALLER);
            final String request = args.getString(TALKBACK_DIALOG_REQUEST);
            final int sessionId = args.getInt(TALKBACK_DIALOG_SESSION_ID);
            AlertDialog ad = (AlertDialog) dialog;
            ad.setButton(DialogInterface.BUTTON_POSITIVE, "Accept", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface d, int which) {
                    mBroadcaster.acceptTalkback(sessionId);
                }
            });
            String msg = "Incoming talkback call";
            if (caller != null && caller.length() > 0)
                msg += " from: " + caller;
            if (request != null && request.length() > 0)
                msg += ": " + request;
            msg += "\nPlease plug in your headphones and accept, or reject the call.";
            ad.setMessage(msg);
        } else if (id == UPLOAD_PROGRESS_DIALOG) {
            ((ProgressBar)dialog.findViewById(R.id.UploadProgressBar)).setProgress(0);
            ((TextView)dialog.findViewById(R.id.UploadStatusText)).setText("Connecting...");
        }
        super.onPrepareDialog(id, dialog);
    }

    @Override
    public void onSuccess(String fileName) {
        runOnUiThread(new Runnable() { @Override public void run() {
            Toast.makeText(getApplicationContext(), "Upload of " + fileName + " completed", Toast.LENGTH_SHORT).show();
            mUploadDialog = null;
            try {
                removeDialog(UPLOAD_PROGRESS_DIALOG);
            } catch (Exception ignored) {}
            getWindow().clearFlags(FLAG_KEEP_SCREEN_ON);
        }});
    }

    @Override
    public void onError(String error) {
        runOnUiThread(new Runnable() { @Override public void run() {
            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
            mUploadDialog = null;
            try {
                removeDialog(UPLOAD_PROGRESS_DIALOG);
            } catch (Exception ignored) {}
            getWindow().clearFlags(FLAG_KEEP_SCREEN_ON);
        }});
    }

    @Override
    public boolean onProgress(long currentBytes, long totalBytes) {
        if (currentBytes == totalBytes || System.currentTimeMillis() > mLastUploadStatusUpdateTime + 500) {
            mLastUploadStatusUpdateTime = System.currentTimeMillis();
            runOnUiThread(new Runnable() { @Override public void run() {
                if (mUploadDialog == null)
                    return;
                int permille = (int) (currentBytes * 1000 / totalBytes);
                ((ProgressBar)mUploadDialog.findViewById(R.id.UploadProgressBar)).setProgress(permille);
                String status = "Sent " + currentBytes/1024 + " KB / " + totalBytes/1024 + " KB";
                ((TextView)mUploadDialog.findViewById(R.id.UploadStatusText)).setText(status);
            }});
        }
        return mUploading;
    }

    public class ItemSelectedListener implements AdapterView.OnItemSelectedListener{
        //get first string in the array
        String firstItem = String.valueOf(live_stream_types.getSelectedItem());
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
            ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            firstItem.equals(String.valueOf(live_stream_types.getSelectedItem()));
        }
        @Override
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
                                rae.startResolutionForResult(picture_uploader.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sie) {
                                Log.i(TAG, "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            String errorMessage = "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings.";
                            Log.e(TAG, errorMessage);

                            Toast.makeText(picture_uploader.this, errorMessage, Toast.LENGTH_LONG).show();
                    }

                    updateLocationUI();
                });
    }

    @Override
    public void onActivityResult(int code, int requestCode, int resultCode, Intent data) {
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

    private Broadcaster.Observer mBroadcasterObserver = new Broadcaster.Observer() {
        @Override
        public void onConnectionStatusChange(BroadcastStatus broadcastStatus) {
            Log.i(LOGTAG, "Received status change: " + broadcastStatus);
            if (broadcastStatus == BroadcastStatus.STARTING)
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            if (broadcastStatus == BroadcastStatus.IDLE)
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            //mBroadcastButton.setImageResource(broadcastStatus == BroadcastStatus.IDLE ? R.drawable.live_strm_button: R.drawable.stop);
            mBroadcastButton2.setImageResource(broadcastStatus == BroadcastStatus.IDLE ? R.drawable.blank_change: R.drawable.ic_action_mic2);
            mBroadcastsatus.setImageResource(broadcastStatus == BroadcastStatus.IDLE ? R.drawable.blank_change: R.drawable.live_action);
            mBroadcastButton3.setImageResource(broadcastStatus == BroadcastStatus.IDLE ? R.drawable.blank_change: R.drawable.ic_action_vid);
            mBroadcastclose.setImageResource(broadcastStatus == BroadcastStatus.IDLE ? R.drawable.ic_clear_black_24dp: R.drawable.ic_clear_black_24dp);
            mBroadcastcamchange.setImageResource(broadcastStatus == BroadcastStatus.IDLE ? R.drawable.blank_change: R.drawable.ic_baseline_sync_24);
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

    SurfaceView mPreviewSurface;
    Broadcaster mBroadcaster;
    //ImageView mBroadcastButton;
    ImageView mBroadcastButton2;
    ImageView mBroadcastsatus;
    ImageView mBroadcastButton3;
    ImageView mBroadcastclose;
    ImageView mBroadcastcamchange;
    ImageView SwitchCameraButton;
    ImageView mPhotoButton;
    ImageView mChosePhotoButton;
    private boolean mInPermissionRequest = false;
    private boolean mUploading = false;
    private AlertDialog mUploadDialog;
    private long mLastUploadStatusUpdateTime = 0;

}
