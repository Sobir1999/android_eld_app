package com.iosix.eldblesample.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.adapters.GeneralFragmentPagerAdapter;
import com.iosix.eldblesample.adapters.TrailerRecyclerAdapter;
import com.iosix.eldblesample.dialogs.HomeTerminalDialog;
import com.iosix.eldblesample.dialogs.MainOfficeAddressDialog;
import com.iosix.eldblesample.models.ExampleSMSModel;
import com.iosix.eldblesample.roomDatabase.entities.GeneralEntity;
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;
import com.iosix.eldblesample.shared_prefs.SessionManager;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.GeneralViewModel;
import com.iosix.eldblesample.viewModel.UserViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class GeneralFragment extends Fragment {
//    private ViewPager pager;
//    private GeneralFragmentPagerAdapter adapter;

    private TrailerRecyclerAdapter adapter;
    private ArrayList<TrailersEntity> selectedTrailers;
    private DayDaoViewModel daoViewModel;
    private UserViewModel userViewModel;
    private GeneralViewModel generalViewModel;
    private SessionManager sessionManager;


    private Context context;
    private Button idSaveGeneral;
    private RecyclerView idTrailersRecyclerView;
    private ImageView idShippingClear,idTrailersClear,idVehiclesClear;
    private TextView driverFullName,tvDistance,tvShippingdocs,tvTrailers,idVehiclesEdit,
            tv_carrier,tv_terminal_address,tv_from_destination,tv_to_destination,tv_main_office,tv_notes;
    private EditText driverName,driverSurname,distanceEdit,ShippingDocsEdit,idTrailersEdit,
            idCarrierEdit,idfromEdit,idtoEdit,idNotesEdit;
    private ConstraintLayout idMainOffice;
    private LinearLayout driverInfo,distanceContainer,idDistanceLayout,
            shippingDocsLayout,trailersLayout,idTrailersLayout,vehiclesLayout,idVehiclesLayout,
            layout_carrier,idCarrierLayout,from_container,idfromLayout,to_container,idtoLayout,notes_container,idNotesLayout;

    private String s;

    public static GeneralFragment newInstance(String c) {
        GeneralFragment fragment = new GeneralFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_general, container, false);
        context = container.getContext();

        userViewModel = new UserViewModel(this.requireActivity().getApplication());
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        generalViewModel = new GeneralViewModel(this.requireActivity().getApplication());
        generalViewModel = ViewModelProviders.of(this).get(GeneralViewModel.class);

        sessionManager = new SessionManager(requireContext());

        driverFullName = view.findViewById(R.id.tv_driver_name);
        driverInfo = view.findViewById(R.id.idDriverNameCons);
        distanceContainer = view.findViewById(R.id.distance_container);
        tvDistance = view.findViewById(R.id.tv_distance);
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
        idMainOffice = view.findViewById(R.id.idMainOffice);
        tv_terminal_address = view.findViewById(R.id.tv_terminal_address);
        from_container = view.findViewById(R.id.from_container);
        tv_from_destination = view.findViewById(R.id.tv_from_destination);
        idfromLayout = view.findViewById(R.id.idfromLayout);
        idfromEdit = view.findViewById(R.id.idfromEdit);
        to_container = view.findViewById(R.id.to_container);
        tv_to_destination = view.findViewById(R.id.tv_to_destination);
        idtoLayout = view.findViewById(R.id.idtoLayout);
        idtoEdit = view.findViewById(R.id.idtoEdit);
        notes_container = view.findViewById(R.id.notes_container);
        idNotesLayout = view.findViewById(R.id.idNotesLayout);
        idNotesEdit = view.findViewById(R.id.idNotesEdit);
        tv_notes = view.findViewById(R.id.tv_notes);
        idSaveGeneral = view.findViewById(R.id.idSaveGeneral);
        tv_main_office = view.findViewById(R.id.tv_main_office);

        userViewModel.getMgetUser().observe(getViewLifecycleOwner(),user -> {
            if (user != null){
                s = user.getName() + " " + user.getLastName();
                driverFullName.setText(s);
                tv_terminal_address.setText(user.getHomeTerminalAddress());
            }
        });

        getGeneralInfo();

//        idSaveGeneral.setOnClickListener(v -> {
//            try {
//                generalViewModel.insertGeneral(
//                        new GeneralEntity(driverFullName.getText().toString(),tvDistance.getText().toString(),
//                                null,null,null,null,tv_from_destination.getText().toString(),
//                                tv_to_destination.getText().toString(),null,sessionManager.fetchSignature(),null));
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
        return view;
    }

    private void getGeneralInfo(){

        selectedTrailers = new ArrayList<>();
        daoViewModel = ViewModelProviders.of(requireActivity()).get(DayDaoViewModel.class);

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

        from_container.setOnClickListener(v -> {
            if (idfromLayout.getVisibility() != View.GONE){
                idfromLayout.setVisibility(View.GONE);
                if (idfromEdit.getText() != null){
                    tv_from_destination.setText(idfromEdit.getText().toString());
                }
            }else {
                idfromLayout.setVisibility(View.VISIBLE);
            }
        });

        to_container.setOnClickListener(v -> {
            if (idtoLayout.getVisibility() != View.GONE){
                idtoLayout.setVisibility(View.GONE);
                if(idtoEdit.getText() != null){
                    tv_to_destination.setText(idtoEdit.getText().toString());
                }
            }else {
                idtoLayout.setVisibility(View.VISIBLE);
            }
        });

        notes_container.setOnClickListener(v -> {
            if (idNotesLayout.getVisibility() != View.GONE){
                idNotesLayout.setVisibility(View.GONE);
                if(idNotesEdit.getText() != null){
                    tv_notes.setText(idNotesEdit.getText().toString());
                }
            }else {
                idNotesLayout.setVisibility(View.VISIBLE);
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