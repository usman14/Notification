package com.example.notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    public boolean shouldSound;
    public boolean shouldVibrate;
    NotificationManager notificationManager;

    Button button;
    Switch soundSwitch;
    Switch vibrationSwitch;

    @TargetApi(Build.VERSION_CODES.O)
    public void registerNormalNotificationChannel(android.app.NotificationManager notificationManager) {

        NotificationChannel channel_all = new NotificationChannel(getString(R.string.CHANNEL_ID_ALL), getString(R.string.CHANNEL_NAME_ALL), NotificationManager.IMPORTANCE_DEFAULT);
        channel_all.enableVibration(true);
        notificationManager.createNotificationChannel(channel_all);


        NotificationChannel channel_sound = new NotificationChannel(getString(R.string.CHANNEL_ID_SOUND), getString(R.string.CHANNEL_NAME_SOUND), NotificationManager.IMPORTANCE_DEFAULT);
        channel_sound.enableVibration(false);
        notificationManager.createNotificationChannel(channel_sound);

        NotificationChannel channel_vibrate = new NotificationChannel(getString(R.string.CHANNEL_ID_VIBRATE), getString(R.string.CHANNEL_NAME_VIBRATE), NotificationManager.IMPORTANCE_DEFAULT);
        channel_vibrate.setSound(null, null);
        channel_vibrate.enableVibration(true);
        notificationManager.createNotificationChannel(channel_vibrate);


        NotificationChannel channel_none = new NotificationChannel(getString(R.string.CHANNEL_ID_NONE), getString(R.string.CHANNEL_NAME_NONE), NotificationManager.IMPORTANCE_DEFAULT);
        channel_none.setSound(null, null);
        channel_none.enableVibration(false);
        notificationManager.createNotificationChannel(channel_none);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.btn);
        soundSwitch = findViewById(R.id.switch_sound);
        vibrationSwitch = findViewById(R.id.switch_vibration);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (isOreoOrAbove()) {
            setupNotificationChannels();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeNotification();
            }
        });

        soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    shouldSound = true;
                } else {
                    shouldSound = false;
                }
            }
        });

        vibrationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    shouldVibrate = true;
                } else {
                    shouldVibrate = false;
                }
            }
        });
    }

    private void setupNotificationChannels() {
        registerNormalNotificationChannel(notificationManager);
    }

    public void makeNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, getChannelId())
                .setContentTitle("Hi")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Welcome to Android");

        Intent intent = new Intent(MainActivity.this, ActivityOne.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        if (shouldSound && !shouldVibrate) {
            builder.setDefaults(Notification.DEFAULT_SOUND)
                    .setVibrate(new long[]{0L});
        }
        if (shouldVibrate && !shouldSound) {
            builder.setDefaults(Notification.DEFAULT_VIBRATE)
                    .setSound(null);
        }
        if (shouldSound && shouldVibrate) {
            builder.setDefaults(Notification.DEFAULT_ALL);
        }


        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    private String getChannelId() {
        if (shouldSound && shouldVibrate) {
            return getString(R.string.CHANNEL_ID_ALL);
        } else if (shouldSound && !shouldVibrate) {
            return getString(R.string.CHANNEL_ID_SOUND);
        } else if (!shouldSound && shouldVibrate) {
            return getString(R.string.CHANNEL_ID_VIBRATE);
        } else {
            return getString(R.string.CHANNEL_ID_NONE);
        }
    }

    private boolean isOreoOrAbove() {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O;
    }
}




