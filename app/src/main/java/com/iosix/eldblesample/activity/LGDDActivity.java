package com.iosix.eldblesample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.adapters.LGDDFragmentAdapter;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.models.ExampleSMSModel;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.DvirViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LGDDActivity extends BaseActivity {

    private TextView textFrag;
    private int index = 0;
    private DvirViewModel dvirViewModel;
    private List<DayEntity> dayEntities = new ArrayList<>();
    private String currDay;
    private String mParams;
    private final String time = "" + Calendar.getInstance().getTime();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lgdd;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }

    @Override
    public void initView() {
        super.initView();

        dvirViewModel = new DvirViewModel(this.getApplication());
        dvirViewModel = ViewModelProviders.of(this).get(DvirViewModel.class);

        DayDaoViewModel dayDaoViewModel;
        dayDaoViewModel = ViewModelProviders.of(this).get(DayDaoViewModel.class);

        dayDaoViewModel.getMgetAllDays().observe(this, dayEntities -> {
            LGDDActivity.this.dayEntities = dayEntities;
            dvirViewModel.getCurrentName().setValue(mParams);
            for (int i = 0; i < dayEntities.size(); i++) {
                if (dayEntities.get(i).getDay().equals(mParams)){
                     index = i;
                }
            }
            textFrag.setText(mParams);
        });

        int pos = getIntent().getIntExtra("position", 0);
            mParams = getIntent().getStringExtra("currDay");

        findViewById(R.id.idImageBack).setOnClickListener(v -> {
            this.finish();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        textFrag = findViewById(R.id.idTextFrag);
        FragmentManager fragmentManager = getSupportFragmentManager();
        LGDDFragmentAdapter adapter = new LGDDFragmentAdapter(fragmentManager, 1, mParams);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.pager);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(pos);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                EventBus.getDefault().postSticky(new ExampleSMSModel(currDay));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        clickPrevAndNext();

    }

    private void clickPrevAndNext(){
        TextView prev, next;
        prev = findViewById(R.id.idPreviewLog);
        next = findViewById(R.id.idNextLog);

        prev.setOnClickListener(v12 -> {
            if (index > 0) {
                index--;
                currDay = dayEntities.get(index).getDay();
                textFrag.setText(currDay);
                dvirViewModel.getCurrentName().setValue(currDay);
                EventBus.getDefault().postSticky(new ExampleSMSModel(currDay));

            }
        });

        next.setOnClickListener(v1 -> {
            if (index < dayEntities.size()-1) {
                index++;
                currDay = dayEntities.get(index).getDay();
                textFrag.setText(currDay);
                dvirViewModel.getCurrentName().setValue(currDay);
                EventBus.getDefault().postSticky(new ExampleSMSModel(currDay));
            }
        });
    }
}