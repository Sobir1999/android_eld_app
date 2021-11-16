package com.iosix.eldblesample.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.enums.Day;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;
import com.iosix.eldblesample.shared_prefs.SessionManager;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.StatusDaoViewModel;

import java.util.concurrent.ExecutionException;

public class SplashActivity extends BaseActivity {
    private StatusDaoViewModel statusDaoViewModel;
    private DayDaoViewModel daoViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main2;
    }

    @Override
    public void initView() {
        super.initView();
        getWindow().setStatusBarColor(ActivityCompat.getColor(this,R.color.example));

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        statusDaoViewModel = new StatusDaoViewModel(this.getApplication());
        daoViewModel = new DayDaoViewModel(this.getApplication());

        statusDaoViewModel = ViewModelProviders.of(this).get(StatusDaoViewModel.class);
        daoViewModel = ViewModelProviders.of(this).get(DayDaoViewModel.class);

        for (int i = 14; i >= 0; i--) {

            String time = Day.getCalculatedDate(-i);

            try {
                daoViewModel.insertDay(new DayEntity(Day.getDayTime1(time), Day.getDayName2(time)));
//                statusDaoViewModel.getmAllStatus().observe((LifecycleOwner) this, logEntities -> {
//                    if (!logEntities.isEmpty()) {
//                        for (LogEntity logEntity : logEntities ){
//
//                        }
//                        statusDaoViewModel.insertStatus(new LogEntity(logEntities.get(logEntities.size() - 1).getTo_status(), logEntities.get(logEntities.size() - 1).getTo_status(), null, null, null, Day.getDayTime1(time), 0));
//                    } else {
//                        statusDaoViewModel.insertStatus(new LogEntity(0, 0, null, null, null, Day.getDayTime1(time), 0));
//                    }
//                });
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            SessionManager sessionManager = new SessionManager(this);
            if(sessionManager.fetchAccessToken() == null){
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }else {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }, 2000);
//        }, 3000);
    }
}