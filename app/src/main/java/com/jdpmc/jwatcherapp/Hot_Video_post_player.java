package com.jdpmc.jwatcherapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bambuser.broadcaster.BroadcastPlayer;
import com.bambuser.broadcaster.LatencyMeasurement;
import com.bambuser.broadcaster.PlayerState;
import com.bambuser.broadcaster.SurfaceViewWithAutoAR;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mancj.slideup.SlideUp;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Hot_Video_post_player extends Activity {
    private static final String APPLICATION_ID = "mOQq8sbExROCWxFjbkGoaA";
    private static final String API_KEY = "FKmAFDPiQFccMBe9Pp8d5Q";
    private SlideUp slideUp;
    private SlideUp slideUpnewLive;
    private View dim;
    private View slideView;
    private View liveVid;
    FloatingActionButton flGoLive, flShortVid, flImagePost;
    FloatingActionButton mMainbutton;
    Boolean isAllFabsVisible;

    private static final String resourceuri = "https://cdn.bambuser.net/broadcasts/14905708-614c-4594-82b5-204480d79088?da_signature_method=HMAC-SHA256&da_id=9e1b1e83-657d-7c83-b8e7-0b782ac9543a&da_timestamp=1668467523&da_static=1&da_ttl=0&da_signature=803fc706e07267c94297983bc5ae2728d7b415ff1efc9b72f6a5580df814d442";
    private static final String live_vid = "amlive";
    //TextView receiver_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView txtMarquee;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hot_video_player_post);
        txtMarquee = (TextView) findViewById(R.id.rep_comment);
        txtMarquee.setSelected(true);
        mDefaultDisplay = getWindowManager().getDefaultDisplay();
        mPlayerContentView = findViewById(R.id.PlayerContentView2);
        mPlayerStatusTextView = findViewById(R.id.PlayerStatusTextView);
        mBroadcastLiveTextView = findViewById(R.id.BroadcastLiveTextView);
        mBroadcastLatencyTextView = findViewById(R.id.BroadcastLatencyTextView);
        mVideoSurfaceView = findViewById(R.id.VideoSurfaceView);
        //receiver_msg = findViewById(R.id.vidarea);
        //mVolumeSeekBar = findViewById(R.id.PlayerVolumeSeekBar);
        //mVolumeSeekBar.setOnSeekBarChangeListener(mVolumeSeekBarListener);
        mViewerStatusTextView = findViewById(R.id.ViewerStatusTextView);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        mOkHttpClient = builder.build();

        Intent intent = getIntent();
        String str = intent.getStringExtra("message_key");
        String state = intent.getStringExtra("state_key");
        String lga = intent.getStringExtra("lga_key");
        String town = intent.getStringExtra("town_key");
        String imgurl = intent.getStringExtra("imgurl_key");
        String comment = intent.getStringExtra("comment_key");
        String username = intent.getStringExtra("username_key");
        String rscurl = intent.getStringExtra("rscurl_key");
        String userimg = intent.getStringExtra("userimg_key");
        String date = intent.getStringExtra("date_key");
        String source = intent.getStringExtra("postsrc_key");
        getLatestResourceUri(rscurl);
        RelativeLayout caution = findViewById(R.id.cursion_text);
        if (Objects.equals(source, "Hot Zone")){
            caution.setVisibility(View.VISIBLE);
        }else{
            caution.setVisibility(View.GONE);
        }
        TextView nametext = findViewById(R.id.poster_name);
        nametext.setText(username);
        TextView statetext = findViewById(R.id.rep_state);
        statetext.setText(state);
        TextView Areaofrep = findViewById(R.id.vidarea);
        Areaofrep.setText(lga);
        TextView ReportType = findViewById(R.id.rep_town);
        ReportType.setText(town);
        TextView RepDate = findViewById(R.id.report_date);
        RepDate.setText(date);
        TextView RepSource = findViewById(R.id.post_source);
        RepSource.setText(source);
        TextView ReporComment = findViewById(R.id.rep_comment);
        ReporComment.setText(comment);

        ImageView imageView = (ImageView) findViewById(R.id.reporter_image);
        Glide.with(Hot_Video_post_player.this)
                .load(userimg)
                .into(imageView);

        ImageView close = findViewById(R.id.stop_close_live3);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void getLatestResourceUri(String rscurl) {
        Request request = new Request.Builder()
                .url("https://api.bambuser.com/broadcasts")
                .addHeader("Accept", "application/vnd.bambuser.v1+json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + API_KEY)
                .get()
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                runOnUiThread(new Runnable() { @Override public void run() {
                    if (mPlayerStatusTextView != null)
                        mPlayerStatusTextView.setText("Http exception: " + e);
                }});
            }
            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                String resourceUri = null;
                                /*try {
                                    String body = response.body().string();
                                    JSONObject json = new JSONObject(body);
                                    JSONArray results = json.getJSONArray("results");
                                    JSONObject latestBroadcast = results.optJSONObject(0);
                                    resourceUri = latestBroadcast.optString("resourceUri");
                                } catch (Exception ignored) {}*/
                String vidresource = rscurl;
                resourceUri = vidresource;
                final String uri = resourceUri;
                runOnUiThread(new Runnable() { @Override public void run() {
                    initPlayer(uri);
                }});
            }
        });
    }

    private void showDialog(String sgresponse, String sgname, String sgphone, String sgstate, String imageUrl, String videosrc, String sgreptype, String sgreparea, String sgcomment, String sgstatuse, String date) {
        //TextView verifiedResponse = view.findViewById(R.id.verifiedResponse);
        //verifiedResponse.setText(sgresponse);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoSurfaceView = findViewById(R.id.VideoSurfaceView);
        mPlayerStatusTextView.setText("Loading broadcast...");
        //String callid = (live_vid);
        //verifyPsid(callid);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mOkHttpClient.dispatcher().cancelAll();
        setLatencyTimer(false);
        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        mBroadcastPlayer = null;
        mVideoSurfaceView = null;
        if (mMediaController != null)
            mMediaController.hide();
        mMediaController = null;
        if (mBroadcastLiveTextView != null)
            mBroadcastLiveTextView.setVisibility(View.GONE);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_options_menu, menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about_menu_item) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_UP && mBroadcastPlayer != null && mMediaController != null) {
            PlayerState state = mBroadcastPlayer.getState();
            if (state == PlayerState.PLAYING ||
                    state == PlayerState.BUFFERING ||
                    state == PlayerState.PAUSED ||
                    state == PlayerState.COMPLETED) {
                if (mMediaController.isShowing())
                    mMediaController.hide();
                else
                    mMediaController.show();
            } else {
                mMediaController.hide();
            }
        }
        return false;
    }

    private void initPlayer(String resourceUri) {
        if (resourceUri == null) {
            if (mPlayerStatusTextView != null)
                mPlayerStatusTextView.setText("Could not get info about broadcast");
            return;
        }
        if (mVideoSurfaceView == null) {
            // UI no longer active
            return;
        }
        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        mBroadcastPlayer = new BroadcastPlayer(this, resourceUri, APPLICATION_ID, mPlayerObserver);
        mBroadcastPlayer.setSurfaceView(mVideoSurfaceView);
        mBroadcastPlayer.setAcceptType(BroadcastPlayer.AcceptType.ANY);
        mBroadcastPlayer.setViewerCountObserver(mViewerCountObserver);
        //updateVolume(mVolumeSeekBar.getProgress() / (float) mVolumeSeekBar.getMax());
        mBroadcastPlayer.load();
    }

    public static class AboutActivity extends Activity {
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            WebView webView = new WebView(this);
            webView.loadUrl("file:///android_asset/licenses.html");
            // WebViewClient necessary since Android N to handle links in the license document
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }
            });
            setContentView(webView);
        }
    }

    private void updateVolume(float progress) {
        // Output volume should optimally increase logarithmically, but Android media player APIs
        // respond linearly. Producing non-linear scaling between 0.0 and 1.0 by using x^4.
        // Not exactly logarithmic, but has the benefit of satisfying the end points exactly.
        if (mBroadcastPlayer != null)
            mBroadcastPlayer.setAudioVolume(progress * progress * progress * progress);
    }

    private Point getScreenSize() {
        if (mDefaultDisplay == null)
            mDefaultDisplay = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        mDefaultDisplay.getRealSize(size);
        return size;
    }

    private final SeekBar.OnSeekBarChangeListener mVolumeSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            updateVolume(seekBar.getProgress() / (float) seekBar.getMax());
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };

    private final BroadcastPlayer.Observer mPlayerObserver = new BroadcastPlayer.Observer() {
        @Override
        public void onStateChange(PlayerState state) {
            if (mPlayerStatusTextView != null)
                mPlayerStatusTextView.setText("");
            //mPlayerStatusTextView.setText("Status: " + state);
            boolean isPlayingLive = mBroadcastPlayer != null && mBroadcastPlayer.isTypeLive() && mBroadcastPlayer.isPlaying();
            if (mBroadcastLiveTextView != null) {
                mBroadcastLiveTextView.setVisibility(isPlayingLive ? View.GONE : View.GONE);
            }
            updateLatencyView();
            setLatencyTimer(isPlayingLive);
            if (state == PlayerState.PLAYING || state == PlayerState.PAUSED || state == PlayerState.COMPLETED) {
                if (mMediaController == null && mBroadcastPlayer != null && !mBroadcastPlayer.isTypeLive()) {
                    mMediaController = new MediaController(Hot_Video_post_player.this);
                    mMediaController.setAnchorView(mPlayerContentView);
                    mMediaController.setMediaPlayer(mBroadcastPlayer);
                }
                if (mMediaController != null) {
                    mMediaController.setEnabled(true);
                    mMediaController.show();
                }
            } else if (state == PlayerState.ERROR || state == PlayerState.CLOSED) {
                if (mMediaController != null) {
                    mMediaController.setEnabled(false);
                    mMediaController.hide();
                }
                mMediaController = null;
                if (mViewerStatusTextView != null)
                    mViewerStatusTextView.setText("");
            }
        }
        @Override
        public void onBroadcastLoaded(boolean live, int width, int height) {
            if (mBroadcastLiveTextView != null)
                mBroadcastLiveTextView.setVisibility(live ? View.GONE : View.GONE);

            Point size = getScreenSize();
            float screenAR = size.x / (float) size.y;
            float videoAR = width / (float) height;
            float arDiff = screenAR - videoAR;
            mVideoSurfaceView.setCropToParent(Math.abs(arDiff) < 0.2);
        }
    };

    private void updateLatencyView() {
        if (mBroadcastLatencyTextView != null) {
            LatencyMeasurement lm = mBroadcastPlayer != null ? mBroadcastPlayer.getEndToEndLatency() : null;
            if (lm != null)
                mBroadcastLatencyTextView.setText("Latency: " + (lm.latency / 1000.0) + " s");
            mBroadcastLatencyTextView.setVisibility(lm != null ? View.VISIBLE : View.GONE);
        }
    }

    private void setLatencyTimer(boolean enable) {
        mMainHandler.removeCallbacks(mLatencyUpdateRunnable);
        if (enable)
            mMainHandler.postDelayed(mLatencyUpdateRunnable, 1000);
    }

    private final Runnable mLatencyUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            updateLatencyView();
            mMainHandler.postDelayed(this, 1000);
        }
    };

    private final BroadcastPlayer.ViewerCountObserver mViewerCountObserver = new BroadcastPlayer.ViewerCountObserver() {
        @Override
        public void onCurrentViewersUpdated(long viewers) {
            if (mViewerStatusTextView != null)
                mViewerStatusTextView.setText("Viewers: " + viewers);
        }
        @Override
        public void onTotalViewersUpdated(long viewers) {
        }
    };

    private final Handler mMainHandler = new Handler();
    private OkHttpClient mOkHttpClient;
    private Display mDefaultDisplay;
    private BroadcastPlayer mBroadcastPlayer = null;
    private SurfaceViewWithAutoAR mVideoSurfaceView = null;
    private SeekBar mVolumeSeekBar = null;
    private View mPlayerContentView = null;
    private TextView mPlayerStatusTextView = null;
    private TextView mViewerStatusTextView = null;
    private TextView mBroadcastLiveTextView = null;
    private TextView mBroadcastLatencyTextView = null;
    private MediaController mMediaController = null;
}
