package com.iosix.eldblesample.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.shared_prefs.DriverSharedPrefs;

public class CarrierInfoActivity extends BaseActivity {

    DriverSharedPrefs driverSharedPrefs;

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

        TextView idCarrierInfoName = findViewById(R.id.idCarrierInfoName);
        TextView idCarrierInfoAddress = findViewById(R.id.idCarrierInfoAddress);
        TextView idCarrierInfoCity = findViewById(R.id.idCarrierInfoCity);
        TextView idCarrierTexasCity = findViewById(R.id.idCarrierTexasCity);
        TextView idCarrierZipCity = findViewById(R.id.idCarrierZipCity);
        ImageView imageView = findViewById(R.id.idImageBack);
        imageView.setOnClickListener(view -> onBackPressed());

        driverSharedPrefs = DriverSharedPrefs.getInstance(getApplicationContext());

        idCarrierInfoName.setText(driverSharedPrefs.getCompany());
        idCarrierInfoAddress.setText(driverSharedPrefs.getMainOffice());
        idCarrierInfoCity.setText(driverSharedPrefs.getHomeTerAdd());

    }
}