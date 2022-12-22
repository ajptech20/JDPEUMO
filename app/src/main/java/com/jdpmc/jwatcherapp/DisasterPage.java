package com.jdpmc.jwatcherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class DisasterPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disaster_page);

        ImageView fire  = findViewById(R.id.preventFire);
        ImageView flood  = findViewById(R.id.preventFlood);

        flood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NEMA.class);
                startActivity(intent);
            }
        });
    }
}
