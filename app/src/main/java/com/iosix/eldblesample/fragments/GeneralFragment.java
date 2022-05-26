package com.iosix.eldblesample.fragments;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.adapters.DriversListAdapter;
import com.iosix.eldblesample.adapters.TrailerListAdapter;
import com.iosix.eldblesample.adapters.TrailerRecyclerAdapter;
import com.iosix.eldblesample.adapters.VehiclesListAdapter;
import com.iosix.eldblesample.models.ExampleSMSModel;
import com.iosix.eldblesample.models.TrailNubmer;
import com.iosix.eldblesample.models.User;
import com.iosix.eldblesample.models.VehicleList;
import com.iosix.eldblesample.retrofit.APIInterface;
import com.iosix.eldblesample.retrofit.ApiClient;
import com.iosix.eldblesample.roomDatabase.entities.GeneralEntity;
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;
import com.iosix.eldblesample.shared_prefs.DriverSharedPrefs;
import com.iosix.eldblesample.shared_prefs.SessionManager;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.GeneralViewModel;
import com.iosix.eldblesample.viewModel.UserViewModel;
import com.iosix.eldblesample.viewModel.apiViewModel.EldJsonViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeneralFragment extends Fragment {

    private ArrayList<TrailersEntity> selectedTrailers = new ArrayList<>();
    private ArrayList<String> selectedTrailersNumber = new ArrayList<>();
    private DayDaoViewModel daoViewModel;
    private UserViewModel userViewModel;
    private GeneralViewModel generalViewModel;
    private SessionManager sessionManager;
    private DriverSharedPrefs driverSharedPrefs;
    private APIInterface apiInterface;
    private EldJsonViewModel eldJsonViewModel;
    private DvirViewModel dvirViewModel;
    TrailerRecyclerAdapter adapter;
    private String distance = "15";
    private ArrayList<String> trailers = new ArrayList<>();
    private String day;


    private Context context;
    private Button idSaveGeneral;
    private RecyclerView idTrailersRecyclerView;
    private ImageView idShippingClear,idTrailersClear,idVehiclesClear;
    private TextView driverFullName,tvDistance,tvShippingdocs,tvTrailers,shippingDocsEdit,idVehiclesEdit,
            tv_carrier,tv_terminal_address,tv_from_destination,tv_to_destination,tv_main_office,tv_notes,tv_co_drivers;
    private EditText driverName,driverSurname,distanceEdit,idTrailersEdit,
            idCarrierEdit,idfromEdit,idtoEdit,idNotesEdit;
    private ConstraintLayout idMainOffice,coDriverContainer;
    private LinearLayout driverInfo,distanceContainer,idDistanceLayout,
            trailersLayout,idTrailersLayout,vehiclesLayout,
            layout_carrier,idCarrierLayout,from_container,idfromLayout,to_container,idtoLayout,notes_container,idNotesLayout;
    private ConstraintLayout shippingDocsLayout,idVehiclesLayout;

    public static GeneralFragment newInstance(String c) {
        GeneralFragment fragment = new GeneralFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            day = getArguments().getString("currDay");
        }
        eldJsonViewModel = new EldJsonViewModel(requireActivity().getApplication());
        eldJsonViewModel = ViewModelProviders.of(requireActivity()).get(EldJsonViewModel.class);
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
        driverSharedPrefs = DriverSharedPrefs.getInstance(requireContext().getApplicationContext());

        dvirViewModel = new DvirViewModel(getActivity().getApplication());
        dvirViewModel = ViewModelProviders.of(getActivity()).get(DvirViewModel.class);

        driverFullName = view.findViewById(R.id.tv_driver_name);
        driverInfo = view.findViewById(R.id.idDriverNameCons);
        distanceContainer = view.findViewById(R.id.distance_container);
        tvDistance = view.findViewById(R.id.tv_distance);
        tvShippingdocs = view.findViewById(R.id.idAddShippingDocs);
        shippingDocsEdit = view.findViewById(R.id.idShippingDocsEdit);
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
        tv_co_drivers = view.findViewById(R.id.tv_co_drivers);
        coDriverContainer = view.findViewById(R.id.tv_co_driver_container);

        driverFullName.setText(String.format("%s %s",driverSharedPrefs.getFirstname(),driverSharedPrefs.getLastname()));
        tv_main_office.setText(driverSharedPrefs.getMainOffice());
        tv_terminal_address.setText(driverSharedPrefs.getHomeTerAdd());

        apiInterface = ApiClient.getClient().create(APIInterface.class);

        getGeneralInfo();
        getDriverInfo();

        dvirViewModel.getCurrentName().observe(getViewLifecycleOwner(),c -> {
            day = c;
        });

        idSaveGeneral.setOnClickListener(v -> {
            apiInterface.sendGeneralInfo(new GeneralEntity(distance,shippingDocsEdit.getText().toString(),
                    idVehiclesEdit.getText().toString(), selectedTrailersNumber,driverSharedPrefs.getCompany(),
                    driverSharedPrefs.getMainOffice(),"driverSharedPrefs.getHomeTerAdd()",
                    tv_co_drivers.getText().toString(),tv_from_destination.getText().toString(),
                    tv_to_destination.getText().toString(),tv_notes.getText().toString(),day)).enqueue(new Callback<GeneralEntity>() {
                @Override
                public void onResponse(Call<GeneralEntity> call, Response<GeneralEntity> response) {
                    if (response.isSuccessful()){

                    }
                }

                @Override
                public void onFailure(Call<GeneralEntity> call, Throwable t) {
                }
            });

            try {
                generalViewModel.insertGeneral(new GeneralEntity(distance,shippingDocsEdit.getText().toString(),
                        idVehiclesEdit.getText().toString(), selectedTrailersNumber,driverSharedPrefs.getCompany(),
                        driverSharedPrefs.getMainOffice(),"driverSharedPrefs.getHomeTerAdd()",
                        tv_co_drivers.getText().toString(),tv_from_destination.getText().toString(),
                        tv_to_destination.getText().toString(),tv_notes.getText().toString(),day));
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        return view;
    }

    private void getDriverInfo(){
        apiInterface.getVehicle().enqueue(new Callback<VehicleList>() {
            @Override
            public void onResponse(Call<VehicleList> call, Response<VehicleList> response) {
                if (response.isSuccessful()){
                    if (response.body() != null) {
                        VehicleList vehicle = response.body();
                    }
                }
            }

            @Override
            public void onFailure(Call<VehicleList> call, Throwable t) {
            }
        });

        apiInterface.getCoDriver().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    if (response.body().getName() != null){
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
    }

    private void getGeneralInfo(){

        daoViewModel = ViewModelProviders.of(requireActivity()).get(DayDaoViewModel.class);

        tvShippingdocs.setOnClickListener(v2 -> {

            Dialog shippingDialog = new Dialog(context);
            shippingDialog.setContentView(R.layout.custom_general_layout);
            shippingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            EditText shippingDocs = shippingDialog.findViewById(R.id.idGeneralDialogEdit);
            TextView cancel = shippingDialog.findViewById(R.id.idGeneralDialogCancel);
            TextView send = shippingDialog.findViewById(R.id.idGeneralDialogSend);

            send.setOnClickListener(view -> {
                if (!shippingDocs.toString().equals("")){
                    shippingDocsEdit.setText(shippingDocs.getText());
                    shippingDocsLayout.setVisibility(View.VISIBLE);
                    shippingDialog.dismiss();
                }else {
                    Toast.makeText(context,"Please create shipping docs!",Toast.LENGTH_SHORT).show();
                }
            });

            cancel.setOnClickListener(view -> {
                shippingDialog.dismiss();
            });
            shippingDialog.show();

        });

        idShippingClear.setOnClickListener(v -> {
            shippingDocsLayout.setVisibility(View.GONE);
        });

        trailersLayout.setOnClickListener(v4 -> {

            Dialog trailerBaseDialog = new Dialog(context);
            trailerBaseDialog.setContentView(R.layout.trailer_base_dialog);
            trailerBaseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            TextView selectTrailer = trailerBaseDialog.findViewById(R.id.idSelectTrailer);
            TextView addTrailer = trailerBaseDialog.findViewById(R.id.idAddTrailer);
            TextView cancelTrailerDialog = trailerBaseDialog.findViewById(R.id.idDialogCancel);

            addTrailer.setOnClickListener(view -> {
                trailerBaseDialog.dismiss();
                Dialog trailerDialog = new Dialog(context);
                trailerDialog.setContentView(R.layout.custom_general_layout);
                trailerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView cancel = trailerDialog.findViewById(R.id.idGeneralDialogCancel);
                TextView send = trailerDialog.findViewById(R.id.idGeneralDialogSend);
                TextView header = trailerDialog.findViewById(R.id.idGeneralHeaderTv);
                TextView title = trailerDialog.findViewById(R.id.idGeneralTitleTv);
                EditText trailer = trailerDialog.findViewById(R.id.idGeneralDialogEdit);

                header.setText(R.string.new_trailer);
                title.setText(R.string.please_enter_new_trailer);
                cancel.setOnClickListener(view1 -> {
                    trailerDialog.dismiss();
                });

                send.setOnClickListener(view1 -> {
                    if (!trailer.getText().toString().equals("")){
                        trailerDialog.dismiss();
                        apiInterface.sendTrailer(new TrailNubmer(trailer.getText().toString())).enqueue(new Callback<TrailersEntity>() {
                            @Override
                            public void onResponse(Call<TrailersEntity> call, Response<TrailersEntity> response) {
                                if (response.isSuccessful()){
                                    try {
                                        daoViewModel.insertTrailer(new TrailersEntity(response.body().getTrailer_id(),response.body().getNumber()));
                                    } catch (ExecutionException | InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<TrailersEntity> call, Throwable t) {
                            }
                        });
                    }else {
                        Toast.makeText(context,"Please,create trailer number!",Toast.LENGTH_SHORT).show();
                    }
                });
                trailerDialog.show();

            });

            cancelTrailerDialog.setOnClickListener(view -> {
                trailerBaseDialog.dismiss();
            });

            selectTrailer.setOnClickListener(view -> {
                trailerBaseDialog.dismiss();
                Dialog selectTrailerDialog = new Dialog(context);
                selectTrailerDialog.setContentView(R.layout.custom_list_dialog);
                selectTrailerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView cancelSelectTrailerDialog = selectTrailerDialog.findViewById(R.id.idDialogCancel);
                RecyclerView recyclerView = selectTrailerDialog.findViewById(R.id.idRecyclerViewUnit);

                daoViewModel.getGetAllTrailers().observe(getViewLifecycleOwner(),trailersEntities -> {
                    TrailerListAdapter trailerListAdapter = new TrailerListAdapter(context,trailersEntities);
                    recyclerView.setAdapter(trailerListAdapter);

                    trailerListAdapter.setUpdateListener(position ->{
                        selectTrailerDialog.dismiss();
                        int n = 0;
                        for (int i = 0; i < selectedTrailers.size(); i++) {
                            if (trailersEntities.get(position).equals(selectedTrailers.get(i))){
                                n++;
                            }
                        }
                        if (n == 0){
                            selectedTrailers.add(trailersEntities.get(position));
                            selectedTrailersNumber.add(trailersEntities.get(position).getTrailer_id());
                            dvirViewModel.getSelectedTrailerCount().postValue(selectedTrailers.size());

                        }
                    });
                });

                cancelSelectTrailerDialog.setOnClickListener(view1 -> {
                    selectTrailerDialog.dismiss();
                });

                selectTrailerDialog.show();
            });
            trailerBaseDialog.show();
        });

        dvirViewModel.getSelectedTrailerCount().observe(getViewLifecycleOwner(),count ->{
            idTrailersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new TrailerRecyclerAdapter(selectedTrailers);
            idTrailersRecyclerView.setAdapter(adapter);
            adapter.setDeleteListener(s->{
                selectedTrailers.remove(s);
                dvirViewModel.getSelectedTrailerCount().postValue(selectedTrailers.size());
            });
        });


        idTrailersClear.setOnClickListener(view -> {
            idTrailersLayout.setVisibility(View.GONE);
        });

        vehiclesLayout.setOnClickListener(v -> {

            Dialog vehiclesDialog = new Dialog(context);
            vehiclesDialog.setContentView(R.layout.custom_vehicles_dialog);
            vehiclesDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            TextView cancel = vehiclesDialog.findViewById(R.id.idVehicleCancel);
            TextView header = vehiclesDialog.findViewById(R.id.idVehicleHeaderTv);
            RecyclerView recyclerView = vehiclesDialog.findViewById(R.id.idVehicleRecyclerview);

            header.setText(R.string.select_unit);
            daoViewModel.getGetAllVehicles().observe(getViewLifecycleOwner(),vehicleEntities->{
                VehiclesListAdapter adapter = new VehiclesListAdapter(context,vehicleEntities);
                recyclerView.setAdapter(adapter);

                adapter.setListener(s -> {
                    vehiclesDialog.dismiss();
                    idVehiclesLayout.setVisibility(View.VISIBLE);
                    idVehiclesEdit.setText(s);
                });
            });

            cancel.setOnClickListener(view -> vehiclesDialog.dismiss());

            vehiclesDialog.show();

        });

        idVehiclesClear.setOnClickListener(v -> {
            idVehiclesLayout.setVisibility(View.GONE);
        });

//            tv_co_drivers.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);


        coDriverContainer.setOnClickListener(view -> {

            Dialog driversDialog = new Dialog(context);
            driversDialog.setContentView(R.layout.custom_vehicles_dialog);
            driversDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            TextView cancel = driversDialog.findViewById(R.id.idVehicleCancel);
            TextView header = driversDialog.findViewById(R.id.idVehicleHeaderTv);
            RecyclerView recyclerView = driversDialog.findViewById(R.id.idVehicleRecyclerview);

            header.setText(R.string.select_co_driver);

            userViewModel.getMgetDrivers().observe(getViewLifecycleOwner(),driverEntities->{
                DriversListAdapter adapter = new DriversListAdapter(context,driverEntities);
                recyclerView.setAdapter(adapter);

                adapter.setListener(s -> {
                    driversDialog.dismiss();
                    tv_co_drivers.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
                    tv_co_drivers.setText(s);
                });
            });

            cancel.setOnClickListener(v -> driversDialog.dismiss());

            driversDialog.show();

        });


        from_container.setOnClickListener(v -> {
            idfromLayout.setVisibility(View.VISIBLE);
        });

        if(idfromEdit.getText() != null){
            idfromEdit.setOnEditorActionListener((textView, i, keyEvent) -> {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE){
                    tv_from_destination.setText(idfromEdit.getText().toString());
                    idfromLayout.setVisibility(View.GONE);
                    handled = true;
                }
                return handled;
            });
        }

        to_container.setOnClickListener(v -> {
            idtoLayout.setVisibility(View.VISIBLE);
        });

        if(idtoEdit.getText() != null){
            idtoEdit.setOnEditorActionListener((textView, i, keyEvent) -> {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE){
                    tv_to_destination.setText(idtoEdit.getText().toString());
                    idtoLayout.setVisibility(View.GONE);
                    handled = true;
                }
                return handled;
            });
        }

        notes_container.setOnClickListener(v -> {
            idNotesLayout.setVisibility(View.VISIBLE);
        });

        if(idNotesEdit.getText() != null){
            idNotesEdit.setOnEditorActionListener((textView, i, keyEvent) -> {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE){
                    tv_notes.setText(idNotesEdit.getText().toString());
                    idNotesLayout.setVisibility(View.GONE);
                    handled = true;
                }
                return handled;
            });
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

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}