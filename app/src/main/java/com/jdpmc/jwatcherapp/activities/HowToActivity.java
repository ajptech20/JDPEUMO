package com.jdpmc.jwatcherapp.activities;

import android.net.Uri;
import android.os.Bundle;

import com.jdpmc.jwatcherapp.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

public class HowToActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.how_to);


        String url = "http://tocnetng.com.ng/pdf/NISPSAS%20HOW%20IT%20WORKS.pdf";
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));

    }
}
