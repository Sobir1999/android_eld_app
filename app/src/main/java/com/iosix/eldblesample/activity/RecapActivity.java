package com.iosix.eldblesample.activity;

import static com.iosix.eldblesample.MyApplication.executorService;
import static com.iosix.eldblesample.enums.Day.intToTime;

import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.enums.HOSConstants;
import com.iosix.eldblesample.viewModel.StatusDaoViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;


public class RecapActivity extends BaseActivity {

    private TextView breakTv;
    private TextView drivingTv;
    private TextView shiftTv;
    private TextView cycleTv;
    private TextView idCurrDayRecap;
    private TextView idTimeRecap;
    private TextView idStatusRecap;
    private ImageView idImageBack;
    private int current_status;
    public static int offTime = 0;
    public static int onTime = 0;
    public static int sbTime = 0;
    public static int drTime = 0;

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
        breakTv = findViewById(R.id.idTimeBreak);
        drivingTv = findViewById(R.id.idTimeDriving);
        shiftTv = findViewById(R.id.idTimeShift);
        cycleTv = findViewById(R.id.idTimeCycle);
        idCurrDayRecap = findViewById(R.id.idCurrDayRecap);
        idTimeRecap = findViewById(R.id.idTimeRecap);
        idStatusRecap = findViewById(R.id.idStatusRecap);
        idImageBack = findViewById(R.id.idImageBack);

        idImageBack.setOnClickListener(view -> onBackPressed());
        DateFormat dateFormat=new SimpleDateFormat("EEE, MMM d");
        DateFormat dateFormat2=new SimpleDateFormat("hh:mm a");

        Runnable r = () -> runOnUiThread(() ->{
                idCurrDayRecap.setText(dateFormat.format(Calendar.getInstance().getTime()));
                idTimeRecap.setText(dateFormat2.format(Calendar.getInstance().getTime()));
        });

        executorService.scheduleAtFixedRate(r,0,1,TimeUnit.SECONDS);

        StatusDaoViewModel statusDaoViewModel = ViewModelProviders.of(this).get(StatusDaoViewModel.class);
        current_status = getIntent().getIntExtra("currStatus",0);
        Log.d("Adverse Diving",current_status + "A");
        calculateLimits();
    }

    private void calculateLimits(){
        breakTv.setText(intToTime(HOSConstants.BREAK));
        drivingTv.setText(intToTime(HOSConstants.DRIVING));
        shiftTv.setText(intToTime(HOSConstants.SHIFT));
        cycleTv.setText(intToTime(HOSConstants.CYCLE));

        switch (current_status){
            case 0: idStatusRecap.setText("ENGINE OFF");
            case 1: idStatusRecap.setText("SLEEPING BERTH");
            case 2: idStatusRecap.setText("DRIVING");
            case 3: idStatusRecap.setText("ENGINE ON");
        }
    }
}