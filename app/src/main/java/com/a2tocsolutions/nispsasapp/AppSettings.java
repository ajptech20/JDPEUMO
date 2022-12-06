package com.a2tocsolutions.nispsasapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.a2tocsolutions.nispsasapp.utils.PreferenceUtils;
import com.bumptech.glide.Glide;

public class AppSettings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_settings_options);
        ImageView go_home = findViewById(R.id.close_settings);
        ImageView edit_user_profile = findViewById(R.id.edit_profile);
        RelativeLayout aboutUs = findViewById(R.id.about_us);
        RelativeLayout Privacy = findViewById(R.id.privacy_policy);
        RelativeLayout Ratting = findViewById(R.id.rate_us);
        RelativeLayout Terms = findViewById(R.id.term_and_con);
        RelativeLayout FAQ = findViewById(R.id.faq_questions);

        TextView username = findViewById(R.id.user_name);
        username.setText(PreferenceUtils.getUsername(getApplicationContext()));
        TextView userphone = findViewById(R.id.user_phone);
        userphone.setText(PreferenceUtils.getPhoneNumber(getApplicationContext()));
        TextView userState = findViewById(R.id.user_state);
        userState.setText(PreferenceUtils.getState(getApplicationContext()));
        TextView userLga = findViewById(R.id.user_lga);
        userLga.setText(PreferenceUtils.getLga(getApplicationContext()));
        String user_image = (PreferenceUtils.getUserImage(getApplicationContext()));
        ImageView imageView = (ImageView) findViewById(R.id.user_image);
        Glide.with(AppSettings.this)
                .load(user_image)
                .into(imageView);

        go_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    intent = new Intent(AppSettings.this, Activity_home.class);
                }
                //Intent intent = new Intent(getApplicationContext(), Go_New_live.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                finish();
            }
        });

        edit_user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserDataUpdate.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                //finish();
            }
        });

    }
}
