package com.jdpmc.jwatcherapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.jdpmc.jwatcherapp.R;
import com.jdpmc.jwatcherapp.RAPE;
import com.jdpmc.jwatcherapp.fakeNews;
import com.jdpmc.jwatcherapp.nscdclanding;
import com.jdpmc.jwatcherapp.pg;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.play.core.appupdate.AppUpdateManager;

import java.util.Timer;

import butterknife.BindView;

public class ImageAdapter extends PagerAdapter {
    @BindView(R.id.progress)
    RelativeLayout progress;

    @BindView(R.id.progress_text)
    TextView progress_text;

    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;

    @BindView(R.id.flood)
    AppCompatImageView flood;



    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;

    private String longitude, latitude, phonenumber, reporter;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    boolean boolean_permission;
    /**
     * Provides the entry point to the Fused Location Provider API.
     */
    //private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;
    private static final int REQUEST_CALL_PHONE = 878;
    private static final int REQUEST_READ_GPS = 565;
    private static final int REQUEST_READ_PHONE_STATE = 45;
    private TextView nam;
    private TextView namm;


    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;


    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private String InteriorLink ="http://interior.gov.ng";

    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;
    private AppUpdateManager mAppUpdateManager;
    private Activity thisActivity;
    private Timer timer;
    private int current_position = 0;
    private int Num = 3;
    ////testin the recycler slide
    public Handler handler = new Handler();
    final int duration = 6000;
    final int pixelsToMove = 1080;
    private boolean isCanceled;
    private boolean Gott;
    private int progressStatus = 0;


        /*
            //Initialize a new CountDownTimer instance
            new CountDownTimer(millisInFuture, countDownInterval){
                public void onTick(long millisUntilFinished){

                    //Another one second passed
                    //  progress_text.setText(millisUntilFinished/1000 + "  Seconds...");
                    //Each second ProgressBar progress counter added one
                    progressStatus +=1;
                    progress_bar.setProgress(progressStatus);
                    progress_text.setText(new StringBuilder().append("Sending Crime Alert in ").append(progressStatus - progress_bar.getMax()).append(" Seconds...").toString());
                    progress_bar.setVisibility(View.VISIBLE);


                }

                public void onFinish(){
                    //Do something when count down end.
                    crimeAlert();
                }
            }.start();*/






MainActivity mFrag = new MainActivity();
    private Context context;
    private LayoutInflater layoutInflater;

    private Integer [] image1 = {R.drawable.nothing};
    private Integer [] image2 = {R.drawable.nothing};
    private Integer [] image3 = {R.drawable.nothing};
    private Integer [] image4 = {R.drawable.eye, R.drawable.nothing,R.drawable.nothing};
    private Integer [] image5 = {R.drawable.m_o_i, R.drawable.nothing,R.drawable.nothing};
    private Integer [] image6 = {R.drawable.nothing, R.drawable.nothing,R.drawable.nothing};


    public ImageAdapter(Context context) {
        this.context = context;
    }
    @Override
    public int getCount() {
        return image1.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_adminis, null);






        ImageView imageView2 = (ImageView) view.findViewById(R.id.image2);
        imageView2.setImageResource(image2[position]);
        imageView2.requestLayout();

if (imageView2.getDrawable().getConstantState() ==
        imageView2.getResources().getDrawable(R.drawable.stoprape).getConstantState()){
    imageView2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, RAPE.class);
            context.startActivity(intent);
        }
    });
}else if (imageView2.getDrawable().getConstantState() ==
        imageView2.getResources().getDrawable(R.drawable.verify_driver).getConstantState()){
imageView2.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, VerifyTransporter.class);
        context.startActivity(intent);
    }
});
}else if (imageView2.getDrawable().getConstantState() ==
        imageView2.getResources().getDrawable(R.drawable.nscdc).getConstantState()){
    imageView2.setOnClickListener(new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, nscdclanding.class);
            context.startActivity(intent);
        }
    });
}


        ImageView imageView3 = (ImageView) view.findViewById(R.id.image3);
        imageView3.setImageResource(image3[position]);
        imageView3.requestLayout();

        if (imageView3.getDrawable().getConstantState() ==
                imageView2.getResources().getDrawable(R.drawable.fake).getConstantState()){
            imageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, fakeNews.class);
                    context.startActivity(intent);
                }
            });
        }else if (imageView3.getDrawable().getConstantState() ==
                imageView2.getResources().getDrawable(R.drawable.verify_v_p).getConstantState()){
            imageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Vigilante.class);
                    context.startActivity(intent);
                }
            });
        }
        else if (imageView3.getDrawable().getConstantState() ==
                imageView2.getResources().getDrawable(R.drawable.prisons).getConstantState()){
            imageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CorrectionServices.class);
                    context.startActivity(intent);
                }
            });
        }


        ImageView imageView4 = (ImageView) view.findViewById(R.id.image6);
        imageView4.setImageResource(image4[position]);
        if (imageView4.getDrawable().getConstantState() ==
                imageView4.getResources().getDrawable(R.drawable.eye).getConstantState()){
            imageView4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CovidActivity.class);
                    context.startActivity(intent);
                }
            });
        }




        ImageView imageView5 = (ImageView) view.findViewById(R.id.image7);
        imageView5.setImageResource(image5[position]);
        if (imageView5.getDrawable().getConstantState() ==
                imageView5.getResources().getDrawable(R.drawable.m_o_i).getConstantState()){
            imageView5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, pg.class);
                    context.startActivity(intent);
                }
            });
        }




        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}
