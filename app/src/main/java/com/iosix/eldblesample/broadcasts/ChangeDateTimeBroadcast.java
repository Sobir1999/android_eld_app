package com.iosix.eldblesample.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.iosix.eldblesample.enums.Day;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.StatusDaoViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static com.iosix.eldblesample.utils.Utils.setBluetoothDataEnabled;

public abstract class ChangeDateTimeBroadcast extends BroadcastReceiver {

    private Date date = new Date();
    private final DateFormat dateFormat = new SimpleDateFormat("yyMMdd", Locale.getDefault());
    private DayDaoViewModel daoViewModel;
    private StatusDaoViewModel statusDaoViewModel;

    public ChangeDateTimeBroadcast() {
    }

    public abstract void onDayChanged();

    private boolean isSameDay(Date currentDate){
        return dateFormat.format(currentDate).equals(dateFormat.format(date));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_DATE_CHANGED.equalsIgnoreCase(intent.getAction())) {
            String action = intent.getAction();
            Date currentDate = new Date();

            if (action != null && !isSameDay(currentDate) && (action.equals(Intent.ACTION_TIME_CHANGED)
                    || action.equals(Intent.ACTION_TIMEZONE_CHANGED)
                    || action.equals(Intent.ACTION_TIME_TICK))){
                date = currentDate;
                onDayChanged();
            }
        }

        daoViewModel = ViewModelProviders.of((FragmentActivity) context).get(DayDaoViewModel.class);
        statusDaoViewModel = ViewModelProviders.of((FragmentActivity) context).get(StatusDaoViewModel.class);
        try {
            daoViewModel.insertDay(new DayEntity(Day.getDayTime1(), Day.getDayName2()));
            statusDaoViewModel.getmAllStatus().observe((LifecycleOwner) context, logEntities -> {
                if (!logEntities.isEmpty()) {
                    statusDaoViewModel.insertStatus(new LogEntity(logEntities.get(logEntities.size() - 1).getTo_status(), logEntities.get(logEntities.size() - 1).getTo_status(), null, null, null, Day.getDayTime1(), 0));
                } else {
                    statusDaoViewModel.insertStatus(new LogEntity(0, 0, null, null, null, Day.getDayTime1(), 0));
                }
            });
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
//        daoViewModel.

//        MediaPlayer mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
//        mediaPlayer.start();
        setBluetoothDataEnabled(context);
//        Log.d("BR", "onReceive: ChangedT");

    }

    public static IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);

        return intentFilter;
    }
}
