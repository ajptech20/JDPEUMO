package com.a2tocsolutions.nispsasapp;

import android.app.Application;
import android.content.Context;

import com.androidnetworking.AndroidNetworking;

public class MyApp extends Application {
    private static MyApp sInstance;

    public void onCreate() {
        super.onCreate();
        sInstance = this;
        AndroidNetworking.initialize(getApplicationContext());
    }

    public static MyApp getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }
}
