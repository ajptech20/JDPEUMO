package com.jdpmc.jwatcherapp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.jdpmc.jwatcherapp.MyApp;
import com.jdpmc.jwatcherapp.utils.PreferenceUtils;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class NispsasScreenReceiver extends BroadcastReceiver {

    public static boolean wasScreenOn = true;
    private Context cntx;
    private Vibrator vibe;
    private long a;
    private long seconds_screenoff, OLD_TIME;
    private boolean OFF_SCREEN, ON_SCREEN;
    private long diffrence;
    private long seconds_screenon, actual_diff;
    private int no_clicks = 0;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        cntx = context;
        vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        Log.v("onReceive", "Power button is pressed.");
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            a = System.currentTimeMillis();
            seconds_screenoff = a;
            OLD_TIME = seconds_screenoff;
            OFF_SCREEN = true;

            new CountDownTimer(5000, 200) {

                public boolean sent_msg;

                public void onTick(long millisUntilFinished) {


                    if (ON_SCREEN) {
                        if (seconds_screenon != 0 && seconds_screenoff != 0) {

                            actual_diff = cal_diff(seconds_screenon, seconds_screenoff);
                            if (actual_diff <= 700) {
                                sent_msg = true;
                                if (sent_msg) {
                                    vibe.vibrate(100);
                                    Log.v("onReceive", "I reach Hear.");
                                    no_clicks = no_clicks +1;
                                    if(no_clicks >= 2){
                                        Log.v("onReceive", "I fit send Alert.");
                                        vibe.vibrate(100);
                                        sendToServer();
                                        Toast.makeText(MyApp.getAppContext(), "Power Botton Off", Toast.LENGTH_LONG).show();
                                    }

                                    seconds_screenon = 0L;
                                    seconds_screenoff = 0L;
                                    sent_msg = false;

                                }
                            } else {
                                seconds_screenon = 0L;
                                seconds_screenoff = 0L;

                            }
                        }
                    }
                }

                public void onFinish() {

                    seconds_screenoff = 0L;
                    no_clicks = 0;
                }
            }.start();


        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            a = System.currentTimeMillis();
            seconds_screenon = a;
            OLD_TIME = seconds_screenoff;

            new CountDownTimer(5000, 200) {
                public boolean sent_msg;
                public void onTick(long millisUntilFinished) {
                    if (OFF_SCREEN) {
                        if (seconds_screenon != 0 && seconds_screenoff != 0) {
                            actual_diff = cal_diff(seconds_screenon, seconds_screenoff);
                            if (actual_diff <= 700) {
                                sent_msg = true;
                                if (sent_msg) {
                                    vibe.vibrate(100);
                                    Log.v("onReceive", "Na Second Place I dey");
                                    no_clicks = no_clicks +1;
                                    if(no_clicks >= 2){
                                        Log.v("onReceive", "I fit send Alert.");
                                        vibe.vibrate(100);
                                        sendToServer();
                                        Toast.makeText(MyApp.getAppContext(), "Power Button On", Toast.LENGTH_LONG).show();
                                    }
                                    seconds_screenon = 0L;
                                    seconds_screenoff = 0L;
                                    sent_msg = false;
                                }
                            } else {
                                seconds_screenon = 0L;
                                seconds_screenoff = 0L;
                            }
                        }
                    }
                }

                public void onFinish() {
                    seconds_screenon = 0L;
                    no_clicks = 0;
                }
            }.start();
        }
    }

    private long cal_diff(long seconds_screenon2, long seconds_screenoff2) {
        if (seconds_screenon2 >= seconds_screenoff2) {
            diffrence = (seconds_screenon2) - (seconds_screenoff2);
            seconds_screenon2 = 0;
            seconds_screenoff2 = 0;
        } else {
            diffrence = (seconds_screenoff2) - (seconds_screenon2);
            seconds_screenon2 = 0;
            seconds_screenoff2 = 0;
        }
        return diffrence;
    }


    private void sendToServer() {
        String lat = PreferenceUtils.getLocationLatitude(MyApp.getAppContext());
        String lon = PreferenceUtils.getLocationLongitude(MyApp.getAppContext());
        String phonenumber = PreferenceUtils.getPhoneNumber(MyApp.getAppContext());
        Log.d(TAG, "This is lat " );

        Log.d("#####LOCA","lat: "+lat+"\nlon: "+lon);

        String baseurl = "http://104.131.77.176/RCCSGOV/NPF/pushPanicAlert/";
        RestClientHelper.getInstance().post(baseurl + phonenumber + "/" + lat + "/" + lon+ "/PanicButton", null, new RestClientHelper.RestClientListener() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(MyApp.getAppContext(), "Location Sent", Toast.LENGTH_SHORT).show();
                vibe.vibrate(100);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(MyApp.getAppContext(), "Panic Alert not Sent", Toast.LENGTH_SHORT).show();

            }
        });
    }
}