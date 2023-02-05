package com.jdpmc.jwatcherapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
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
import com.jdpmc.jwatcherapp.utils.FancyToast;
import com.jdpmc.jwatcherapp.utils.PreferenceUtils;
import com.mancj.slideup.SlideUp;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Video_post_player extends Activity {

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

    //private static final String resourceuri = "https://cdn.bambuser.net/broadcasts/14905708-614c-4594-82b5-204480d79088?da_signature_method=HMAC-SHA256&da_id=9e1b1e83-657d-7c83-b8e7-0b782ac9543a&da_timestamp=1668467523&da_static=1&da_ttl=0&da_signature=803fc706e07267c94297983bc5ae2728d7b415ff1efc9b72f6a5580df814d442";
    private static final String live_vid = "amlive";
    //TextView receiver_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player_post);
        mMainbutton = findViewById(R.id.floating_action_button);
        flGoLive = findViewById(R.id.go_live_stream);
        flShortVid = findViewById(R.id.post_new_short);
        flImagePost = findViewById(R.id.post_new_image);

        /*addAlarmActionText = findViewById(R.id.add_alarm_action_text);
        addPersonActionText = findViewById(R.id.add_person_action_text);*/

        flGoLive.setVisibility(View.GONE);
        flShortVid.setVisibility(View.GONE);
        flImagePost.setVisibility(View.GONE);
        isAllFabsVisible = false;

        slideView = findViewById(R.id.slideView);
        liveVid = findViewById(R.id.new_stream);
        dim = findViewById(R.id.dim);
        slideUp = new SlideUp(slideView);
        slideUpnewLive = new SlideUp(liveVid);
        slideUp.hideImmediately();
        slideUpnewLive.hideImmediately();

        mDefaultDisplay = getWindowManager().getDefaultDisplay();
        mPlayerContentView = findViewById(R.id.PlayerContentView2);
        mPlayerStatusTextView = findViewById(R.id.PlayerStatusTextView);

        /*final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mrmoveprogress.setVisibility(View.GONE);
            }
        }, 6000);*/

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
        String user_name = intent.getStringExtra("username_key");
        String rscurl = intent.getStringExtra("vidurl_key");
        String userimg_url = intent.getStringExtra("userimg_key");
        String rep_type = intent.getStringExtra("reptype_key");
        String rep_state = intent.getStringExtra("state_key");
        String rep_date = intent.getStringExtra("date_key");
        String rep_comment = intent.getStringExtra("comment_key");
        String rep_area = intent.getStringExtra("area_key");
        String rep_status = intent.getStringExtra("repstatus_key");
        getLatestResourceUri();
        TextView nametext = findViewById(R.id.poster_name);
        nametext.setText(user_name);
        TextView statustext = findViewById(R.id.brstatus);
        statustext.setText(rep_status);
        TextView psttype = findViewById(R.id.post_type);
        psttype.setText(rep_status);
        TextView statetext = findViewById(R.id.rep_state);
        statetext.setText(rep_state);
        TextView Areaofrep = findViewById(R.id.vidarea);
        Areaofrep.setText(rep_area);
        TextView ReportType = findViewById(R.id.rep_type);
        ReportType.setText(rep_type);
        TextView RepDate = findViewById(R.id.report_date);
        RepDate.setText(rep_date);
        TextView ReporComment = findViewById(R.id.rep_comment);
        ReporComment.setText(rep_comment);
        ImageView imageView = (ImageView) findViewById(R.id.reporter_image);
        Glide.with(Video_post_player.this)
                .load(userimg_url)
                .into(imageView);

        ImageView go_home = findViewById(R.id.app_home1);
        go_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView New_postbam = findViewById(R.id.new_post);
        New_postbam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideUp.animateIn();
            }
        });

        slideUpnewLive.setSlideListener(new SlideUp.SlideListener() {
            @Override
            public void onSlideDown(float v) {
                dim.setAlpha(1 - (v / 100));
            }

            @Override
            public void onVisibilityChanged(int i) {
                if (i == View.GONE) {
                    //fab.show();
                }

            }
        });

        mMainbutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isAllFabsVisible) {

                            flGoLive.show();
                            flShortVid.show();
                            flImagePost.show();
                            //addAlarmActionText.setVisibility(View.VISIBLE);
                            //addPersonActionText.setVisibility(View.VISIBLE);

                            //mAddFab.extend();
                            isAllFabsVisible = true;
                        } else {
                            flGoLive.hide();
                            flShortVid.hide();
                            flImagePost.hide();
                            //addAlarmActionText.setVisibility(View.GONE);
                            //addPersonActionText.setVisibility(View.GONE);
                            //mAddFab.shrink();
                            isAllFabsVisible = false;
                        }
                    }
                });

        flShortVid.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String user_image = (PreferenceUtils.getUserImage(getApplicationContext()));
                        if (!user_image.equals("")){
                            Intent intent = new Intent(Video_post_player.this, shortvideo_uploader.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                        }else {
                            FancyToast.makeText(getApplicationContext(), "Kindly Update Your profile Image before you can create a  post", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                            Intent intent = new Intent(Video_post_player.this, UserDataUpdate.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                        }
                    }
                });
        flGoLive.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String user_image = (PreferenceUtils.getUserImage(getApplicationContext()));
                        if (!user_image.equals("")){
                            Intent intent = new Intent(Video_post_player.this, Go_New_live.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                        }else {
                            FancyToast.makeText(getApplicationContext(), "Kindly Update Your profile Image before you can create a  post", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                            Intent intent = new Intent(Video_post_player.this, UserDataUpdate.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                        }
                    }
                });
        flImagePost.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String user_image = (PreferenceUtils.getUserImage(getApplicationContext()));
                        if (!user_image.equals("")){
                            Intent intent = new Intent(Video_post_player.this, picture_uploader.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                        }else {
                            FancyToast.makeText(getApplicationContext(), "Kindly Update Your profile Image before you can create a  post", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                            Intent intent = new Intent(Video_post_player.this, UserDataUpdate.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                        }
                    }
                });

        ImageView view_live_videos = findViewById(R.id.view_live_posts);
        view_live_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    intent = new Intent(Video_post_player.this, LiveVideoActivity.class);
                }
                //Intent intent = new Intent(getApplicationContext(), shortvideo_uploader.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                finish();
            }
        });

        ImageView view_image_post = findViewById(R.id.image_posts);
        view_image_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    intent = new Intent(Video_post_player.this, ImagePostsActivity.class);
                }
                //Intent intent = new Intent(getApplicationContext(), shortvideo_uploader.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                finish();
            }
        });
        ImageView short_video_post = findViewById(R.id.short_videos);
        short_video_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    intent = new Intent(Video_post_player.this, ShortVideoActivity.class);
                }
                //Intent intent = new Intent(getApplicationContext(), picture_uploader.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                finish();
            }
        });

        ImageView close = findViewById(R.id.stop_close_live3);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        startProgress();
    }

    private void startProgress(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mrmoveprogress = findViewById(R.id.play_status);
                mrmoveprogress.setVisibility(View.VISIBLE);
            }
        });
    }

    private void endProgress(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mrmoveprogress = findViewById(R.id.play_status);
                mrmoveprogress.setVisibility(View.GONE);
            }
        });
    }

    private void getLatestResourceUri() {
        Intent intent = getIntent();
        String rscurl = intent.getStringExtra("vidurl_key");
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
                        mPlayerStatusTextView.setText("Please check your internet connection");
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
                    endProgress();
                }});
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoSurfaceView = findViewById(R.id.VideoSurfaceView);
        mPlayerStatusTextView.setText("Loading broadcast...");
        String resourceUri = null;
        Intent intent = getIntent();
        String rscurl = intent.getStringExtra("vidurl_key");
        String vidresource = rscurl;
        resourceUri = vidresource;
        final String uri = resourceUri;
        runOnUiThread(new Runnable() { @Override public void run() {
            initPlayer(uri);
        }});
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
                    mMediaController = new MediaController(Video_post_player.this);
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
    private RelativeLayout mrmoveprogress = null;
    private TextView mViewerStatusTextView = null;
    private TextView mBroadcastLiveTextView = null;
    private TextView mBroadcastLatencyTextView = null;
    private MediaController mMediaController = null;
}
