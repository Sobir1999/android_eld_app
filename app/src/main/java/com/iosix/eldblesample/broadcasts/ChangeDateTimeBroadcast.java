package com.iosix.eldblesample.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class ChangeDateTimeBroadcast extends BroadcastReceiver {

    public ChangeDateTimeBroadcast() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_DATE_CHANGED.equalsIgnoreCase(intent.getAction())) {
            Toast.makeText(context, "Date Changed", Toast.LENGTH_SHORT).show();
            Log.d("BR", "onReceive: Changed");
        }
        MediaPlayer mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
        mediaPlayer.start();
    }
}
