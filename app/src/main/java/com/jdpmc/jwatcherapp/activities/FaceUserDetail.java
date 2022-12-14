package com.jdpmc.jwatcherapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jdpmc.jwatcherapp.R;
import com.jdpmc.jwatcherapp.model.VerifyResponse;
import com.jdpmc.jwatcherapp.networking.api.Service;
import com.jdpmc.jwatcherapp.networking.generator.DataGenerator;
import com.jdpmc.jwatcherapp.utils.PreferenceUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jdpmc.jwatcherapp.utils.Constants.FACE_VERIFY;

public class FaceUserDetail extends AppCompatActivity {

    public static String FVNAME;
    public static String FVPHONE ;



    @BindView(R.id.fname_text)
    TextView fname_text;
    @BindView(R.id.fphone_text)
    TextView fphone_text;
    @BindView(R.id.comment_layout)
    TextInputLayout comment_layout;
    @BindView(R.id.input_ratecomment)
    EditText input_ratecomment;
    @BindView(R.id.submitrate)
    MaterialButton submitrate;
    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;

    private String mfname, mfphone,  mfempcomment,  mfemprate;
    private Spinner rateid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_rate_view);

        ButterKnife.bind(this);

        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setTitle("Please rate me");
        progress_bar.setVisibility(View.GONE);

        Intent intent = getIntent();
        if (intent.hasExtra(FVNAME)) {
            mfname = intent.getExtras().getString(FVNAME);
            fname_text.setText(mfname);
            mfphone = intent.getExtras().getString(FVPHONE);
            fphone_text.setText(mfphone);
        }

        rateid = (Spinner) findViewById(R.id.rateid);
        rateid.setOnItemSelectedListener(new ItemSelectedListener());
        submitrate.setOnClickListener(v ->  sendToServer(mfphone));
    }

    public class ItemSelectedListener implements AdapterView.OnItemSelectedListener{

        //get first string in the array
        String firstItem = String.valueOf(rateid.getSelectedItem());
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
          firstItem.equals(String.valueOf(rateid.getSelectedItem()));
        }
        @Override
         public void onNothingSelected(AdapterView<?> arg){

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

private void sendToServer(String fphone) {
    input_ratecomment.setError(null);
    if (input_ratecomment.length() == 0) {
        comment_layout.setError("comment is required!");
    } else {
        String mfempcomment = Objects.requireNonNull(input_ratecomment.getText()).toString();
        String mfemprate = rateid.getSelectedItem().toString();

        progress_bar.setVisibility(View.VISIBLE);
        try {
            Service service = DataGenerator.createService(Service.class, FACE_VERIFY);
            String raterphone = PreferenceUtils.getPhoneNumber(getApplicationContext());
            String username = PreferenceUtils.getUsername(getApplicationContext());
            Call <VerifyResponse> call = service.uploadRate(mfempcomment, username, fphone, mfemprate, raterphone);

            call.enqueue(new Callback <VerifyResponse>() {
                @Override
                public void onResponse(Call <VerifyResponse> call, Response <VerifyResponse> response) {
                    if (response.isSuccessful()) {
                        progress_bar.setVisibility(View.GONE);
                        Toast.makeText(FaceUserDetail.this, "Rating Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        progress_bar.setVisibility(View.GONE);
                        Toast.makeText(FaceUserDetail.this, "error in uploading comment and rating data", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call <VerifyResponse> call, Throwable t) {
                    progress_bar.setVisibility(View.GONE);
                    Toast.makeText(FaceUserDetail.this, "send data", Toast.LENGTH_SHORT).show();

                }
            });

        } catch (Exception e) {
            progress_bar.setVisibility(View.GONE);
            Toast.makeText(FaceUserDetail.this, "Could not Send data", Toast.LENGTH_SHORT).show();

        }
    }
}

}
