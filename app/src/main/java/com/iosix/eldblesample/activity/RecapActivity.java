package com.iosix.eldblesample.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.enums.HOSConstants;

public class RecapActivity extends BaseActivity {

    private TextView breakTv;
    private TextView drivingTv;
    private TextView shiftTv;
    private TextView cycleTv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recap;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.clear();
    }

    @Override
    public void initView() {
        super.initView();
        breakTv = findViewById(R.id.idTimeBreak);
        drivingTv = findViewById(R.id.idTimeDriving);
        shiftTv = findViewById(R.id.idTimeShift);
        cycleTv = findViewById(R.id.idTimeCycle);
    }

    private void calculateLimits(){
        breakTv.setText(HOSConstants.BREAK);
        drivingTv.setText(HOSConstants.DRIVING);
        shiftTv.setText(HOSConstants.SHIFT);
        cycleTv.setText(HOSConstants.CYCLE);
    }
}