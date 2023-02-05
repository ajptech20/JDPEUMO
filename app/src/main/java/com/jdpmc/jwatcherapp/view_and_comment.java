package com.jdpmc.jwatcherapp;

import static com.jdpmc.jwatcherapp.utils.Constants.USER_COMMENT_BASE_URL;
import static com.jdpmc.jwatcherapp.utils.Constants.VERIFY_BASE_URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jdpmc.jwatcherapp.model.CommentResponse;
import com.jdpmc.jwatcherapp.model.VideoReportDetails;
import com.jdpmc.jwatcherapp.networking.api.Service;
import com.jdpmc.jwatcherapp.networking.generator.DataGenerator;
import com.jdpmc.jwatcherapp.utils.FancyToast;
import com.jdpmc.jwatcherapp.utils.PreferenceUtils;

import java.util.Objects;

import retrofit2.Call;

public class view_and_comment extends Activity {
    public TextView textViewCommentsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_view_activity);
        Intent intent = getIntent();
        String str = intent.getStringExtra("message_key");
        String poster_image = intent.getStringExtra("commenter_userimg_key");
        String comments_count = intent.getStringExtra("comment_count_key");
        String reptype = intent.getStringExtra("reptype_key");
        String state = intent.getStringExtra("state_key");
        String imgurl = intent.getStringExtra("imgurl_key");
        String comment = intent.getStringExtra("comment_key");
        String username = intent.getStringExtra("username_key");
        String date = intent.getStringExtra("date_key");
        String areaflag = intent.getStringExtra("area_key");
        String repstatus = intent.getStringExtra("repstatus_key");
        String type = intent.getStringExtra("rep_type");
        TextView nametext = findViewById(R.id.poster_name);
        nametext.setText(username);
        TextView posttype = findViewById(R.id.post_source);
        posttype.setText(reptype);
        /*TextView statetext = findViewById(R.id.rep_state);
        statetext.setText(state);
        TextView RepDate = findViewById(R.id.report_date);
        RepDate.setText(date);
        TextView RepArea = findViewById(R.id.rep_type);
        RepArea.setText(areaflag);*/
        TextView ReporComment = findViewById(R.id.rep_comment);
        ReporComment.setText(comment);
        ImageView postimageView = (ImageView) findViewById(R.id.post_image_view);
        Glide.with(view_and_comment.this).load(imgurl)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .thumbnail(0.05f)
                .fitCenter()
                .transition(DrawableTransitionOptions.withCrossFade())
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

                }).into(postimageView);
        ImageView reporter_img = (ImageView) findViewById(R.id.reporter_image);
        Glide.with(view_and_comment.this)
                .load(poster_image)
                .into(reporter_img);

        String user_image = (PreferenceUtils.getUserImage(getApplicationContext()));
        TextView counts = findViewById(R.id.comment_counts);
        counts.setText(comments_count);
        ImageView imageView = (ImageView) findViewById(R.id.userimage);
        Glide.with(view_and_comment.this)
                .load(user_image)
                .into(imageView);
        ImageView ucomm_img = (ImageView) findViewById(R.id.comenter_img);
        Glide.with(view_and_comment.this)
                .load(poster_image)
                .into(ucomm_img);
        String postpid = (str);
        textViewCommentsCount = (TextView) findViewById(R.id.comment_counts);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input_post_comment = (EditText) findViewById(R.id.my_comment);
                String comment = input_post_comment.getText().toString();
                if (comment.matches("")) {
                    FancyToast.makeText(getApplicationContext(), "You did not enter a comment", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }else{
                    if (Objects.equals(type, "Hot Zone")){
                        HotCommentOnPost(postpid);
                        //FancyToast.makeText(getApplicationContext(), "Yes Hot Zone Comment Api needed", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                    }else if (Objects.equals(type, "Resource")){
                        RscCommentOnPost(postpid);
                        //FancyToast.makeText(getApplicationContext(), "Yes Resource Comment Api needed", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                    }else {
                        CommentOnPost(postpid);
                    }

                }
                //Toast.makeText(view_and_comment.this, postpid, Toast.LENGTH_SHORT).show();
            }
        });



        ImageView close = findViewById(R.id.stop_close_live3);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void CommentOnPost(String postpid) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.prgress);
        progressBar.setVisibility(View.VISIBLE);
        try {
            Service service = DataGenerator.createService(Service.class, USER_COMMENT_BASE_URL);
            EditText input_post_comment = findViewById(R.id.my_comment);
            String comment = input_post_comment.getText().toString().trim();
            String userphone = PreferenceUtils.getPhoneNumber(getApplicationContext());
            retrofit2.Call<CommentResponse> call = service.commentApost(postpid, userphone, comment);
            call.enqueue(new retrofit2.Callback<CommentResponse>() {
                @Override
                public void onResponse(@NonNull retrofit2.Call<CommentResponse> call, @NonNull retrofit2.Response<CommentResponse> response) {
                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        EditText input_app_rate = findViewById(R.id.my_comment);
                        input_app_rate.setText("");
                        //FancyToast.makeText(context.getApplicationContext(), "Like Taken", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        if (response.body() != null) {
                            CommentResponse likeResponse = response.body();
                            String commcount = likeResponse.getCommCount();
                            String success = likeResponse.getSuccessres();
                            String isErr = likeResponse.getCommErr();
                            if (commcount != null){
                                String icomment = likeResponse.getCommCount();
                                textViewCommentsCount.setText(icomment);
                                //liks_count.setVisibility(View.GONE);
                                FancyToast.makeText(getApplicationContext(),success, FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                            }
                        }else {
                            FancyToast.makeText(getApplicationContext(), "Comment Failed", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        }
                    }
                }
                @Override
                public void onFailure(@NonNull Call<CommentResponse> call, @NonNull Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    FancyToast.makeText(getApplicationContext(), "No Internet", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
            });
        } catch (Exception e) {
            FancyToast.makeText(getApplicationContext(), "Comment Failed", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

        }
    }

    /////////Hot Comment//////
    private void HotCommentOnPost(String postpid) {
        try {
            Service service = DataGenerator.createService(Service.class, USER_COMMENT_BASE_URL);
            EditText input_post_comment = findViewById(R.id.my_comment);
            String comment = input_post_comment.getText().toString().trim();
            String userphone = PreferenceUtils.getPhoneNumber(getApplicationContext());
            retrofit2.Call<CommentResponse> call = service.hotcommentApost(postpid, userphone, comment);
            call.enqueue(new retrofit2.Callback<CommentResponse>() {
                @Override
                public void onResponse(@NonNull retrofit2.Call<CommentResponse> call, @NonNull retrofit2.Response<CommentResponse> response) {
                    if (response.isSuccessful()) {
                        EditText input_app_rate = findViewById(R.id.my_comment);
                        input_app_rate.setText("");
                        //FancyToast.makeText(context.getApplicationContext(), "Like Taken", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        if (response.body() != null) {
                            CommentResponse likeResponse = response.body();
                            String commcount = likeResponse.getCommCount();
                            String success = likeResponse.getSuccessres();
                            String isErr = likeResponse.getCommErr();
                            if (commcount != null){
                                String icomment = likeResponse.getCommCount();
                                textViewCommentsCount.setText(icomment);
                                //liks_count.setVisibility(View.GONE);
                                FancyToast.makeText(getApplicationContext(),success, FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                            }
                        }else {
                            FancyToast.makeText(getApplicationContext(), "Comment Failed", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        }
                    }
                }
                @Override
                public void onFailure(@NonNull Call<CommentResponse> call, @NonNull Throwable t) {
                    //progression.setVisibility(View.GONE);
                    FancyToast.makeText(getApplicationContext(), "No Internet", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
            });
        } catch (Exception e) {
            FancyToast.makeText(getApplicationContext(), "Comment Failed", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

        }
    }
    /////////////////////////

    /////////Use full Resource Comment//////
    private void RscCommentOnPost(String postpid) {
        try {
            Service service = DataGenerator.createService(Service.class, USER_COMMENT_BASE_URL);
            EditText input_post_comment = findViewById(R.id.my_comment);
            String comment = input_post_comment.getText().toString().trim();
            String userphone = PreferenceUtils.getPhoneNumber(getApplicationContext());
            retrofit2.Call<CommentResponse> call = service.ResourceCommentOnPost(postpid, userphone, comment);
            call.enqueue(new retrofit2.Callback<CommentResponse>() {
                @Override
                public void onResponse(@NonNull retrofit2.Call<CommentResponse> call, @NonNull retrofit2.Response<CommentResponse> response) {
                    if (response.isSuccessful()) {
                        EditText input_app_rate = findViewById(R.id.my_comment);
                        input_app_rate.setText("");
                        //FancyToast.makeText(context.getApplicationContext(), "Like Taken", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        if (response.body() != null) {
                            CommentResponse likeResponse = response.body();
                            String commcount = likeResponse.getCommCount();
                            String success = likeResponse.getSuccessres();
                            String isErr = likeResponse.getCommErr();
                            if (commcount != null){
                                String icomment = likeResponse.getCommCount();
                                textViewCommentsCount.setText(icomment);
                                //liks_count.setVisibility(View.GONE);
                                FancyToast.makeText(getApplicationContext(),success, FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                            }
                        }else {
                            FancyToast.makeText(getApplicationContext(), "Comment Failed", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        }
                    }
                }
                @Override
                public void onFailure(@NonNull Call<CommentResponse> call, @NonNull Throwable t) {
                    //progression.setVisibility(View.GONE);
                    FancyToast.makeText(getApplicationContext(), "No Internet", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
            });
        } catch (Exception e) {
            FancyToast.makeText(getApplicationContext(), "Comment Failed", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

        }
    }
    /////////////////////////









    // validation on all fields
    public Boolean verifyFields() {
        return true;
    }

    private void verifyPsid(String callid) {
        if (verifyFields()) {
            //progress.setVisibility(View.VISIBLE);
            try {
                Service service = DataGenerator.createService(Service.class, VERIFY_BASE_URL);
                retrofit2.Call<VideoReportDetails> call = service.getlivevideos(callid);

                call.enqueue(new retrofit2.Callback<VideoReportDetails>() {
                    @Override
                    public void onResponse(@NonNull retrofit2.Call<VideoReportDetails> call, @NonNull retrofit2.Response<VideoReportDetails> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                VideoReportDetails verifyResponse = response.body();
                                String sgresponse = verifyResponse.getResponse();
                                String sgname = verifyResponse.getSGname();
                                String sgphone = verifyResponse.getSGphone();
                                String sgstate = verifyResponse.getSGstate();
                                String date = verifyResponse.getSGdate();

                                String sgreptype = verifyResponse.getSgreptype();
                                String sgreparea = verifyResponse.getSgreparea();
                                String sgcomment = verifyResponse.getSGcomment();
                                String sgstatuse = verifyResponse.getSgstatuse();

                                String videosrc = verifyResponse.getSGvideourl();
                                String imageUrl = verifyResponse.getImage();
                                //progress.setVisibility(View.GONE);
                                showDialog(sgresponse, sgname, sgphone, sgstate, imageUrl, videosrc, sgreptype, sgreparea, sgcomment, sgstatuse, date);
                                String resourceuri = (videosrc);
                            }
                        } else {
                            //progress.setVisibility(View.GONE);
                            Toast.makeText(view_and_comment.this, "Invalid PSID Service Code !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull retrofit2.Call<VideoReportDetails> call, @NonNull Throwable t) {
                        //progress.setVisibility(View.GONE);
                        Toast.makeText(view_and_comment.this, "I am not Connected to the Internet !", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                //progress.setVisibility(View.GONE);
                Toast.makeText(view_and_comment.this, "Invalid PSID Service Code !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDialog(String sgresponse, String sgname, String sgphone, String sgstate, String imageUrl, String videosrc, String sgreptype, String sgreparea, String sgcomment, String sgstatuse, String date) {
        //TextView verifiedResponse = view.findViewById(R.id.verifiedResponse);
        //verifiedResponse.setText(sgresponse);
        /*TextView nametext = findViewById(R.id.poster_name);
        nametext.setText(sgname);
        TextView statustext = findViewById(R.id.brstatus);
        statustext.setText(sgstatuse);
        TextView psttype = findViewById(R.id.post_type);
        psttype.setText(sgstatuse);
        TextView statetext = findViewById(R.id.rep_state);
        statetext.setText(sgstate);
        TextView Areaofrep = findViewById(R.id.vidarea);
        Areaofrep.setText(sgreparea);
        TextView ReportType = findViewById(R.id.rep_type);
        ReportType.setText(sgreptype);
        TextView RepDate = findViewById(R.id.report_date);
        RepDate.setText(date);
        TextView ReporComment = findViewById(R.id.rep_comment);
        ReporComment.setText(sgcomment);
        ImageView imageView = (ImageView) findViewById(R.id.reporter_image);
        Glide.with(view_and_comment.this)
                .load(imageUrl)
                .into(imageView);*/
    }

}
