package com.jdpmc.jwatcherapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jdpmc.jwatcherapp.R;
import com.jdpmc.jwatcherapp.utils.PreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.splash_layout)
    ConstraintLayout splash_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.splash_layout);
        ButterKnife.bind(this);

        Animation a = AnimationUtils.loadAnimation(this, R.anim.fade);
        splash_layout.startAnimation(a);//start imageview animation
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(4000);//splash time
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                    }
                    startActivity(intent);
                    PreferenceUtils.saveSplashScreenState(Boolean.TRUE, getApplicationContext());
                    finish();
                }
            }
        };
        timerThread.start();//start the splash activity
    }
}
