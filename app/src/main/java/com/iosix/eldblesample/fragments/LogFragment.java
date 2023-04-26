package com.iosix.eldblesample.fragments;

import static com.iosix.eldblesample.enums.Day.getDayFormat;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.broadcasts.ChangeDateTimeBroadcast;
import com.iosix.eldblesample.customViews.CustomLiveRulerChart;
import com.iosix.eldblesample.customViews.CustomStableRulerChart;
import com.iosix.eldblesample.customViews.MyListView;
import com.iosix.eldblesample.interfaces.FetchStatusCallback;
import com.iosix.eldblesample.models.Status;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.StatusDaoViewModel;

import org.threeten.bp.ZonedDateTime;

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
    private MyListView recyclerView_log;
    private DvirViewModel dvirViewModel;



    public static LogFragment newInstance() {
        LogFragment fragment = new LogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusDaoViewModel = ViewModelProviders.of(requireActivity()).get(StatusDaoViewModel.class);
        dvirViewModel = ViewModelProviders.of(requireActivity()).get(DvirViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        changeDateTimeBroadcast = new ChangeDateTimeBroadcast() {
            @Override
            public void onDayChanged() {
                time = "" + Calendar.getInstance().getTime();
                today = time.split(" ")[1] + " " + time.split(" ")[2];
                dvirViewModel.getCurrentName().postValue(today);
            }
        };
        _context.registerReceiver(changeDateTimeBroadcast, ChangeDateTimeBroadcast.getIntentFilter());

        dvirViewModel.getCurrentName().observe(getViewLifecycleOwner(), c -> {
                statusDaoViewModel.getLastActonStatus(c, new FetchStatusCallback() {
                            @Override
                            public void onSuccess(Status status, List<Status> statuses) {
                                statusDaoViewModel.logRecyclerList(getContext(),status,c,recyclerView_log);
                            }

                            @Override
                            public void onError(Throwable throwable) {

                            }
                });
            if(c.equals(getDayFormat(ZonedDateTime.now()))){
                idCustomChart.setVisibility(View.INVISIBLE);
                idCustomChartLive.setVisibility(View.VISIBLE);
                statusDaoViewModel.getCurDateStatus(idCustomChartLive,idCustomChart,c);
            }else {
                idCustomChartLive.setVisibility(View.INVISIBLE);
                idCustomChart.setVisibility(View.VISIBLE);
                statusDaoViewModel.getCurDateStatus(idCustomChartLive,idCustomChart,c);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_log, container, false);
        idCustomChart = view.findViewById(R.id.idCustomChart);
        idCustomChartLive = view.findViewById(R.id.idCustomChartLive);
        recyclerView_log = view.findViewById(R.id.recyclerView_log_page);


        return view;
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        _context = context;
    }
}