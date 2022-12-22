package com.jdpmc.jwatcherapp;

import static com.jdpmc.jwatcherapp.utils.Constants.VERIFY_BASE_URL;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.jdpmc.jwatcherapp.model.VideoReportDetails;
import com.jdpmc.jwatcherapp.networking.api.Service;
import com.jdpmc.jwatcherapp.networking.generator.DataGenerator;

public class view_and_comment extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_view_activity);
        Intent intent = getIntent();
        String str = intent.getStringExtra("message_key");
        //receiver_msg.setText(str);
        String callid = (str);
        //String callid = (live_vid);
        //verifyPsid(callid);
        Toast.makeText(view_and_comment.this, callid, Toast.LENGTH_SHORT).show();

        ImageView close = findViewById(R.id.stop_close_live3);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

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
