package com.iosix.eldblesample;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import com.iosix.eldblesample.shared_prefs.TinyDB;
import com.iosix.eldblesample.shared_prefs.UserData;

import java.security.Security;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.iosix.eldblesample.shared_prefs.SharedPrefs.callUserPrefs;
import static com.iosix.eldblesample.utils.Constants.CHANNEL_ID;

import androidx.core.os.HandlerCompat;

public class MyApplication extends Application {
    public static Application instance;
    public static Context context;
    public static UserData userData;
    public static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        Security.insertProviderAt(
                new BouncyCastleProvider(),1
        );
        createNotificationChannel();

        instance = this;
        context = getApplicationContext();
        userData = new UserData(context);
        sharedPreferences = callUserPrefs(context);
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
