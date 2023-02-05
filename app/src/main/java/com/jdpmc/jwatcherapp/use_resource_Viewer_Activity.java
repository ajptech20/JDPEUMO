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

public class use_resource_Viewer_Activity extends Activity {
    //TextView receiver_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView txtMarquee;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rse_image_viewer_post);
        txtMarquee = (TextView) findViewById(R.id.rsc_comment);
        txtMarquee.setSelected(true);

        Intent intent = getIntent();
        String title = intent.getStringExtra("srctitle_key");
        String description = intent.getStringExtra("description_key");
        String vidurl = intent.getStringExtra("vidurl_key");
        String fileurl = intent.getStringExtra("fileurl_key");
        String previewimg = intent.getStringExtra("prevurl_key");
        String imgfileurl = intent.getStringExtra("imgurl_key");
        String authour = intent.getStringExtra("author_key");
        String date = intent.getStringExtra("date_key");
        String type = intent.getStringExtra("type_key");

        TextView nametext = findViewById(R.id.usersc_title);
        nametext.setText(title);
        TextView area = findViewById(R.id.usersc_author);
        area.setText(authour);
        TextView statustext = findViewById(R.id.title_set);
        statustext.setText(title);
        TextView descripttext = findViewById(R.id.rsc_comment);
        descripttext.setText(description);
        TextView RepDate = findViewById(R.id.rsc_date);
        RepDate.setText(date);
        ImageView imageView = (ImageView) findViewById(R.id.rscprev_image);
        Glide.with(use_resource_Viewer_Activity.this)
                .load(previewimg)
                .into(imageView);

        ImageView postimageView = (ImageView) findViewById(R.id.rscimgfileurl_image_view);
        Glide.with(use_resource_Viewer_Activity.this).load(fileurl)
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
