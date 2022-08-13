package com.iosix.eldblesample.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.adapters.LGDDFragmentAdapter;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.fragments.DVIRFragment;
import com.iosix.eldblesample.fragments.DocsFragment;
import com.iosix.eldblesample.fragments.GeneralFragment;
import com.iosix.eldblesample.fragments.LogFragment;
import com.iosix.eldblesample.models.ExampleSMSModel;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.DvirViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class LGDDActivity extends BaseActivity{

    private TextView textFrag;
    private int index = 0;
    private DvirViewModel dvirViewModel;
    private List<DayEntity> dayEntities = new ArrayList<>();
    private String currDay;
    private String mParams;
    private DayDaoViewModel dayDaoViewModel;
    ViewPager2 viewPager;


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

        dvirViewModel = ViewModelProviders.of(this).get(DvirViewModel.class);

        dayDaoViewModel = ViewModelProviders.of(this).get(DayDaoViewModel.class);

        dayDaoViewModel.getMgetAllDays().observe(this, dayEntities -> {
            LGDDActivity.this.dayEntities = dayEntities;
            dvirViewModel.getCurrentName().postValue(mParams);
            for (int i = 0; i < dayEntities.size(); i++) {
                if (dayEntities.get(i).getDay().equals(mParams)){
                     index = i;
                }
            }
            textFrag.setText(currDay);
        });

        int pos = getIntent().getIntExtra("position", 4);
            mParams = getIntent().getStringExtra("currDay");
            currDay = mParams;

        findViewById(R.id.idImageBack).setOnClickListener(v -> onBackPressed());

        textFrag = findViewById(R.id.idTextFrag);
        LGDDFragmentAdapter adapter = new LGDDFragmentAdapter(getSupportFragmentManager(),getLifecycle());
        int limit = (adapter.getItemCount() > 1 ? adapter.getItemCount() - 1 : 1);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Log"));
        tabLayout.addTab(tabLayout.newTab().setText("General"));
        tabLayout.addTab(tabLayout.newTab().setText("Sign"));
        tabLayout.addTab(tabLayout.newTab().setText("Dvir"));

        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(limit);
        viewPager.setCurrentItem(pos,false);
        tabLayout.selectTab(tabLayout.getTabAt(pos));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                dvirViewModel.getCurrentName().postValue(currDay);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
                dvirViewModel.getCurrentName().postValue(currDay);
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
                dvirViewModel.getCurrentName().postValue(currDay);

            }
        });

        next.setOnClickListener(v1 -> {
            if (index < dayEntities.size()-1) {
                index++;
                currDay = dayEntities.get(index).getDay();
                textFrag.setText(currDay);
                dvirViewModel.getCurrentName().postValue(currDay);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.getViewModelStore().clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.getViewModelStore().clear();
    }
}