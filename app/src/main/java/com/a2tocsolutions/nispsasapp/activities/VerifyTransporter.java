package com.a2tocsolutions.nispsasapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NavUtils;

import com.a2tocsolutions.nispsasapp.R;
import com.a2tocsolutions.nispsasapp.model.ResponseTransporter;
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

import static com.a2tocsolutions.nispsasapp.utils.Constants.BASE_URL;

public class VerifyTransporter extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.read_barcode)
    AppCompatButton read_barcode;

    @BindView(R.id.submit)
    AppCompatButton submit;

    @BindView(R.id.transport_layout)
    TextInputLayout transport_layout;

    @BindView(R.id.input_text)
    TextInputEditText input_text;

    @BindView(R.id.progress)
    RelativeLayout progress;

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_transporter);

        ButterKnife.bind(this);

        setTitle("Verify Transporter");
        read_barcode.setOnClickListener(this);
        submit.setOnClickListener(this);
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("code")) {

            String code = getIntent().getStringExtra("code");
            input_text.setText(code);
            verifyTransporter(code);
        }

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_barcode) {
            Intent intent = new Intent(this, ScanActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.submit) {
            String barcode_text = Objects.requireNonNull(input_text.getText()).toString().trim();
            verifyTransporter(barcode_text);
        }
    }


    // validation on all fields
    public Boolean verifyFields() {

        transport_layout.setError(null);

        if (input_text.length() == 0) {

            input_text.setError(getString(R.string.error_transport));

            return false;
        }

        return true;
    }

    private void verifyTransporter (String code) {
        if (verifyFields()) {
            progress.setVisibility(View.VISIBLE);
            try {
                Service service = DataGenerator.createService(Service.class, BASE_URL);
                Call<ResponseTransporter> call = service.transporter(code);

                call.enqueue(new Callback<ResponseTransporter>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseTransporter> call, @NonNull Response<ResponseTransporter> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                ResponseTransporter transporter = response.body();
                                String dresponse = transporter.getResponse();
                                String dname = transporter.getDname();
                                String dphone = transporter.getDphone();
                                String dunion = transporter.getDunion();
                                String dimage = transporter.getImage();
                                progress.setVisibility(View.GONE);
                                showDialog(dresponse,dname, dphone, dunion, dimage);

                            }
                        } else {
                            progress.setVisibility(View.GONE);
                            Toast.makeText(VerifyTransporter.this, "Invalid EN-Code !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseTransporter> call, @NonNull Throwable t) {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(VerifyTransporter.this, "I am not Connected to the Internet !", Toast.LENGTH_SHORT).show();

                    }
                });
            } catch (Exception e) {
                progress.setVisibility(View.GONE);
                Toast.makeText(VerifyTransporter.this, "Invalid EN-Code Can't Try!", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void showDialog(String dresponse, String dname, String dphone, String dunion, String dimage) {
        boolean wrapInScrollView = true;
        MaterialDialog dialog = new MaterialDialog.Builder(VerifyTransporter.this)
                .title("Verified Response")
                .customView(R.layout.driver_res_view, wrapInScrollView)
                .positiveText("OK")
                .onPositive((dialog1, which) -> {
                    dialog1.dismiss();
                })
                .show();
        View view = dialog.getCustomView();

        if (view != null) {
            TextView verifiedResponse = view.findViewById(R.id.verifiedResponse);
            verifiedResponse.setText(dresponse);
            TextView nametext = view.findViewById(R.id.nametext);
            nametext.setText(dname);
            TextView phonetext = view.findViewById(R.id.phonetext);
           phonetext.setText(dphone);
            TextView uniontext = view.findViewById(R.id.uniontext);
            uniontext.setText(dunion);
            ImageView imageView = view.findViewById(R.id.image);
            Glide.with(this)
                    .load(dimage)
                    .into(imageView);
            //  imageView.setVisibility(View.GONE);
            //  verifiedResponse.setText(message);


        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }
}
