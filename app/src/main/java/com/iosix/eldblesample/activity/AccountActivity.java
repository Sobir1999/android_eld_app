package com.iosix.eldblesample.activity;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.shared_prefs.DriverSharedPrefs;
import com.iosix.eldblesample.viewModel.UserViewModel;

public class AccountActivity extends BaseActivity {

    ImageView imageView;
    TextView idAccountFirstName,idAccountLastName,idAccountPhoneNumber;
    UserViewModel userViewModel;
    DriverSharedPrefs driverSharedPrefs;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    public void initView() {
        super.initView();
        imageView = findViewById(R.id.idImageBack);
        idAccountFirstName = findViewById(R.id.idAccountFirstName);
        idAccountLastName = findViewById(R.id.idAccountLastName);
        idAccountPhoneNumber = findViewById(R.id.idAccountPhoneNumber);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        driverSharedPrefs = DriverSharedPrefs.getInstance(getApplicationContext());

            idAccountFirstName.setText(driverSharedPrefs.getFirstname());
            idAccountLastName.setText(driverSharedPrefs.getLastname());
            idAccountPhoneNumber.setText(driverSharedPrefs.getPhoneNumber());

        imageView.setOnClickListener(view -> onBackPressed());
    }
}
