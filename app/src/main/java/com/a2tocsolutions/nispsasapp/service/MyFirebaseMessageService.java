package com.a2tocsolutions.nispsasapp.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.a2tocsolutions.nispsasapp.R;
import com.a2tocsolutions.nispsasapp.activities.NotificationActivity;
import com.a2tocsolutions.nispsasapp.model.LoginResponse;
import com.a2tocsolutions.nispsasapp.model.Profile;
import com.a2tocsolutions.nispsasapp.networking.api.Service;
import com.a2tocsolutions.nispsasapp.networking.generator.DataGenerator;
import com.a2tocsolutions.nispsasapp.utils.FancyToast;
import com.a2tocsolutions.nispsasapp.utils.PreferenceUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.a2tocsolutions.nispsasapp.activities.NotificationActivity.BODYS;
import static com.a2tocsolutions.nispsasapp.activities.NotificationActivity.SENDA;
import static com.a2tocsolutions.nispsasapp.activities.NotificationActivity.TITLES;
import static com.a2tocsolutions.nispsasapp.utils.Constants.BASE_URL;

public class MyFirebaseMessageService extends FirebaseMessagingService implements TextToSpeech.OnInitListener {
    private static final String TAG = "FBM";
    TextToSpeech t1;
    private String body,title, icon,sender;


    public MyFirebaseMessageService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();

        t1 = new TextToSpeech(this, this);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //Log data to Log Cat
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String icon = remoteMessage.getData().get("icon");
        String sender = remoteMessage.getData().get("sender");
        Map<String, String> remoteData = remoteMessage.getData();
        if (remoteData != null && remoteData.size() > 0) {
            createNotification(body, title, icon, sender);
        }



    }


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            t1.setLanguage(Locale.US);
            Log.i(TAG, "I dey Speech side");
        }

    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        super.onDestroy();
    }



    private void createNotification( String messagebody, String messagetitle, String messageicon, String messagesender) {
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.putExtra(TITLES, messagetitle);
        intent.putExtra(BODYS, messagebody);
        intent.putExtra(SENDA, messagesender);
        startActivity(intent);

        PendingIntent resultIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        URL urlLargeImageURL = null;
        try {
            urlLargeImageURL = new URL(messageicon);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Bitmap largeImage = null;
        try {
            largeImage = BitmapFactory.decodeStream(urlLargeImageURL.openConnection().getInputStream());
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

       // Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.siren);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mNotificationBuilder.setSmallIcon(R.drawable.logo);
            mNotificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
            mNotificationBuilder.setContentTitle(messagetitle);
            mNotificationBuilder.setContentText(messagebody);
            mNotificationBuilder.setAutoCancel(true);
          //  mNotificationBuilder.setSound(soundUri);
            mNotificationBuilder.setLargeIcon(largeImage);
            mNotificationBuilder.setContentIntent(resultIntent);
        } else {
            mNotificationBuilder.setSmallIcon(R.drawable.logo);
            mNotificationBuilder.setContentTitle(messagetitle);
            mNotificationBuilder.setContentText(messagebody);
            mNotificationBuilder.setAutoCancel(true);
         //   mNotificationBuilder.setSound(soundUri);
            mNotificationBuilder.setLargeIcon(largeImage);
            mNotificationBuilder.setContentIntent(resultIntent);
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotificationBuilder.build());


    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        String usertoken = token;
        String name = PreferenceUtils.getUsername(getApplicationContext());
        String phonenumber = PreferenceUtils.getPhoneNumber(getApplicationContext());

        if (phonenumber != null && name != null) {
            try {
                Service service = DataGenerator.createService(Service.class, BASE_URL);
                Call<LoginResponse> call = service.loginUser(name, phonenumber, usertoken);

                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                LoginResponse loginResponse = response.body();
                                List<Profile> profileList = loginResponse.getProfile();
                                if (profileList != null) {
                                    for (Profile profile : profileList) {
                                        String username = profile.getUsername();
                                        String phonenumber = profile.getPhonenumber();

                                        PreferenceUtils.saveUsername(username, getApplicationContext());
                                        PreferenceUtils.savePhoneNumber(phonenumber, getApplicationContext());
                                    }
                                }
                                FancyToast.makeText(getApplicationContext(), "Token Updated", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

                            }
                        } else {
                            FancyToast.makeText(getApplicationContext(), "Token Update Failed", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                        FancyToast.makeText(getApplicationContext(), "Token Update Failed", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }
                });
            } catch (Exception e) {
                FancyToast.makeText(getApplicationContext(), "error sign up", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        }
    }
}

