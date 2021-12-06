package com.iosix.eldblesample.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.activity.AddDvirActivity;
import com.iosix.eldblesample.activity.MainActivity;
import com.iosix.eldblesample.adapters.DvirlistAdapter;
import com.iosix.eldblesample.models.ExampleSMSModel;
import com.iosix.eldblesample.roomDatabase.entities.DvirEntity;
import com.iosix.eldblesample.viewModel.DvirViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class DVIRFragment extends Fragment {
    private boolean hasDefects = false;
    private TextView textView;
    private ImageView imageView;
    private Button createButton;
    private LinearLayout container1;
    private DvirViewModel dvirViewModel;
    private String currDay;
    private DvirlistAdapter adapter;
    private RecyclerView dvir_recyclerview;
    private List<DvirEntity> dvirEntities;


    public static DVIRFragment newInstance(String currDay) {
        DVIRFragment fragment = new DVIRFragment();
        Bundle args = new Bundle();
        args.putString("ARG_PARAM1", currDay);
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_d_v_i_r, container, false);
        container1 = v.findViewById(R.id.idDVIRContainer1);
        textView = v.findViewById(R.id.idDvirExText);
        createButton = v.findViewById(R.id.create_new_dvir_button);
        dvir_recyclerview = v.findViewById(R.id.idDvirRecyclerView);

        dvirViewModel = new DvirViewModel(requireActivity().getApplication());
        dvirViewModel = ViewModelProviders.of((FragmentActivity) requireContext()).get(DvirViewModel.class);

        dvirEntities = new ArrayList<>();

        dvirViewModel.getMgetDvirs().observe(getViewLifecycleOwner(),dvirEntities1 -> {
            adapter = new DvirlistAdapter(getContext(),dvirViewModel,currDay);
                dvir_recyclerview.setVisibility(View.VISIBLE);
                container1.setVisibility(View.GONE);
                dvir_recyclerview.setAdapter(adapter);
        });


        dvirViewModel.getCurrentName().observe(getViewLifecycleOwner(), s -> {
            adapter = new DvirlistAdapter(requireContext(),dvirViewModel,s);
            currDay = s;
            dvir_recyclerview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            container1.setVisibility(View.GONE);
            dvir_recyclerview.setVisibility(View.VISIBLE);
            if (adapter.getItemCount() != 0) {
                dvirViewModel.getMgetDvirs().observe(getViewLifecycleOwner(),dvirEntities1 -> {
                    dvir_recyclerview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    if (adapter.getItemCount() == 0){
                        container1.setVisibility(View.VISIBLE);
                        dvir_recyclerview.setVisibility(View.GONE);
                    }
                });
            }else {
                container1.setVisibility(View.VISIBLE);
                dvir_recyclerview.setVisibility(View.GONE);
                createButton.setOnClickListener(button ->{
                    Intent intent = new Intent(requireActivity(), AddDvirActivity.class);
                    intent.putExtra("currDay",currDay);
                    startActivity(intent);
                });
            }
        });


        return v;
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
        textView.setText(sendModels.getS());
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}