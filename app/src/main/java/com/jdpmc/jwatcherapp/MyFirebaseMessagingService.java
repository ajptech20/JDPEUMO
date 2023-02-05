package com.jdpmc.jwatcherapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                //scheduleJob();
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            } else {
                // Handle message within 10 seconds
                //handleNow();
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            }

        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Details: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "Message Notification Title: " + remoteMessage.getNotification().getTitle());
            Log.d(TAG, "Message Notification Image: " + remoteMessage.getNotification().getImageUrl());
            Log.d(TAG, "Message Notification Click Action: " + remoteMessage.getNotification().getClickAction());
        }
        String title = "0";
        String message = "0";
        String user_img = "0";
        String uuid = "0";

        String click_action = remoteMessage.getNotification().getClickAction();
        Intent intent = new Intent(click_action);
        intent.putExtra("message", remoteMessage.getData().get("body"));
        intent.putExtra("title", remoteMessage.getData().get("title"));
        intent.putExtra("uuid", remoteMessage.getData().get("uuid"));
        intent.putExtra("comment", remoteMessage.getData().get("comment"));
        intent.putExtra("username", remoteMessage.getData().get("username"));
        intent.putExtra("reptype", remoteMessage.getData().get("reptype"));
        intent.putExtra("userimg", remoteMessage.getData().get("userimg"));
        intent.putExtra("post_img", remoteMessage.getData().get("post_img"));
        intent.putExtra("vid_url", remoteMessage.getData().get("vid_url"));
        intent.putExtra("state", remoteMessage.getData().get("state"));
        intent.putExtra("date", remoteMessage.getData().get("date"));
        intent.putExtra("area", remoteMessage.getData().get("area"));
        intent.putExtra("repstatus", remoteMessage.getData().get("repstatus"));
        title = remoteMessage.getData().get("title");
        user_img = remoteMessage.getData().get("userimg");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle(), title, message, user_img);

       /* String title = "0";
        String message = "0";

        /*if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Log.d(TAG, "Message data payload:title " + remoteMessage.getData().get("title"));

            sendNotification(remoteMessage.getData().get("body"),
                    remoteMessage.getData().get("title"), remoteMessage.getData().get("click_action"));

        }

       if (remoteMessage.getData().size() == 0) {
            title = remoteMessage.getData().get("title");
            message = remoteMessage.getData().get("body");
            Log.d(TAG, "Message data payload: " + title + message);
            Log.i("myTag:","Title"+title + message);
            Toast.makeText(MyFirebaseMessagingService.this, "Rep Id" +title, Toast.LENGTH_SHORT).show();
        }

        String click_action = remoteMessage.getNotification().getClickAction();
        Intent intent = new Intent(click_action);
        intent.putExtra("message", remoteMessage.getData().get("body"));
        intent.putExtra("title", remoteMessage.getData().get("title"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        //Calling method to generate notification
        sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle(), title, message);
        */
        //sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle(), title, message, click_action);
    }

        private void sendNotification(String messageBody, String title, String user_img, String message, String click_action) {
            Intent intent = new Intent(click_action);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(intent);
            intent.putExtra("title", title);
            intent.putExtra("body", messageBody);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            Bitmap remote_picture = null;
            NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
            try {
                remote_picture = BitmapFactory.decodeStream((InputStream) new URL(user_img).getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
            notiStyle.bigPicture(remote_picture);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "111")
                    .setSmallIcon(R.drawable.jdlogo)
                    .setContentText(messageBody)
                    .setStyle(notiStyle)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent)
                    .setLights(Color.GREEN, 3000, 3000);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel("111", "Urgent", importance);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.enableVibration(true);
                notificationChannel.setShowBadge(false);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                assert notificationManager != null;
                notificationBuilder.setChannelId("111");
                notificationManager.createNotificationChannel(notificationChannel);
                notificationManager.notify(0, notificationBuilder.build());
            } else {
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(0, notificationBuilder.build());
            }
        }

    }



    /*private void sendNotification(String messageBody, String messageTitle, String title, String message) {
        /*Intent intent = new Intent(click_action);
        intent.putExtra("title", title);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
                PendingIntent.FLAG_ONE_SHOT);*/

        /*Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.jdlogo)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);
                //.setContentIntent(pendingIntent);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());*/


