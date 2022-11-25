package com.a2tocsolutions.nispsasapp.activities;

import android.os.Bundle;
import android.widget.TextView;
import com.a2tocsolutions.nispsasapp.R;
import java.util.Objects;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class About extends AppCompatActivity {

    @BindView(R.id.aboutText)
    TextView about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setTitle("About NISPSAS");

    }
}
