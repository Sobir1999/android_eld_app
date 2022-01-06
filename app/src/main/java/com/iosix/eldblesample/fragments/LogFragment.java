package com.iosix.eldblesample.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.activity.MainActivity;
import com.iosix.eldblesample.adapters.LogFragmentPagerAdapter;
import com.iosix.eldblesample.adapters.LogRecyclerViewAdapter;
import com.iosix.eldblesample.customViews.CustomLiveRulerChart;
import com.iosix.eldblesample.customViews.CustomStableRulerChart;
import com.iosix.eldblesample.enums.EnumsConstants;
import com.iosix.eldblesample.models.ExampleSMSModel;
import com.iosix.eldblesample.retrofit.APIInterface;
import com.iosix.eldblesample.retrofit.ApiClient;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.StatusDaoViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class LogFragment extends Fragment {
    private LogFragmentPagerAdapter adapter;
    private ViewPager pager;
    private StatusDaoViewModel statusDaoViewModel;
    private CustomStableRulerChart idCustomChart;
    private CustomLiveRulerChart idCustomChartLive;
    private DvirViewModel dvirViewModel;
    private String currDay;
    private int last_status;
    private RecyclerView recyclerView_log;
    private LogRecyclerViewAdapter logRecyclerViewAdapter;


    public static LogFragment newInstance(String param1) {
        LogFragment fragment = new LogFragment();
        Bundle args = new Bundle();
        args.putString("ARG_PARAM1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currDay = getArguments().getString("ARG_PARAM1");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_log, container, false);
        idCustomChart = view.findViewById(R.id.idCustomChart);
        idCustomChartLive = view.findViewById(R.id.idCustomChartLive);
        recyclerView_log = view.findViewById(R.id.recyclerView_log_page);

        dvirViewModel = new DvirViewModel(requireActivity().getApplication());
        dvirViewModel = ViewModelProviders.of((FragmentActivity) requireContext()).get(DvirViewModel.class);


        statusDaoViewModel = new StatusDaoViewModel(requireActivity().getApplication());
        statusDaoViewModel = ViewModelProviders.of(this).get(StatusDaoViewModel.class);
        statusDaoViewModel.getmAllStatus().observe(getViewLifecycleOwner(),trackEntities ->{

        });

        dvirViewModel.getCurrentName().observe(getViewLifecycleOwner(),c ->{
            ArrayList<LogEntity> truckStatusEntities = new ArrayList<>();

            statusDaoViewModel = ViewModelProviders.of(this).get(StatusDaoViewModel.class);
            statusDaoViewModel.getmAllStatus().observe(getViewLifecycleOwner(), truckStatusEntities1 -> {
                for (LogEntity logEntity: truckStatusEntities1) {
                    if (logEntity.getTime().equalsIgnoreCase(c)) {
                        truckStatusEntities.add(logEntity);
                    }
                }

                if (truckStatusEntities.size() != 0) {
                    last_status = truckStatusEntities.get(truckStatusEntities.size() - 1).getTo_status();
                    logRecyclerViewAdapter = new LogRecyclerViewAdapter(requireContext(),truckStatusEntities);
                    recyclerView_log.setAdapter(logRecyclerViewAdapter);
                } else {
                    last_status = EnumsConstants.STATUS_OFF;
                }
            });
            if(c.equals(currDay)){
                idCustomChart.setVisibility(View.GONE);
                idCustomChartLive.setVisibility(View.VISIBLE);
                idCustomChartLive.setArrayList(truckStatusEntities);
            }else {
                idCustomChartLive.setVisibility(View.GONE);
                idCustomChart.setVisibility(View.VISIBLE);
                idCustomChart.setArrayList(truckStatusEntities);
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSMSHandler(ExampleSMSModel sendModels){
    }
}