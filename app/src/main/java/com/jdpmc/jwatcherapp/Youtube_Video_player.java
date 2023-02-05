package com.jdpmc.jwatcherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class Youtube_Video_player extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_video_player);

        ImageView close = findViewById(R.id.stop_close_live3);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        String title = intent.getStringExtra("srctitle_key");
        String description = intent.getStringExtra("description_key");
        String fileurl = intent.getStringExtra("fileurl_key");
        String previewimg = intent.getStringExtra("prevurl_key");
        String imgfileurl = intent.getStringExtra("imgurl_key");
        String authour = intent.getStringExtra("author_key");
        String date = intent.getStringExtra("date_key");
        String type = intent.getStringExtra("type_key");
        String vidurl = intent.getStringExtra("vidurl_key");

        TextView titletext = findViewById(R.id.src_title);
        titletext.setText(title);
        TextView RepSource = findViewById(R.id.usersc_author);
        RepSource.setText(authour);
        TextView ReporComment = findViewById(R.id.rsc_comment);
        ReporComment.setText(description);
        ImageView imageView = (ImageView) findViewById(R.id.rsc_image);
        Glide.with(Youtube_Video_player.this)
                .load(previewimg)
                .into(imageView);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        YouTubePlayerView youTubePlayerView =
                (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubePlayerView.initialize("AIzaSyAk7aCp3z0njfkgkdd9xUudN1LiSyev4vM",
        new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {

                // do any work here to cue video, play video, etc.
                youTubePlayer.cueVideo(vidurl);
            }
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
