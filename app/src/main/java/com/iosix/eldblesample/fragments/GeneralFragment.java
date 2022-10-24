package com.iosix.eldblesample.fragments;

import static com.iosix.eldblesample.enums.Day.dateToString;
import static com.iosix.eldblesample.enums.Day.stringToDate;

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

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.adapters.DriversListAdapter;
import com.iosix.eldblesample.adapters.GeneralAdapter;
import com.iosix.eldblesample.adapters.VehiclesListAdapter;
import com.iosix.eldblesample.enums.GeneralConstants;
import com.iosix.eldblesample.models.TrailNubmer;
import com.iosix.eldblesample.models.User;
import com.iosix.eldblesample.roomDatabase.entities.GeneralEntity;
import com.iosix.eldblesample.roomDatabase.entities.VehiclesEntity;
import com.iosix.eldblesample.shared_prefs.DriverSharedPrefs;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.GeneralViewModel;
import com.iosix.eldblesample.viewModel.UserViewModel;
import com.iosix.eldblesample.viewModel.apiViewModel.EldJsonViewModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class GeneralFragment extends Fragment {

    private TextView driverName;
    private TextView distance;
    private TextView carrierName;
    private TextView homeTerAdd;
    private TextView carrierOffice;
    private TextView idFromText;
    private TextView idToText;
    private TextView idNotesText;
    private LinearLayout addVehicle;
    private ConstraintLayout addCoDriver;
    private LinearLayout addTrailer;
    private LinearLayout addShippingDocs;
    private LinearLayout addFromDes;
    private LinearLayout addToDes;
    private LinearLayout addNotes;
    private ConstraintLayout fromDes;
    private ConstraintLayout toDes;
    private ConstraintLayout notesContainer;
    private Button save;
    private ImageView idFromDelete;
    private ImageView idToDelete;
    private ImageView idNotesDelete;
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
    private final ArrayList<VehiclesEntity> vehicleList = new ArrayList<>();
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
        driverName = view.findViewById(R.id.tv_driver_name);
        addVehicle = view.findViewById(R.id.vehicles_layout);
        addCoDriver = view.findViewById(R.id.tv_co_driver_container);
        addTrailer = view.findViewById(R.id.trailers_layout);
        addShippingDocs = view.findViewById(R.id.idShippingDocsContainer);
        addFromDes = view.findViewById(R.id.from_container);
        addToDes = view.findViewById(R.id.to_container);
        distance = view.findViewById(R.id.tv_distance);
        addNotes = view.findViewById(R.id.notes_container);
        carrierName = view.findViewById(R.id.tv_carrier);
        carrierOffice = view.findViewById(R.id.tv_main_office);
        homeTerAdd = view.findViewById(R.id.tv_terminal_address);
        save = view.findViewById(R.id.idSaveGeneral);
        vehicleRv = view.findViewById(R.id.idVehiclessRecyclerView);
        coDriverRv = view.findViewById(R.id.idCoDriverssRecyclerView);
        trailerRv = view.findViewById(R.id.idTrailersRecyclerView);
        shippingDocsRv = view.findViewById(R.id.idShippingDocsRecyclerView);
        fromDes = view.findViewById(R.id.idfromLayout);
        toDes = view.findViewById(R.id.idToLayout);
        idFromText = view.findViewById(R.id.idFromText);
        idToText = view.findViewById(R.id.idToText);
        idFromDelete = view.findViewById(R.id.idFromDelete);
        idToDelete = view.findViewById(R.id.idToDelete);
        notesContainer = view.findViewById(R.id.idNotesLayout);
        idNotesDelete = view.findViewById(R.id.idNotesDelete);
        idNotesText = view.findViewById(R.id.idNotesText);

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
            cancel.setOnClickListener(view1->{
                dialog.dismiss();
            });
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
            cancel.setOnClickListener(view1->{
                dialog.dismiss();
            });
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

            cancel.setOnClickListener(v1 ->{
                dialog.dismiss();
            });

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

            cancel.setOnClickListener(v1 ->{
                dialog.dismiss();
            });

            save.setOnClickListener(v1->{
                if (!createCoDriver.getText().toString().equals("")){
                    shippingDocsListSelected.add(createCoDriver.getText().toString());
                    dvirViewModel.getShippingDocsMutableLiveData().postValue(shippingDocsListSelected.size());
                    dialog.dismiss();
                }
            });

            dialog.show();
        });

        addFromDes.setOnClickListener( v->{
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

            cancel.setOnClickListener(v1 ->{
                dialog.dismiss();
            });

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

            cancel.setOnClickListener(v1 ->{
                dialog.dismiss();
            });

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

            cancel.setOnClickListener(v1 ->{
                dialog.dismiss();
            });

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

        save.setOnClickListener(v ->{
            if (vehicleListSelected.size() > 0 && shippingDocsListSelected.size() > 0 && trailerListSelected.size() > 0){
                showDialog();
                try {
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
                            stringToDate(currDay)));


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
                            stringToDate(currDay)));
                } catch (ParseException | ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(requireContext(),"Some fields didn't create",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setupViewModelObservers(){

        userViewModel.getGetAllVehicles().observe(getViewLifecycleOwner(),vehiclesEntities -> {
            if (vehicleList.isEmpty()){
                vehicleList.addAll(vehiclesEntities);
            }
        });

        userViewModel.getMgetDrivers().observe(getViewLifecycleOwner(),users -> {
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
            currDay = day;
            generalViewModel.getMgetGenerals().observe(getViewLifecycleOwner(),generalEntities -> {
                int j = 0;
                for (int i = generalEntities.size()-1; i >=0; i--) {
                    try {
                        if (dateToString(generalEntities.get(i).getDay()).equals(day)){
                            j++;
                            vehicleListSelected.clear();
                            coDriversListSelected.clear();
                            trailerListSelected.clear();
                            shippingDocsListSelected.clear();
                            vehicleListSelected.addAll(generalEntities.get(i).getVehicle());
                            coDriversListSelected.addAll(generalEntities.get(i).getCo_driver_name());
                            trailerListSelected.addAll(generalEntities.get(i).getTrailers());
                            shippingDocsListSelected.addAll(generalEntities.get(i).getShippingDocs());
                            fromDesString = generalEntities.get(i).getFrom_info();
                            toDesString = generalEntities.get(i).getTo_info();
                            notesString = generalEntities.get(i).getNote();
                            dvirViewModel.getStringMutableLiveData().postValue(day);
                            dvirViewModel.getVehiclesMutableLiveData().postValue(vehicleListSelected.size());
                            dvirViewModel.getCoDriversMutableLiveData().postValue(coDriversListSelected.size());
                            dvirViewModel.getTrailersMutableLiveData().postValue(trailerListSelected.size());
                            dvirViewModel.getShippingDocsMutableLiveData().postValue(shippingDocsListSelected.size());
                            break;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (j == 0){
                    vehicleListSelected.clear();
                    coDriversListSelected.clear();
                    trailerListSelected.clear();
                    shippingDocsListSelected.clear();
                    fromDesString = "";
                    toDesString = "";
                    notesString = "";
                    dvirViewModel.getStringMutableLiveData().postValue(day);
                    dvirViewModel.getVehiclesMutableLiveData().postValue(vehicleListSelected.size());
                    dvirViewModel.getCoDriversMutableLiveData().postValue(coDriversListSelected.size());
                    dvirViewModel.getTrailersMutableLiveData().postValue(trailerListSelected.size());
                    dvirViewModel.getShippingDocsMutableLiveData().postValue(shippingDocsListSelected.size());
                }
            });
        });
    }

    private void showDialog(){
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.custom_submitted_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView ok = dialog.findViewById(R.id.idSubmittedCancel);
        ok.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().getViewModelStore().clear();
        this.getViewModelStore().clear();
    }

}