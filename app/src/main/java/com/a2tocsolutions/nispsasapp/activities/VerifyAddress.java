package com.a2tocsolutions.nispsasapp.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.a2tocsolutions.nispsasapp.networking.api.Service;
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

public class VerifyAddress extends AppCompatActivity {

    @BindView(R.id.verifyAddress)
    AppCompatButton verifyAddress;

    @BindView(R.id.address_layout)
    TextInputLayout address_layout;

    @BindView(R.id.input_text)
    TextInputEditText input_text;

    @BindView(R.id.progress)
    RelativeLayout progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_address);

        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setTitle("Verify Address");
        verifyAddress.setOnClickListener(v -> {
            String input = Objects.requireNonNull(input_text.getText()).toString().trim();
            verifyExtinguisher(input);
        });

    }

    // validation on all fields
    public Boolean verifyFields() {

        address_layout.setError(null);

        if (input_text.length() == 0) {

            address_layout.setError(getString(R.string.error_phone));

            return false;
        }

        return true;
    }

    private void verifyExtinguisher(String phone) {
        if (verifyFields()) {
            progress.setVisibility(View.VISIBLE);
            try {
                Service service = DataGenerator.createService(Service.class, BASE_URL);
                Call<ExtinguisherResponse> call = service.verifyAddress(phone);

                call.enqueue(new Callback<ExtinguisherResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ExtinguisherResponse> call, @NonNull Response<ExtinguisherResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                ExtinguisherResponse extinguisherResponse = response.body();
                                String message = extinguisherResponse.getResponse();
                                progress.setVisibility(View.GONE);
                                showDialog(message);
                                //Toast.makeText(VerifyAddress.this, extinguisherResponse.getResponse(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            progress.setVisibility(View.GONE);
                            Toast.makeText(VerifyAddress.this, "error verifying address", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ExtinguisherResponse> call, @NonNull Throwable t) {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(VerifyAddress.this, "I am not Connected to the Internet !", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                progress.setVisibility(View.GONE);
                Toast.makeText(VerifyAddress.this, "error verifying address", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog(String text) {
        boolean wrapInScrollView = true;
        MaterialDialog dialog = new MaterialDialog.Builder(VerifyAddress.this)

                .title("Verified Response")
                .customView(R.layout.driver_res_view, wrapInScrollView)
                .positiveText("OK")
                .onPositive((dialog1, which) -> {
                    dialog1.dismiss();
                })
                .show();
        View view = dialog.getCustomView();

        if (view != null) {
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            TextView verifiedResponse = view.findViewById(R.id.verifiedResponse);
            imageView.setVisibility(View.GONE);

            verifiedResponse.setText(text);
        }

    }


}
