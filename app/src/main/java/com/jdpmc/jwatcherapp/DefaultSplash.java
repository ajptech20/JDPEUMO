package com.jdpmc.jwatcherapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jdpmc.jwatcherapp.activities.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DefaultSplash extends AppCompatActivity {

    @BindView(R.id.splash_layout)
    ConstraintLayout splash_layout;

    Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.splash_layout);
        ButterKnife.bind(this);

        Animation a = AnimationUtils.loadAnimation(this, R.anim.fade);
        splash_layout.startAnimation(a);//start imageview animation
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent= null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    intent = new Intent(DefaultSplash.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        },5000);
        /*Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(4000);//splash time
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        intent = new Intent(DefaultSplash.this, MainActivity.class);
                    }
                    startActivity(intent);
                    finish();
                }
            }
        };
        timerThread.start();*///start the splash activity
    }
}
