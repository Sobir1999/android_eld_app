package com.iosix.eldblesample.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.adapters.LogFragmentPagerAdapter;

public class LogFragment extends Fragment {
    private LogFragmentPagerAdapter adapter;
    private ViewPager pager;

    public static LogFragment newInstance(String param1, String param2) {
        LogFragment fragment = new LogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_log, container, false);
        pager = view.findViewById(R.id.idLogFragmentPager);
        adapter = new LogFragmentPagerAdapter(getContext());
        pager.setAdapter(adapter);
        pager.setCurrentItem(adapter.getCount(), true);

        return view;
    }
}