package com.jdpmc.jwatcherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jdpmc.jwatcherapp.activities.CorrectionServices;
import com.jdpmc.jwatcherapp.activities.Imigration;

public class pg extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pg);

        ImageView immigration = findViewById(R.id.imageView24);
        ImageView nscdc = findViewById(R.id.imageView26);
        ImageView correction = findViewById(R.id.imageView25);

        immigration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Imigration.class);
               startActivity(intent);
            }
        });
        nscdc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), nscdclanding.class);
                startActivity(intent);
            }
        });
        correction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CorrectionServices.class);
                startActivity(intent);
            }
        });
    }
}
