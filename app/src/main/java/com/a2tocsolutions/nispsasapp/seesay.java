package com.a2tocsolutions.nispsasapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.a2tocsolutions.nispsasapp.activities.CovidActivity;

public class seesay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seesay);

        ImageView ctt =  findViewById(R.id.ctt);
        ImageView uploadpic = findViewById(R.id.uploadpic);

        ctt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distressCall();
            }
        });
        uploadpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CovidActivity.class);
                startActivity(intent);
            }
        });
    }
    private void distressCall() {


        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:080046837467"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }
}
