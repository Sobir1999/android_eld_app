package com.iosix.eldblesample.activity;

import static com.iosix.eldblesample.MyApplication.executorService;
import static com.iosix.eldblesample.enums.Day.intToTime;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.enums.HOSConstants;
import com.iosix.eldblesample.viewModel.StatusDaoViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class RecapActivity extends BaseActivity {

    public static RecapActivity instance = null;

    private TextView breakTv;
    private TextView drivingTv;
    private TextView shiftTv;
    private TextView cycleTv;
    private TextView idCurrDayRecap;
    private TextView idTimeRecap;
    private Button idStatusRecap;
    private TextView idImageBack;
    private ProgressBar idProgressbarBreak;
    private ProgressBar idProgressbarDriving;
    private ProgressBar idProgressbarShift;
    private ProgressBar idProgressbarCycle;
    private StatusDaoViewModel statusDaoViewModel;
    int current_status;
    int breakPercent = 100;
    int drivingPercent = 100;
    int shiftPercent = 100;
    int cyclePercent = 100;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_recap;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void initView() {
        super.initView();
        instance = this;
        breakTv = findViewById(R.id.idTimeBreak);
        drivingTv = findViewById(R.id.idTimeDriving);
        shiftTv = findViewById(R.id.idTimeShift);
        cycleTv = findViewById(R.id.idTimeCycle);
        idCurrDayRecap = findViewById(R.id.idCurrDayRecap);
        idTimeRecap = findViewById(R.id.idTimeRecap);
        idStatusRecap = findViewById(R.id.idStatusRecap);
        idImageBack = findViewById(R.id.idImageBack);
        idProgressbarBreak = findViewById(R.id.idProgressbarBreak);
        idProgressbarDriving = findViewById(R.id.idProgressbarDriving);
        idProgressbarShift = findViewById(R.id.idProgressbarShift);
        idProgressbarCycle = findViewById(R.id.idProgressbarCycle);

        DateFormat dateFormat=new SimpleDateFormat("EEE, MMM d", Locale.getDefault());
        DateFormat dateFormat2=new SimpleDateFormat("hh:mm a",Locale.getDefault());

        Runnable r = () -> runOnUiThread(() ->{
            breakTv.setText(intToTime(HOSConstants.BREAK));
            drivingTv.setText(intToTime(HOSConstants.DRIVING));
            shiftTv.setText(intToTime(HOSConstants.SHIFT));
            cycleTv.setText(intToTime(HOSConstants.CYCLE));
            idCurrDayRecap.setText(dateFormat.format(Calendar.getInstance().getTime()));
            idTimeRecap.setText(dateFormat2.format(Calendar.getInstance().getTime()));
        });

        executorService.scheduleAtFixedRate(r,0,1,TimeUnit.SECONDS);
        statusDaoViewModel = ViewModelProviders.of(this).get(StatusDaoViewModel.class);
        calculateLimits();
    }

    @Override
    public void onBackPressed() {

    }

    private void calculateLimits(){

        breakPercent = idProgressbarBreak.getProgress();
        drivingPercent = idProgressbarDriving.getProgress();
        shiftPercent = idProgressbarShift.getProgress();
        cyclePercent = idProgressbarCycle.getProgress();

        new Thread(() -> {
            while (breakPercent > 0){
                breakPercent = HOSConstants.BREAK*100/28800;
                idProgressbarBreak.post(() -> idProgressbarBreak.setProgress(breakPercent));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            while (drivingPercent > 0){
                drivingPercent = HOSConstants.DRIVING*100/39600;
                idProgressbarDriving.post(() -> idProgressbarDriving.setProgress(drivingPercent));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            while (shiftPercent > 0){
                shiftPercent = HOSConstants.SHIFT*100/50400;
                idProgressbarShift.post(() -> idProgressbarShift.setProgress(shiftPercent));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            while (cyclePercent > 0){
                cyclePercent = HOSConstants.CYCLE*100/252000;
                idProgressbarCycle.post(() -> idProgressbarCycle.setProgress(cyclePercent));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void finish() {
        super.finish();
        instance = null;
    }
}