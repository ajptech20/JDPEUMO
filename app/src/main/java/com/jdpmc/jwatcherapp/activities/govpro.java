package com.jdpmc.jwatcherapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jdpmc.jwatcherapp.CameraAndPage;
import com.jdpmc.jwatcherapp.R;

public class govpro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_govpro);

        ImageView report = (ImageView)findViewById(R.id.report);
        ImageView request = (ImageView)findViewById(R.id.request);
        ImageView national = (ImageView)findViewById(R.id.national);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CameraAndPage.class);
               startActivity(intent);
            }
        });
        national.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AlgonActivity.class);
                startActivity(intent);
            }
        });
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://nispsas.com.ng/NISPSAS/Registration/requestproject"));
                startActivity(browserIntent);
            }
        });
    }
}
