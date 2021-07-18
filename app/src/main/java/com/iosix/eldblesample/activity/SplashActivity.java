package com.iosix.eldblesample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.StatusDaoViewModel;

public class SplashActivity extends AppCompatActivity {
    private StatusDaoViewModel statusDaoViewModel;
    private DayDaoViewModel daoViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_main2);

        statusDaoViewModel = new StatusDaoViewModel(this.getApplication());
        daoViewModel = new DayDaoViewModel(this.getApplication());

        statusDaoViewModel = ViewModelProviders.of(this).get(StatusDaoViewModel.class);
        daoViewModel = ViewModelProviders.of(this).get(DayDaoViewModel.class);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);

//                Bundle bundle = new Bundle();
//                bundle.putSerializable("dayDao", daoViewModel);
//                bundle.putSerializable("statusDao", statusDaoViewModel);
//                intent.putExtra("bundle", bundle);
//                intent.putExtra("dayDao", (Serializable) daoViewModel);
                startActivity(intent);
                finish();
            }
        }, 100);
//        }, 3000);

    }
}
