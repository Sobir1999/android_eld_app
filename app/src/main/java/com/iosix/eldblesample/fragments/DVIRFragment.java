package com.iosix.eldblesample.fragments;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iosix.eldblesample.R;
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
    private ArrayList<String> mParams;
    private TextView textView,vehicleName,time,location,satsfaction,notes,unitDefects;
    private ImageView bottomArrow,delete,imgSatisfaction,arrowUp;
    private Button createButton;
    private LinearLayout container1,container2,unitsContainer;
    private View view;
    private DvirViewModel dvirViewModel;
    private String currDay;
    private List<DvirEntity> dvirEntities;
    private int currpos;


    public static DVIRFragment newInstance(ArrayList<String> params,String currDay) {
        DVIRFragment fragment = new DVIRFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("ARG_PARAM2", params);
        args.putString("ARG_PARAM1", currDay);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParams = getArguments().getStringArrayList("ARG_PARAM2");
            currDay = getArguments().getString("ARG_PARAM1");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_d_v_i_r, container, false);
        container1 = v.findViewById(R.id.idDVIRContainer1);
        container2 = v.findViewById(R.id.idDVIRContainer2);
        textView = v.findViewById(R.id.idDvirExText);
        createButton = v.findViewById(R.id.create_new_dvir_button);
        vehicleName = v.findViewById(R.id.idTVVehicleName);
        time = v.findViewById(R.id.idTvVehicleTime);
        location = v.findViewById(R.id.idTvVehicleLocation);
        bottomArrow = v.findViewById(R.id.idDVIRBottomArrow);
        arrowUp = v.findViewById(R.id.idDVIRArrowUp);
        delete = v.findViewById(R.id.idDVIRDelete);
        satsfaction = v.findViewById(R.id.idTvVehicleSatisfaction);
        imgSatisfaction = v.findViewById(R.id.ImageView);
        notes = v.findViewById(R.id.idDVIRNotes);
        unitDefects = v.findViewById(R.id.idUnitDefectName);
        view = v.findViewById(R.id.idDVIRView);
        unitsContainer = v.findViewById(R.id.idUnitsContainer);


        dvirEntities = new ArrayList<>();

        dvirViewModel = new DvirViewModel(requireActivity().getApplication());
        dvirViewModel = ViewModelProviders.of((FragmentActivity) requireContext()).get(DvirViewModel.class);

        dvirViewModel.getMgetDvirs().observe(getViewLifecycleOwner(),dvirEntities1 -> {
            dvirEntities = dvirEntities1;
        });

        dvirViewModel.getCurrentName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s)
            {
                addDvir(s);
                Log.d("Yes",s);
            }
        });

        addDvir(currDay);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
                alertDialog.setTitle("Delete DVIR?")
                        .setNegativeButton("No", (dialog, which) ->
                                alertDialog.setCancelable(true))
                .setPositiveButton("Yes",((dialog, which) -> {
                    dvirViewModel.deleteDvir(dvirEntities.get(currpos));
                    container1.setVisibility(View.VISIBLE);
                    container2.setVisibility(View.GONE);
                }));
                AlertDialog alert = alertDialog.create();
                alert.show();

            }
        });


//        createButton.setOnClickListener(button ->{
//                loadFragment(AddDvirFragment.newInstance(currDay));
//        });
        return v;
    }

    private void addDvir(String s){

        int val = 0;
            if (dvirEntities != null){
                for (int i = 0; i < dvirEntities.size(); i++) {
                    if (dvirEntities.get(i).getDay().equals(s)) {

                        val++;
                        currpos = i;
                        container1.setVisibility(View.GONE);
                        container2.setVisibility(View.VISIBLE);
                        time.setText(dvirEntities.get(i).getTime());
                        location.setText(dvirEntities.get(i).getLocation());
                        unitDefects.setText(dvirEntities.get(i).getUnitDefect());
                        notes.setText(dvirEntities.get(i).getNote());
                        vehicleName.setText(dvirEntities.get(i).getTrailer());
                        if (dvirEntities.get(i).getHasMechanicSignature()) {
                            satsfaction.setText("Veihcle Satisfactory");
                            satsfaction.setTextColor(Color.parseColor("#2D9B05"));
                            imgSatisfaction.setBackgroundResource(R.drawable.ic_baseline_verified_24);
                        } else {
                            satsfaction.setText("Veihcle Unsatisfactory");
                            satsfaction.setTextColor(Color.parseColor("#C10303"));
                            imgSatisfaction.setBackgroundResource(R.drawable.ic_baseline_info_24);
                        }

                        view.setVisibility(View.GONE);
                        unitsContainer.setVisibility(View.GONE);

                        bottomArrow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                view.setVisibility(View.VISIBLE);
                                unitsContainer.setVisibility(View.VISIBLE);
                                arrowUp.setVisibility(View.VISIBLE);
                                bottomArrow.setVisibility(View.GONE);
                            }
                        });

                        arrowUp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                view.setVisibility(View.GONE);
                                unitsContainer.setVisibility(View.GONE);
                                arrowUp.setVisibility(View.GONE);
                                bottomArrow.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
                if (val == 0){
                    container1.setVisibility(View.VISIBLE);
                    container2.setVisibility(View.GONE);
                }
            }else {
                container1.setVisibility(View.VISIBLE);
                container2.setVisibility(View.GONE);
            }
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