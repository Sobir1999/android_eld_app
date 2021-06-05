package com.iosix.eldblesample.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.adapters.LGDDFragmentAdapter;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

public class LGDDFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LGDDFragmentAdapter adapter;
    private FragmentManager manager;

    public static LGDDFragment newInstance(int position) {
        LGDDFragment fragment = new LGDDFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_l_g_d_d, container, false);

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

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.pager);
        adapter = new LGDDFragmentAdapter(getChildFragmentManager(), 1);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(pos);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

}