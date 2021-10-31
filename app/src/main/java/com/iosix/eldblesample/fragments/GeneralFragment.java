package com.iosix.eldblesample.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.adapters.GeneralFragmentPagerAdapter;
import com.iosix.eldblesample.adapters.TrailerRecyclerAdapter;
import com.iosix.eldblesample.models.ExampleSMSModel;
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class GeneralFragment extends Fragment {
//    private ViewPager pager;
//    private GeneralFragmentPagerAdapter adapter;

    private TrailerRecyclerAdapter adapter;
    private ArrayList<TrailersEntity> selectedTrailers;
    private DayDaoViewModel daoViewModel;


    private Context context;
    private RecyclerView idTrailersRecyclerView;
    private ImageView idShippingClear,idTrailersClear,idVehiclesClear;
    private TextView driverFullName,tvDistance,tvShippingdocs,tvTrailers,idVehiclesEdit,tv_carrier;
    private EditText driverName,driverSurname,distanceEdit,ShippingDocsEdit,idTrailersEdit,idCarrierEdit;
    private String driverNameString,driverSurnameString;
    private LinearLayout driverInfo,distanceContainer,idDistanceLayout,
            shippingDocsLayout,trailersLayout,idTrailersLayout,vehiclesLayout,idVehiclesLayout,
            layout_carrier,idCarrierLayout;

    public static GeneralFragment newInstance() {
        GeneralFragment fragment = new GeneralFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_general, container, false);
        context = container.getContext();
//        pager = view.findViewById(R.id.idGeneralFragmentPage);
//        adapter = new GeneralFragmentPagerAdapter(getContext());
//        pager.setAdapter(adapter);
//        pager.setCurrentItem(adapter.getCount(), true);
//
//        onPager();
        driverFullName = view.findViewById(R.id.tv_driver_name);
        driverName = view.findViewById(R.id.idDriverNameEdit);
        driverSurname = view.findViewById(R.id.idDriverFamilyEdit);
        driverInfo = view.findViewById(R.id.idDriverNameCons);
        distanceContainer = view.findViewById(R.id.distance_container);
        distanceEdit = view.findViewById(R.id.idDistanceEdit);
        tvDistance = view.findViewById(R.id.tv_distance);
        idDistanceLayout = view.findViewById(R.id.idDistanceLayout);
        tvShippingdocs = view.findViewById(R.id.tv_shipping_docs);
        ShippingDocsEdit = view.findViewById(R.id.idShippingDocsEdit);
        shippingDocsLayout = view.findViewById(R.id.shipping_docs_layout);
        idShippingClear = view.findViewById(R.id.idShippingClear);
        trailersLayout = view.findViewById(R.id.trailers_layout);
        tvTrailers = view.findViewById(R.id.tv_trailers);
        idTrailersRecyclerView = view.findViewById(R.id.idTrailersRecyclerView);
        idTrailersLayout = view.findViewById(R.id.idTrailersLayout);
        idTrailersEdit = view.findViewById(R.id.idTrailersEdit);
        idTrailersClear = view.findViewById(R.id.idTrailersClear);
        idVehiclesEdit = view.findViewById(R.id.idVehiclesEdit);
        idVehiclesClear = view.findViewById(R.id.idVehiclesClear);
        vehiclesLayout = view.findViewById(R.id.vehicles_layout);
        idVehiclesLayout = view.findViewById(R.id.idVehiclesLayout);
        layout_carrier = view.findViewById(R.id.layout_carrier);
        tv_carrier = view.findViewById(R.id.tv_carrier);
        idCarrierLayout = view.findViewById(R.id.idCarrierLayout);
        idCarrierEdit = view.findViewById(R.id.idCarrierEdit);

        getGeneralInfo();

        return view;
    }

    private void getGeneralInfo(){

        selectedTrailers = new ArrayList<>();
        daoViewModel = ViewModelProviders.of(requireActivity()).get(DayDaoViewModel.class);

        driverInfo.setOnClickListener(v -> {

            String s = driverFullName.getText().toString();
            Log.d("getGeneralInfo: ", s);

            driverNameString = s.substring(0,s.indexOf(" "));
            driverSurnameString = s.substring(s.indexOf(" ")+1);

            if (v.findViewById(R.id.idDriverInfoEdit).getVisibility() == View.GONE){
                v.findViewById(R.id.idDriverInfoEdit).setVisibility(View.VISIBLE);
            }else {
                if (!driverName.getText().toString().equals(driverNameString) && !driverName.getText().toString().equals("")){
                    driverNameString = driverName.getText().toString();
                }else {
                    driverNameString = s.substring(0,s.indexOf(" "));
                }

                if (!driverSurname.getText().toString().equals("") && !driverSurname.getText().toString().equals(driverSurnameString) ){
                    driverSurnameString = driverSurname.getText().toString();
                }else {
                    driverSurnameString = s.substring(s.indexOf(" ")+1);
                }
                driverFullName.setText(driverNameString + " " +driverSurnameString);
                Log.d("getGeneralInfo: ", driverFullName.getText().toString());
                v.findViewById(R.id.idDriverInfoEdit).setVisibility(View.GONE);

            }
        });

        distanceContainer.setOnClickListener(v1 -> {
            if (idDistanceLayout.getVisibility() == View.GONE){
                idDistanceLayout.setVisibility(View.VISIBLE);
            }else {
                if(!distanceEdit.getText().toString().equals(tvDistance.getText().toString()) && !distanceEdit.getText().toString().equals("")){
                    tvDistance.setText(distanceEdit.getText().toString());
                }
                idDistanceLayout.setVisibility(View.GONE);

            }
        });

        tvShippingdocs.setOnClickListener(v2 -> {


            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Please select from the following options");
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_selectable_list_item);
            arrayAdapter.add("Select BOL");
            arrayAdapter.add("Enter BOL/DOC Number Manually");
            alertDialog.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());

            alertDialog.setAdapter(arrayAdapter, (dialog, which) -> {
                String strName = arrayAdapter.getItem(which);
                if (which == 1){
                    ShippingDocsEdit.setHint(strName);
                    shippingDocsLayout.setVisibility(View.VISIBLE);
                }else {

                    AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(context);
                    alertDialog1.setTitle("Warning!")
                            .setMessage("You don't have shipping documents for selection")
                            .setPositiveButton("OK", (dialog1, which1) -> alertDialog.setCancelable(true));
                    AlertDialog alert = alertDialog1.create();
                    alert.show();
                }
            });
            alertDialog.show();

            idShippingClear.setOnClickListener(v3 -> {
                shippingDocsLayout.setVisibility(View.GONE);
            });
        });

        trailersLayout.setOnClickListener(v4 -> {


            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Please select from the following options");
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_selectable_list_item);
            arrayAdapter.add("Select Trailers");
            arrayAdapter.add("Enter Trailer Manually");
            alertDialog.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());

            alertDialog.setAdapter(arrayAdapter, (dialog, which) -> {
                String strName = arrayAdapter.getItem(which);
                if (which == 1){
                    idTrailersEdit.setHint(strName);
                    idTrailersLayout.setVisibility(View.VISIBLE);
                }else  {

                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                        builderSingle.setTitle("Select Trailers");

                        final ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(getContext(), android.R.layout.simple_selectable_list_item);
                        daoViewModel.getGetAllTrailers().observe((LifecycleOwner) requireContext(), trailersEntities -> {
                            for (int i = 0; i < trailersEntities.size(); i++) {
                                arrayAdapter2.add(trailersEntities.get(i).getNumber());
                            }
                        });

                        builderSingle.setNegativeButton("cancel", (dialog2, which2) -> dialog2.dismiss());

                        builderSingle.setAdapter(arrayAdapter2, (dialog2, which2) -> {
                            String strName2 = arrayAdapter2.getItem(which2);
                            selectedTrailers.add(new TrailersEntity(strName2));
                            idTrailersRecyclerView.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();

                        });
                        builderSingle.show();
                }
            });
            alertDialog.show();

            idTrailersRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            adapter = new TrailerRecyclerAdapter(selectedTrailers);
            idTrailersRecyclerView.setAdapter(adapter);
            adapter.setUpdateListener((position) -> {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                builderSingle.setTitle("Select Trailers");

                final ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<>(getContext(), android.R.layout.simple_selectable_list_item);
                daoViewModel.getGetAllTrailers().observe((LifecycleOwner) requireContext(), trailersEntities -> {
                    for (int i = 0; i < trailersEntities.size(); i++) {
                        arrayAdapter3.add(trailersEntities.get(i).getNumber());
                    }
                });

                builderSingle.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());

                builderSingle.setAdapter(arrayAdapter3, (dialog, which) -> {
                    String strName = arrayAdapter3.getItem(which);
                    selectedTrailers.get(position).setNumber(strName);
                    adapter.notifyDataSetChanged();
                });
                builderSingle.show();

                adapter.notifyDataSetChanged();
            });

            adapter.setDeleteListener((position) -> {
                selectedTrailers.remove(position);
                adapter.notifyDataSetChanged();
            });

            idTrailersClear.setOnClickListener(v -> {
                idTrailersLayout.setVisibility(View.GONE);
            });
        });

        vehiclesLayout.setOnClickListener(v -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Select unit");

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_selectable_list_item);
            daoViewModel.getGetAllVehicles().observe((LifecycleOwner) requireContext(), vehiclesEntities -> {
                for (int i = 0; i < vehiclesEntities.size(); i++) {
                    arrayAdapter.add(vehiclesEntities.get(i).getName());
                }
            });
            alertDialog.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());

            alertDialog.setAdapter(arrayAdapter, (dialog, which) -> {
                String strName = arrayAdapter.getItem(which);
                idVehiclesEdit.setText(strName);
                idVehiclesLayout.setVisibility(View.VISIBLE);
            });
            alertDialog.show();
        });
        idVehiclesClear.setOnClickListener(v -> {
            idVehiclesLayout.setVisibility(View.GONE);
        });

        layout_carrier.setOnClickListener(v -> {
            if (idCarrierLayout.getVisibility() == View.GONE){
                idCarrierLayout.setVisibility(View.VISIBLE);
            }else {
                if(!idCarrierEdit.getText().toString().equals("")){
                    tv_carrier.setText(idCarrierEdit.getText().toString());
                }
                idCarrierLayout.setVisibility(View.GONE);
            }
        });
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
}