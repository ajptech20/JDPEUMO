package com.jdpmc.jwatcherapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mancj.slideup.SlideUp;

import java.util.Objects;

public class Hot_Image_post_viewer extends Activity {
    private static final String APPLICATION_ID = "mOQq8sbExROCWxFjbkGoaA";
    private static final String API_KEY = "FKmAFDPiQFccMBe9Pp8d5Q";
    private SlideUp slideUp;
    private SlideUp slideUpnewLive;
    private View dim;
    private View slideView;
    private View liveVid;
    FloatingActionButton flGoLive, flShortVid, flImagePost;
    FloatingActionButton mMainbutton;
    Boolean isAllFabsVisible;

    private static final String resourceuri = "https://cdn.bambuser.net/broadcasts/14905708-614c-4594-82b5-204480d79088?da_signature_method=HMAC-SHA256&da_id=9e1b1e83-657d-7c83-b8e7-0b782ac9543a&da_timestamp=1668467523&da_static=1&da_ttl=0&da_signature=803fc706e07267c94297983bc5ae2728d7b415ff1efc9b72f6a5580df814d442";
    private static final String live_vid = "amlive";
    //TextView receiver_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView txtMarquee;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hot_image_viewer_post);
        txtMarquee = (TextView) findViewById(R.id.rep_comment);
        txtMarquee.setSelected(true);

        Intent intent = getIntent();
        String state = intent.getStringExtra("state_key");
        String lga = intent.getStringExtra("lga_key");
        String town = intent.getStringExtra("town_key");
        String comment = intent.getStringExtra("comment_key");
        String username = intent.getStringExtra("username_key");
        String postimg = intent.getStringExtra("imgurl_key");
        String userimg = intent.getStringExtra("userimg_key");
        String date = intent.getStringExtra("date_key");
        String source = intent.getStringExtra("postsrc_key");
        RelativeLayout caution = findViewById(R.id.cursion_text);
        if (Objects.equals(source, "Hot Zone")){
            caution.setVisibility(View.VISIBLE);
        }else{
            caution.setVisibility(View.GONE);
        }
        TextView nametext = findViewById(R.id.poster_name);
        nametext.setText(username);
        TextView statetext = findViewById(R.id.rep_state);
        statetext.setText(state);
        TextView Areaofrep = findViewById(R.id.vidarea);
        Areaofrep.setText(lga);
        TextView ReportType = findViewById(R.id.rep_town);
        ReportType.setText(town);
        TextView RepDate = findViewById(R.id.report_date);
        RepDate.setText(date);
        TextView RepSource = findViewById(R.id.post_source);
        RepSource.setText(source);
        TextView ReporComment = findViewById(R.id.rep_comment);
        ReporComment.setText(comment);

        ImageView imageView = (ImageView) findViewById(R.id.reporter_image);
        Glide.with(Hot_Image_post_viewer.this)
                .load(userimg)
                .into(imageView);

        ImageView postimageView = (ImageView) findViewById(R.id.post_image_view);
        Glide.with(Hot_Image_post_viewer.this).load(postimg)
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

        ImageView close = findViewById(R.id.stop_close_live3);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
