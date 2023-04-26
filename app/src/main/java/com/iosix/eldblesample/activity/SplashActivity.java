package com.iosix.eldblesample.activity;


import static com.iosix.eldblesample.enums.Day.getDayFormat;

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
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.shared_prefs.SessionManager;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;

public class SplashActivity extends BaseActivity {
    private DayDaoViewModel daoViewModel;

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

        daoViewModel.deleteAllDays();

        new Handler().postDelayed(() -> {

            SessionManager sessionManager = SessionManager.getInstance(getApplicationContext());
            if (sessionManager.fetchAccessToken() == null){
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("login","main");
                        startActivity(intent);
                        finish();
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