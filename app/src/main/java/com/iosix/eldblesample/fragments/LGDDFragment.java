package com.iosix.eldblesample.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.adapters.LGDDFragmentAdapter;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class LGDDFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LGDDFragmentAdapter adapter;
    private FragmentManager manager;
    private TextView textFrag;
    private int index = 0;
    private DayDaoViewModel dayDaoViewModel;
    private List<DayEntity> dayEntities = new ArrayList<>();

    private String[] str = {"1", "2", "3", "5", "6"};

    public static LGDDFragment newInstance(int position, DayDaoViewModel viewModel) {
        LGDDFragment fragment = new LGDDFragment();
        fragment.dayDaoViewModel = viewModel;
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_l_g_d_d, container, false);

        dayDaoViewModel.getMgetAllDays().observe((LifecycleOwner) container.getContext(), new Observer<List<DayEntity>>() {
            @Override
            public void onChanged(List<DayEntity> dayEntities) {
//                for (int i=0; i<dayEntities.size(); i++) {
//                    Log.d("FRAG", "onChanged: " + dayEntities.get(i).getDay());
//                }
                LGDDFragment.this.dayEntities = dayEntities;
                index = dayEntities.size() - 1;
            }
        });

        Bundle bundle = getArguments();
        assert bundle != null;
        int pos = bundle.getInt("position");

        view.findViewById(R.id.idImageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();
            }
        });

        textFrag = view.findViewById(R.id.idTextFrag);

        textFrag.setText(dayEntities.get(index).getDay());

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.pager);
        adapter = new LGDDFragmentAdapter(getChildFragmentManager(), 1);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(pos);
        tabLayout.setupWithViewPager(viewPager);

        clickPrevAndNext(view);

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return view;
    }


    private void clickPrevAndNext(View v){
        TextView prev, next;
        prev = v.findViewById(R.id.idPreviewLog);
        next = v.findViewById(R.id.idNextLog);

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index > 0) {
                    index--;
                    textFrag.setText(dayEntities.get(index).getDay());
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index < dayEntities.size()-1) {
                    index++;
                    textFrag.setText(dayEntities.get(index).getDay());
                }
            }
        });
    }

}