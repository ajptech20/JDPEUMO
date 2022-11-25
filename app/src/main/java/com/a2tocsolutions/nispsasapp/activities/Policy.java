package com.a2tocsolutions.nispsasapp.activities;

import android.os.Bundle;

import com.a2tocsolutions.nispsasapp.R;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

public class Policy extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy);

        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setTitle("NISPSAS PRIVACY POLICY");
    }
}
