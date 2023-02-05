package com.jdpmc.jwatcherapp;

import static com.jdpmc.jwatcherapp.utils.Constants.CONFIRM_ACC_BASE_URL;
import static com.jdpmc.jwatcherapp.utils.Constants.REGISTER_USER_BASE_URL;
import static com.jdpmc.jwatcherapp.utils.Constants.USER_COMMENT_BASE_URL;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import androidx.browser.customtabs.CustomTabsIntent;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.jdpmc.jwatcherapp.model.ExtinguisherResponse;
import com.jdpmc.jwatcherapp.model.LoginResponse;
import com.jdpmc.jwatcherapp.model.Profile;
import com.jdpmc.jwatcherapp.model.RatingResponse;
import com.jdpmc.jwatcherapp.networking.api.Service;
import com.jdpmc.jwatcherapp.networking.generator.DataGenerator;
import com.jdpmc.jwatcherapp.utils.FancyToast;
import com.jdpmc.jwatcherapp.utils.PreferenceUtils;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppSettings extends AppCompatActivity {

    private String Unconfirmed;
    private String Confirmed;
    private String rate1, rate2, rate3, rate4, rate5, clearstr;

    @BindView(R.id.very_progress_bar)
    ProgressBar veryprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        ImageView notify_option = findViewById(R.id.my_notification);
        String user_key = PreferenceUtils.getUserkey(getApplicationContext());
        if (user_key.equals("")){
                notify_option.setImageResource(R.drawable.enable_notification);
                FancyToast.makeText(getApplicationContext(), "Notifications are Disabled Kindly Enable using the bell icon", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                notify_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            String TAG = null;
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "getInstanceId token failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            String token = Objects.requireNonNull(task.getResult()).getToken();

                            // Log and toast
                            Log.d(TAG, "This is the token " +token);
                            // FancyToast.makeText(getApplicationContext(), token, FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                            PreferenceUtils.saveUserkey(token, getApplicationContext());
                            EnableNotification();
                        }
                    });
                }
            });

        }else{
            notify_option.setImageResource(R.drawable.disable_notification);
        }

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
        RelativeLayout about = findViewById(R.id.about_us);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://j-watcher.org/Apps/Mobile/jdabout";
                //String url = "https://j-watcher.org/about/about.html";
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                customTabsIntent.launchUrl(getApplicationContext(), Uri.parse(url));
            }
        });

        RelativeLayout terms = findViewById(R.id.term_and_con);
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout privacy = findViewById(R.id.privacy_text);
                privacy.setVisibility(View.GONE);
                RelativeLayout faq = findViewById(R.id.faq_text);
                faq.setVisibility(View.GONE);
                RelativeLayout ratting = findViewById(R.id.rate_us_area);
                ratting.setVisibility(View.GONE);
                RelativeLayout termstext = findViewById(R.id.terms_text);
                termstext.setVisibility(View.VISIBLE);
                //FancyToast.makeText(getApplicationContext(), "Terms clicked", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
            }
        });

        RelativeLayout privacy = findViewById(R.id.privacy_policy);
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RelativeLayout terms = findViewById(R.id.terms_text);
                terms.setVisibility(View.GONE);
                RelativeLayout faq = findViewById(R.id.faq_text);
                faq.setVisibility(View.GONE);
                RelativeLayout ratting = findViewById(R.id.rate_us_area);
                ratting.setVisibility(View.GONE);
                RelativeLayout privacy = findViewById(R.id.privacy_text);
                privacy.setVisibility(View.VISIBLE);
                //FancyToast.makeText(getApplicationContext(), "Privacy clicked", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
            }
        });

        RelativeLayout ratting = findViewById(R.id.rate_us);
        ratting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout privacy = findViewById(R.id.privacy_text);
                privacy.setVisibility(View.GONE);
                RelativeLayout faq = findViewById(R.id.faq_text);
                faq.setVisibility(View.GONE);
                RelativeLayout termstext = findViewById(R.id.terms_text);
                termstext.setVisibility(View.GONE);
                RelativeLayout ratting = findViewById(R.id.rate_us_area);
                ratting.setVisibility(View.VISIBLE);
                //FancyToast.makeText(getApplicationContext(), "Ratting clicked", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
            }
        });

        RelativeLayout faq = findViewById(R.id.faq_questions);
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout privacy = findViewById(R.id.privacy_text);
                privacy.setVisibility(View.GONE);
                RelativeLayout ratting = findViewById(R.id.rate_us_area);
                ratting.setVisibility(View.GONE);
                RelativeLayout termstext = findViewById(R.id.terms_text);
                termstext.setVisibility(View.GONE);
                RelativeLayout faq = findViewById(R.id.faq_text);
                faq.setVisibility(View.VISIBLE);
                //FancyToast.makeText(getApplicationContext(), "FAQ clicked", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
            }
        });

        /////Rating///
        ImageView stars1 = findViewById(R.id.star_1);
        ImageView stars2 = findViewById(R.id.star_2);
        ImageView stars3 = findViewById(R.id.star_3);
        ImageView stars4 = findViewById(R.id.star_4);
        ImageView stars5 = findViewById(R.id.star_5);

        String starrt1 = PreferenceUtils.getStar1(getApplicationContext());
        String starrt2 = PreferenceUtils.getStar2(getApplicationContext());
        String starrt3 = PreferenceUtils.getStar3(getApplicationContext());
        String starrt4 = PreferenceUtils.getStar4(getApplicationContext());
        String starrt5 = PreferenceUtils.getStar5(getApplicationContext());
        //stars1.setImageResource(android.R.drawable.btn_star_big_off);
        if (starrt5.equals("")){
            stars5.setImageResource(android.R.drawable.btn_star_big_off);
            stars1.setImageResource(android.R.drawable.btn_star_big_off);
            stars2.setImageResource(android.R.drawable.btn_star_big_off);
            stars3.setImageResource(android.R.drawable.btn_star_big_off);
            stars4.setImageResource(android.R.drawable.btn_star_big_off);
        }else{
            //FancyToast.makeText(getApplicationContext(), "No Stars", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
        }

        if (Objects.equals(starrt1, "")){
            stars1.setImageResource(android.R.drawable.btn_star_big_off);
        }
        if (Objects.equals(starrt2, "")){
            stars2.setImageResource(android.R.drawable.btn_star_big_off);
        }
        if (Objects.equals(starrt3, "")){
            stars3.setImageResource(android.R.drawable.btn_star_big_off);
        }
        if (Objects.equals(starrt4, "")){
            stars4.setImageResource(android.R.drawable.btn_star_big_off);
        }

        if (Objects.equals(starrt5, "5")){
            stars1.setImageResource(android.R.drawable.btn_star_big_on);
            stars2.setImageResource(android.R.drawable.btn_star_big_on);
            stars3.setImageResource(android.R.drawable.btn_star_big_on);
            stars4.setImageResource(android.R.drawable.btn_star_big_on);
            stars5.setImageResource(android.R.drawable.btn_star_big_on);
        }
        if (Objects.equals(starrt1, "1")){
            stars1.setImageResource(android.R.drawable.btn_star_big_on);
        }
        if (Objects.equals(starrt2, "2")){
            stars2.setImageResource(android.R.drawable.btn_star_big_on);
        }
        if (Objects.equals(starrt3, "3")){
            stars3.setImageResource(android.R.drawable.btn_star_big_on);
        }
        if (Objects.equals(starrt4, "4")){
            stars4.setImageResource(android.R.drawable.btn_star_big_on);
        }
        //rate1 = "1"; rate2 = "2"; rate3 = "3"; rate4 = "4"; rate5 = "5";
        rate1 = ""; rate2 = ""; rate3 = ""; rate4 = ""; rate5 = "";
        stars1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stars1.setImageResource(android.R.drawable.btn_star_big_on);
                String rated = (PreferenceUtils.getStar1(getApplicationContext()));
                if (rated.equals("1")){
                    /*PreferenceUtils.saveStar1(rate1, getApplicationContext());
                    PreferenceUtils.saveStar2(rate2, getApplicationContext());
                    PreferenceUtils.saveStar3(rate3, getApplicationContext());
                    PreferenceUtils.saveStar4(rate4, getApplicationContext());
                    PreferenceUtils.saveStar5(rate5, getApplicationContext());*/
                    FancyToast.makeText(getApplicationContext(), "Thanks for Rating us", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                }else {
                    RelativeLayout rate_com =findViewById(R.id.rate_comment);
                    rate_com.setVisibility(View.VISIBLE);
                    TextView input_post_rated = findViewById(R.id.rate_string);
                    input_post_rated.setText("1");
                    FancyToast.makeText(getApplicationContext(), "1 Star Rating", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                }
            }
        });
        stars2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stars1.setImageResource(android.R.drawable.btn_star_big_on);
                stars2.setImageResource(android.R.drawable.btn_star_big_on);
                String rated = (PreferenceUtils.getStar2(getApplicationContext()));
                if (rated.equals("2")){
                    FancyToast.makeText(getApplicationContext(), "Thanks for Rating us", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                }else{
                    RelativeLayout rate_com =findViewById(R.id.rate_comment);
                    rate_com.setVisibility(View.VISIBLE);
                    TextView input_post_rated = findViewById(R.id.rate_string);
                    input_post_rated.setText("2");
                    FancyToast.makeText(getApplicationContext(), "2 Stars Rating", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                }
            }
        });
        stars3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stars1.setImageResource(android.R.drawable.btn_star_big_on);
                stars2.setImageResource(android.R.drawable.btn_star_big_on);
                stars3.setImageResource(android.R.drawable.btn_star_big_on);
                String rated = (PreferenceUtils.getStar3(getApplicationContext()));
                if (rated.equals("3")){
                    FancyToast.makeText(getApplicationContext(), "Thanks for Rating us", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                }else{
                    RelativeLayout rate_com =findViewById(R.id.rate_comment);
                    rate_com.setVisibility(View.VISIBLE);
                    TextView input_post_rated = findViewById(R.id.rate_string);
                    input_post_rated.setText("3");
                    FancyToast.makeText(getApplicationContext(), "3 Stars Rating", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                }
            }
        });
        stars4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stars1.setImageResource(android.R.drawable.btn_star_big_on);
                stars2.setImageResource(android.R.drawable.btn_star_big_on);
                stars3.setImageResource(android.R.drawable.btn_star_big_on);
                stars4.setImageResource(android.R.drawable.btn_star_big_on);
                String rated = (PreferenceUtils.getStar4(getApplicationContext()));
                if (rated.equals("4")){
                    FancyToast.makeText(getApplicationContext(), "Thanks for Rating us", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                }else{
                    RelativeLayout rate_com =findViewById(R.id.rate_comment);
                    rate_com.setVisibility(View.VISIBLE);
                    TextView input_post_rated = findViewById(R.id.rate_string);
                    input_post_rated.setText("4");
                    FancyToast.makeText(getApplicationContext(), "4 Stars Rating", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                }
            }
        });
        stars5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stars1.setImageResource(android.R.drawable.btn_star_big_on);
                stars2.setImageResource(android.R.drawable.btn_star_big_on);
                stars3.setImageResource(android.R.drawable.btn_star_big_on);
                stars4.setImageResource(android.R.drawable.btn_star_big_on);
                stars5.setImageResource(android.R.drawable.btn_star_big_on);
                String rated = (PreferenceUtils.getStar5(getApplicationContext()));
                if (rated.equals("4")){
                    FancyToast.makeText(getApplicationContext(), "Thanks for Rating us", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                }else{
                    RelativeLayout rate_com =findViewById(R.id.rate_comment);
                    rate_com.setVisibility(View.VISIBLE);
                    TextView input_post_rated = findViewById(R.id.rate_string);
                    input_post_rated.setText("5");
                    FancyToast.makeText(getApplicationContext(), "5 Stars Rating", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                }
            }
        });
        Button submstr = findViewById(R.id.ratebtn);
        submstr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input_rate_comment = (EditText) findViewById(R.id.my_comment);
                TextView input_post_rated = findViewById(R.id.rate_string);
                String star = input_post_rated.getText().toString();
                String comment = input_rate_comment.getText().toString();
                if (comment.matches("")) {
                    FancyToast.makeText(getApplicationContext(), "You did not enter a comment", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }else{
                    Rateme(star);
                }
            }
        });
        /////////////
        String cofirmed = PreferenceUtils.getConfirmedCode(getApplicationContext());
        if (cofirmed.equals("")){
            verifyActivePersonel();
        }else {

        }

    }

    private void Rateme(String star) {
        ProgressBar progress = findViewById(R.id.very_progress_bar);
        progress.setVisibility(View.VISIBLE);
        try {
            Service service = DataGenerator.createService(Service.class, USER_COMMENT_BASE_URL);
            EditText input_post_comment = findViewById(R.id.my_comment);
            String comment = input_post_comment.getText().toString().trim();
            String userphone = PreferenceUtils.getPhoneNumber(getApplicationContext());
            retrofit2.Call<RatingResponse> call = service.RateApp(star, userphone, comment);
            call.enqueue(new retrofit2.Callback<RatingResponse>() {
                @Override
                public void onResponse(@NonNull retrofit2.Call<RatingResponse> call, @NonNull retrofit2.Response<RatingResponse> response) {
                    progress.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        EditText input_post_comment = findViewById(R.id.my_comment);
                        input_post_comment.setText("");
                        //FancyToast.makeText(context.getApplicationContext(), "Like Taken", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        if (response.body() != null) {
                            RatingResponse RatingResponse = response.body();
                            String rate1 = RatingResponse.getRatestr1();
                            String rate2 = RatingResponse.getRatestr2();
                            String rate3 = RatingResponse.getRatestr3();
                            String rate4 = RatingResponse.getRatestr4();
                            String rate5 = RatingResponse.getRatestr5();
                            String success = RatingResponse.getSuccessres();
                            String isErr = RatingResponse.getCommErr();
                            if (rate1 != null){
                                PreferenceUtils.saveStar1(rate1, getApplicationContext());
                                FancyToast.makeText(getApplicationContext(),success, FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                            }else if (rate2 != null){
                                rate1 = "1";
                                PreferenceUtils.saveStar1(rate1, getApplicationContext());
                                PreferenceUtils.saveStar2(rate2, getApplicationContext());
                                FancyToast.makeText(getApplicationContext(),success, FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                            }else if (rate3 != null){
                                rate1 = "1";
                                rate2 = "2";
                                PreferenceUtils.saveStar1(rate1, getApplicationContext());
                                PreferenceUtils.saveStar2(rate2, getApplicationContext());
                                PreferenceUtils.saveStar3(rate3, getApplicationContext());
                                FancyToast.makeText(getApplicationContext(),success, FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                            }else if (rate4 != null){
                                rate1 = "1";
                                rate2 = "2";
                                rate3 = "3";
                                PreferenceUtils.saveStar1(rate1, getApplicationContext());
                                PreferenceUtils.saveStar2(rate2, getApplicationContext());
                                PreferenceUtils.saveStar3(rate3, getApplicationContext());
                                PreferenceUtils.saveStar4(rate4, getApplicationContext());
                                FancyToast.makeText(getApplicationContext(),success, FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                            }else if (rate5 != null){
                                rate1 = "1";
                                rate2 = "2";
                                rate3 = "3";
                                rate4 = "4";
                                PreferenceUtils.saveStar1(rate1, getApplicationContext());
                                PreferenceUtils.saveStar2(rate2, getApplicationContext());
                                PreferenceUtils.saveStar3(rate3, getApplicationContext());
                                PreferenceUtils.saveStar4(rate4, getApplicationContext());
                                PreferenceUtils.saveStar5(rate5, getApplicationContext());
                                FancyToast.makeText(getApplicationContext(),success, FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                            }
                        }else {
                            FancyToast.makeText(getApplicationContext(), "Comment Failed", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        }
                    }
                }
                @Override
                public void onFailure(@NonNull Call<RatingResponse> call, @NonNull Throwable t) {
                    progress.setVisibility(View.GONE);
                    FancyToast.makeText(getApplicationContext(), "No Internet", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
            });
        } catch (Exception e) {
            FancyToast.makeText(getApplicationContext(), "Comment Failed", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

        }
    }

    private void verifyPersonel(String jwcode){
        ProgressBar progression = findViewById(R.id.very_progress_bar);
        progression.setVisibility(View.VISIBLE);
        try {
            Service service = DataGenerator.createService(Service.class, CONFIRM_ACC_BASE_URL);
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
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
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

    private void verifyActivePersonel(){
        ProgressBar progression = findViewById(R.id.very_progress_bar);
        progression.setVisibility(View.VISIBLE);
        try {
            Service service = DataGenerator.createService(Service.class, CONFIRM_ACC_BASE_URL);
            String sername = PreferenceUtils.getUsername(getApplicationContext());
            String userphone = PreferenceUtils.getPhoneNumber(getApplicationContext());
            Call<ExtinguisherResponse> call = service.verifyexistJpersonnel(sername, userphone);
            call.enqueue(new Callback<ExtinguisherResponse>() {
                @Override
                public void onResponse(@NonNull Call<ExtinguisherResponse> call, @NonNull Response<ExtinguisherResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            ExtinguisherResponse extinguisherResponse = response.body();
                            String message = extinguisherResponse.getResponse();
                            progression.setVisibility(View.GONE);
                            FancyToast.makeText(getApplicationContext(), "Congrats Your Account has already been verified kindly close this window and reopen ", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                            updateUi();
                            PreferenceUtils.saveConfirmedCode("Confirmed", getApplicationContext());
                            finish();
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
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

    private void EnableNotification() {
        ProgressBar progression = findViewById(R.id.very_progress_bar);
        ImageView notify_option = findViewById(R.id.my_notification);
        progression.setVisibility(View.VISIBLE);
        String name = PreferenceUtils.getUsername(getApplicationContext());
        String phonenumber = PreferenceUtils.getPhoneNumber(getApplicationContext());
        String usertoken =   PreferenceUtils.getUserkey(getApplicationContext());
        try {
            Service service = DataGenerator.createService(Service.class, REGISTER_USER_BASE_URL);
            Call<LoginResponse> call = service.loginUser(name, phonenumber, usertoken);

            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            LoginResponse loginResponse = response.body();
                            List<Profile> profileList = loginResponse.getProfile();
                            if (profileList != null) {
                                FancyToast.makeText(getApplicationContext(), "Notifications Enabled", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                                notify_option.setImageResource(R.drawable.disable_notification);
                            }
                            progression.setVisibility(View.GONE);
                        }
                    } else {
                        progression.setVisibility(View.GONE);
                        FancyToast.makeText(getApplicationContext(), "error Enabling Notification", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                    progression.setVisibility(View.GONE);
                    FancyToast.makeText(getApplicationContext(), "I am not Connected to the Internet !", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
            });
        } catch (Exception e) {
            progression.setVisibility(View.GONE);
            FancyToast.makeText(getApplicationContext(), "error Enabling Notification", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
        }
    }

    private void updateUi(){
        Unconfirmed = (PreferenceUtils.getConfStatus(getApplicationContext()));
        Confirmed = (PreferenceUtils.getConfStatus(getApplicationContext()));
        if (Unconfirmed.equals("")){
            LinearLayout notyetcon = findViewById(R.id.unconfirmed);
            notyetcon.setVisibility(View.VISIBLE);
        }
        if (Confirmed.equals("Confirmed")){
            LinearLayout confirmed = findViewById(R.id.confirmed_acc);
            confirmed.setVisibility(View.VISIBLE);
        }
    }

}
