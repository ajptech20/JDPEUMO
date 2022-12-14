package com.jdpmc.jwatcherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class DisasterPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disaster_page);

        ImageView fire  = findViewById(R.id.preventFire);
        ImageView flood  = findViewById(R.id.preventFlood);

        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FireService.class);
                startActivity(intent);
            }
        });
        flood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NEMA.class);
                startActivity(intent);
            }
        });
    }
}
