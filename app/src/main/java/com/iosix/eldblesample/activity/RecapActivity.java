package com.iosix.eldblesample.activity;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;

public class RecapActivity extends BaseActivity {


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
    }
}