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
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static com.iosix.eldblesample.utils.Utils.setBluetoothDataEnabled;

public abstract class ChangeDateTimeBroadcast extends BroadcastReceiver {

    private Date curDate = new Date();
    private final DateFormat dateFormat = new SimpleDateFormat("yyMMdd", Locale.getDefault());
    private DayDaoViewModel daoViewModel;
    private StatusDaoViewModel statusDaoViewModel;


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
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        daoViewModel = ViewModelProviders.of((FragmentActivity) context).get(DayDaoViewModel.class);
        statusDaoViewModel = ViewModelProviders.of((FragmentActivity) context).get(StatusDaoViewModel.class);



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
