package com.a2tocsolutions.nispsasapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.a2tocsolutions.nispsasapp.R;
import com.a2tocsolutions.nispsasapp.utils.PreferenceUtils;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Terms extends AppCompatActivity {

    @BindView(R.id.checkTerms)
    CheckBox checkTerms;

    @BindView(R.id.continueBtn)
    MaterialButton continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms);
        ButterKnife.bind(this);

        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setTitle("Terms");

        if (checkTerms.isChecked()) {
           continueBtn.setEnabled(true);
        } else {
            continueBtn.setEnabled(false);
        }

        continueBtn.setOnClickListener(v -> {
            PreferenceUtils.acceptedTerms("accepted", getApplicationContext());
            Intent intent = new Intent(Terms.this, SplashActivity.class);
            startActivity(intent);
            finish();
        });

    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkTerms:
                if (checked) {
                    continueBtn.setEnabled(true);
                } else {
                    continueBtn.setEnabled(false);
                }
                break;

        }
    }
}
