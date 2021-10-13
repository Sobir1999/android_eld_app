package com.iosix.eldblesample.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.activity.MainActivity;
import com.iosix.eldblesample.adapters.LGDDFragmentAdapter;
import com.iosix.eldblesample.models.ExampleSMSModel;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.DvirViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class LGDDFragment extends Fragment{
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LGDDFragmentAdapter adapter;
    private FragmentManager manager;
    private TextView textFrag;
    private int index = 0;
    private int pos;
    private DayDaoViewModel dayDaoViewModel;
    private DvirViewModel dvirViewModel;
    private List<DayEntity> dayEntities = new ArrayList<>();
    private String currDay;
    private ArrayList<String> mParams;

    private String[] str = {"1", "2", "3", "5", "6"};

    public static LGDDFragment newInstance(int position, DayDaoViewModel viewModel,ArrayList<String> mParams) {
        LGDDFragment fragment = new LGDDFragment();
        fragment.dayDaoViewModel = viewModel;
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putStringArrayList("params",mParams);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                getActivity().finish();
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_l_g_d_d, container, false);

        dvirViewModel = new DvirViewModel(requireActivity().getApplication());
        dvirViewModel = ViewModelProviders.of((FragmentActivity) requireContext()).get(DvirViewModel.class);

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
        pos = bundle.getInt("position");
        mParams = bundle.getStringArrayList("params");

        view.findViewById(R.id.idImageBack).setOnClickListener(v -> {
            getActivity().finish();
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        textFrag = view.findViewById(R.id.idTextFrag);


        if (mParams != null){
            currDay = mParams.get(7);
            dvirViewModel.getCurrentName().setValue(currDay);
            for (int i = 0; i < dayEntities.size(); i++) {
                if (dayEntities.get(i).getDay().equals(mParams.get(7))){
                    index = i;
                }
            }
        }else {
            currDay = dayEntities.get(index).getDay();
            dvirViewModel.getCurrentName().setValue(currDay);
        }

        textFrag.setText(currDay);
        adapter = new LGDDFragmentAdapter(getChildFragmentManager(), 1, currDay,mParams);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.pager);

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

        clickPrevAndNext(view);

        return view;
    }

    private void clickPrevAndNext(View v){
        TextView prev, next;
        prev = v.findViewById(R.id.idPreviewLog);
        next = v.findViewById(R.id.idNextLog);

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