package com.jdpmc.jwatcherapp;

import static com.jdpmc.jwatcherapp.utils.Constants.VERIFY_BASE_URL;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.jdpmc.jwatcherapp.model.VerifyDetails;
import com.jdpmc.jwatcherapp.networking.api.Service;
import com.jdpmc.jwatcherapp.networking.generator.DataGenerator;
import com.jdpmc.jwatcherapp.utils.PreferenceUtils;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bambuser.broadcaster.BroadcastPlayer;
import com.bambuser.broadcaster.BroadcastStatus;
import com.bambuser.broadcaster.Broadcaster;
import com.bambuser.broadcaster.CameraError;
import com.bambuser.broadcaster.ConnectionError;
import com.bambuser.broadcaster.PlayerState;
import com.bambuser.broadcaster.SurfaceViewWithAutoAR;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class A_live_video_posts extends AppCompatActivity {

    private static final String APPLICATION_ID = "mOQq8sbExROCWxFjbkGoaA";
    private static final String API_KEY = "FKmAFDPiQFccMBe9Pp8d5Q";
    private static final String LOGTAG = "Mybroadcastingapp";

    @BindView(R.id.psid_layout1)
    TextInputLayout psid_layout;

    @BindView(R.id.input_service_code1)
    TextInputEditText input_service_code;

    @BindView(R.id.verifyServiceCode1)
    AppCompatButton verifyServiceCode;

    @BindView(R.id.progress1)
    RelativeLayout progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testasingle_vidpost);
        mVideoSurface = findViewById(R.id.VideoSurfaceView);
        mPlayerContentView = findViewById(R.id.PlayerContentView);
        mPlayerStatusTextView = findViewById(R.id.PlayerStatusTextView);
        mDefaultDisplay = getWindowManager().getDefaultDisplay();
        mBroadcaster = new Broadcaster(this, APPLICATION_ID, mBroadcasterObserver);
        ButterKnife.bind(this);
        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setTitle("Verify PSID");

        verifyServiceCode.setOnClickListener(v -> {
            String callid = input_service_code.getText().toString().trim();
            verifyPsid(callid);
            getLatestResourceUri();
        });

    }

    // validation on all fields
    public Boolean verifyFields() {

        psid_layout.setError(null);

        if (input_service_code.length() == 0) {

            input_service_code.setError(getString(R.string.error_psid));

            return false;
        }

        return true;
    }

    private void verifyPsid(String callid) {
        if (verifyFields()) {
            progress.setVisibility(View.VISIBLE);
            try {
                Service service = DataGenerator.createService(Service.class, VERIFY_BASE_URL);
                Call<VerifyDetails> call = service.recievcall(callid);

                call.enqueue(new Callback<VerifyDetails>() {
                    @Override
                    public void onResponse(@NonNull Call<VerifyDetails> call, @NonNull Response<VerifyDetails> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                VerifyDetails verifyResponse = response.body();
                                String sgresponse = verifyResponse.getResponse();
                                String sgname = verifyResponse.getSGname();
                                String sgphone = verifyResponse.getSGphone();
                                String sgcompany = verifyResponse.getSGcompany();
                                String imageUrl = verifyResponse.getImage();
                                progress.setVisibility(View.GONE);
                                showDialog(sgresponse, sgname, sgphone, sgcompany, imageUrl);
                            }
                        } else {
                            progress.setVisibility(View.GONE);
                            Toast.makeText(A_live_video_posts.this, "Invalid PSID Service Code !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<VerifyDetails> call, @NonNull Throwable t) {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(A_live_video_posts.this, "I am not Connected to the Internet !", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                progress.setVisibility(View.GONE);
                Toast.makeText(A_live_video_posts.this, "Invalid PSID Service Code !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDialog(String sgresponse, String sgname, String sgphone, String sgcompany, String imageurl) {
        boolean wrapInScrollView = true;
        MaterialDialog dialog = new MaterialDialog.Builder(A_live_video_posts.this)
                .title(sgresponse)
                .customView(R.layout.security_res_view, wrapInScrollView)
                .positiveText("OK")
                .onPositive((dialog1, which) -> {
                    dialog1.dismiss();
                })
                .show();
        View view = dialog.getCustomView();
        if (view != null) {
            TextView verifiedResponse = view.findViewById(R.id.verifiedResponse);
            verifiedResponse.setText(sgresponse);
            TextView nametext = view.findViewById(R.id.nametext);
            nametext.setText(sgname);
            TextView phonetext = view.findViewById(R.id.phonetext);
            phonetext.setText(sgphone);
            TextView companytext = view.findViewById(R.id.companytext);
            companytext.setText(sgcompany);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            Glide.with(A_live_video_posts.this)
                    .load(imageurl)
                    .into(imageView);

        }

        getLatestResourceUri();
    }

    @Override
    public void onResume(){
        super.onResume();

        mVideoSurface = findViewById(R.id.VideoSurfaceView);
        mPlayerStatusTextView.setText("Loading latest broadcast");
        getLatestResourceUri();
    }

    void getLatestResourceUri() {
        Request request = new Request.Builder()
                .url("https://api.bambuser.com/broadcasts")
                .addHeader("Accept", "application/vnd.bambuser.v1+json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + API_KEY)
                .get()
                .build();
        mOkHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(final okhttp3.Call call, final IOException e) {
                runOnUiThread(new Runnable() { @Override public void run() {
                    if (mPlayerStatusTextView != null)
                        mPlayerStatusTextView.setText("Http exception: " + e);
                }});
            }
            @Override
            public void onResponse(final okhttp3.Call call, final okhttp3.Response response) throws IOException {
                String body = response.body().string();
                String resourceUri = null;
                try {
                    JSONObject json = new JSONObject(body);
                    JSONArray results = json.getJSONArray("results");
                    JSONObject latestBroadcast = results.optJSONObject(0);
                    resourceUri = PreferenceUtils.getResourceUri(getApplicationContext());
                } catch (Exception ignored) {}
                final String uri = resourceUri;
                runOnUiThread(new Runnable() { @Override public void run() {
                    initPlayer(uri);
                }});
            }
        });
    }

    void initPlayer(String resourceUri) {
        if (resourceUri == null) {
            if (mPlayerStatusTextView != null)
                mPlayerStatusTextView.setText("Could not get info about latest broadcast");
            return;
        }
        if (mVideoSurface == null) {
            // UI no longer active
            return;
        }
        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        mBroadcastPlayer = new BroadcastPlayer(this, resourceUri, APPLICATION_ID, mBroadcastPlayerObserver);
        mBroadcastPlayer.setSurfaceView(mVideoSurface);
        mBroadcastPlayer.load();

    }

    BroadcastPlayer.Observer mBroadcastPlayerObserver = new BroadcastPlayer.Observer() {
        @Override
        public void onStateChange(PlayerState playerState) {
            if (mPlayerStatusTextView != null)
                mPlayerStatusTextView.setText("Status: " + playerState);

        }
        @Override
        public void onBroadcastLoaded(boolean live, int width, int height) {
            Point size = getScreenSize();
            float screenAR = size.x / (float) size.y;
            float videoAR = width / (float) height;
            float arDiff = screenAR - videoAR;
            mVideoSurface.setCropToParent(Math.abs(arDiff) < 0.2);

        }
    };

    private Point getScreenSize() {
        if (mDefaultDisplay == null)
            mDefaultDisplay = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        try {
            // this is officially supported since SDK 17 and said to work down to SDK 14 through reflection,
            // so it might be everything we need.
            mDefaultDisplay.getClass().getMethod("getRealSize", Point.class).invoke(mDefaultDisplay, size);
        } catch (Exception e) {
            // fallback to approximate size.
            mDefaultDisplay.getSize(size);
        }
        return size;
    }

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

    final OkHttpClient mOkHttpClient = new OkHttpClient();
    BroadcastPlayer mBroadcastPlayer;

    SurfaceViewWithAutoAR mVideoSurface;
    View mPlayerContentView;
    TextView mPlayerStatusTextView;
    Display mDefaultDisplay;
    Broadcaster mBroadcaster;
}
