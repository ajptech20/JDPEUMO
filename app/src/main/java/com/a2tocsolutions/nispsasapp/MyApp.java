package com.a2tocsolutions.nispsasapp;

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {
    private static MyApp sInstance;

    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static MyApp getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }
}
