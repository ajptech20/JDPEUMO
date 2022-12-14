package com.jdpmc.jwatcherapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.mancj.slideup.SlideUp;


public class All_live_video_posts extends Activity {
    private SlideUp slideUp;
    private SlideUp slideUpnewLive;
    private View dim;
    private View slideView;
    private View liveVid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_video_activity);
        ImageView live_stream_starter = findViewById(R.id.go_live_stream);
        ImageView image_uploader_starter = findViewById(R.id.post_new_image);
        ImageView video_uploader_starter = findViewById(R.id.post_new_short);
        //ImageView test_videopull = findViewById(R.id.video_pull_test);
        slideView = findViewById(R.id.slideView);
        liveVid = findViewById(R.id.new_stream);
        dim = findViewById(R.id.dim);
        slideUp = new SlideUp(slideView);
        slideUpnewLive = new SlideUp(liveVid);
        slideUp.hideImmediately();
        slideUpnewLive.hideImmediately();

        ImageView go_home = findViewById(R.id.app_home1);
        go_home.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(All_live_video_posts.this, Activity_home_backup.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
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

        live_stream_starter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(All_live_video_posts.this, Go_New_live.class);
                //Intent intent = new Intent(getApplicationContext(), Go_New_live.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                finish();
            }
        });

        image_uploader_starter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(All_live_video_posts.this, picture_uploader.class);
                //Intent intent = new Intent(getApplicationContext(), picture_uploader.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                finish();
            }
        });

        video_uploader_starter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(All_live_video_posts.this, shortvideo_uploader.class);
                //Intent intent = new Intent(getApplicationContext(), shortvideo_uploader.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                finish();
            }
        });

        /*test_videopull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(All_live_video_posts.this, Video_post_player.class);
                //Intent intent = new Intent(getApplicationContext(), shortvideo_uploader.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                finish();
            }
        });*/

    }
}
