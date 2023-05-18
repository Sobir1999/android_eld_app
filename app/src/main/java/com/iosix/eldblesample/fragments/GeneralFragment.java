package com.iosix.eldblesample.fragments;


import static com.iosix.eldblesample.enums.Day.stringToDay;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.iosix.eldblelib.EldBleDataCallback;
import com.iosix.eldblelib.EldBroadcast;
import com.iosix.eldblelib.EldBroadcastTypes;
import com.iosix.eldblelib.EldDataRecord;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.adapters.DriversListAdapter;
import com.iosix.eldblesample.adapters.GeneralAdapter;
import com.iosix.eldblesample.adapters.VehiclesListAdapter;
import com.iosix.eldblesample.enums.GeneralConstants;
import com.iosix.eldblesample.models.TrailNubmer;
import com.iosix.eldblesample.models.User;
import com.iosix.eldblesample.models.VehicleList;
import com.iosix.eldblesample.roomDatabase.entities.GeneralEntity;
import com.iosix.eldblesample.shared_prefs.DriverSharedPrefs;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.GeneralViewModel;
import com.iosix.eldblesample.viewModel.UserViewModel;
import com.iosix.eldblesample.viewModel.apiViewModel.EldJsonViewModel;

import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;

public class GeneralFragment extends Fragment {

    private TextView distance;
    private TextView idFromText;
    private TextView idToText;
    private TextView idNotesText;
    private ConstraintLayout fromDes;
    private ConstraintLayout toDes;
    private ConstraintLayout notesContainer;
    private RecyclerView vehicleRv;
    private RecyclerView coDriverRv;
    private RecyclerView trailerRv;
    private RecyclerView shippingDocsRv;
    private DriverSharedPrefs driverSharedPrefs;
    private EldJsonViewModel eldJsonViewModel;
    private DvirViewModel dvirViewModel;
    private GeneralViewModel generalViewModel;
    private UserViewModel userViewModel;
    private final ArrayList<String> trailerListSelected = new ArrayList<>();
    private final ArrayList<String> vehicleListSelected = new ArrayList<>();
    private final ArrayList<String> coDriversListSelected = new ArrayList<>();
    private final ArrayList<String> shippingDocsListSelected = new ArrayList<>();
    private final ArrayList<VehicleList> vehicleList = new ArrayList<>();
    private String fromDesString = "";
    private String toDesString = "";
    private String notesString = "";
    private final ArrayList<User> driverList = new ArrayList<>();
    private String currDay;

    public static GeneralFragment newInstance() {
        GeneralFragment fragment = new GeneralFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        driverSharedPrefs = DriverSharedPrefs.getInstance(getContext());
        eldJsonViewModel = ViewModelProviders.of(requireActivity()).get(EldJsonViewModel.class);
        dvirViewModel = ViewModelProviders.of(requireActivity()).get(DvirViewModel.class);
        generalViewModel = ViewModelProviders.of(requireActivity()).get(GeneralViewModel.class);
        userViewModel = ViewModelProviders.of(requireActivity()).get(UserViewModel.class);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_general, container, false);
        setupViewModelObservers();
        setupUI(view);
        return view;
    }

    private void setupUI(View view){
        TextView driverName = view.findViewById(R.id.tv_driver_name);
        LinearLayout addVehicle = view.findViewById(R.id.vehicles_layout);
        ConstraintLayout addCoDriver = view.findViewById(R.id.tv_co_driver_container);
        LinearLayout addTrailer = view.findViewById(R.id.trailers_layout);
        LinearLayout addShippingDocs = view.findViewById(R.id.idShippingDocsContainer);
        LinearLayout addFromDes = view.findViewById(R.id.from_container);
        LinearLayout addToDes = view.findViewById(R.id.to_container);
        distance = view.findViewById(R.id.tv_distance);
        LinearLayout addNotes = view.findViewById(R.id.notes_container);
        TextView carrierName = view.findViewById(R.id.tv_carrier);
        TextView carrierOffice = view.findViewById(R.id.tv_main_office);
        TextView homeTerAdd = view.findViewById(R.id.tv_terminal_address);
        Button save1 = view.findViewById(R.id.idSaveGeneral);
        vehicleRv = view.findViewById(R.id.idVehiclessRecyclerView);
        coDriverRv = view.findViewById(R.id.idCoDriverssRecyclerView);
        trailerRv = view.findViewById(R.id.idTrailersRecyclerView);
        shippingDocsRv = view.findViewById(R.id.idShippingDocsRecyclerView);
        fromDes = view.findViewById(R.id.idfromLayout);
        toDes = view.findViewById(R.id.idToLayout);
        idFromText = view.findViewById(R.id.idFromText);
        idToText = view.findViewById(R.id.idToText);
        ImageView idFromDelete = view.findViewById(R.id.idFromDelete);
        ImageView idToDelete = view.findViewById(R.id.idToDelete);
        notesContainer = view.findViewById(R.id.idNotesLayout);
        ImageView idNotesDelete = view.findViewById(R.id.idNotesDelete);
        idNotesText = view.findViewById(R.id.idNotesText);
        distance = view.findViewById(R.id.idDistance);

        new EldBleDataCallback(){
            @Override
            public void OnDataRecord(EldBroadcast dataRec, EldBroadcastTypes RecordType) {
                if (RecordType == EldBroadcastTypes.ELD_DATA_RECORD){
                    distance.setText(String.format("%s", ((EldDataRecord) dataRec).getTripDistance()));
                }
            }
        };

        driverName.setText(String.format("%s %s", driverSharedPrefs.getFirstname(), driverSharedPrefs.getLastname()));
        carrierName.setText(driverSharedPrefs.getCompany());
        carrierOffice.setText(driverSharedPrefs.getMainOffice());
        homeTerAdd.setText(driverSharedPrefs.getHomeTerAdd());

        addVehicle.setOnClickListener(v ->{
            Dialog dialog = new Dialog(requireContext());
            dialog.setContentView(R.layout.custom_vehicles_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            RecyclerView vehicleListRv = dialog.findViewById(R.id.idVehicleRecyclerview);
            TextView cancel = dialog.findViewById(R.id.idVehicleCancel);
            VehiclesListAdapter vehiclesListAdapter = new VehiclesListAdapter(requireContext(),vehicleList);
            vehicleListRv.setAdapter(vehiclesListAdapter);
            vehiclesListAdapter.setListener(s -> {
                if (!vehicleListSelected.contains(s)){
                    vehicleListSelected.add(s);
                    dvirViewModel.getVehiclesMutableLiveData().postValue(vehicleListSelected.size());
                }
                dialog.dismiss();
            });
            cancel.setOnClickListener(view1-> dialog.dismiss());
            dialog.show();
        });

        addCoDriver.setOnClickListener(v ->{
            Dialog dialog = new Dialog(requireContext());
            dialog.setContentView(R.layout.custom_vehicles_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            RecyclerView vehicleListRv = dialog.findViewById(R.id.idVehicleRecyclerview);
            TextView cancel = dialog.findViewById(R.id.idVehicleCancel);
            TextView idVehicleHeaderTv = dialog.findViewById(R.id.idVehicleHeaderTv);
            idVehicleHeaderTv.setText(R.string.select_co_driver);
            DriversListAdapter driversListAdapter = new DriversListAdapter(requireContext(),driverList);
            vehicleListRv.setAdapter(driversListAdapter);
            driversListAdapter.setListener(s -> {
                if (!coDriversListSelected.contains(s)){
                    coDriversListSelected.add(s);
                    dvirViewModel.getCoDriversMutableLiveData().postValue(coDriversListSelected.size());
                }
                dialog.dismiss();
            });
            cancel.setOnClickListener(view1-> dialog.dismiss());
            dialog.show();
        });

        addTrailer.setOnClickListener(v ->{
            Dialog dialog = new Dialog(requireContext());
            dialog.setContentView(R.layout.custom_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            EditText createTrailer = dialog.findViewById(R.id.idDialogEdit);
            TextView cancel = dialog.findViewById(R.id.idDialogCancel);
            TextView save = dialog.findViewById(R.id.idDialogSave);

            cancel.setOnClickListener(v1 -> dialog.dismiss());

            save.setOnClickListener(v1->{
                if (!createTrailer.getText().toString().equals("")){
                    trailerListSelected.add(createTrailer.getText().toString());
                    dvirViewModel.getTrailersMutableLiveData().postValue(trailerListSelected.size());
                    eldJsonViewModel.sendTrailer(new TrailNubmer(createTrailer.getText().toString()));
                    dialog.dismiss();
                }
            });

            dialog.show();
        });

        addShippingDocs.setOnClickListener(v->{
            Dialog dialog = new Dialog(requireContext());
            dialog.setContentView(R.layout.custom_general_layout);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            EditText createCoDriver = dialog.findViewById(R.id.idGeneralDialogEdit);
            TextView cancel = dialog.findViewById(R.id.idGeneralDialogCancel);
            TextView save = dialog.findViewById(R.id.idGeneralDialogSend);

            cancel.setOnClickListener(v1 -> dialog.dismiss());

            save.setOnClickListener(v1->{
                if (!createCoDriver.getText().toString().equals("")){
                    shippingDocsListSelected.add(createCoDriver.getText().toString());
                    dvirViewModel.getShippingDocsMutableLiveData().postValue(shippingDocsListSelected.size());
                    dialog.dismiss();
                }
            });

            dialog.show();
        });

        addFromDes.setOnClickListener(v->{
            Dialog dialog = new Dialog(requireContext());
            dialog.setContentView(R.layout.custom_general_layout);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            EditText createCoDriver = dialog.findViewById(R.id.idGeneralDialogEdit);
            TextView header = dialog.findViewById(R.id.idGeneralHeaderTv);
            TextView title = dialog.findViewById(R.id.idGeneralTitleTv);
            TextView cancel = dialog.findViewById(R.id.idGeneralDialogCancel);
            TextView save = dialog.findViewById(R.id.idGeneralDialogSend);

            header.setText(R.string.add_from_destination);
            title.setText(R.string.please_enter_from);

            cancel.setOnClickListener(v1 -> dialog.dismiss());

            save.setOnClickListener(v1->{
                if (!createCoDriver.getText().toString().equals("")){
                    fromDesString = createCoDriver.getText().toString();
                    dvirViewModel.getStringMutableLiveData().postValue(createCoDriver.getText().toString());
                    dialog.dismiss();
                }
            });

            dialog.show();
        });

        idFromDelete.setOnClickListener(view1 -> {
            idFromText.setText("");
            fromDes.setVisibility(View.GONE);
        });

        addToDes.setOnClickListener(v->{
            Dialog dialog = new Dialog(requireContext());
            dialog.setContentView(R.layout.custom_general_layout);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView header = dialog.findViewById(R.id.idGeneralHeaderTv);
            TextView title = dialog.findViewById(R.id.idGeneralTitleTv);
            EditText createCoDriver = dialog.findViewById(R.id.idGeneralDialogEdit);
            TextView cancel = dialog.findViewById(R.id.idGeneralDialogCancel);
            TextView save = dialog.findViewById(R.id.idGeneralDialogSend);

            header.setText(R.string.add_to_destination);
            title.setText(R.string.please_enter_to);

            cancel.setOnClickListener(v1 -> dialog.dismiss());

            save.setOnClickListener(v1->{
                if (!createCoDriver.getText().toString().equals("")){
                    toDesString = createCoDriver.getText().toString();
                    dvirViewModel.getStringMutableLiveData().postValue(createCoDriver.getText().toString());
                    idToText.setText(createCoDriver.getText().toString());
                    toDes.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }
            });

            dialog.show();
        });

        idToDelete.setOnClickListener(view1 -> {
            idToText.setText("");
            toDes.setVisibility(View.GONE);
        });

        addNotes.setOnClickListener(v->{
            Dialog dialog = new Dialog(requireContext());
            dialog.setContentView(R.layout.custom_general_layout);
            TextView header = dialog.findViewById(R.id.idGeneralHeaderTv);
            TextView title = dialog.findViewById(R.id.idGeneralTitleTv);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            EditText createCoDriver = dialog.findViewById(R.id.idGeneralDialogEdit);
            TextView cancel = dialog.findViewById(R.id.idGeneralDialogCancel);
            TextView save = dialog.findViewById(R.id.idGeneralDialogSend);

            header.setText(R.string.add_notes);
            title.setText(R.string.please_enter_notes);

            cancel.setOnClickListener(v1 -> dialog.dismiss());

            save.setOnClickListener(v1->{
                if (!createCoDriver.getText().toString().equals("")){
                    notesString = createCoDriver.getText().toString();
                    dvirViewModel.getStringMutableLiveData().postValue(createCoDriver.getText().toString());
                    idNotesText.setText(createCoDriver.getText().toString());
                    notesContainer.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }
            });

            dialog.show();
        });

        idNotesDelete.setOnClickListener(view1 -> {
            idNotesText.setText("");
            notesContainer.setVisibility(View.GONE);
        });

        save1.setOnClickListener(v ->{
            if (vehicleListSelected.size() > 0 && shippingDocsListSelected.size() > 0 && trailerListSelected.size() > 0){
                showDialog();

                eldJsonViewModel.sendGeneralInfo(new GeneralEntity(
                        "0",
                        driverSharedPrefs.getDriverId(),
                        shippingDocsListSelected,
                        vehicleListSelected,
                        trailerListSelected,
                        driverSharedPrefs.getCompany(),
                        driverSharedPrefs.getMainOffice(),
                        "driverSharedPrefs.getHomeTerAdd()",
                        coDriversListSelected,
                        idFromText.getText().toString(),
                        idToText.getText().toString(),
                        idNotesText.getText().toString(),
                        stringToDay(currDay)));


                generalViewModel.insertGeneral(new GeneralEntity("0",
                        driverSharedPrefs.getDriverId(),
                        shippingDocsListSelected,
                        vehicleListSelected,
                        trailerListSelected,
                        driverSharedPrefs.getCompany(),
                        driverSharedPrefs.getMainOffice(),
                        "driverSharedPrefs.getHomeTerAdd()",
                        coDriversListSelected,
                        idFromText.getText().toString(),
                        idToText.getText().toString(),
                        idNotesText.getText().toString(),
                        stringToDay(currDay)));
            }else {
                Toast.makeText(requireContext(),"Some fields didn't create",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setupViewModelObservers(){

        userViewModel.getGetAllVehiclesGen(vehicleList);

        userViewModel.getMgetDrivers().observe(getViewLifecycleOwner(),users -> {
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getDriverId().equals(driverSharedPrefs.getDriverId())){
                    users.remove(users.get(i));
                }
            }
            if (driverList.isEmpty()){
                driverList.addAll(users);
            }
        });

        dvirViewModel.getVehiclesMutableLiveData().observe(getViewLifecycleOwner(),size ->{
            if (size > 0){
                vehicleRv.setVisibility(View.VISIBLE);
                GeneralAdapter adapter = new GeneralAdapter(getContext(),vehicleListSelected,GeneralConstants.VEHICLES);
                vehicleRv.setAdapter(adapter);
                adapter.setDeleteListener(s1 -> {
                    vehicleListSelected.remove(s1);
                    dvirViewModel.getVehiclesMutableLiveData().postValue(vehicleListSelected.size());
                });
            }else {
                vehicleRv.setVisibility(View.GONE);
            }
        });

        dvirViewModel.getCoDriversMutableLiveData().observe(getViewLifecycleOwner(),size ->{
            if (size > 0){
                coDriverRv.setVisibility(View.VISIBLE);
                GeneralAdapter adapter = new GeneralAdapter(getContext(),coDriversListSelected,GeneralConstants.CODRIVERS);
                coDriverRv.setAdapter(adapter);
                adapter.setDeleteListener(s1 -> {
                    coDriversListSelected.remove(s1);
                    dvirViewModel.getCoDriversMutableLiveData().postValue(coDriversListSelected.size());
                });
            }else {
                coDriverRv.setVisibility(View.GONE);
            }
        });

        dvirViewModel.getTrailersMutableLiveData().observe(getViewLifecycleOwner(),size ->{
            if (size > 0){
                trailerRv.setVisibility(View.VISIBLE);
                GeneralAdapter adapter = new GeneralAdapter(getContext(),trailerListSelected,GeneralConstants.TRAILERS);
                trailerRv.setAdapter(adapter);
                adapter.setDeleteListener(s1 -> {
                    trailerListSelected.remove(s1);
                    dvirViewModel.getTrailersMutableLiveData().postValue(trailerListSelected.size());
                });
            }else {
                trailerRv.setVisibility(View.GONE);
            }
        });

        dvirViewModel.getShippingDocsMutableLiveData().observe(getViewLifecycleOwner(),size ->{
            if (size > 0){
                shippingDocsRv.setVisibility(View.VISIBLE);
                GeneralAdapter adapter = new GeneralAdapter(getContext(),shippingDocsListSelected,GeneralConstants.SHIPPINGDOCS);
                shippingDocsRv.setAdapter(adapter);
                adapter.setDeleteListener(s1 -> {
                    shippingDocsListSelected.remove(s1);
                    dvirViewModel.getShippingDocsMutableLiveData().postValue(shippingDocsListSelected.size());
                });
            }else {
                shippingDocsRv.setVisibility(View.GONE);
            }
        });

        dvirViewModel.getStringMutableLiveData().observe(getViewLifecycleOwner(),s->{

            if (!fromDesString.equals("")){
                idFromText.setText(fromDesString);
                fromDes.setVisibility(View.VISIBLE);
            }else {
                fromDes.setVisibility(View.GONE);
            }

            if (!toDesString.equals("")){
                idToText.setText(toDesString);
                toDes.setVisibility(View.VISIBLE);
            }else {
                toDes.setVisibility(View.GONE);
            }

            if (!notesString.equals("")){
                idNotesText.setText(notesString);
                notesContainer.setVisibility(View.VISIBLE);
            }else {
                notesContainer.setVisibility(View.GONE);
            }
        });

        dvirViewModel.getCurrentName().observe(getViewLifecycleOwner(),day ->{
            generalViewModel.getCurrDayGenerals(stringToDay(day), (vehicles, trailersEntities, co_drivers, shippingDocs, from, to, notes) -> {
                vehicleListSelected.clear();
                coDriversListSelected.clear();
                trailerListSelected.clear();
                shippingDocsListSelected.clear();
                if (vehicles != null){
                    vehicleListSelected.addAll(vehicles);
                }
                if (shippingDocs != null){
                    shippingDocsListSelected.addAll(shippingDocs);
                }
                if (trailersEntities != null){
                    trailerListSelected.addAll(trailersEntities);
                }
                if (co_drivers != null){
                    coDriversListSelected.addAll(co_drivers);
                }
                fromDesString = from;
                toDesString = to;
                notesString = notes;
                dvirViewModel.getStringMutableLiveData().postValue(day);
                dvirViewModel.getVehiclesMutableLiveData().postValue(vehicleListSelected.size());
                dvirViewModel.getCoDriversMutableLiveData().postValue(coDriversListSelected.size());
                dvirViewModel.getTrailersMutableLiveData().postValue(trailerListSelected.size());
                dvirViewModel.getShippingDocsMutableLiveData().postValue(shippingDocsListSelected.size());
            });
            currDay = day;
        });
    }

    private void showDialog(){
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.custom_submitted_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView ok = dialog.findViewById(R.id.idSubmittedCancel);
        ok.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

}