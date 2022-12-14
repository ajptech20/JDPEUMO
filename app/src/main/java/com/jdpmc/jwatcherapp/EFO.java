package com.jdpmc.jwatcherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jdpmc.jwatcherapp.activities.StateComplaint;

public class EFO extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_f_o);

        ImageView states = findViewById(R.id.efoState);

        states.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StateComplaint.class);
                startActivity(intent);
            }
        });
    }
}
