package com.iosix.eldblesample.activity;

import static com.iosix.eldblesample.enums.Day.getCurrentSeconds;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.enums.Day;
import com.iosix.eldblesample.enums.EnumsConstants;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;
import com.iosix.eldblesample.shared_prefs.DriverSharedPrefs;
import com.iosix.eldblesample.shared_prefs.LastStopSharedPrefs;
import com.iosix.eldblesample.shared_prefs.SessionManager;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.StatusDaoViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class SplashActivity extends BaseActivity {
    private DayDaoViewModel daoViewModel;
    private StatusDaoViewModel statusDaoViewModel;
    private ArrayList<LogEntity> logEntities;
    private ArrayList<LogEntity> logEntitiesCurr;
    private ArrayList<LogEntity> logEntitiesLastDays;
    private ArrayList<DayEntity> dayEntities;
    private final String time = "" + Calendar.getInstance().getTime();
    private final String today = time.split(" ")[1] + " " + time.split(" ")[2];
    private LastStopSharedPrefs lastStopSharedPrefs;
    private DriverSharedPrefs driverSharedPrefs;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main2;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.clear();
    }

    @Override
    public void initView() {
        super.initView();

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        daoViewModel = ViewModelProviders.of(this).get(DayDaoViewModel.class);
        statusDaoViewModel = ViewModelProviders.of(this).get(StatusDaoViewModel.class);
        logEntities = new ArrayList<>();
        dayEntities = new ArrayList<>();
        logEntitiesCurr = new ArrayList<>();
        logEntitiesLastDays = new ArrayList<>();

        driverSharedPrefs = DriverSharedPrefs.getInstance(this);
        lastStopSharedPrefs = LastStopSharedPrefs.getInstance(this.getApplicationContext());

        daoViewModel.deleteAllDays();
        for (int i = 7; i >= 0; i--) {
            String time = Day.getCalculatedDate(-i);
            try {
                daoViewModel.insertDay(new DayEntity(Day.getDayTime1(time), Day.getDayName2(time)));
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        statusDaoViewModel.getmAllStatus().observe(this,statusEntites->{
            logEntities.addAll(statusEntites);
        });

        daoViewModel.getMgetAllDays().observe(this,dayEntities -> {
            this.dayEntities.addAll(dayEntities);
        });

        new Handler().postDelayed(() -> {

            SessionManager sessionManager = SessionManager.getInstance(getApplicationContext());
            if(sessionManager.fetchAccessToken() != null){
                if (lastStopSharedPrefs.getLastStopTime() != 0 && !lastStopSharedPrefs.getLastStopDate().equals("")){
                    if (lastStopSharedPrefs.getLastStopDate().equals(today)){

                        for (int i = 0; i < logEntities.size(); i++) {
                            if (!logEntities.get(i).getDriverId().equals(driverSharedPrefs.getDriverId()) || logEntities.get(i).getTo_status() > 5){
                                logEntities.remove(logEntities.get(i));
                            }
                        }

                        if (logEntities.size() > 0){
                            for (int i = 0; i < dayEntities.size(); i++) {
                                logEntitiesCurr.clear();
                                logEntitiesLastDays.clear();
                                for (int j = 0; j < logEntities.size(); j++) {
                                    if (logEntities.get(j).getTime().equals(dayEntities.get(i).getDay())){
                                        logEntitiesCurr.add(logEntities.get(j));
                                    }
                                }
                                for (int j = 0; j < i; j++) {
                                    for (int k = 0; k < logEntities.size(); k++) {
                                        if (logEntities.get(k).getTime().equals(dayEntities.get(j).getDay())){
                                            logEntitiesLastDays.add(logEntities.get(k));
                                        }
                                    }
                                }
                                if (logEntitiesCurr.size() == 0){
                                    if (logEntitiesLastDays.size() > 0){
                                        statusDaoViewModel.insertStatus(new LogEntity(
                                                        logEntitiesLastDays.get(logEntitiesLastDays.size()-1).getDriverId(),
                                                        logEntitiesLastDays.get(logEntitiesLastDays.size()-1).getTo_status(),
                                                        logEntitiesLastDays.get(logEntitiesLastDays.size()-1).getLocation(),
                                                        logEntitiesLastDays.get(logEntitiesLastDays.size()-1).getNote(),
                                                        null,
                                                        dayEntities.get(i).getDay(),
                                                        0)
                                        );
                                    }else {
                                        statusDaoViewModel.insertStatus(new LogEntity(
                                                driverSharedPrefs.getDriverId(),
                                                EnumsConstants.STATUS_OFF,
                                                null,
                                                null,
                                                null,
                                                dayEntities.get(i).getDay(),
                                                0)
                                        );
                                    }
                                }
                            }

                        }

                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else if (lastStopSharedPrefs.getLastStopDate().equals(Day.getCalculatedDate(-1))){
                        if ((getCurrentSeconds() - lastStopSharedPrefs.getLastStopTime()) < 0){

                            for (int i = 0; i < logEntities.size(); i++) {
                                if (!logEntities.get(i).getDriverId().equals(driverSharedPrefs.getDriverId()) || logEntities.get(i).getTo_status() > 5){
                                    logEntities.remove(logEntities.get(i));
                                }
                            }

                            if (logEntities.size() > 0){
                                for (int i = 0; i < dayEntities.size(); i++) {
                                    logEntitiesCurr.clear();
                                    logEntitiesLastDays.clear();
                                    for (int j = 0; j < logEntities.size(); j++) {
                                        if (logEntities.get(j).getTime().equals(dayEntities.get(i).getDay())){
                                            logEntitiesCurr.add(logEntities.get(i));
                                        }
                                    }
                                    for (int j = 0; j < i; j++) {
                                        for (int k = 0; k < logEntities.size(); k++) {
                                            if (logEntities.get(k).getTime().equals(dayEntities.get(j).getDay())){
                                                logEntitiesLastDays.add(logEntities.get(k));
                                            }
                                        }
                                    }
                                    if (logEntitiesCurr.size() == 0){
                                        if (logEntitiesLastDays.size() > 0){
                                            statusDaoViewModel.insertStatus(new LogEntity(
                                                    logEntitiesLastDays.get(logEntitiesLastDays.size()-1).getDriverId(),
                                                    logEntitiesLastDays.get(logEntitiesLastDays.size()-1).getTo_status(),
                                                    logEntitiesLastDays.get(logEntitiesLastDays.size()-1).getLocation(),
                                                    logEntitiesLastDays.get(logEntitiesLastDays.size()-1).getNote(),
                                                    null,
                                                    dayEntities.get(i).getDay(),
                                                    0)
                                            );
                                        }else {
                                            statusDaoViewModel.insertStatus(new LogEntity(
                                                    driverSharedPrefs.getDriverId(),
                                                    EnumsConstants.STATUS_OFF,
                                                    null,
                                                    null,
                                                    null,
                                                    dayEntities.get(i).getDay(),
                                                    0)
                                            );
                                        }
                                    }
                                }

                            }

                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }else {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }else {

                    for (int i = 0; i < logEntities.size(); i++) {
                        if (!logEntities.get(i).getDriverId().equals(driverSharedPrefs.getDriverId()) || logEntities.get(i).getTo_status() > 5){
                            logEntities.remove(logEntities.get(i));
                        }
                    }

                    if (logEntities.size() > 0){
                        for (int i = 0; i < dayEntities.size(); i++) {
                            logEntitiesCurr.clear();
                            logEntitiesLastDays.clear();
                            for (int j = 0; j < logEntities.size(); j++) {
                                if (logEntities.get(j).getTime().equals(dayEntities.get(i).getDay())){
                                    logEntitiesCurr.add(logEntities.get(i));
                                }
                            }
                            for (int j = 0; j < i; j++) {
                                for (int k = 0; k < logEntities.size(); k++) {
                                    if (logEntities.get(k).getTime().equals(dayEntities.get(j).getDay())){
                                        logEntitiesLastDays.add(logEntities.get(k));
                                    }
                                }
                            }
                            if (logEntitiesCurr.size() == 0){
                                if (logEntitiesLastDays.size() > 0){
                                    statusDaoViewModel.insertStatus(new LogEntity(
                                            logEntitiesLastDays.get(logEntitiesLastDays.size()-1).getDriverId(),
                                            logEntitiesLastDays.get(logEntitiesLastDays.size()-1).getTo_status(),
                                            logEntitiesLastDays.get(logEntitiesLastDays.size()-1).getLocation(),
                                            logEntitiesLastDays.get(logEntitiesLastDays.size()-1).getNote(),
                                            null,
                                            dayEntities.get(i).getDay(),
                                            0)
                                    );
                                }else {
                                    statusDaoViewModel.insertStatus(new LogEntity(
                                            driverSharedPrefs.getDriverId(),
                                            EnumsConstants.STATUS_OFF,
                                            null,
                                            null,
                                            null,
                                            dayEntities.get(i).getDay(),
                                            0)
                                    );
                                }
                            }
                        }

                    }

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        },3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getViewModelStore();
        if (!isChangingConfigurations()) {
            getViewModelStore().clear();
        }
    }
}