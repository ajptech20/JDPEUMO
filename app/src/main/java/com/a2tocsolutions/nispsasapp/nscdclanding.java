package com.a2tocsolutions.nispsasapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.a2tocsolutions.nispsasapp.activities.AlgonActivity;
import com.a2tocsolutions.nispsasapp.activities.NSCDC;
import com.a2tocsolutions.nispsasapp.activities.institutions.SchoolReg;

public class nscdclanding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nscdclanding);

        ImageView nscdc = (ImageView)findViewById(R.id.imageView7);
        ImageView school = (ImageView)findViewById(R.id.imageView21);

        nscdc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NSCDC.class);
                startActivity(intent);
            }
        });

        school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SchoolReg.class);
                startActivity(intent);
            }
        });
    }
}
