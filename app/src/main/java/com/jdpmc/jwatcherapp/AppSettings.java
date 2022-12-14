package com.jdpmc.jwatcherapp;

import static com.jdpmc.jwatcherapp.utils.Constants.BASE_URL;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jdpmc.jwatcherapp.model.ExtinguisherResponse;
import com.jdpmc.jwatcherapp.networking.api.Service;
import com.jdpmc.jwatcherapp.networking.generator.DataGenerator;
import com.jdpmc.jwatcherapp.utils.FancyToast;
import com.jdpmc.jwatcherapp.utils.PreferenceUtils;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppSettings extends AppCompatActivity {

    private String Unconfirmed;
    private String Confirmed;

    @BindView(R.id.very_progress_bar)
    ProgressBar veryprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Unconfirmed = "Confirmed";
        //Unconfirmed = (PreferenceUtils.getConfStatus(getApplicationContext()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_settings_options);
        if ((PreferenceUtils.getConfStatus(getApplicationContext())) != ""){
            LinearLayout confirmed = findViewById(R.id.confirmed_acc);
            confirmed.setVisibility(View.VISIBLE);
        }else{
            LinearLayout notyetcon = findViewById(R.id.unconfirmed);
            notyetcon.setVisibility(View.VISIBLE);
        }

        Button show = (Button) findViewById(R.id.confirm_me);
        show.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AppSettings.this);
                //alertDialog.setTitle("Verification Code");
                alertDialog.setMessage("Enter Your J-WATCHER Verification Code");
                alertDialog.setIcon(R.drawable.ic_baseline_vpn_key_24);
                final EditText input = new EditText(AppSettings.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);


                alertDialog.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                //String username = PreferenceUtils.getUsername(getApplicationContext());
                                Boolean wantToCloseDialog = false;
                                String jwcode = input.getText().toString();
                                if (jwcode != ""){
                                    verifyPersonel(jwcode);
                                    //Toast.makeText(getApplicationContext(),"Your Code is" + jwcode , Toast.LENGTH_SHORT).show();
                                }return;
                            }
                        });
                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }

        });

        ImageView edit_user_profile = findViewById(R.id.edit_profile);
        RelativeLayout aboutUs = findViewById(R.id.about_us);
        RelativeLayout Privacy = findViewById(R.id.privacy_policy);
        RelativeLayout Ratting = findViewById(R.id.rate_us);
        RelativeLayout Terms = findViewById(R.id.term_and_con);
        RelativeLayout FAQ = findViewById(R.id.faq_questions);

        TextView username = findViewById(R.id.user_name);
        username.setText(PreferenceUtils.getUsername(getApplicationContext()));
        TextView userphone = findViewById(R.id.user_phone);
        userphone.setText(PreferenceUtils.getPhoneNumber(getApplicationContext()));
        TextView userState = findViewById(R.id.user_state);
        userState.setText(PreferenceUtils.getState(getApplicationContext()));
        TextView userLga = findViewById(R.id.user_lga);
        userLga.setText(PreferenceUtils.getLga(getApplicationContext()));
        String user_image = (PreferenceUtils.getUserImage(getApplicationContext()));
        ImageView imageView = (ImageView) findViewById(R.id.user_image);
        Glide.with(AppSettings.this).load(user_image)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .error(R.drawable.img)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }

                }).into(imageView);

        ImageView close = findViewById(R.id.close_settings);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edit_user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserDataUpdate.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                //finish();
            }
        });

    }

    private void verifyPersonel(String jwcode){
        ProgressBar progression = findViewById(R.id.very_progress_bar);
        progression.setVisibility(View.VISIBLE);
        try {
            Service service = DataGenerator.createService(Service.class, BASE_URL);
            String userphone = PreferenceUtils.getPhoneNumber(getApplicationContext());
            Call<ExtinguisherResponse> call = service.verifyJpersonnel(jwcode, userphone);
            call.enqueue(new Callback<ExtinguisherResponse>() {
                @Override
                public void onResponse(@NonNull Call<ExtinguisherResponse> call, @NonNull Response<ExtinguisherResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            ExtinguisherResponse extinguisherResponse = response.body();
                            String message = extinguisherResponse.getResponse();
                            progression.setVisibility(View.GONE);
                            FancyToast.makeText(getApplicationContext(), "Congrats Your Account is verified kindly close this window and reopen ", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                            updateUi();
                            PreferenceUtils.saveConfirmedCode("Confirmed", getApplicationContext());
                            //Toast.makeText(AppSettings.this, extinguisherResponse.getResponse(), Toast.LENGTH_LONG).show();
                        }else {

                        }
                    }
                }
                @Override
                public void onFailure(@NonNull Call<ExtinguisherResponse> call, @NonNull Throwable t) {
                    progression.setVisibility(View.GONE);
                    FancyToast.makeText(getApplicationContext(), "No Internet Connection or Invalid Code", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
            });
        } catch (Exception e) {
            progression.setVisibility(View.GONE);
            FancyToast.makeText(getApplicationContext(), "Error Invalid Code", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

        }

    }

    private void updateUi(){
        Unconfirmed = (PreferenceUtils.getConfStatus(getApplicationContext()));
        Confirmed = (PreferenceUtils.getConfStatus(getApplicationContext()));
        if (Unconfirmed == ""){
            LinearLayout notyetcon = findViewById(R.id.unconfirmed);
            notyetcon.setVisibility(View.VISIBLE);
        }
        if (Confirmed == "Confirmed"){
            LinearLayout confirmed = findViewById(R.id.confirmed_acc);
            confirmed.setVisibility(View.VISIBLE);
        }
    }

}
