package com.iosix.eldblesample.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;

public class RulesActivity extends BaseActivity {

    ImageView imageView;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_rules;
    }

    @Override
    public void initView() {
        super.initView();

        imageView = findViewById(R.id.idImageBack);
        imageView.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.clear();
    }
}
