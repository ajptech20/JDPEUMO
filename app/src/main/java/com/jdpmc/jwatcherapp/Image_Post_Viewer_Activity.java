package com.jdpmc.jwatcherapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class Image_Post_Viewer_Activity extends Activity {
    //TextView receiver_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView txtMarquee;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_image_viewer);
        txtMarquee = (TextView) findViewById(R.id.rep_comment);
        txtMarquee.setSelected(true);

        Intent intent = getIntent();
        String reptype = intent.getStringExtra("reptype_key");
        String state = intent.getStringExtra("state_key");
        String imgurl = intent.getStringExtra("imgurl_key");
        String comment = intent.getStringExtra("comment_key");
        String username = intent.getStringExtra("username_key");
        String userimg = intent.getStringExtra("userimg_key");
        String date = intent.getStringExtra("date_key");
        String areaflag = intent.getStringExtra("area_key");
        String repstatus = intent.getStringExtra("repstatus_key");
        TextView nametext = findViewById(R.id.poster_name);
        nametext.setText(username);
        TextView area = findViewById(R.id.post_area);
        area.setText(repstatus);
        TextView posttype = findViewById(R.id.post_type);
        posttype.setText(reptype);
        TextView statustext = findViewById(R.id.post_area2);
        statustext.setText(repstatus);
        TextView statetext = findViewById(R.id.rep_state);
        statetext.setText(state);
        TextView RepDate = findViewById(R.id.report_date);
        RepDate.setText(date);
        TextView RepArea = findViewById(R.id.rep_type);
        RepArea.setText(areaflag);
        TextView ReporComment = findViewById(R.id.rep_comment);
        ReporComment.setText(comment);
        ImageView imageView = (ImageView) findViewById(R.id.reporter_image);
        Glide.with(Image_Post_Viewer_Activity.this)
                .load(userimg)
                .into(imageView);

        ImageView postimageView = (ImageView) findViewById(R.id.post_image_view);
        Glide.with(Image_Post_Viewer_Activity.this).load(imgurl)
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



    /*@Override
    public boolean onTouchEvent(MotionEvent ev) {

    }*/

}
