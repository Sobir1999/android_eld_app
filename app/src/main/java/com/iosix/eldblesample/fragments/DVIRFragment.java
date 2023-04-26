package com.iosix.eldblesample.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.activity.AddDvirActivity;
import com.iosix.eldblesample.adapters.DvirlistAdapter;
import com.iosix.eldblesample.customViews.MyListView;
import com.iosix.eldblesample.models.Dvir;
import com.iosix.eldblesample.viewModel.DvirViewModel;

import java.util.ArrayList;

public class DVIRFragment extends Fragment{
    private boolean hasDefects = false;
    private boolean isSatisfactory = false;
    private TextView idDvirExText;
    private ImageView imageView;
    private Button createButton;
    private FloatingActionButton idFABAddDvir;
    private LinearLayout container1;
    private ConstraintLayout idDVIRContainer;
    private DvirViewModel dvirViewModel;
    private String currDay;
    private ListView dvir_recyclerview;
    private ArrayList<Dvir> dvirEntitiesCurr;

    public static DVIRFragment newInstance(boolean isSatisfactory) {
        DVIRFragment fragment = new DVIRFragment();
        Bundle args = new Bundle();
        args.putBoolean("isSatisfactory", isSatisfactory);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dvirViewModel = ViewModelProviders.of(requireActivity()).get(DvirViewModel.class);
        dvirEntitiesCurr = new ArrayList<>();
        if (getArguments() != null) {
            isSatisfactory = getArguments().getBoolean("isSatisfactory");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dvirViewModel.getCurrentName().observe(getViewLifecycleOwner(), s -> {
            currDay = s;
            dvirViewModel.getMgetDvirs(requireContext(),s,dvir_recyclerview,container1);
        });

        idFABAddDvir.setOnClickListener(view3 ->{
            Intent intent = new Intent(getContext(),AddDvirActivity.class);
            intent.putExtra("currDay",currDay);
            startActivity(intent);
            requireActivity().finish();
        });

        createButton.setOnClickListener(view4 ->{
            Intent intent = new Intent(getContext(),AddDvirActivity.class);
            intent.putExtra("currDay",currDay);
            startActivity(intent);
            requireActivity().finish();
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_d_v_i_r, container, false);
        container1 = v.findViewById(R.id.idDVIRContainer1);
        idDVIRContainer = v.findViewById(R.id.idDVIRContainer);
        idDvirExText = v.findViewById(R.id.idDvirExText);
        createButton = v.findViewById(R.id.create_new_dvir_button);
        dvir_recyclerview = v.findViewById(R.id.idDvirRecyclerView);
        idFABAddDvir = v.findViewById(R.id.idFABAddDvir);

        return v;
    }
}