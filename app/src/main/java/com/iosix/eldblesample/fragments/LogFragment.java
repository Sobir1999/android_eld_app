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
import java.util.List;

public class LogFragment extends Fragment {
    private StatusDaoViewModel statusDaoViewModel;
    private CustomStableRulerChart idCustomChart;
    private CustomLiveRulerChart idCustomChartLive;
    Context _context;
    private ChangeDateTimeBroadcast changeDateTimeBroadcast;
    private String time = "" + Calendar.getInstance().getTime();
    private String today = time.split(" ")[1] + " " + time.split(" ")[2];
    String currDay;
    private RecyclerView recyclerView_log;
    private LogRecyclerViewAdapter logRecyclerViewAdapter;
    private DvirViewModel dvirViewModel;
    List<LogEntity> truckStatusEntities = new ArrayList<>();
    ArrayList<LogEntity> truckStatusEntitiesCurr = new ArrayList<>();


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
        statusDaoViewModel = ViewModelProviders.of(requireActivity()).get(StatusDaoViewModel.class);
        dvirViewModel = ViewModelProviders.of(requireActivity()).get(DvirViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_log, container, false);
        idCustomChart = view.findViewById(R.id.idCustomChart);
        idCustomChartLive = view.findViewById(R.id.idCustomChartLive);
        recyclerView_log = view.findViewById(R.id.recyclerView_log_page);

        statusDaoViewModel.getmAllStatus().observe(getViewLifecycleOwner(),logEntities -> {
            truckStatusEntities = logEntities;
        });

        changeDateTimeBroadcast = new ChangeDateTimeBroadcast() {
            @Override
            public void onDayChanged() {
                time = "" + Calendar.getInstance().getTime();
                today = time.split(" ")[1] + " " + time.split(" ")[2];
                dvirViewModel.getCurrentName().postValue(today);
            }
        };
        _context.registerReceiver(changeDateTimeBroadcast, ChangeDateTimeBroadcast.getIntentFilter());

        dvirViewModel.getCurrentName().observe(getViewLifecycleOwner(), c ->{
                truckStatusEntitiesCurr.clear();
                logRecyclerViewAdapter = new LogRecyclerViewAdapter(requireContext().getApplicationContext(),truckStatusEntities,c);
                recyclerView_log.setAdapter(logRecyclerViewAdapter);
                for (int i = 0; i < truckStatusEntities.size(); i++) {
                    if (truckStatusEntities.get(i).getTime().equals(c)){
                        truckStatusEntitiesCurr.add(truckStatusEntities.get(i));
                    }
                }
                if(c.equals(today)){
                    idCustomChart.setVisibility(View.INVISIBLE);
                    idCustomChartLive.setVisibility(View.VISIBLE);
                    idCustomChartLive.setArrayList(truckStatusEntitiesCurr);
                }else {
                    idCustomChartLive.setVisibility(View.INVISIBLE);
                    idCustomChart.setVisibility(View.VISIBLE);
                    idCustomChart.setArrayList(truckStatusEntitiesCurr);
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