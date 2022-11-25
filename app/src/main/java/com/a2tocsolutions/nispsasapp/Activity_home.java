package com.a2tocsolutions.nispsasapp;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.a2tocsolutions.nispsasapp.database.AppDatabase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.mancj.slideup.SlideUp;

public class Activity_home extends AppCompatActivity {
    private AppDatabase mDb;
    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private String longitude, latitude, phonenumber, reporter;

    private SlideUp slideUp;
    private SlideUp slideUpnewLive;
    private View dim;
    private View slideView;
    private View liveVid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ImageView live_stream_starter = findViewById(R.id.go_live_stream);
        ImageView image_uploader_starter = findViewById(R.id.post_new_image);
        ImageView video_uploader_starter = findViewById(R.id.post_new_short);
        ImageView view_image_post = findViewById(R.id.image_posts);
        ImageView NestedScrool = findViewById(R.id.test_scrooldata);
        ImageView view_live_videos = findViewById(R.id.view_live_posts);
        ImageView short_video_post = findViewById(R.id.short_videos);
        slideView = findViewById(R.id.slideView);
        liveVid = findViewById(R.id.new_stream);
        dim = findViewById(R.id.dim);
        slideUp = new SlideUp(slideView);
        slideUpnewLive = new SlideUp(liveVid);
        slideUp.hideImmediately();
        slideUpnewLive.hideImmediately();

        ImageView New_postbam = findViewById(R.id.new_post);
        New_postbam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideUp.animateIn();
            }
        });

        ImageView New_Stream = findViewById(R.id.go_live_stream);
        New_Stream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideUpnewLive.animateIn();
            }
        });

        slideUp.setSlideListener(new SlideUp.SlideListener() {
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
                Intent intent = new Intent(Activity_home.this, Go_New_live.class);
                //Intent intent = new Intent(getApplicationContext(), Go_New_live.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                finish();
            }
        });

        NestedScrool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_home.this, GetArticles.class);
                //Intent intent = new Intent(getApplicationContext(), GetArticles.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                finish();
            }
        });

        image_uploader_starter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_home.this, picture_uploader.class);
                //Intent intent = new Intent(getApplicationContext(), picture_uploader.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                finish();
            }
        });

        short_video_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_home.this, ShortVideoActivity.class);
                //Intent intent = new Intent(getApplicationContext(), picture_uploader.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                finish();
            }
        });

        video_uploader_starter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_home.this, shortvideo_uploader.class);
                //Intent intent = new Intent(getApplicationContext(), shortvideo_uploader.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                finish();
            }
        });

        view_image_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_home.this, ImagePostsActivity.class);
                //Intent intent = new Intent(getApplicationContext(), shortvideo_uploader.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                finish();
            }
        });

        view_live_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_home.this, LiveVideoActivity.class);
                //Intent intent = new Intent(getApplicationContext(), shortvideo_uploader.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                finish();
            }
        });

    }

}
