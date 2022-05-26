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
import com.iosix.eldblesample.shared_prefs.LastStopSharedPrefs;
import com.iosix.eldblesample.shared_prefs.SessionManager;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.StatusDaoViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SplashActivity extends BaseActivity {
    private DayDaoViewModel daoViewModel;
    private ArrayList<LogEntity> logEntities;
    private ArrayList<LogEntity> truckEntities;
    private String time = "" + Calendar.getInstance().getTime();
    private String today = time.split(" ")[1] + " " + time.split(" ")[2];
    private LastStopSharedPrefs lastStopSharedPrefs;

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

        daoViewModel = new DayDaoViewModel(this.getApplication());

        daoViewModel = ViewModelProviders.of(this).get(DayDaoViewModel.class);
        logEntities = new ArrayList<>();
        truckEntities = new ArrayList<>();
        lastStopSharedPrefs = new LastStopSharedPrefs(this);

        daoViewModel.deleteAllDays();
        for (int i = 7; i >= 0; i--) {
            String time = Day.getCalculatedDate(-i);
            try {
                daoViewModel.insertDay(new DayEntity(Day.getDayTime1(time), Day.getDayName2(time)));
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        daoViewModel.getmAllStatus().observe(this,statusEntites->{
            for (int i = 0; i < statusEntites.size() ; i++) {
                if (statusEntites.get(i).getTime().equals(today)){
                    logEntities.add(statusEntites.get(i));
                }
                truckEntities.add(statusEntites.get(i));
            }


        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (logEntities.size() == 0){
                    if (truckEntities.size() != 0){
                        try {
                            daoViewModel.insertStatus(new LogEntity(truckEntities.get(truckEntities.size()-1).getTo_status(),truckEntities.get(truckEntities.size()-1).getTo_status(),
                                    truckEntities.get(truckEntities.size()-1).getLocation(),truckEntities.get(truckEntities.size()-1).getNote(),null,today,0));
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else {
                        try {
                            daoViewModel.insertStatus(new LogEntity(EnumsConstants.STATUS_OFF,EnumsConstants.STATUS_OFF,
                                    null,null,null,today,0));
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                SessionManager sessionManager = SessionManager.getInstance(getApplicationContext());
                if(sessionManager.fetchAccessToken() != null){
                    if (lastStopSharedPrefs.getLastStopTime() != 0 && !lastStopSharedPrefs.getLastStopDate().equals("")){
                        if (lastStopSharedPrefs.getLastStopDate().equals(today)){
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }else if (lastStopSharedPrefs.getLastStopDate().equals(Day.getCalculatedDate(-1))){
                            if ((getCurrentSeconds() - lastStopSharedPrefs.getLastStopTime()) < 0){
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
            }
        },3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}