package com.jdpmc.jwatcherapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jdpmc.jwatcherapp.R;
import com.jdpmc.jwatcherapp.model.VerifyResponse;
import com.jdpmc.jwatcherapp.networking.api.Service;
import com.jdpmc.jwatcherapp.networking.generator.DataGenerator;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jdpmc.jwatcherapp.utils.Constants.BASE_URL;

public class AgricSatDetail extends AppCompatActivity {

    public static String DATE = "date";
    public static String NAME = "name";
    public static String SEX = "sex";
    public static String PHONE_NO = "phoneno";
    public static String PLATENO = "plateno";
    public static String ENCODE = "encode";
    public static String PRODUCT_INFO = "productinfo";
    public static String CUGNAME = "cugname";
    public static String SESAPH_ID = "sesaphid";
    public static String DEPARTURE_POINT = "departurepoint";
    public static String DEPARTURE_TIME = "departuretime";
    public static String DESTINATION_POINT = "destinationpoint";
    public static String DESTINATION_TIME = "destinationtime";

    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.sex)
    TextView sex;
    @BindView(R.id.phoneno)
    TextView phoneno;
    @BindView(R.id.plateno)
    TextView plateno;
    @BindView(R.id.encode)
    TextView encode;
    @BindView(R.id.productinfo)
    TextView productinfo;
    @BindView(R.id.cugname)
    TextView cugname;
    @BindView(R.id.sesaphId)
    TextView sesaphId;
    @BindView(R.id.departurePoint)
    TextView departurePoint;
    @BindView(R.id.destinationPoint)
    TextView destinationPoint;
    @BindView(R.id.departureTime)
    TextView departureTime;
    @BindView(R.id.destinationTime)
    TextView destinationTime;
    @BindView(R.id.submit)
    MaterialButton submit;
    @BindView(R.id.progress)
    ProgressBar progress;

    private String mdate, detailName, msex, mphoneno, mplateno, mencode, mproductinfo, mcugname,
    msesaphId, mdeparturePoint, mdepartureTime, mdestinationPoint, mdestinationTime;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.agric_sat_detail);

            ButterKnife.bind(this);


            ButterKnife.bind(this);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setTitle("Receipt");
        progress.setVisibility(View.GONE);

        Intent intent = getIntent();
        if (intent.hasExtra(NAME)) {
            mdate = intent.getExtras().getString(DATE);
            detailName = intent.getExtras().getString(NAME);
            msex = intent.getExtras().getString(SEX);
            mphoneno = intent.getExtras().getString(PHONE_NO);
            mplateno = intent.getExtras().getString(PLATENO);
            mencode = intent.getExtras().getString(ENCODE);
            mproductinfo = intent.getExtras().getString(PRODUCT_INFO);
            mcugname = intent.getExtras().getString(CUGNAME);
            msesaphId = intent.getExtras().getString(SESAPH_ID);
            mdeparturePoint = intent.getExtras().getString(DEPARTURE_POINT);
            mdepartureTime = intent.getExtras().getString(DEPARTURE_TIME);
            mdestinationPoint = intent.getExtras().getString(DESTINATION_POINT);
            mdestinationTime = intent.getExtras().getString(DESTINATION_TIME);

            date.setText(mdate);
            name.setText(detailName);
            sex.setText(msex);
            phoneno.setText(mphoneno);
            plateno.setText(mplateno);
            encode.setText(mencode);
            productinfo.setText(mproductinfo);
            cugname.setText(mcugname);
            sesaphId.setText(msesaphId);
            departurePoint.setText(mdeparturePoint);
            departureTime.setText(mdepartureTime);
            destinationPoint.setText(mdestinationPoint);
            destinationTime.setText(mdestinationTime);

        }

        submit.setOnClickListener(v -> uploadSat());
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

    private void uploadSat() {
        progress.setVisibility(View.VISIBLE);
        try {
            Service service = DataGenerator.createService(Service.class, BASE_URL);
            Call<VerifyResponse> call = service.uploadAgric(detailName, msex,  mphoneno, mplateno,
                    mencode, mproductinfo, mcugname, msesaphId, mdeparturePoint, mdepartureTime, mdestinationPoint, mdestinationTime);

            call.enqueue(new Callback<VerifyResponse>() {
                @Override
                public void onResponse(Call<VerifyResponse> call, Response<VerifyResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            VerifyResponse verifyResponse = response.body();
                            progress.setVisibility(View.GONE);
                            String resp_value = verifyResponse.getResponse();
                            showDialog(resp_value);
                        }
                    } else {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(AgricSatDetail.this, "error in uploading AgricSat data", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<VerifyResponse> call, Throwable t) {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(AgricSatDetail.this, "I am not Connected to the Internet !", Toast.LENGTH_SHORT).show();

                }
            });

        } catch (Exception e) {
            progress.setVisibility(View.GONE);
            Toast.makeText(AgricSatDetail.this, "error in uploading AgricSat data", Toast.LENGTH_SHORT).show();

        }
    }

    private void showDialog(String text) {
        boolean wrapInScrollView = true;
        MaterialDialog dialog = new MaterialDialog.Builder(AgricSatDetail.this)

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
