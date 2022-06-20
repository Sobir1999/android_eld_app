package com.iosix.eldblesample.activity;

import static com.iosix.eldblesample.MyApplication.userData;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Switch;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.viewModel.DvirViewModel;

import java.util.Calendar;

public class NightModeActivity extends BaseActivity {

    private SwitchCompat idSwitchCompat;
    private ImageView imageView;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_night_mode;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }

    @Override
    public void initView() {
        super.initView();
        idSwitchCompat = findViewById(R.id.idSwitchCompat);
        imageView = findViewById(R.id.idImageBack);
        imageView.setOnClickListener(view -> onBackPressed());

        getModeState();
    }

    private void getModeState(){
        checkNightModeActivated();
        idSwitchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                idSwitchCompat.setChecked(true);
                userData.saveAutoSwitch(true);
                recreate();
            }else {
                recreate();
                idSwitchCompat.setChecked(false);
                userData.saveAutoSwitch(false);
            }
        });
    }

    private void checkNightModeActivated() {
        idSwitchCompat.setChecked(userData.getAutoSwitch());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}