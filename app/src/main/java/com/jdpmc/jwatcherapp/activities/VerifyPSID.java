package com.jdpmc.jwatcherapp.activities;

import static com.jdpmc.jwatcherapp.utils.Constants.VERIFY_BASE_URL;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.jdpmc.jwatcherapp.R;
import com.jdpmc.jwatcherapp.model.VerifyDetails;
import com.jdpmc.jwatcherapp.networking.api.Service;
import com.jdpmc.jwatcherapp.networking.generator.DataGenerator;
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

public class VerifyPSID extends AppCompatActivity {
    @BindView(R.id.psid_layout)
    TextInputLayout psid_layout;

    @BindView(R.id.input_service_code)
    TextInputEditText input_service_code;

    @BindView(R.id.verifyServiceCode)
    AppCompatButton verifyServiceCode;

    @BindView(R.id.progress)
    RelativeLayout progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_psid);

        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setTitle("Verify PSID");

        verifyServiceCode.setOnClickListener(v -> {
            String service_code = input_service_code.getText().toString().trim();
            verifyPsid(service_code);
        });

    }

    // validation on all fields
    public Boolean verifyFields() {

        psid_layout.setError(null);

        if (input_service_code.length() == 0) {

            input_service_code.setError(getString(R.string.error_psid));

            return false;
        }

        return true;
    }

    private void verifyPsid(String callid) {
        if (verifyFields()) {
            progress.setVisibility(View.VISIBLE);
            try {
                Service service = DataGenerator.createService(Service.class, VERIFY_BASE_URL);
                Call<VerifyDetails> call = service.recievcall(callid);

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
                            Toast.makeText(VerifyPSID.this, "Invalid PSID Service Code !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<VerifyDetails> call, @NonNull Throwable t) {
                         progress.setVisibility(View.GONE);
                         Toast.makeText(VerifyPSID.this, "I am not Connected to the Internet !", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                progress.setVisibility(View.GONE);
                Toast.makeText(VerifyPSID.this, "Invalid PSID Service Code !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDialog(String sgresponse, String sgname, String sgphone, String sgcompany, String imageurl) {
        boolean wrapInScrollView = true;
        MaterialDialog dialog = new MaterialDialog.Builder(VerifyPSID.this)
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
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            Glide.with(VerifyPSID.this)
                    .load(imageurl)
                    .into(imageView);
        }
    }
}