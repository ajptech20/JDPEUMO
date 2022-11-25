package com.a2tocsolutions.nispsasapp.utils;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {
    public static boolean saveMenuTitle(String menutitle, Context context) {
        SharedPreferences prefs = getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.MENU_TITLE, menutitle);
        prefsEditor.apply();
        return true;
    }

    public static String getMenuTitle(Context context) {
        SharedPreferences prefs = getDefaultSharedPreferences(context);
        return prefs.getString(Constants.MENU_TITLE, "");
    }

    public static boolean saveUsername(String username, Context context) {
        SharedPreferences prefs = getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.USERNAME, username);
        prefsEditor.apply();
        return true;
    }

    public static String getUsername(Context context) {
        SharedPreferences prefs = getDefaultSharedPreferences(context);
        return prefs.getString(Constants.USERNAME, "");
    }

    public static boolean savePhoneNumber(String phonenumber, Context context) {
        SharedPreferences prefs = getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.PHONE_NUMBER, phonenumber);
        prefsEditor.apply();
        return true;
    }

    public static String getPhoneNumber(Context context) {
        SharedPreferences prefs = getDefaultSharedPreferences(context);
        return prefs.getString(Constants.PHONE_NUMBER, "");
    }

    public static boolean saveUserkey(String userkey, Context context) {
        SharedPreferences prefs = getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.FIREBASE_TOKEN, userkey);
        prefsEditor.apply();
        return true;
    }

    public static String getUserkey(Context context) {
        SharedPreferences prefs = getDefaultSharedPreferences(context);
        return prefs.getString(Constants.FIREBASE_TOKEN, "");
    }


    public static boolean saveSplashScreenState(Boolean splashscreen, Context context) {
        SharedPreferences prefs = getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putBoolean(Constants.SPLASH_SCREEN, splashscreen);
        prefsEditor.apply();
        return true;
    }

    public static Boolean getSplashScreenState(Context context) {
        SharedPreferences prefs = getDefaultSharedPreferences(context);
        return prefs.getBoolean(Constants.SPLASH_SCREEN, Boolean.FALSE);
    }

    public static boolean saveLocationLatitude (String latitude, Context context) {
        SharedPreferences prefs = getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.LATITUDE, latitude);
        prefsEditor.apply();
        return true;
    }

    public static String getLocationLatitude (Context context) {
        SharedPreferences prefs = getDefaultSharedPreferences(context);
        return prefs.getString(Constants.LATITUDE, "");
    }

    public static boolean saveLocationLongitude(String longitude, Context context) {
        SharedPreferences prefs = getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.LONGITUDE, longitude);
        prefsEditor.apply();
        return true;
    }

    public static String getLocationLongitude (Context context) {
        SharedPreferences prefs = getDefaultSharedPreferences(context);
        return prefs.getString(Constants.LONGITUDE, "");
    }

    public static boolean acceptedTerms(String terms, Context context) {
        SharedPreferences prefs = getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.TERMS, terms);
        prefsEditor.apply();
        return true;
    }

    public static String getTerms(Context context) {
        SharedPreferences prefs = getDefaultSharedPreferences(context);
        return prefs.getString(Constants.TERMS, "");
    }

    public static void saveCode(String s, Context applicationContext) {
    }

    public static void saveResourceUri(String sgcompany, Context applicationContext) {
    }

    public static void saveName(String sgname, Context applicationContext) {
    }

    public static String getResourceUri(Context context) {
        SharedPreferences prefs = getDefaultSharedPreferences(context);
        return prefs.getString(Constants.RESOURCE_URI, "");
    }
}
