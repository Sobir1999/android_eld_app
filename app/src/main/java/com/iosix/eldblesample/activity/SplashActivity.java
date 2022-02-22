package com.iosix.eldblesample.activity;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.enums.Day;
import com.iosix.eldblesample.enums.EnumsConstants;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;
import com.iosix.eldblesample.shared_prefs.LastStatusData;
import com.iosix.eldblesample.shared_prefs.LastStopSharedPrefs;
import com.iosix.eldblesample.shared_prefs.SessionManager;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.StatusDaoViewModel;
import com.iosix.eldblesample.viewModel.UserViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SplashActivity extends BaseActivity {
    private StatusDaoViewModel statusDaoViewModel;
    private DayDaoViewModel daoViewModel;
    private ArrayList<LogEntity> logEntities;
    private ArrayList<LogEntity> truckEntities;
    private String time = "" + Calendar.getInstance().getTime();
    private String today = time.split(" ")[1] + " " + time.split(" ")[2];
    private LastStatusData lastStatusData;
    private LastStopSharedPrefs lastStopSharedPrefs;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main2;
    }

    @Override
    public void initView() {
        super.initView();

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        statusDaoViewModel = new StatusDaoViewModel(this.getApplication());
        daoViewModel = new DayDaoViewModel(this.getApplication());

        statusDaoViewModel = ViewModelProviders.of(this).get(StatusDaoViewModel.class);
        daoViewModel = ViewModelProviders.of(this).get(DayDaoViewModel.class);
        lastStatusData = new LastStatusData(this);
        logEntities = new ArrayList<>();
        truckEntities = new ArrayList<>();
        lastStopSharedPrefs = new LastStopSharedPrefs(this);

        daoViewModel.deleteAllDays();
        for (int i = 14; i >= 0; i--) {
            String time = Day.getCalculatedDate(-i);
            try {
                daoViewModel.insertDay(new DayEntity(Day.getDayTime1(time), Day.getDayName2(time)));
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        statusDaoViewModel.getmAllStatus().observe(this,statusEntites->{
            for (LogEntity logEntity: statusEntites){
                if (logEntity.getTime().equals(today)){
                    logEntities.add(logEntity);
                }
                truckEntities.add(logEntity);
            }
        });


        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            SessionManager sessionManager = new SessionManager(this);
            if (logEntities.size() == 0){
                if (truckEntities.size() != 0){
                    statusDaoViewModel.insertStatus(new LogEntity(lastStatusData.getLastStatus(),lastStatusData.getLastStatus(),
                            truckEntities.get(truckEntities.size()-1).getLocation(),
                            truckEntities.get(truckEntities.size()-1).getNote(),null,today,0));
                }else {
                    statusDaoViewModel.insertStatus(new LogEntity(lastStatusData.getLastStatus(),lastStatusData.getLastStatus(),
                            null,
                            null,null,today,0));
                }
            }
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
                }
            }else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    private int getCurrentSeconds() {
        int hour = Calendar.getInstance().getTime().getHours();
        int minute = Calendar.getInstance().getTime().getMinutes();
        int second = Calendar.getInstance().getTime().getSeconds();
        return hour * 3600 + minute * 60 + second;
    }
}