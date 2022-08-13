package com.iosix.eldblesample.activity;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.fragments.BeginInspectionFragment;
import com.iosix.eldblesample.fragments.SendLogFragment;

public class InspectionModuleActivity extends BaseActivity {

    Button beginInspection, sendLogs, sendELDFile;
    ImageView img;
    TextView idTextFrag;
    String filename;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inspection_module;
    }

    @Override
    public void initView() {
        super.initView();
        loadViews();


    }

    private void loadViews() {

        img = findViewById(R.id.idImageBack);
        beginInspection = findViewById(R.id.idBeginInspection);
        sendLogs = findViewById(R.id.idSendLogs);
        sendELDFile = findViewById(R.id.idSendELDFile);
        idTextFrag = findViewById(R.id.idTextFrag);

        img.setOnClickListener(v -> {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0){
                idTextFrag.setText(R.string.inspection_mode);
            }
            onBackPressed();
        });

        sendLogs.setOnClickListener(v -> {
            loadFragment(SendLogFragment.newInstance());
            idTextFrag.setText(R.string.send_logs);
        });

        beginInspection.setOnClickListener(v -> {
            loadFragment(BeginInspectionFragment.newInstance());
            idTextFrag.setText(R.string.log_reports);
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        assert fm != null;
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.addToBackStack("fragment");
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}