package com.a2tocsolutions.nispsasapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.a2tocsolutions.nispsasapp.networking.api.Service;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NavUtils;

import com.a2tocsolutions.nispsasapp.R;
import com.a2tocsolutions.nispsasapp.model.ExtinguisherResponse;
import com.a2tocsolutions.nispsasapp.networking.generator.DataGenerator;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.a2tocsolutions.nispsasapp.utils.Constants.BASE_URL;

public class VerifyNdic extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.read_barcode)
    AppCompatButton read_barcode;

    @BindView(R.id.submit)
    AppCompatButton submit;

    @BindView(R.id.fire_layout)
    TextInputLayout fire_layout;

    @BindView(R.id.input_text)
    TextInputEditText input_text;

    @BindView(R.id.progress)
    RelativeLayout progress;

    private static final String TAG = "BarcodeMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_ndic);

        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setTitle("Verify NDIC Code");
        read_barcode.setOnClickListener(this);
        submit.setOnClickListener(this);
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("code")) {

            String code = getIntent().getStringExtra("code");
            input_text.setText(code);
            verifyExtinguisher(code);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_barcode) {
            Intent intent = new Intent(this, ScanActivity.class);
            intent.putExtra("verify", "good");
            startActivity(intent);
        } else if (v.getId() == R.id.submit) {
            String barcode_text = Objects.requireNonNull(input_text.getText()).toString().trim();
            verifyExtinguisher(barcode_text);
        }
    }

    // validation on all fields
    public Boolean verifyFields() {

        fire_layout.setError(null);

        if (input_text.length() == 0) {

            fire_layout.setError("NDIC Code is Required ...");

            return false;
        }

        return true;
    }

    private void verifyExtinguisher(String code) {
        if (verifyFields()) {
            progress.setVisibility(View.VISIBLE);
            try {
                Service service = DataGenerator.createService(Service.class, BASE_URL);
                Call<ExtinguisherResponse> call = service.verifyExtinguisher(code);

                call.enqueue(new Callback<ExtinguisherResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ExtinguisherResponse> call, @NonNull Response<ExtinguisherResponse> response) {
                       if (response.isSuccessful()) {
                           if (response.body() != null) {
                               ExtinguisherResponse extinguisherResponse = response.body();
                               String resp_message = extinguisherResponse.getResponse();
                               progress.setVisibility(View.GONE);
                               showDialog(resp_message);

                               //Toast.makeText(VerifyExtinguisher.this, extinguisherResponse.getResponse(), Toast.LENGTH_LONG).show();
                           }
                       } else {
                           progress.setVisibility(View.GONE);
                           Toast.makeText(VerifyNdic.this, "error verifying extinguisher", Toast.LENGTH_SHORT).show();
                       }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ExtinguisherResponse> call, @NonNull Throwable t) {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(VerifyNdic.this, "I am not Connected to the Internet !", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                progress.setVisibility(View.GONE);
                Toast.makeText(VerifyNdic.this, "error verifying extinguisher", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }

    private void showDialog(String text) {
        boolean wrapInScrollView = true;
        MaterialDialog dialog = new MaterialDialog.Builder(VerifyNdic.this)

                .title("Verified Response")
                .customView(R.layout.fire_res_view, wrapInScrollView)
                .positiveText("OK")
                .onPositive((dialog1, which) -> {
                    dialog1.dismiss();
                })
                .show();
        View view = dialog.getCustomView();

        if (view != null) {
            TextView verifiedResponse = view.findViewById(R.id.verifiedResponse);
            verifiedResponse.setText(text);
        }

    }

}
