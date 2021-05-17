package com.iosix.eldblesample.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class ChangeDateTimeBroadcast extends BroadcastReceiver {

    private Date date = new Date();
    private DateFormat dateFormat = new SimpleDateFormat("yyMMdd", Locale.getDefault());

    public ChangeDateTimeBroadcast() {
    }

    public abstract void onDayChanged();

    private boolean isSameDay(Date currentDate){
        return dateFormat.format(currentDate).equals(dateFormat.format(date));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_DATE_CHANGED.equalsIgnoreCase(intent.getAction())) {
            Toast.makeText(context, "Date Changed", Toast.LENGTH_SHORT).show();
            Log.d("BR", "onReceive: Changed");

            String action = intent.getAction();
            Date currentDate = new Date();

            if (action != null && !isSameDay(currentDate) && (action.equals(Intent.ACTION_TIME_CHANGED)
                    || action.equals(Intent.ACTION_TIMEZONE_CHANGED)
                    || action.equals(Intent.ACTION_TIME_TICK))){
                date = currentDate;
                onDayChanged();
            }
        }
        MediaPlayer mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
        mediaPlayer.start();
    }

    public static IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);

        return intentFilter;
    }
}
