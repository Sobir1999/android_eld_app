package com.iosix.eldblesample.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public abstract class ChangeDateTimeBroadcast extends BroadcastReceiver {

    private Date curDate = new Date();
    private final DateFormat dateFormat = new SimpleDateFormat("yyMMdd", Locale.getDefault());

    public abstract void onDayChanged() throws ExecutionException, InterruptedException;

    private boolean isSameDay(Date currentDate){
        return dateFormat.format(currentDate).equals(dateFormat.format(curDate));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
          String action = intent.getAction();
          Date currentDate = new Date();

        if (action != null && !isSameDay(currentDate) && (action.equals(Intent.ACTION_TIME_CHANGED)
                    || action.equals(Intent.ACTION_TIMEZONE_CHANGED)
                    || action.equals(Intent.ACTION_TIME_TICK))){
                curDate = currentDate;
            try {
                onDayChanged();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);

        return intentFilter;
    }
}
