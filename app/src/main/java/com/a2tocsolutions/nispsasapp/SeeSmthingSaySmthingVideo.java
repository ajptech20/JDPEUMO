package com.a2tocsolutions.nispsasapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.a2tocsolutions.nispsasapp.networking.api.Service;
import com.a2tocsolutions.nispsasapp.networking.generator.DataGenerator;
import com.a2tocsolutions.nispsasapp.utils.PreferenceUtils;
import com.bambuser.broadcaster.BroadcastStatus;
import com.bambuser.broadcaster.Broadcaster;
import com.bambuser.broadcaster.CameraError;
import com.bambuser.broadcaster.ConnectionError;

import java.io.File;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeeSmthingSaySmthingVideo extends AppCompatActivity {
    private static final String LOGTAG = "d";

    private static final String APPLICATION_ID = "mOQq8sbExROCWxFjbkGoaA";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_smthing_say_smthing_video);

        mPreviewSurface = findViewById(R.id.PreviewSurfaceView2);
        mBroadcaster = new Broadcaster(this, APPLICATION_ID, mBroadcasterObserver);
        mBroadcaster.setRotation(getWindowManager().getDefaultDisplay().getRotation());
        mBroadcastButton = findViewById(R.id.BroadcastButton);

        mBroadcaster.setTitle("nispsasseesay");
        mBroadcaster.setAuthor("See-Say");
        mBroadcaster.setSendPosition(true);
        mBroadcastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitDetails(null);
                if (mBroadcaster.canStartBroadcasting())

                    mBroadcaster.startBroadcast();

                else
                    mBroadcaster.stopBroadcast();
            }
        });

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

    private void submitDetails(String mcomment){
        
        Service userService = DataGenerator.createService(Service.class, "https://nispsas.com.ng/");

        // create part for file (photo, video, ...)
        MultipartBody.Part body = null ;
        // create a map of data to pass along
        String mtype = "Intelligence";
        String mlongitude = PreferenceUtils.getLocationLongitude(getApplicationContext());
        String mlatitude = PreferenceUtils.getLocationLatitude(getApplicationContext());
        String mphonenumber = PreferenceUtils.getPhoneNumber(getApplicationContext());
        String mreportedto = "";
        //convert String to RequestBody to pass along with Image
        RequestBody comment = null;
        RequestBody longitude = createPartFromString(mlongitude);
        RequestBody latitude = createPartFromString(mlatitude);
        RequestBody reporter = createPartFromString(mphonenumber);
        RequestBody type = createPartFromString(mtype);
        RequestBody reportedto = createPartFromString(mreportedto);

        Call<Void> call = userService.uploadVideo(comment, body, reporter, latitude, longitude, type , reportedto);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                   
                    Toast.makeText(SeeSmthingSaySmthingVideo.this, "See Something Reported successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SeeSmthingSaySmthingVideo.this, "error Reporting Covid19...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(SeeSmthingSaySmthingVideo.this, "Error Reporting See something..." + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }



    @Override
    public void onResume() {
        super.onResume();
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
            mBroadcastButton.setImageResource(broadcastStatus == BroadcastStatus.IDLE ? R.drawable.record : R.drawable.stop);
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
    ImageView mBroadcastButton;

}
