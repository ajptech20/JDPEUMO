package com.a2tocsolutions.nispsasapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.a2tocsolutions.nispsasapp.R;
import com.a2tocsolutions.nispsasapp.model.VerifyDetails;
import com.a2tocsolutions.nispsasapp.networking.api.Service;
import com.a2tocsolutions.nispsasapp.networking.generator.DataGenerator;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.a2tocsolutions.nispsasapp.utils.Constants.VERIFY_BASE_URL;

public class VerifySecurityCode extends AppCompatActivity {
    @BindView(R.id.vigilante_layout)
    TextInputLayout vigilante_layout;

    @BindView(R.id.input_service_code)
    TextInputEditText input_service_code;

    @BindView(R.id.verifyServiceCode)
    AppCompatButton verifyServiceCode;

    @BindView(R.id.progress)
    RelativeLayout progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_vigilante);

        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setTitle("Verify Service Code");

        verifyServiceCode.setOnClickListener(v -> {
            String service_code = Objects.requireNonNull(input_service_code.getText()).toString().trim();
            verifyVigilante(service_code);
        });
    }

    public Boolean verifyFields() {

        vigilante_layout.setError(null);

        if (input_service_code.length() == 0) {

            input_service_code.setError("Service Code is Required !");

            return false;
        }

        return true;
    }

    private void verifyVigilante(String servicecode) {
        if (verifyFields()) {
            progress.setVisibility(View.VISIBLE);
            try {
                Service service = DataGenerator.createService(Service.class, VERIFY_BASE_URL);
                Call<VerifyDetails> call = service.securitycode(servicecode);

                call.enqueue(new Callback<VerifyDetails>() {
                    @Override
                    public void onResponse(@NonNull Call<VerifyDetails> call, @NonNull Response<VerifyDetails> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                VerifyDetails verifyResponse = response.body();
                                String sgresponse = verifyResponse.getResponse();
                                String sgname = verifyResponse.getSGname();
                                String sgphone = verifyResponse.getSGphone();
                                String sgcompany = verifyResponse.getSGcompany();
                                String imageUrl = verifyResponse.getImage();
                                progress.setVisibility(View.GONE);
                                showDialog(sgresponse, sgname, sgphone, sgcompany, imageUrl);
                            }
                        } else {
                            progress.setVisibility(View.GONE);
                            Toast.makeText(VerifySecurityCode.this, "Invalid Service Code !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<VerifyDetails> call, @NonNull Throwable t) {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(VerifySecurityCode.this, "I am not Connected to the Internet !", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                progress.setVisibility(View.GONE);
                Toast.makeText(VerifySecurityCode.this, "Invalid Service Code !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDialog(String sgresponse, String sgname, String sgphone, String sgcompany, String imageurl) {
        boolean wrapInScrollView = true;
        MaterialDialog dialog = new MaterialDialog.Builder(VerifySecurityCode.this)
                .title(sgresponse)
                .customView(R.layout.security_res_view, wrapInScrollView)
                .positiveText("OK")
                .onPositive((dialog1, which) -> {
                    dialog1.dismiss();
                })
                .show();
        View view = dialog.getCustomView();

        if (view != null) {
            TextView verifiedResponse = view.findViewById(R.id.verifiedResponse);
            verifiedResponse.setText(sgresponse);
            TextView nametext = view.findViewById(R.id.nametext);
            nametext.setText(sgname);
            TextView phonetext = view.findViewById(R.id.phonetext);
            phonetext.setText(sgphone);
            TextView companytext = view.findViewById(R.id.companytext);
            companytext.setText(sgcompany);
            ImageView imageView = view.findViewById(R.id.image);
            Glide.with(this)
                    .load(imageurl)
                    .into(imageView);
        }
    }
}
