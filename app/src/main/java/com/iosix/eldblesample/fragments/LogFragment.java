package com.iosix.eldblesample.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.adapters.LogRecyclerViewAdapter;
import com.iosix.eldblesample.broadcasts.ChangeDateTimeBroadcast;
import com.iosix.eldblesample.customViews.CustomLiveRulerChart;
import com.iosix.eldblesample.customViews.CustomStableRulerChart;
import com.iosix.eldblesample.enums.EnumsConstants;
import com.iosix.eldblesample.models.ExampleSMSModel;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;
import com.iosix.eldblesample.shared_prefs.LastStatusData;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.StatusDaoViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;

public class LogFragment extends Fragment {
    private StatusDaoViewModel statusDaoViewModel;
    private CustomStableRulerChart idCustomChart;
    private CustomLiveRulerChart idCustomChartLive;
    Context _context;
    private int last_status;
    private ChangeDateTimeBroadcast changeDateTimeBroadcast;
    private String time = "" + Calendar.getInstance().getTime();
    private String today = time.split(" ")[1] + " " + time.split(" ")[2];
    private RecyclerView recyclerView_log;
    private LogRecyclerViewAdapter logRecyclerViewAdapter;
    private LastStatusData lastStatusData;
    private DvirViewModel dvirViewModel;


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
            String currDay = getArguments().getString("ARG_PARAM1");
        }
        dvirViewModel = ViewModelProviders.of(requireActivity()).get(DvirViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_log, container, false);
        idCustomChart = view.findViewById(R.id.idCustomChart);
        idCustomChartLive = view.findViewById(R.id.idCustomChartLive);
        recyclerView_log = view.findViewById(R.id.recyclerView_log_page);

        lastStatusData = LastStatusData.getInstance(requireContext().getApplicationContext());
        last_status = lastStatusData.getLastStatus();

        statusDaoViewModel = ViewModelProviders.of(this).get(StatusDaoViewModel.class);
        statusDaoViewModel.getmAllStatus().observe(getViewLifecycleOwner(),trackEntities ->{

        });

        changeDateTimeBroadcast = new ChangeDateTimeBroadcast() {
            @Override
            public void onDayChanged() {
                time = "" + Calendar.getInstance().getTime();
                today = time.split(" ")[1] + " " + time.split(" ")[2];
            }
        };
        _context.registerReceiver(changeDateTimeBroadcast, ChangeDateTimeBroadcast.getIntentFilter());

        dvirViewModel.getCurrentName().observe(getViewLifecycleOwner(), c ->{
            ArrayList<LogEntity> truckStatusEntities = new ArrayList<>();
            statusDaoViewModel.getmAllStatus().observe(getViewLifecycleOwner(), truckStatusEntities1 -> {
                for (LogEntity logEntity: truckStatusEntities1) {
                    if (logEntity.getTime().equalsIgnoreCase(c)) {
                        truckStatusEntities.add(logEntity);
                    }
                }

                logRecyclerViewAdapter = new LogRecyclerViewAdapter(requireContext().getApplicationContext(),truckStatusEntities,last_status,c);
                recyclerView_log.setAdapter(logRecyclerViewAdapter);
            });
            if(c.equals(today)){
                idCustomChart.setVisibility(View.INVISIBLE);
                idCustomChartLive.setVisibility(View.VISIBLE);
                idCustomChartLive.setArrayList(truckStatusEntities);
            }else {
                idCustomChartLive.setVisibility(View.INVISIBLE);
                idCustomChart.setVisibility(View.VISIBLE);
                idCustomChart.setArrayList(truckStatusEntities);
            }
        });


        return view;
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        _context = context;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().getViewModelStore().clear();
        this.getViewModelStore().clear();
        _context.unregisterReceiver(changeDateTimeBroadcast);
    }
}