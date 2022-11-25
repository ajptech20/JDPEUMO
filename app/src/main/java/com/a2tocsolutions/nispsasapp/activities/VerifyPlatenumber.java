package com.a2tocsolutions.nispsasapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.a2tocsolutions.nispsasapp.R;
import com.a2tocsolutions.nispsasapp.model.ExtinguisherResponse;
import com.a2tocsolutions.nispsasapp.networking.api.Service;
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

import static com.a2tocsolutions.nispsasapp.utils.Constants.FACE_VERIFY;

public class VerifyPlatenumber extends AppCompatActivity {

    @BindView(R.id.platenumber_layout)
    TextInputLayout platenumber_layout;

    @BindView(R.id.input_platenumber)
    TextInputEditText input_platenumber;

    @BindView(R.id.platenumber)
    AppCompatButton platenumber;

    @BindView(R.id.progress)
    RelativeLayout progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_platenumber);

        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle("Verify Plate Number");

        platenumber.setOnClickListener(v -> {
            String plate_num = Objects.requireNonNull(input_platenumber.getText()).toString().trim();
            verifyPlatenum(plate_num);
        });
    }

    public Boolean verifyFields() {

        platenumber_layout.setError(null);

        if (input_platenumber.length() == 0) {

            input_platenumber.setError("Plate Number is Required");

            return false;
        }

        return true;
    }

    private void verifyPlatenum(String platenum) {
        if (verifyFields()) {
            progress.setVisibility(View.VISIBLE);
            try {
                Service service = DataGenerator.createService(Service.class, FACE_VERIFY);
                Call <ExtinguisherResponse> call = service.plateupload(platenum);

                call.enqueue(new Callback<ExtinguisherResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ExtinguisherResponse> call, @NonNull Response<ExtinguisherResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                ExtinguisherResponse verifyResponse = response.body();
                                String plateresponse = verifyResponse.getResponse();
                                progress.setVisibility(View.GONE);
                                showDialog(plateresponse);
                            }
                        } else {
                            progress.setVisibility(View.GONE);
                            Toast.makeText(VerifyPlatenumber.this, "Plate Number Has not been Registered !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call <ExtinguisherResponse> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(VerifyPlatenumber.this, "I am not Connected to the Internet !", Toast.LENGTH_SHORT).show();
                    }

                });

            } catch (Exception e) {
                progress.setVisibility(View.GONE);
                Toast.makeText(VerifyPlatenumber.this, "Could not try !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDialog(String platenumresponse) {
        boolean wrapInScrollView = true;
        MaterialDialog dialog = new MaterialDialog.Builder(VerifyPlatenumber.this)
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
            verifiedResponse.setText(platenumresponse);

        }
    }
}
