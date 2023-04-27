package com.iosix.eldblesample.activity;

import static com.iosix.eldblesample.MyApplication.executorService;
import static com.iosix.eldblesample.enums.Day.getDayFormat;
import static com.iosix.eldblesample.enums.Day.intToTime;
import static com.iosix.eldblesample.enums.EnumsConstants.STATUS_DR;
import static com.iosix.eldblesample.enums.EnumsConstants.STATUS_OFF;
import static com.iosix.eldblesample.enums.EnumsConstants.STATUS_OF_PC;
import static com.iosix.eldblesample.enums.EnumsConstants.STATUS_ON;
import static com.iosix.eldblesample.enums.EnumsConstants.STATUS_ON_YM;
import static com.iosix.eldblesample.enums.EnumsConstants.STATUS_SB;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.enums.Day;
import com.iosix.eldblesample.enums.HOSConstants;
import com.iosix.eldblesample.interfaces.FetchStatusCallback;
import com.iosix.eldblesample.interfaces.GetAllDrivingTimeCallback;
import com.iosix.eldblesample.interfaces.GetBreakTimeCallBack;
import com.iosix.eldblesample.interfaces.GetCurrDayDrivingTime;
import com.iosix.eldblesample.interfaces.GetCurrDayShiftCallback;
import com.iosix.eldblesample.interfaces.GetLastDrivingTime;
import com.iosix.eldblesample.models.Status;
import com.iosix.eldblesample.shared_prefs.LastStatusData;
import com.iosix.eldblesample.viewModel.StatusDaoViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
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

    int break1 = 0;
    int driving = 0;
    int shift = 100;
    int cycle = 100;


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
        statusDaoViewModel = ViewModelProviders.of(this).get(StatusDaoViewModel.class);
        LastStatusData lastStatusData = LastStatusData.getInstance(getApplicationContext());

        switch (lastStatusData.getLastStatus()){
            case STATUS_OFF: idStatusRecap.setText("STATUS OFF");
            case STATUS_DR: idStatusRecap.setText("DRIVING");
            case STATUS_OF_PC: idStatusRecap.setText("PERSONAL CON");
            case STATUS_ON: idStatusRecap.setText("STATUS ON");
            case STATUS_ON_YM: idStatusRecap.setText("YARD MOVE");
            case STATUS_SB: idStatusRecap.setText("SLEEPING BEARTH");
        }

        breakPercent = idProgressbarBreak.getProgress();
        drivingPercent = idProgressbarDriving.getProgress();
        shiftPercent = idProgressbarShift.getProgress();
        cyclePercent = idProgressbarCycle.getProgress();

        Runnable r = () -> runOnUiThread(() ->{

            statusDaoViewModel.getBreakTime(n -> {
                if (n > 180){
                    breakTv.setText(intToTime(HOSConstants.BREAK));
                }else {
                    statusDaoViewModel.getLastDrivingTime(getDayFormat(Day.getCalculatedDate(0)), n1 -> {
                        Log.d("Adverse",n1 + "n1");
                        break1 = HOSConstants.BREAK - n1;
                        if (n1 > HOSConstants.BREAK){
                            breakTv.setText(intToTime(0));
                            breakPercent = 0 /28800;
                            idProgressbarBreak.post(() -> idProgressbarBreak.setProgress(breakPercent));
                        }else {
                            breakTv.setText(intToTime(HOSConstants.BREAK - n1));
                            breakPercent = (HOSConstants.BREAK - n1)*100/28800;
                            idProgressbarBreak.post(() -> idProgressbarBreak.setProgress(breakPercent));
                        }
                    });
                }
            });


            statusDaoViewModel.getAllDrivingStatusTime(getDayFormat(Day.getCalculatedDate(0)), n -> {
                driving = HOSConstants.DRIVING - n;
                if ((HOSConstants.DRIVING - n) > 0){
                    if (driving > cycle){
                        drivingTv.setText(intToTime(cycle));
                        drivingPercent = cycle*100/39600;
                        idProgressbarDriving.post(() -> idProgressbarDriving.setProgress(drivingPercent));
                    }else if (driving > shift){
                        drivingTv.setText(intToTime(shift));
                        drivingPercent = shift*100/39600;
                        idProgressbarDriving.post(() -> idProgressbarDriving.setProgress(drivingPercent));
                    }else {
                        drivingTv.setText(intToTime(HOSConstants.DRIVING - n));
                        drivingPercent = (HOSConstants.DRIVING - n)*100/39600;
                        idProgressbarDriving.post(() -> idProgressbarDriving.setProgress(drivingPercent));
                    }

                }else {
                    drivingTv.setText(intToTime(0));
                    drivingPercent = 0 /39600;
                    idProgressbarDriving.post(() -> idProgressbarDriving.setProgress(drivingPercent));
                }
            });

            statusDaoViewModel.getCurrDayShiftTime(getDayFormat(Day.getCalculatedDate(0)), n -> {
                shift = HOSConstants.SHIFT - n;
                if (n > HOSConstants.SHIFT){
                    shiftTv.setText(intToTime(0));
                    shiftPercent = 0 /50400;
                    idProgressbarShift.post(() -> idProgressbarShift.setProgress(shiftPercent));
                }else {
                    if (shift > cycle){
                        shiftTv.setText(intToTime(cycle));
                        shiftPercent = cycle*100/50400;
                        idProgressbarShift.post(() -> idProgressbarShift.setProgress(shiftPercent));
                    }else {
                        shiftTv.setText(intToTime(shift));
                        shiftPercent = shift*100/50400;
                        idProgressbarShift.post(() -> idProgressbarShift.setProgress(shiftPercent));
                    }
                }
            });

            statusDaoViewModel.getAllDringTime(n -> {
                cycle = HOSConstants.CYCLE - n;
                if (cycle > 0){
                    cycleTv.setText(intToTime(cycle));
                    cyclePercent = cycle*100/252000;
                    idProgressbarCycle.post(() -> idProgressbarCycle.setProgress(cyclePercent));
                }else {
                    cycleTv.setText(intToTime(0));
                    cyclePercent = 0 /252000;
                    idProgressbarCycle.post(() -> idProgressbarCycle.setProgress(cyclePercent));
                }
            });


            idCurrDayRecap.setText(dateFormat.format(Calendar.getInstance().getTime()));
            idTimeRecap.setText(dateFormat2.format(Calendar.getInstance().getTime()));
        });

        executorService.scheduleAtFixedRate(r,0,1,TimeUnit.SECONDS);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void finish() {
        super.finish();
        instance = null;
    }
}