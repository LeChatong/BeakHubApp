package com.lechatong.beakhub.Activities;

/**
 * Author: LeChatong
 * Desc: First Activity who load
 */

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.lechatong.beakhub.Activities.LoginActivity;
import com.lechatong.beakhub.Models.BhEvent;
import com.lechatong.beakhub.R;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.Tools.Deserializer;
import com.lechatong.beakhub.Tools.Streams.NotificationStreams;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class SplashActivity extends AppCompatActivity {

    Context ctx;
    SharedPreferences sharedPreferences;

    private Intent intent;

    private static final String ID_ACCOUNT = "ID_ACCOUNT";
    private static final String USERNAME = "USERNAME";
    private static final String PREFS = "PREFS";

    private Disposable disposable;

    private Long account_id;

    private String NOTIFICATION_CHANNEL_ID = "LECHATONG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.activity_splash);

        sharedPreferences = getBaseContext().getSharedPreferences(PREFS, MODE_PRIVATE);

        account_id = sharedPreferences.getLong(ID_ACCOUNT, 0);

        this.getAllNotification(account_id);

        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(sharedPreferences.contains(ID_ACCOUNT)){
                    intent = new Intent(ctx, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    intent = new Intent(ctx, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        };
        thread.start();
    }

    private void initNotification(APIResponse response){
        BhEvent bhEvent = new BhEvent();
        ArrayList<BhEvent> bhEventArrayList = (ArrayList<BhEvent>) response.getDATA();
        String message = null;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "BeakHub", NotificationManager.IMPORTANCE_MAX);

            // Configure the notification channel.
            notificationChannel.setDescription("beakhub");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        int i = 0;

        for(Object treeMap : bhEventArrayList){
            bhEvent = Deserializer.getEvent(treeMap);
            if(bhEvent.getAction().equals("LIKE")) {
                message = bhEvent.getName_sender() + " " +ctx.getString(R.string.notif_message_1) + " " + bhEvent.getJob_title();
            } else if(bhEvent.getAction().equals("COMMENT")){
                message = bhEvent.getName_sender() + " " +
                        ctx.getString(R.string.notif_message_2) + " " + bhEvent.getJob_title();
            }

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

            notificationBuilder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.beak_solo)
                    .setTicker("Hearty365")
                    //     .setPriority(Notification.PRIORITY_MAX)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(message)
                    .setContentInfo("Info");

            notificationManager.notify(i++, notificationBuilder.build());
        }
    }

    private void getAllNotification(Long account_id){
        this.disposable = NotificationStreams.streamNotificationNotSeeByUserId(account_id)
                .subscribeWith(new DisposableObserver<APIResponse>(){

                    @Override
                    public void onNext(APIResponse response) {
                        initNotification(response);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
