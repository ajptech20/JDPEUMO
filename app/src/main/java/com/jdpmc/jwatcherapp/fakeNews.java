package com.jdpmc.jwatcherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jdpmc.jwatcherapp.activities.VerifyPSID;

public class fakeNews extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_news);
        ImageView verify = findViewById(R.id.fakenews);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VerifyPSID.class);
                startActivity(intent);
            }
        });
    }
}
