package com.jdpmc.jwatcherapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jdpmc.jwatcherapp.R;
import com.jdpmc.jwatcherapp.utils.PreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.splash_layout)
    ConstraintLayout splash_layout;

    Handler handler;
    FloatingActionButton mMainbutton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.splash_layout);
        ButterKnife.bind(this);

        Animation a = AnimationUtils.loadAnimation(this, R.anim.fade);
        String phonenumber = PreferenceUtils.getPhoneNumber(getApplicationContext());
        splash_layout.startAnimation(a);//start imageview animation
        handler=new Handler();
        if (phonenumber.equals("")){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent= null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        intent = new Intent(SplashActivity.this, LoginActivity.class);
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
        }else{
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent= null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                    }
                    startActivity(intent);
                    finish();
                }
            },10000);
            FloatingActionButton mybutton = findViewById(R.id.floating_action_button);
            mybutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                    }
                    //Intent intent = new Intent(getApplicationContext(), picture_uploader.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                    finish();
                    stopHandler();
                }
            });
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
    public void stopHandler() {
        handler.removeMessages(0);
    }
}
