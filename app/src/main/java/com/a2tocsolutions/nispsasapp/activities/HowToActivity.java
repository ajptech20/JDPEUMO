package com.a2tocsolutions.nispsasapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.a2tocsolutions.nispsasapp.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import butterknife.ButterKnife;

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
