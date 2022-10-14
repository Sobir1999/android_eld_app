package com.iosix.eldblesample;

import static com.iosix.eldblesample.utils.Constants.CHANNEL_ID;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MyApplication extends Application {
    public static Application instance;
    public Context context;

    public static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4);

    @Override
    public void onCreate() {
        super.onCreate();
        Security.insertProviderAt(
                new BouncyCastleProvider(),1
        );
        createNotificationChannel();

        instance = this;
        context = getApplicationContext();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
