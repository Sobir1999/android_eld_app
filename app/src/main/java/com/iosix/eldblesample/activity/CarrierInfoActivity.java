package com.iosix.eldblesample.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ImageView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;

public class CarrierInfoActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_carrier_info;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.clear();
    }

    @Override
    public void initView() {
        super.initView();
        ImageView imageView = findViewById(R.id.idImageBack);
        imageView.setOnClickListener(view -> onBackPressed());
    }
}