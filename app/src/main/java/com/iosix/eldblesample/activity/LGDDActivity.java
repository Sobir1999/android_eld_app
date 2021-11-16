package com.iosix.eldblesample.activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
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

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    private LGDDFragmentAdapter adapter;
    private FragmentManager manager;
    private TextView textFrag,addFrag;
    private int index = 0;
    private int pos;
    private DayDaoViewModel dayDaoViewModel;
    private DvirViewModel dvirViewModel;
    private List<DayEntity> dayEntities = new ArrayList<>();
    private String currDay;
    private String mParams;
    private String time = "" + Calendar.getInstance().getTime();

    private String[] str = {"1", "2", "3", "5", "6"};

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
//        getWindow().setStatusBarColor(ActivityCompat.getColor(this,R.color.colorPrimaryDark));

        dvirViewModel = new DvirViewModel(this.getApplication());
        dvirViewModel = ViewModelProviders.of(this).get(DvirViewModel.class);

        dayDaoViewModel = new DayDaoViewModel(this.getApplication());
        dayDaoViewModel = ViewModelProviders.of(this).get(DayDaoViewModel.class);

        dayDaoViewModel.getMgetAllDays().observe(this, dayEntities -> {
            LGDDActivity.this.dayEntities = dayEntities;
            if (mParams != null){
                currDay = mParams;
                dvirViewModel.getCurrentName().setValue(currDay);
                for (int i = 0; i < dayEntities.size(); i++) {
                    if (dayEntities.get(i).getDay().equals(mParams)){
                        index = i;
                    }
                }
            }else {
                currDay = time.split(" ")[1] + " " + time.split(" ")[2];
                dvirViewModel.getCurrentName().setValue(currDay);
                index = dayEntities.size() - 1;
            }
            textFrag.setText(currDay);
            Log.d("day","currDay: " + index);
        });

        pos = getIntent().getIntExtra("position",0);
        if (getIntent().getStringExtra("day") != null){
            mParams = getIntent().getStringExtra("day");
        }else {
            mParams = getIntent().getStringExtra("currDay");
        }

        Log.d("day","currDay: " + mParams);


        findViewById(R.id.idImageBack).setOnClickListener(v -> {
            this.finish();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        textFrag = findViewById(R.id.idTextFrag);
//        addFrag = findViewById(R.id.idAddFrag);
//
//        addFrag.setOnClickListener(v -> {
//            if (pos == 3){
//                loadFragment(AddDvirFragment.newInstance(currDay));
//            }
//        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new LGDDFragmentAdapter(fragmentManager, 1, mParams);
        Log.d("position","position:" + currDay);


        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.pager);
        appBarLayout = findViewById(R.id.idAppbar);

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