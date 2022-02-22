package com.iosix.eldblesample.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iosix.eldblelib.EldBleConnectionStateChangeCallback;
import com.iosix.eldblelib.EldBleDataCallback;
import com.iosix.eldblelib.EldBleError;
import com.iosix.eldblelib.EldBleScanCallback;
import com.iosix.eldblelib.EldBroadcast;
import com.iosix.eldblelib.EldBroadcastTypes;
import com.iosix.eldblelib.EldBufferRecord;
import com.iosix.eldblelib.EldCachedNewTimeRecord;
import com.iosix.eldblelib.EldCachedNewVinRecord;
import com.iosix.eldblelib.EldCachedPeriodicRecord;
import com.iosix.eldblelib.EldDataRecord;
import com.iosix.eldblelib.EldDriverBehaviorRecord;
import com.iosix.eldblelib.EldEmissionsRecord;
import com.iosix.eldblelib.EldEngineRecord;
import com.iosix.eldblelib.EldEngineStates;
import com.iosix.eldblelib.EldFirmwareUpdateCallback;
import com.iosix.eldblelib.EldFuelRecord;
import com.iosix.eldblelib.EldManager;
import com.iosix.eldblelib.EldParameterTypes;
import com.iosix.eldblelib.EldScanObject;
import com.iosix.eldblelib.EldTransmissionRecord;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.adapters.RecyclerViewLastAdapter;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.broadcasts.ChangeDateTimeBroadcast;
import com.iosix.eldblesample.customViews.CustomLiveRulerChart;
import com.iosix.eldblesample.dialogs.ConnectToEldDialog;
import com.iosix.eldblesample.dialogs.SearchEldDeviceDialog;
import com.iosix.eldblesample.enums.Day;
import com.iosix.eldblesample.enums.EnumsConstants;
import com.iosix.eldblesample.fragments.InspectionModuleFragment;
import com.iosix.eldblesample.fragments.LanguageFragment;
import com.iosix.eldblesample.fragments.RecapFragment;
import com.iosix.eldblesample.models.Data;
import com.iosix.eldblesample.models.MessageModel;
import com.iosix.eldblesample.models.User;
import com.iosix.eldblesample.models.VehicleData;
import com.iosix.eldblesample.models.eld_records.BufferRecord;
import com.iosix.eldblesample.models.eld_records.CachedEngineRecord;
import com.iosix.eldblesample.models.eld_records.CashedMotionRecord;
import com.iosix.eldblesample.models.eld_records.DriverBehaviorRecord;
import com.iosix.eldblesample.models.eld_records.EmissionsRecord;
import com.iosix.eldblesample.models.eld_records.EngineLiveRecord;
import com.iosix.eldblesample.models.eld_records.FuelRecord;
import com.iosix.eldblesample.models.eld_records.LiveDataRecord;
import com.iosix.eldblesample.models.eld_records.NewTimeRecord;
import com.iosix.eldblesample.models.eld_records.Point;
import com.iosix.eldblesample.models.eld_records.TransmissionRecord;
import com.iosix.eldblesample.retrofit.APIInterface;
import com.iosix.eldblesample.retrofit.ApiClient;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.DvirEntity;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;
import com.iosix.eldblesample.roomDatabase.entities.VehiclesEntity;
import com.iosix.eldblesample.services.foreground.ForegroundService;
import com.iosix.eldblesample.shared_prefs.DriverSharedPrefs;
import com.iosix.eldblesample.shared_prefs.LastStatusData;
import com.iosix.eldblesample.shared_prefs.SessionManager;
import com.iosix.eldblesample.shared_prefs.UserData;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.StatusDaoViewModel;
import com.iosix.eldblesample.viewModel.UserViewModel;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.iosix.eldblesample.MyApplication.userData;

public class MainActivity extends BaseActivity implements TimePickerDialog.OnTimeSetListener {

    private DrawerLayout drawerLayout;
    private Toolbar activity_main_toolbar;
    private CardView off, sb, dr, on;
    private LinearLayout visiblityViewCons;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch nightModeSwitch;
    private int current_status = EnumsConstants.STATUS_OFF;
    private int last_status;
    private String time = "" + Calendar.getInstance().getTime();
    private String today = time.split(" ")[1] + " " + time.split(" ")[2];
    private ArrayList<LogEntity> truckStatusEntities;
    private ArrayList<LogEntity> logEntities;
    private APIInterface apiInterface;
    private TextView lastDaysCheck,idUsername,statusOff,statusSB,statusDR,statusON;
    private ImageView toDaySelect,imageOff,imageSB,imageDR,imageON;
    private ChangeDateTimeBroadcast changeDateTimeBroadcast;
    private CustomLiveRulerChart customRulerChart;
    private LastStatusData lastStatusData;
    private ArrayList<VehiclesEntity> vehiclesEntities;
    private ArrayList<User> users;
    private DriverSharedPrefs driverSharedPrefs;

    private double latitude;
    private double longtitude;

    String MAC;
    private int updateSelection;

    private EldManager mEldManager;
    private final Set<EldBroadcastTypes> subscribedRecords = EnumSet.of(EldBroadcastTypes.ELD_BUFFER_RECORD, EldBroadcastTypes.ELD_CACHED_RECORD, EldBroadcastTypes.ELD_FUEL_RECORD, EldBroadcastTypes.ELD_DATA_RECORD, EldBroadcastTypes.ELD_DRIVER_BEHAVIOR_RECORD, EldBroadcastTypes.ELD_EMISSIONS_PARAMETERS_RECORD, EldBroadcastTypes.ELD_ENGINE_PARAMETERS_RECORD, EldBroadcastTypes.ELD_TRANSMISSION_PARAMETERS_RECORD);
    private boolean diagnosticEnabled = false, fuelEnabled = false, engineEnabled = false, transmissionEnabled = false, emissionsEnabled = false, driverEnabled = false;

    private static final int REQUEST_BASE = 100;
    private static final int REQUEST_BT_ENABLE = REQUEST_BASE + 1;

    boolean reqdelinprogress = false;
    int startseq, endseq;
    int reccount = 0;

    private StatusDaoViewModel statusDaoViewModel;
    private DayDaoViewModel daoViewModel;
    private DvirViewModel dvirViewModel;
    private UserViewModel userViewModel;
    private Menu optionMenu;
    private final ArrayList<String> daysArray = new ArrayList<>();
    private RecyclerViewLastAdapter lastAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.clear();
    }

    @Override
    public void initView() {
        super.initView();
        UserData userData = new UserData(this);
        lastStatusData = new LastStatusData(this);
        logEntities = new ArrayList<>();

        userViewModel = new UserViewModel(this.getApplication());
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        setLocale(userData.getLang());

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        setContentView(R.layout.activity_main);

        optimizeViewModels();

        drawerLayout = findViewById(R.id.drawer_layout);
        RecyclerView last_recycler_view = findViewById(R.id.idRecyclerView);
        customRulerChart = findViewById(R.id.idCustomLiveChart);
        customRulerChart.setArrayList(truckStatusEntities);

//        setBluetoothDataEnabled(this);

        //Last Days Recycler Adapter

        daoViewModel.getMgetAllDays().observe(this,dayEntities -> {

            lastAdapter = new RecyclerViewLastAdapter(this, daoViewModel, statusDaoViewModel,dvirViewModel);
            last_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
            last_recycler_view.setAdapter(lastAdapter);
            lastAdapter.notifyDataSetChanged();

            Log.d("123456789A",String.valueOf(dayEntities.size()));

            lastAdapterClicked();
        });



        // Set the toolbar
        activity_main_toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(activity_main_toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //Required to allow bluetooth scanning
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);

        mEldManager = EldManager.GetEldManager(this, "123456789A");

        vehiclesEntities = new ArrayList<>();
        driverSharedPrefs = new DriverSharedPrefs(this);

        if (getIntent().getIntExtra("JSON",0) == 1){
            getUSerInfo();
        }

        onClickCustomBtn();
        onClickVisiblityCanAndSaveBtn();

        getDrawerToggleEvent();
        getDrawerTouchEvent();

        clickLGDDButtons();

        setActivateDr();

//        setTodayAttr(time,today);

        startService();

        update();

        getAllDrivers();
    }

    private void update() {
        new Thread() {
            @SuppressLint("DefaultLocale")
            @SuppressWarnings("BusyWait")
            public void run() {
                while (true) {
                    runOnUiThread(() -> {
                        TextView statusTime = findViewById(R.id.idStatusTime);
                        int last = lastStatusData.getLasStatSecond();
                        int current = getCurrentSeconds();
                        int hour = (current - last) / 3600;
                        int min = ((current - last) % 3600) / 60;
                        statusTime.setText(String.format("%02dh %02dm", hour, min));
                    });
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void getUSerInfo(){


        userViewModel.deleteUser();
        apiInterface.getUser().enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()){
                    User user = response.body();
                    assert user != null;
                    driverSharedPrefs.saveLastUsername(user.getName());
                    driverSharedPrefs.saveLastSurname(user.getLastName());
                    driverSharedPrefs.saveLastImage(user.getImage());
                    driverSharedPrefs.saveLastHomeTerAdd(user.getHomeTerminalAddress());
                    driverSharedPrefs.saveLastHomeTerTimeZone(user.getTimeZone());
                    driverSharedPrefs.saveLastMainOffice(user.getMainOffice());
                    driverSharedPrefs.saveLastPhoneNumber(user.getPhone());
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
            }
        });

    }

    private void getAllDrivers(){
        userViewModel.deleteUser();
        apiInterface.getAllDrivers().enqueue(new Callback<Data>() {
            @Override
            public void onResponse(@NonNull Call<Data> call, @NonNull Response<Data> response) {
                if (response.isSuccessful()){
                    Data users = response.body();
                    assert users != null;
                    Log.d("Adverse diving",users.getDriver().getUser().get(0).getLastName());
                    if (MainActivity.this.users.size() == 0){
                        for(int i = 0;i < users.getDriver().getUser().size();i++){
                            userViewModel.insertUser(users.getDriver().getUser().get(i));
                        }
                    }else {
                        for (int i = 0; i < users.getDriver().getUser().size(); i++) {
                            for (int j = 0; j < MainActivity.this.users.size(); j++) {
                                if (!users.getDriver().getUser().get(i).getPhone().equals(MainActivity.this.users.get(j).getPhone())){
                                    MainActivity.this.users.add(users.getDriver().getUser().get(i));
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Data> call, @NonNull Throwable t) {
                Log.d("Adverse diving",String.valueOf(t.getMessage()));
            }
        });
        apiInterface.getAllVehicles().enqueue(new Callback<VehicleData>() {
            @Override
            public void onResponse(@NonNull Call<VehicleData> call, @NonNull Response<VehicleData> response) {
                VehicleData vehicleData = response.body();
                assert vehicleData != null;
                if (vehiclesEntities.size() != 0){
                    for (int i = 0; i < vehicleData.getVehicle().getVehicleList().size(); i++) {
                        for (int j = 0; j < vehiclesEntities.size(); j++) {
                            Log.d("Adverse diving2",String.valueOf(vehicleData.getVehicle().getVehicleList().get(0).getVehicle_id()));
                            if (!vehicleData.getVehicle().getVehicleList().get(i).getVehicle_id().equals(vehiclesEntities.get(j).getName())){
                                daoViewModel.insertVehicle(new VehiclesEntity(vehicleData.getVehicle().getVehicleList().get(i).getVehicle_id()));
                            }
                        }
                    }
                }else {
                    for (int i = 0; i < vehicleData.getVehicle().getVehicleList().size(); i++) {
                        Log.d("Adverse diving2",String.valueOf(vehicleData.getVehicle().getVehicleList().get(0).getVehicle_id()));
                        daoViewModel.insertVehicle(new VehiclesEntity(vehicleData.getVehicle().getVehicleList().get(i).getVehicle_id()));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<VehicleData> call, @NonNull Throwable t) {
                Log.d("Adverse diving2",String.valueOf(t.getMessage()));
            }
        });
    }

    private void lastAdapterClicked() {
        lastAdapter.setListener(new RecyclerViewLastAdapter.LastDaysRecyclerViewItemClickListener() {
            @Override
            public void onclickItem(String s) {
                if (daysArray.contains(s)) {
                    daysArray.remove(s);
                } else {
                    daysArray.add(s);
                }

                optionMenu.findItem(R.id.shareMenu).setVisible(!daysArray.isEmpty());
            }

            @Override
            public void onclickDvir(String s,ArrayList<DvirEntity> dvirEntities) {

                if (dvirEntities.size() != 0){
                    Intent intent = new Intent(MainActivity.this, LGDDActivity.class);
                    intent.putExtra("position", 3);
                    intent.putExtra("currDay",s);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(MainActivity.this, AddDvirActivity.class);
                    intent.putExtra("currDay",s);
                    startActivity(intent);
                }
            }
        });


//        lastDaysCheck = findViewById(R.id.idLastDaysCheckAll);
//        toDaySelect = findViewById(R.id.idTableRadioBtn);
//
//        lastDaysCheck.setOnClickListener(v -> {
//            if (lastAdapter.isSelected()) {
//                lastAdapter.setSelected(false);
//                daysArray.clear();
//                toDaySelect.setImageResource(R.drawable.state_unchacked);
//                lastDaysCheck.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_circle_outline_24, 0);
//            } else {
//                daoViewModel.getMgetAllDays().observe(this, dayEntities -> {
//                    for (int i = 0; i < dayEntities.size(); i++) {
//                        daysArray.add(dayEntities.get(i).getDay_name());
//                    }
//                });
//
//                lastAdapter.setSelected(true);
//                toDaySelect.setImageResource(R.drawable.state_checked);
//                lastDaysCheck.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.state_checked, 0);
//            }
//            lastAdapter.notifyDataSetChanged();
//
//            optionMenu.findItem(R.id.shareMenu).setVisible(!daysArray.isEmpty());
//        });
    }

    public void setActivateDr() {

//        Button activateDr = findViewById(R.id.idDRWithExeption);
//
//        activateDr.setOnClickListener(v -> showAlertDialog());
    }

    private void showAlertDialog() {
        String[] items = {"Adverse driving", "16-Hour Exception"};

//        final boolean[] checked = new boolean[items.length];
//        for (int i = 0; i < items.length; i++) {
//            checked[i] = false;
//        }
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Activate DR with exception")
                .setSingleChoiceItems(items, 2, (dialog, which) -> Log.d("ECEPT", "onClick: " + which))
                .setPositiveButton("Ok", (dialog, which) -> Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show()).setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(MainActivity.this, "cancel", Toast.LENGTH_SHORT).show());
        AlertDialog alert = alertDialog.create();
        alert.show();

    }

    private void clickLGDDButtons() {
        TextView inspectionMode, addDvir,idPhoneNumber,settings,hosRules;
        Button log, general, doc, dvir;
        log = findViewById(R.id.idTableBtnLog);
        general = findViewById(R.id.idTableBtnGeneral);
        doc = findViewById(R.id.idTableBtnDocs);
        dvir = findViewById(R.id.idTableBtnDVIR);
        inspectionMode = findViewById(R.id.idSpinnerInspection);
        settings = findViewById(R.id.idSettings);
        hosRules = findViewById(R.id.idHOSRules);
//        addDvir = findViewById(R.id.idTableDvir);
        TextView idLogout = findViewById(R.id.idLogout);
        idUsername = findViewById(R.id.idUsername);
//        idPhoneNumber = findViewById(R.id.idPhoneNumber);
        CircleImageView idAvatar = findViewById(R.id.idAvatar);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            idUsername.setText(String.format("%s %s", driverSharedPrefs.getFirstname(),driverSharedPrefs.getLastname()));
//            idPhoneNumber.setText(driverSharedPrefs.getPhoneNumber());
        },1000);

        settings.setOnClickListener(view -> {
            Intent intent = new Intent(this,SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        hosRules.setOnClickListener(view -> {
            Intent intent = new Intent(this,RulesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        idLogout.setOnClickListener(v -> {
            SessionManager sessionManager = new SessionManager(this);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Do you want to Log out")
                    .setPositiveButton("Yes", (dialog, which) ->{
                        sessionManager.clearAccessToken();
                        sessionManager.clearToken();
                        Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("No",((dialog, which) -> dialog.cancel()));
            AlertDialog alert = alertDialog.create();
            alert.show();
        });

        dvirViewModel = new DvirViewModel(this.getApplication());
        dvirViewModel = ViewModelProviders.of(this).get(DvirViewModel.class);
        dvirViewModel.getMgetDvirs().observe(this,dvirEntities -> {

            boolean hasDvir = false;

            if (dvirEntities.size() == 0){

//                addDvir.setText(R.string.no_dvir);
//                addDvir.setOnClickListener(v ->{
//                            Intent intent = new Intent(MainActivity.this, AddDvirActivity.class);
//                            intent.putExtra("currDay",today);
//                            startActivity(intent);
//                        }
//                );
                dvir.setOnClickListener(v -> {
                            Intent intent = new Intent(this, LGDDActivity.class);
                            intent.putExtra("position", 3);
                            intent.putExtra("currDay", today);
                            startActivity(intent);
                        }
                );

            }else {

                for (int i = 0; i < dvirEntities.size(); i++) {
                    if (dvirEntities.get(i).getDay().equals(today)){

                        hasDvir = true;

//                        addDvir.setText(R.string.dvir);
//
                        String c = dvirEntities.get(i).getDay();
//
//                        addDvir.setOnClickListener(v ->{
//                            Intent intent = new Intent(this, LGDDActivity.class);
//                            intent.putExtra("position", 3);
//                            intent.putExtra("currDay",c);
//                            startActivity(intent);
//                                }
//                        );
                        dvir.setOnClickListener(v -> {
                                    Intent intent = new Intent(this, LGDDActivity.class);
                                    intent.putExtra("position", 3);
                                    intent.putExtra("currDay",c);
                                    startActivity(intent);
                                }
                        );
                    }
                }if (!hasDvir){
//                    addDvir.setText(R.string.no_dvir);
//                    addDvir.setOnClickListener(v ->{
//                                Intent intent = new Intent(MainActivity.this, AddDvirActivity.class);
//                                intent.putExtra("currDay",today);
//                                startActivity(intent);
//                            }
//                    );
                    dvir.setOnClickListener(v -> {
                                Intent intent = new Intent(this, LGDDActivity.class);
                                intent.putExtra("position", 3);
                                intent.putExtra("currDay",today);
                                startActivity(intent);
                            }
                    );
                }
            }
        });

        log.setOnClickListener(v ->{
                Intent intent = new Intent(this, LGDDActivity.class);
                intent.putExtra("position", 0);
                intent.putExtra("currDay",today);
                startActivity(intent);
            }
        );


        general.setOnClickListener(v -> {
                    Intent intent = new Intent(this, LGDDActivity.class);
                    intent.putExtra("position", 1);
                    intent.putExtra("currDay",today);
                    startActivity(intent);
                }
        );

        doc.setOnClickListener(v ->{
                    Intent intent = new Intent(this, LGDDActivity.class);
                    intent.putExtra("position", 2);
//                intent.putExtra("currDay",null);
                    startActivity(intent);
                }
        );


//        recap.setOnClickListener(v -> loadLGDDFragment(RecapFragment.newInstance()));
//
        inspectionMode.setOnClickListener(v -> {
            loadLGDDFragment(InspectionModuleFragment.newInstance());
            drawerLayout.close();
        });
    }

    private void loadLGDDFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.addToBackStack("fragment");
        fragmentTransaction.replace(R.id.drawer_layout, fragment);
        fragmentTransaction.commit();
    }

    private void optimizeViewModels() {
        statusDaoViewModel = new StatusDaoViewModel(this.getApplication());
        daoViewModel = new DayDaoViewModel(this.getApplication());

        apiInterface = ApiClient.getClient().create(APIInterface.class);
        truckStatusEntities = new ArrayList<>();
        users = new ArrayList<>();

        daoViewModel.getGetAllVehicles().observe(this,vehiclesEntities -> {
            this.vehiclesEntities.addAll(vehiclesEntities);
        });

        userViewModel.getMgetDrivers().observe(this,drivers -> {
            users.addAll(drivers);
        });

        statusDaoViewModel = ViewModelProviders.of(this).get(StatusDaoViewModel.class);
        statusDaoViewModel.getmAllStatus().observe(this, truckStatusEntities -> {
            for (int i = 0; i < truckStatusEntities.size(); i++) {
                if (truckStatusEntities.get(i).getTime().equalsIgnoreCase(today)) {
                    MainActivity.this.truckStatusEntities.add(truckStatusEntities.get(i));
                }
                logEntities.add(truckStatusEntities.get(i));
            }

            Log.d("SetTextI18n",String.valueOf(truckStatusEntities.size()));

            if (truckStatusEntities.size() != 0) {
                last_status = truckStatusEntities.get(truckStatusEntities.size() - 1).getTo_status();
            } else {
                last_status = EnumsConstants.STATUS_OFF;
            }
            setTopLastPos(last_status);
        });

        daoViewModel = ViewModelProviders.of(this).get(DayDaoViewModel.class);
        daoViewModel.getMgetAllDays().observe(this, dayEntities -> {
        });

    }

    @SuppressLint("SetTextI18n")
    private void setTodayAttr(String time,String today) {
        TextView day = findViewById(R.id.idTableDay);
        TextView month = findViewById(R.id.idTableMonth);
//        TextView lastDays = findViewById(R.id.idLas14DayText);
        day.setText(time.split(" ")[0]);
        month.setText(today);
    }

    @SuppressWarnings("deprecation")
    private int getCurrentSeconds() {
        int hour = Calendar.getInstance().getTime().getHours();
        int minute = Calendar.getInstance().getTime().getMinutes();
        int second = Calendar.getInstance().getTime().getSeconds();
        return hour * 3600 + minute * 60 + second;
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        saveLanguage(lang);
    }

    public void saveLanguage(String language) {
        SharedPreferences pref = getApplicationContext()
                .getSharedPreferences("userData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("lan", language);
        editor.apply();
    }

//    private String loadLocal() {
//        SharedPreferences pref = getApplicationContext()
//                .getSharedPreferences("userData", Context.MODE_PRIVATE);
//        return pref.getString("lan", "en");
//    }

//    public void saveLastPosition(int last_P) {
//        SharedPreferences pref = getApplicationContext()
//                .getSharedPreferences("userData", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putInt("last_P", last_P);
//        editor.apply();
//    }

//    private int getLastP() {
//        SharedPreferences pref = getApplicationContext()
//                .getSharedPreferences("userData", Context.MODE_PRIVATE);
//        return pref.getInt("last_P", 0);
//    }

    public void saveLastStatusTime() {
        SharedPreferences pref = getApplicationContext()
                .getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("last_S_T", (int) SystemClock.uptimeMillis() / 1000);
        editor.apply();
    }

    private int getLastStatusSec() {
        SharedPreferences pref = getApplicationContext()
                .getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        return pref.getInt("last_S_T", 0);
    }

    public void restartActivity() {
        recreate();
    }

    private void onClickVisiblityCanAndSaveBtn() {
        Button save = findViewById(R.id.idSaveStatus);
        Button cancel = findViewById(R.id.idCancelStatus);
        TextView findLocation = findViewById(R.id.idCurrentLocation);
        final EditText editLocation = findViewById(R.id.idTvCurrentLocation);
        final EditText note = findViewById(R.id.idNoteEdit);

        findLocation.setOnClickListener(v -> {
            if (checkGrantResults(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, new int[]{1})) {
                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                if (latitude != 0 && longtitude != 0) {
                    try {
                        List<Address> addresses = geocoder.getFromLocation(latitude, longtitude, 1);
                        Address obj = addresses.get(0);
                        String add = obj.getCountryName();
                        add = add + ", " + obj.getSubAdminArea();

                        editLocation.setText(add);
                        editLocation.setClickable(false);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Failed to detect location", Toast.LENGTH_SHORT).show();
                }
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }

        });

//        findViewById(R.id.idDocumentIcon).setOnClickListener(v -> {
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("application/*");
//            startActivityForResult(intent, 0);
//        });

        cancel.setOnClickListener(v -> {
            visiblityViewCons.setVisibility(View.GONE);
            off.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusOFF));
            sb.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusSB));
            dr.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusDR));
            on.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusON));
            statusOff.setTextColor(ContextCompat.getColor(this, R.color.colorStatusOFFBold));
            imageOff.setColorFilter(ContextCompat.getColor(this, R.color.colorStatusOFFBold));
            statusSB.setTextColor(ContextCompat.getColor(this, R.color.colorStatusSBBold));
            imageSB.setColorFilter(ContextCompat.getColor(this, R.color.colorStatusSBBold));
            statusDR.setTextColor(ContextCompat.getColor(this, R.color.colorStatusDRBold));
            imageDR.setColorFilter(ContextCompat.getColor(this, R.color.colorStatusDRBold));
            statusON.setTextColor(ContextCompat.getColor(this, R.color.colorStatusONBold));
            imageON.setColorFilter(ContextCompat.getColor(this, R.color.colorStatusONBold));
        });

        save.setOnClickListener(v -> {
            visiblityViewCons.setVisibility(View.GONE);
            off.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusOFF));
            sb.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusSB));
            dr.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusDR));
            on.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusON));
            statusOff.setTextColor(ContextCompat.getColor(this, R.color.colorStatusOFFBold));
            imageOff.setColorFilter(ContextCompat.getColor(this, R.color.colorStatusOFFBold));
            statusSB.setTextColor(ContextCompat.getColor(this, R.color.colorStatusSBBold));
            imageSB.setColorFilter(ContextCompat.getColor(this, R.color.colorStatusSBBold));
            statusDR.setTextColor(ContextCompat.getColor(this, R.color.colorStatusDRBold));
            imageDR.setColorFilter(ContextCompat.getColor(this, R.color.colorStatusDRBold));
            statusON.setTextColor(ContextCompat.getColor(this, R.color.colorStatusONBold));
            imageON.setColorFilter(ContextCompat.getColor(this, R.color.colorStatusONBold));
            if (current_status != last_status) {
                reqdelinprogress = true;
                reccount = 0;

                for (int i = startseq; i < startseq + 10; i++) {
                    EldBleError err = mEldManager.RequestRecord(i);
                    Log.d("TESTING", "request " + i + " : " + err);
                    if (err == EldBleError.RECORD_OUT_OF_RANGE) {
                        Log.d("TESTING", "Not enough records on device");
                        break;
                    }
                }
                if (editLocation.getText().equals("")){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle("No location created")
                            .setMessage("Create current location!")
                            .setPositiveButton("OK", (dialog, which) -> alertDialog.setCancelable(true));
                    AlertDialog alert = alertDialog.create();
                    alert.show();
                }
                else if (note.getText().equals("")){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle("No note created")
                            .setMessage("Create note!")
                            .setPositiveButton("OK", (dialog, which) -> alertDialog.setCancelable(true));
                    AlertDialog alert = alertDialog.create();
                    alert.show();
                }
                else{
                    statusDaoViewModel.insertStatus(new LogEntity(last_status, current_status, editLocation.getText().toString(), note.getText().toString(), null, today, getCurrentSeconds()));
                        lastStatusData.saveLasStatus(current_status,getCurrentSeconds());
//                saveLastStatusTime();
                    restartActivity();
                }
            }else {
                if(last_status == EnumsConstants.STATUS_OFF){
                    off.setClickable(false);
                }
                if(last_status == EnumsConstants.STATUS_SB){
                    sb.setClickable(false);
                }
                if(last_status == EnumsConstants.STATUS_DR){
                    dr.setClickable(false);
                }
                if(last_status == EnumsConstants.STATUS_ON){
                    on.setClickable(false);
                }
            }
        });
    }

    private void setTopLastPos(int lastPos) {
        CardView cardView = findViewById(R.id.idCardStatus);
        ImageView icon = findViewById(R.id.idStatusImage);
        TextView statusText = findViewById(R.id.idStatusText);
//        TextView statusTime = findViewById(R.id.idStatusTime);

//        if (lastPos == EnumsConstants.STATUS_ON) {
//            cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusON));
//            statusText.setText(R.string.on);
//            statusText.setTextColor(ContextCompat.getColor(this, R.color.colorStatusONBold));
//            icon.setColorFilter(ContextCompat.getColor(this, R.color.colorStatusONBold));
//            icon.setImageResource(R.drawable.ic_truck);
//        } else if (lastPos == EnumsConstants.STATUS_SB) {
//            cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusSB));
//            statusText.setTextColor(ContextCompat.getColor(this, R.color.colorStatusSBBold));
//            icon.setColorFilter(ContextCompat.getColor(this, R.color.colorStatusSBBold));
//            statusText.setText(R.string.sb);
//            icon.setImageResource(R.drawable.ic__1748117516352401124513);
//        } else if (lastPos == EnumsConstants.STATUS_DR) {
//            cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusDR));
//            statusText.setTextColor(ContextCompat.getColor(this, R.color.colorStatusDRBold));
//            icon.setColorFilter(ContextCompat.getColor(this, R.color.colorStatusDRBold));
//            statusText.setText(R.string.dr);
//            icon.setImageResource(R.drawable.ic_steering_wheel_svgrepo_com);
//        } else {
//            cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusOFF));
//            statusText.setTextColor(ContextCompat.getColor(this, R.color.colorStatusOFFBold));
//            icon.setColorFilter(ContextCompat.getColor(this, R.color.colorStatusOFFBold));
//            statusText.setText(R.string.off);
//            icon.setImageResource(R.drawable.ic_baseline_power_settings_new_24);
//        }
    }

    private void onClickCustomBtn() {
        off = findViewById(R.id.cardOff);
        sb = findViewById(R.id.cardSB);
        dr = findViewById(R.id.cardDR);
        on = findViewById(R.id.cardON);
        imageOff = findViewById(R.id.imageOff);
        statusOff = findViewById(R.id.statusOff);
        imageSB = findViewById(R.id.imageSB);
        statusSB = findViewById(R.id.statusSB);
        imageDR = findViewById(R.id.imageDR);
        statusDR = findViewById(R.id.statusDR);
        imageON = findViewById(R.id.imageON);
        statusON = findViewById(R.id.statusON);
        visiblityViewCons = findViewById(R.id.idVisibilityViewCons);

        off.setOnClickListener(v -> {
            visiblityViewCons.setVisibility(View.VISIBLE);
            off.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusOFFBold));
            sb.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusSB));
            dr.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusDR));
            on.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusON));
            statusOff.setTextColor(ContextCompat.getColor(this, R.color.white));
            imageOff.setColorFilter(ContextCompat.getColor(this, R.color.white));
            statusSB.setTextColor(ContextCompat.getColor(this, R.color.colorStatusSBBold));
            imageSB.setColorFilter(ContextCompat.getColor(this, R.color.colorStatusSBBold));
            statusDR.setTextColor(ContextCompat.getColor(this, R.color.colorStatusDRBold));
            imageDR.setColorFilter(ContextCompat.getColor(this, R.color.colorStatusDRBold));
            statusON.setTextColor(ContextCompat.getColor(this, R.color.colorStatusONBold));
            imageON.setColorFilter(ContextCompat.getColor(this, R.color.colorStatusONBold));
            current_status = EnumsConstants.STATUS_OFF;
        });

        sb.setOnClickListener(v -> {
            visiblityViewCons.setVisibility(View.VISIBLE);
            off.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusOFF));
            sb.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusSBBold));
            dr.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusDR));
            on.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusON));
            statusOff.setTextColor(ContextCompat.getColor(this, R.color.colorStatusOFFBold));
            imageOff.setColorFilter(ContextCompat.getColor(this, R.color.colorStatusOFFBold));
            statusSB.setTextColor(ContextCompat.getColor(this, R.color.white));
            imageSB.setColorFilter(ContextCompat.getColor(this, R.color.white));
            statusDR.setTextColor(ContextCompat.getColor(this, R.color.colorStatusDRBold));
            imageDR.setColorFilter(ContextCompat.getColor(this, R.color.colorStatusDRBold));
            statusON.setTextColor(ContextCompat.getColor(this, R.color.colorStatusONBold));
            imageON.setColorFilter(ContextCompat.getColor(this, R.color.colorStatusONBold));
            current_status = EnumsConstants.STATUS_SB;
        });

        dr.setOnClickListener(v -> {
            visiblityViewCons.setVisibility(View.VISIBLE);
            off.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusOFF));
            sb.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusSB));
            dr.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusDRBold));
            on.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusON));
            statusOff.setTextColor(ContextCompat.getColor(this, R.color.colorStatusOFFBold));
            imageOff.setColorFilter(ContextCompat.getColor(this, R.color.colorStatusOFFBold));
            statusSB.setTextColor(ContextCompat.getColor(this, R.color.colorStatusSBBold));
            imageSB.setColorFilter(ContextCompat.getColor(this, R.color.colorStatusSBBold));
            statusDR.setTextColor(ContextCompat.getColor(this, R.color.white));
            imageDR.setColorFilter(ContextCompat.getColor(this, R.color.white));
            statusON.setTextColor(ContextCompat.getColor(this, R.color.colorStatusONBold));
            imageON.setColorFilter(ContextCompat.getColor(this, R.color.colorStatusONBold));
            current_status = EnumsConstants.STATUS_DR;
        });

        on.setOnClickListener(v -> {
            visiblityViewCons.setVisibility(View.VISIBLE);
            off.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusOFF));
            sb.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusSB));
            dr.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusDR));
            on.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusONBold));
            statusOff.setTextColor(ContextCompat.getColor(this, R.color.colorStatusOFFBold));
            imageOff.setColorFilter(ContextCompat.getColor(this, R.color.colorStatusOFFBold));
            statusSB.setTextColor(ContextCompat.getColor(this, R.color.colorStatusSBBold));
            imageSB.setColorFilter(ContextCompat.getColor(this, R.color.colorStatusSBBold));
            statusDR.setTextColor(ContextCompat.getColor(this, R.color.colorStatusDRBold));
            imageDR.setColorFilter(ContextCompat.getColor(this, R.color.colorStatusDRBold));
            statusON.setTextColor(ContextCompat.getColor(this, R.color.white));
            imageON.setColorFilter(ContextCompat.getColor(this, R.color.white));
            current_status = EnumsConstants.STATUS_ON;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_option_menus, menu);
        optionMenu = menu;
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shareMenu:
                Toast.makeText(this, "Share" + daysArray.size(), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.connectMenu:
                connectToEld();
                return true;
            default:
                return false;
        }
    }

    private void getDrawerTouchEvent() {
        nightModeSwitch = findViewById(R.id.idNightChoose);
//        TextView textView = findViewById(R.id.idSpinnerLanguage);

        checkNightModeActivated();
        nightModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                userData.saveMode(true);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                userData.saveMode(false);
            }
            restartActivity();
        });

//        textView.setOnClickListener(v -> {
//            loadLGDDFragment(new LanguageFragment());
//            drawerLayout.closeDrawers();
//        });
    }

    private void checkNightModeActivated() {
        if (userData.getMode()) {
            nightModeSwitch.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            nightModeSwitch.setChecked(false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void getDrawerToggleEvent() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, activity_main_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                try {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                try {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        };

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            // Checking for fragment count on back stack
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                // Go to the previous fragment
                getSupportFragmentManager().popBackStack();
            }
            else {
                // Exit the app
                super.onBackPressed();
//            }
            }
        }
    }

    //ELD functions
    private void connectToEld() {

        final ConnectToEldDialog dialog = new ConnectToEldDialog(this);
        dialog.setCancelable(false);
        dialog.show();

        final SearchEldDeviceDialog searchEldDeviceDialog = new SearchEldDeviceDialog(MainActivity.this);


        dialog.setAlerrtDialogItemClickInterface(() -> {
            dialog.cancel();

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            if (mEldManager.ScanForElds(bleScanCallback) == EldBleError.BLUETOOTH_NOT_ENABLED){
                mEldManager.EnableBluetooth(REQUEST_BT_ENABLE);
            }
            searchEldDeviceDialog.show();
        });
    }

//    private final EldDtcCallback dtcCallback = new EldDtcCallback() {
//        @Override
//        public void onDtcDetected(final String status, final String jsonString) {
//            runOnUiThread(() -> {
//                EventBus.getDefault().postSticky(new MessageModel(status, "EldDtcCallback"));
//            });
//        }
//    };

    private final EldFirmwareUpdateCallback fwUpdateCallback = new EldFirmwareUpdateCallback() {
        @Override
        public void onUpdateNotification(final String status) {
            runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("", "EldFirmwareUpdateCallback")));
        }
    };


    public void onCheckUpdateClicked(View v) {
        if (v.getId() == R.id.FW_CHECK) {
//            final String Status = mEldManager.CheckFirmwareUpdate();

            runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("Current firmware: " + mEldManager.GetFirmwareVersion() + " Available firmware: " + mEldManager.CheckFirmwareUpdate() + "\r\n", "")));
        }
    }

    public void onReqDebugClicked(View v) {
        if (v.getId() == R.id.REQ_DEBUG) {
            EldBleError status = mEldManager.RequestDebugData();
            if (status != EldBleError.SUCCESS) {
                runOnUiThread(() -> {
//                        mStatusView.append("Request Debug Failed\n");
                    EventBus.getDefault().postSticky(new MessageModel("Request Debug Failed\n", ""));
//                        EventBus.getDefault().postSticky(new MessageModel("Request Debug Failed4", "4"));
                    Log.d("TEST", "onDtcDetected: 4");
                });
            } else {
                runOnUiThread(() -> {
//                        mStatusView.append("Request Debug Succeeded\n");
                    EventBus.getDefault().postSticky(new MessageModel("Request Debug Succeeded\n", ""));
//                        EventBus.getDefault().postSticky(new MessageModel("Request Debug Succeeded5", "5"));
                    Log.d("TEST", "onDtcDetected: 5");

                });
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void onUpdateFwClicked(View v) {
        if (v.getId() == R.id.updateFw) {

            updateSelection = 0;

            // get prompts.xml view
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.update_popup, null);
            final EditText downloadInput = promptsView.findViewById(R.id.downloadInput);
            final EditText localInput = promptsView.findViewById(R.id.localInput);
            final RadioGroup radioGroup = promptsView.findViewById(R.id.radiogroup);

            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setView(promptsView);

            alertDialogBuilder
                    .setCancelable(true)
                    .setPositiveButton("START",
                            (dialog, id) -> {
                                if (updateSelection == 0) {
                                    if (downloadInput.getText().toString().equals("")) {
                                        mEldManager.StartUpdate(fwUpdateCallback, null);
                                    } else if (downloadInput.getText().toString().toUpperCase().equals("FLASHERROR")) {
                                        mEldManager.SendSpecialString("FLASHERROR");
                                    } else {
                                        mEldManager.StartUpdate(fwUpdateCallback, downloadInput.getText().toString());
                                    }
                                } else {
                                    mEldManager.StartUpdateLocal(fwUpdateCallback, localInput.getText().toString());
                                }
                            })
                    .setNegativeButton("Cancel",
                            (dialog, id) -> dialog.cancel());

            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                        switch (checkedId) {
                            case R.id.downloadbutton:
                                updateSelection = 0;
                                break;
                            case R.id.localbutton:
                                updateSelection = 1;
                                break;
                        }
                    }
            );

            // create alert dialog
            android.app.AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public void onEnableParametersClicked(View v) {
        if (v.getId() == R.id.ENABLE_PARAM) {
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.param_popup, null);
            final Switch dtcSwitch = promptsView.findViewById(R.id.diagnostics);
            final Switch fuelSwitch = promptsView.findViewById(R.id.fuel);
            final Switch engineSwitch = promptsView.findViewById(R.id.engine);
            final Switch transmissionSwitch = promptsView.findViewById(R.id.transmission);
            final Switch emissionsSwitch = promptsView.findViewById(R.id.emissions);
            final Switch driverSwitch = promptsView.findViewById(R.id.driver);

            dtcSwitch.setChecked(diagnosticEnabled);
            fuelSwitch.setChecked(fuelEnabled);
            engineSwitch.setChecked(engineEnabled);
            transmissionSwitch.setChecked(transmissionEnabled);
            emissionsSwitch.setChecked(emissionsEnabled);
            driverSwitch.setChecked(driverEnabled);

            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setView(promptsView);

            alertDialogBuilder
                    .setCancelable(true)
                    .setPositiveButton("OK",
                            (dialog, id) -> {
                                if (dtcSwitch.isChecked() && !diagnosticEnabled) {
                                    final EldBleError status = mEldManager.EnableAdditionalParameters(EldParameterTypes.DIAGNOSTIC_PARAMETERS);
                                    subscribedRecords.add(EldBroadcastTypes.ELD_DIAGNOSTIC_RECORD);
                                    diagnosticEnabled = true;
                                    runOnUiThread(() -> {
                                        //todo mStatusView.append("EnableAdditionalParameters (" + status + ")\n");
                                    });
                                } else if (!dtcSwitch.isChecked() && diagnosticEnabled) {
                                    final EldBleError status = mEldManager.DisableAdditionalParameters(EldParameterTypes.DIAGNOSTIC_PARAMETERS);
                                    subscribedRecords.remove(EldBroadcastTypes.ELD_DIAGNOSTIC_RECORD);
                                    diagnosticEnabled = false;
                                    runOnUiThread(() -> {
                                        //todo mStatusView.append("DisableAdditionalParameters (" + status + ")\n");
                                    });
                                }

                                if (fuelSwitch.isChecked() && !fuelEnabled) {
                                    final EldBleError status = mEldManager.EnableAdditionalParameters(EldParameterTypes.FUEL_PARAMETERS);
                                    subscribedRecords.add(EldBroadcastTypes.ELD_FUEL_RECORD);
                                    fuelEnabled = true;
                                    runOnUiThread(() -> {
                                        //todo mStatusView.append("EnableAdditionalParameters (" + status + ")\n");
                                    });
                                } else if (!fuelSwitch.isChecked() && fuelEnabled) {
                                    final EldBleError status = mEldManager.DisableAdditionalParameters(EldParameterTypes.FUEL_PARAMETERS);
                                    subscribedRecords.remove(EldBroadcastTypes.ELD_FUEL_RECORD);
                                    fuelEnabled = false;
                                    runOnUiThread(() -> {
                                        //todo mStatusView.append("DisableAdditionalParameters (" + status + ")\n");
                                    });
                                }

                                if (engineSwitch.isChecked() && !engineEnabled) {
                                    final EldBleError status = mEldManager.EnableAdditionalParameters(EldParameterTypes.ENGINE_PARAMETERS);
                                    subscribedRecords.add(EldBroadcastTypes.ELD_ENGINE_PARAMETERS_RECORD);
                                    engineEnabled = true;
                                    runOnUiThread(() -> {
                                        //todo mStatusView.append("EnableAdditionalParameters (" + status + ")\n");
                                    });
                                } else if (!engineSwitch.isChecked() && engineEnabled) {
                                    final EldBleError status = mEldManager.DisableAdditionalParameters(EldParameterTypes.ENGINE_PARAMETERS);
                                    subscribedRecords.remove(EldBroadcastTypes.ELD_ENGINE_PARAMETERS_RECORD);
                                    engineEnabled = false;
                                    runOnUiThread(() -> {
                                        //todo mStatusView.append("DisableAdditionalParameters (" + status + ")\n");
                                    });
                                }

                                if (transmissionSwitch.isChecked() && !transmissionEnabled) {
                                    final EldBleError status = mEldManager.EnableAdditionalParameters(EldParameterTypes.TRANSMISSION_PARAMETERS);
                                    subscribedRecords.add(EldBroadcastTypes.ELD_TRANSMISSION_PARAMETERS_RECORD);
                                    transmissionEnabled = true;
                                    runOnUiThread(() -> {
                                        //todo mStatusView.append("EnableAdditionalParameters (" + status + ")\n");
                                    });
                                } else if (!transmissionSwitch.isChecked() && transmissionEnabled) {
                                    final EldBleError status = mEldManager.DisableAdditionalParameters(EldParameterTypes.TRANSMISSION_PARAMETERS);
                                    subscribedRecords.remove(EldBroadcastTypes.ELD_TRANSMISSION_PARAMETERS_RECORD);
                                    transmissionEnabled = false;
                                    runOnUiThread(() -> {
                                        //todo mStatusView.append("DisableAdditionalParameters (" + status + ")\n");
                                    });
                                }

                                if (emissionsSwitch.isChecked() && !emissionsEnabled) {
                                    final EldBleError status = mEldManager.EnableAdditionalParameters(EldParameterTypes.EMISSIONS_PARAMETERS);
                                    subscribedRecords.add(EldBroadcastTypes.ELD_EMISSIONS_PARAMETERS_RECORD);
                                    emissionsEnabled = true;
                                    runOnUiThread(() -> {
                                        //todo mStatusView.append("EnableAdditionalParameters (" + status + ")\n");
                                    });
                                } else if (!emissionsSwitch.isChecked() && emissionsEnabled) {
                                    final EldBleError status = mEldManager.DisableAdditionalParameters(EldParameterTypes.EMISSIONS_PARAMETERS);
                                    subscribedRecords.remove(EldBroadcastTypes.ELD_EMISSIONS_PARAMETERS_RECORD);
                                    emissionsEnabled = false;
                                    runOnUiThread(() -> {
                                        //todo mStatusView.append("DisableAdditionalParameters (" + status + ")\n");
                                    });
                                }

                                if (driverSwitch.isChecked() && !driverEnabled) {
                                    final EldBleError status = mEldManager.EnableAdditionalParameters(EldParameterTypes.DRIVER_BEHAVIOR);
                                    subscribedRecords.add(EldBroadcastTypes.ELD_DRIVER_BEHAVIOR_RECORD);
                                    driverEnabled = true;
                                    runOnUiThread(() -> {
                                        //todo mStatusView.append("EnableAdditionalParameters (" + status + ")\n");
                                    });
                                } else if (!driverSwitch.isChecked() && driverEnabled) {
                                    final EldBleError status = mEldManager.DisableAdditionalParameters(EldParameterTypes.DRIVER_BEHAVIOR);
                                    subscribedRecords.remove(EldBroadcastTypes.ELD_DRIVER_BEHAVIOR_RECORD);
                                    driverEnabled = false;
                                    runOnUiThread(() -> {
                                        //todo mStatusView.append("DisableAdditionalParameters (" + status + ")\n");
                                    });
                                }

                                mEldManager.UpdateSubscribedRecordTypes(subscribedRecords);
                            })
                    .setNegativeButton("Cancel",
                            (dialog, id) -> dialog.cancel());

            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    public void onSetPeriodClicked(View v) {
        if (v.getId() == R.id.SET_PERIOD) {
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.period_popup, null);
            final EditText periodInput = promptsView.findViewById(R.id.periodinput);

            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setView(promptsView);

            alertDialogBuilder
                    .setCancelable(true)
                    .setPositiveButton("OK",
                            (dialog, id) -> {
                                int period = Integer.parseInt(periodInput.getText().toString()) * 1000;
                                final EldBleError status = mEldManager.SetRecordingInterval(period);
                                runOnUiThread(() -> {
                                    //todo mStatusView.append("Set Period Status (" + status + ")\n");
                                });
                            })
                    .setNegativeButton("Cancel",
                            (dialog, id) -> dialog.cancel());

            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    public void onReqRecordClicked(View v) {
        if (v.getId() == R.id.reqButton) {
            final EldBleError status = mEldManager.RequestRecord();
            runOnUiThread(() -> {
                //todo mStatusView.append("ReqRecordStatus (" + status + ")\n");
            });
        }
    }

    public void onReqDelClicked(View v) {
        if (v.getId() == R.id.reqDelButton) {
//            1. request 10 records, for example from 1 to 10
//            2. Wait until 10th record is received
//            3. call deleteRecord method with parameters 1, 10
//            4. request record 11
//               In that case library does not return 11th record, need to request it few times or wait some time

            reqdelinprogress = true;
            reccount = 0;

            for (int i = startseq; i < startseq + 10; i++) {
                EldBleError err = mEldManager.RequestRecord(i);
                Log.d("TESTING", "request " + i + " : " + err);
                if (err == EldBleError.RECORD_OUT_OF_RANGE) {
                    Log.d("TESTING", "Not enough records on device");
                    break;
                }
            }
        }
    }

    public void onDisconnectClicked(View v) {
        if (v.getId() == R.id.Disconnect) {
            mEldManager.DisconnectEld();
        }
    }


    public void onRescanClicked(View v) {
        if (v.getId() == R.id.Rescan) {
            ScanForEld();
        }
    }

    public void onDelRecordClicked(View v) {
        if (v.getId() == R.id.delButton) {
            final EldBleError status = mEldManager.DeleteRecord(startseq, startseq);
            runOnUiThread(() -> {
                //todo mStatusView.append("DelRecordStatus (" + status + ")\n");
            });
        }
    }

    public void onSetOdoClicked(View v) {
        if (v.getId() == R.id.SET_ODO) {
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.odo_popup, null);
            final EditText odoInput = promptsView.findViewById(R.id.odoinput);

            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setView(promptsView);

            alertDialogBuilder
                    .setCancelable(true)
                    .setPositiveButton("OK",
                            (dialog, id) -> {
                                int odo = Integer.parseInt(odoInput.getText().toString());
                                if (odo > 0) {
                                    mEldManager.SetOdometer(odo);
                                }
                            })
                    .setNegativeButton("Cancel",
                            (dialog, id) -> dialog.cancel());

            // create alert dialog
            android.app.AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    }

    public void onSetTimeClicked(View v) {
        if (v.getId() == R.id.SET_TIME) {
            // get prompts.xml view
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.time_popup, null);
            final EditText timeInput = promptsView.findViewById(R.id.timeinput);

            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setView(promptsView);

            alertDialogBuilder
                    .setCancelable(true)
                    .setPositiveButton("OK",
                            (dialog, id) -> {
                                int time = Integer.parseInt(timeInput.getText().toString());
                                if (time > 0) {
                                    mEldManager.SetTime(time);
                                }
                            })
                    .setNegativeButton("Cancel",
                            (dialog, id) -> dialog.cancel());

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    }

    /**
     * @param context application Context so method can access LOCATION_SERVICE
     * @return true if location services are enabled else false
     */
    public boolean areLocationServicesEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        try {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void requestBlePermissions(final Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, requestCode);
    }

    public boolean checkGrantResults(String[] permissions, int[] grantResults) {
        int granted = 0;

        if (grantResults.length > 0) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION) || permission.equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        granted++;
                    }
                }
            }
        } else { // if cancelled
            return false;
        }

        return granted == 2;
    }

    private final EldBleConnectionStateChangeCallback bleConnectionStateChangeCallback = new EldBleConnectionStateChangeCallback() {
        @Override
        public void onConnectionStateChange(final int newState) {
            runOnUiThread(() -> {
                //todo mDataView.append("New State of connection" + Integer.toString(newState, 10) + "\n");
                EventBus.getDefault().postSticky(new MessageModel("newState", "EldBleConnectionStateChangeCallback"));
//                EventBus.getDefault().postSticky(new MessageModel("newState6", "EldBleConnectionStateChangeCallback6"));
                Log.d("TEST", "onDtcDetected: 6");
            });
        }
    };

    private final EldBleDataCallback bleDataCallback = new EldBleDataCallback() {
        @Override
        public void OnDataRecord(final EldBroadcast dataRec, final EldBroadcastTypes RecordType) {
            runOnUiThread(() -> {

                EventBus.getDefault().postSticky(new MessageModel("", dataRec.getBroadcastString().trim() + "\n"));
                Log.d("TEST", "onDtcDetected: 7");

                if (dataRec instanceof EldBufferRecord) {
                            startseq = ((EldBufferRecord) dataRec).getStartSeqNo();
                            endseq = ((EldBufferRecord) dataRec).getEndSeqNo();
                            Call<BufferRecord> call = apiInterface.sendBuffer(new BufferRecord(
                                    ((EldBufferRecord) dataRec).getStartSeqNo(),
                                    ((EldBufferRecord) dataRec).getEndSeqNo(),
                                    ((EldBufferRecord) dataRec).getStorageRemaining(),
                                    ((EldBufferRecord) dataRec).getTotRecords()
                            ));

                            call.enqueue(new Callback<BufferRecord>() {
                                @Override
                                public void onResponse(@NonNull Call<BufferRecord> call, @NonNull Response<BufferRecord> response) {

                                }

                                @Override
                                public void onFailure(@NonNull Call<BufferRecord> call, @NonNull Throwable t) {
                                    Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            });

//                } else if (RecordType != EldBroadcastTypes.ELD_DATA_RECORD) {
//                    if (RecordType == EldBroadcastTypes.ELD_CACHED_RECORD) {
//                        //Shows how to get to the specific record types created based on the broadcast info
//
//                        if (reqdelinprogress) {
//                            reccount++;
//                            Log.d("TESTING", "received " + reccount + " records");
//                            if (reccount == 10) {
//                                Log.d("TESTING", "delete " + startseq + "-" + (startseq + 9));
//                                mEldManager.DeleteRecord(startseq, startseq + 9);
//                                Log.d("TESTING", "request " + (startseq + 10));
//                                mEldManager.RequestRecord(startseq + 10);
//                            } else if (reccount == 11) {
//                                Log.d("TESTING", "success!");
//
//                                reqdelinprogress = false;
//                                reccount = 0;
//                            }
//                        }
//
//                        if (dataRec instanceof EldCachedPeriodicRecord) {
//
//                            ArrayList<Double> arrayList = new ArrayList<>();
//                            arrayList.add(((EldCachedPeriodicRecord) dataRec).getLatitude());
//                            arrayList.add(((EldCachedPeriodicRecord) dataRec).getLongitude());
//
//                            Call<CashedMotionRecord> call = apiInterface.sendMotion(new CashedMotionRecord(
//                                    ((EldCachedPeriodicRecord)dataRec).getRpm(),
//                                    ((EldCachedPeriodicRecord)dataRec).getSpeed(),
//                                    ((EldCachedPeriodicRecord)dataRec).getOdometer(),
//                                    ((EldCachedPeriodicRecord)dataRec).getEngineHours(),
//                                    new Point("Point",arrayList),
//                                    ((EldCachedPeriodicRecord)dataRec).getGpsSpeed(),
//                                    ((EldCachedPeriodicRecord)dataRec).getRecType().toString()
//                            ));
//                            call.enqueue(new Callback<CashedMotionRecord>() {
//                                @Override
//                                public void onResponse(@NonNull Call<CashedMotionRecord> call, @NonNull Response<CashedMotionRecord> response) {
//
//                                }
//
//                                @Override
//                                public void onFailure(@NonNull Call<CashedMotionRecord> call, @NonNull Throwable t) {
//
//                                }
//                            });
//
//
//                        } else if (dataRec instanceof EldCachedNewTimeRecord) {
//                            ((EldCachedNewTimeRecord) (dataRec)).getEngineHours();
//                            ((EldCachedNewTimeRecord) (dataRec)).getNewUnixTime();
//
//                            Call<NewTimeRecord> call = apiInterface.sendNewtime(new NewTimeRecord(
//                                    ((EldCachedNewTimeRecord)dataRec).getOldUnixTime(),
//                                    ((EldCachedNewTimeRecord)dataRec).getNewUnixTime(),
//                                    ((EldCachedNewTimeRecord)dataRec).getSeqNum()
//                            ));
//
//                            call.enqueue(new Callback<NewTimeRecord>() {
//                                @Override
//                                public void onResponse(@NonNull Call<NewTimeRecord> call, @NonNull Response<NewTimeRecord> response) {
//
//                                }
//
//                                @Override
//                                public void onFailure(@NonNull Call<NewTimeRecord> call, @NonNull Throwable t) {
//
//                                }
//                            });
//                        } else if (dataRec instanceof EldCachedNewVinRecord) {
//
//                            Call<CachedEngineRecord> call = apiInterface.sendEnginecache(new CachedEngineRecord(
//                                    ((EldCachedNewVinRecord)dataRec).getVin(),
//                                    ((EldCachedNewVinRecord)dataRec).getOdometer(),
//                                    ((EldCachedNewVinRecord)dataRec).getEngineHours(),
//                                    ((int) ((EldCachedNewVinRecord) dataRec).getUnixTime()),
//                                    ((EldCachedNewVinRecord)dataRec).getSeqNum(),
//                                    ((EldCachedNewVinRecord)dataRec).getRecType().toString()
//                            ));
//
//                            call.enqueue(new Callback<CachedEngineRecord>() {
//                                @Override
//                                public void onResponse(@NonNull Call<CachedEngineRecord> call, @NonNull Response<CachedEngineRecord> response) {
//
//                                }
//
//                                @Override
//                                public void onFailure(@NonNull Call<CachedEngineRecord> call, @NonNull Throwable t) {
//
//                                }
//                            });
//                        }
//
//                    } else if (RecordType == EldBroadcastTypes.ELD_DRIVER_BEHAVIOR_RECORD) {
//                        driverEnabled = true;
//
//                                int cruiseStatus = 0;
//
//                                switch (((EldDriverBehaviorRecord)dataRec).getCruiseStatus()){
//                                    case CRUISE_ACCELERATE: cruiseStatus = 1;
//                                        break;
//
//                                    case CRUISE_ACCELERATOR_OVERRIDE: cruiseStatus = 2;
//                                        break;
//
//                                    case CRUISE_DECELERATE: cruiseStatus = 3;
//                                        break;
//
//                                    case CRUISE_HOLD: cruiseStatus = 4;
//                                        break;
//
//                                    case CRUISE_INVALID: cruiseStatus = 5;
//                                        break;
//
//                                    case CRUISE_NA: cruiseStatus = 6;
//                                        break;
//
//                                    case CRUISE_OFF: cruiseStatus = 7;
//                                        break;
//
//                                    case CRUISE_RESUME: cruiseStatus = 8;
//                                        break;
//
//                                    case CRUISE_SET: cruiseStatus = 9;
//                                        break;
//
//                                }
//
//                                int seatBeltStatus = 0;
//
//                                switch (((EldDriverBehaviorRecord)dataRec).getSeatBeltStatus()){
//                                    case BELT_INVALID: seatBeltStatus = 1;
//                                        break;
//
//                                    case BELT_LOCKED: seatBeltStatus = 2;
//                                        break;
//
//                                    case BELT_NA: seatBeltStatus = 3;
//                                        break;
//
//                                    case BELT_UNKNOWN: seatBeltStatus = 4;
//                                        break;
//
//                                    case BELT_UNLOCKED: seatBeltStatus = 5;
//                                        break;
//                                }
//
//                                int absStatus = 0;
//
//                                switch (((EldDriverBehaviorRecord)dataRec).getAbsStatus()){
//                                    case ABS_ACTIVE: absStatus = 1;
//                                        break;
//
//                                    case ABS_INVALID: absStatus = 2;
//                                        break;
//
//                                    case ABS_NA: absStatus = 3;
//                                        break;
//
//                                    case ABS_PASSIVE: absStatus = 4;
//                                        break;
//
//                                    case ABS_RESERVED: absStatus = 5;
//                                        break;
//                                }
//
//                                int tractionStatus = 0;
//
//                                switch (((EldDriverBehaviorRecord)dataRec).getTractionStatus()){
//                                    case TRACTION_ERROR: tractionStatus = 1;
//                                        break;
//
//                                    case TRACTION_INVALID: tractionStatus = 2;
//                                        break;
//
//                                    case TRACTION_NA: tractionStatus = 3;
//                                        break;
//
//                                    case TRACTION_OFF: tractionStatus = 4;
//                                        break;
//
//                                    case TRACTION_ON: tractionStatus = 5;
//                                        break;
//                                }
//
//                                int stabilityStatus = 0;
//
//                                switch (((EldDriverBehaviorRecord)dataRec).getStabilityStatus()){
//                                    case STABILITY_ACTIVE: stabilityStatus = 1;
//                                        break;
//
//                                    case STABILITY_INVALID: stabilityStatus = 2;
//                                        break;
//
//                                    case STABILITY_NA: stabilityStatus = 3;
//                                        break;
//
//                                    case STABILITY_PASSIVE: stabilityStatus = 4;
//                                        break;
//
//                                    case STABILITY_RESERVED: stabilityStatus = 5;
//                                        break;
//                                }
//                                Call<DriverBehaviorRecord> sendData = apiInterface.sendDriverbehavior(new DriverBehaviorRecord(
//                                        ((EldDriverBehaviorRecord) dataRec).getCruiseSetSpeed_kph(),
//                                        cruiseStatus,
//                                        ((EldDriverBehaviorRecord) dataRec).getThrottlePosition_pct(),
//                                        ((EldDriverBehaviorRecord) dataRec).getAcceleratorPosition_pct(),
//                                        ((EldDriverBehaviorRecord) dataRec).getBrakePosition_pct(),
//                                        seatBeltStatus,
//                                        ((EldDriverBehaviorRecord) dataRec).getSteeringWheelAngle_deg(),
//                                        absStatus,
//                                        tractionStatus,
//                                        stabilityStatus,
//                                        ((EldDriverBehaviorRecord) dataRec).getBrakeSystemPressure_kpa()
//                                ));
//
//                                sendData.enqueue(new Callback<DriverBehaviorRecord>() {
//                                    @Override
//                                    public void onResponse(@NonNull Call<DriverBehaviorRecord> call, @NonNull Response<DriverBehaviorRecord> response) {
//
//                                    }
//
//                                    @Override
//                                    public void onFailure(@NonNull Call<DriverBehaviorRecord> call, @NonNull Throwable t) {
//                                        Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//
//                    } else if (RecordType == EldBroadcastTypes.ELD_ENGINE_PARAMETERS_RECORD) {
//                        engineEnabled = true;
//
//                        Call<EngineLiveRecord> sendData = apiInterface.sendEnginelive(new EngineLiveRecord(
//                                ((EldEngineRecord)dataRec).getOilPressure_kpa(),
//                                ((EldEngineRecord)dataRec).getTurboBoost_kpa(),
//                                ((EldEngineRecord)dataRec).getIntakePressure_kpa(),
//                                ((EldEngineRecord)dataRec).getFuelPressure_kpa(),
//                                ((EldEngineRecord)dataRec).getLoad_pct(),
//                                ((EldEngineRecord)dataRec).getMassAirFlow_galPerSec(),
//                                ((EldEngineRecord)dataRec).getTurboRpm(),
//                                ((EldEngineRecord)dataRec).getIntakeTemp_c(),
//                                ((EldEngineRecord)dataRec).getEngineCoolantTemp_c(),
//                                ((EldEngineRecord)dataRec).getEngineOilTemp_c(),
//                                ((EldEngineRecord)dataRec).getFuelTemp_c(),
//                                ((EldEngineRecord)dataRec).getChargeCoolerTemp_c(),
//                                ((EldEngineRecord)dataRec).getTorque_Nm(),
//                                ((EldEngineRecord)dataRec).getEngineOilLevel_pct(),
//                                ((EldEngineRecord)dataRec).getEngineCoolantLevel_pct(),
//                                ((EldEngineRecord)dataRec).getTripFuel_L(),
//                                ((EldEngineRecord)dataRec).getDrivingFuelEconomy_LPerKm()
//                        ));
//
//                        sendData.enqueue(new Callback<EngineLiveRecord>() {
//                            @Override
//                            public void onResponse(@NonNull Call<EngineLiveRecord> call, @NonNull Response<EngineLiveRecord> response) {
//
//                            }
//
//                            @Override
//                            public void onFailure(@NonNull Call<EngineLiveRecord> call, @NonNull Throwable t) {
//                                Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    } else if (RecordType == EldBroadcastTypes.ELD_EMISSIONS_PARAMETERS_RECORD) {
//                        emissionsEnabled = true;
//
//                        int dpfRegenStatus = 0;
//
//                        switch (((EldEmissionsRecord)dataRec).getDpfRegenStatus()){
//                            case DPFREGEN_ACTIVE: dpfRegenStatus = 1;
//                                break;
//
//                            case DPFREGEN_INVALID: dpfRegenStatus = 2;
//                                break;
//
//                            case DPFREGEN_NA: dpfRegenStatus = 3;
//                                break;
//
//                            case DPFREGEN_NOT_ACTIVE: dpfRegenStatus = 4;
//                                break;
//
//                            case DPFREGEN_PASSIVE: dpfRegenStatus = 5;
//                                break;
//                        }
//
//                        int scrInducementFaultStatus = 0;
//
//                        switch (((EldEmissionsRecord)dataRec).getScrInducementFaultStatus()){
//                            case SCRINDUCEMENT_INACTIVE: scrInducementFaultStatus = 1;
//                                break;
//
//                            case SCRINDUCEMENT_INVALID: scrInducementFaultStatus = 2;
//                                break;
//
//                            case SCRINDUCEMENT_LEVEL1: scrInducementFaultStatus = 3;
//                                break;
//
//                            case SCRINDUCEMENT_LEVEL2: scrInducementFaultStatus = 4;
//                                break;
//
//                            case SCRINDUCEMENT_LEVEL3: scrInducementFaultStatus = 5;
//                                break;
//
//                            case SCRINDUCEMENT_LEVEL4: scrInducementFaultStatus = 6;
//                                break;
//
//                            case SCRINDUCEMENT_LEVEL5: scrInducementFaultStatus = 7;
//                                break;
//
//                            case SCRINDUCEMENT_NA: scrInducementFaultStatus = 8;
//                                break;
//
//                            case SCRINDUCEMENT_TEMPORARY_OVERRIDE: scrInducementFaultStatus = 9;
//                                break;
//                        }
//
//                        Call<EmissionsRecord> sendData = apiInterface.sendEmission(new EmissionsRecord(
//                                ((EldEmissionsRecord) dataRec).getNOxInlet(),
//                                ((EldEmissionsRecord) dataRec).getNOxOutlet(),
//                                ((EldEmissionsRecord) dataRec).getAshLoad(),
//                                ((EldEmissionsRecord) dataRec).getDpfSootLoad(),
//                                dpfRegenStatus,
//                                ((EldEmissionsRecord) dataRec).getDpfDifferentialPressure(),
//                                ((EldEmissionsRecord) dataRec).getEgrValvePosition(),
//                                ((EldEmissionsRecord) dataRec).getAfterTreatmentFuelPressure(),
//                                ((EldEmissionsRecord) dataRec).getEngineExhaustTemperature(),
//                                ((EldEmissionsRecord) dataRec).getExhaustTemperature1(),
//                                ((EldEmissionsRecord) dataRec).getExhaustTemperature2(),
//                                ((EldEmissionsRecord) dataRec).getExhaustTemperature3(),
//                                ((EldEmissionsRecord) dataRec).getDefFluidLevel(),
//                                ((EldEmissionsRecord) dataRec).getDefTankTemperature(),
//                                scrInducementFaultStatus
//                        ));
//
//                        sendData.enqueue(new Callback<EmissionsRecord>() {
//                            @Override
//                            public void onResponse(@NonNull Call<EmissionsRecord> call, @NonNull Response<EmissionsRecord> response) {
//
//                            }
//
//                            @Override
//                            public void onFailure(@NonNull Call<EmissionsRecord> call, @NonNull Throwable t) {
//                                Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                    } else if (RecordType == EldBroadcastTypes.ELD_TRANSMISSION_PARAMETERS_RECORD) {
//                        transmissionEnabled = true;
//
//                        int torqueConverterLockupState = 0;
//
//                        switch (((EldTransmissionRecord)dataRec).getTorqueConverterLockupStatus()){
//                            case TORQUECNV_LOCKUP_DISENGAGED: torqueConverterLockupState = 1;
//                                break;
//
//                            case TORQUECNV_LOCKUP_ENGAGED: torqueConverterLockupState = 2;
//                                break;
//
//                            case TORQUECNV_LOCKUP_ERROR: torqueConverterLockupState = 3;
//                                break;
//
//                            case TORQUECNV_LOCKUP_INVALID: torqueConverterLockupState = 4;
//                                break;
//
//                            case TORQUECNV_LOCKUP_NA: torqueConverterLockupState = 5;
//                                break;
//                        }
//                        Call<TransmissionRecord> call = apiInterface.sendTransmission(new TransmissionRecord(
//                                ((EldTransmissionRecord) dataRec).getOutputShaftRpm(),
//                                ((EldTransmissionRecord) dataRec).getGearStatus(),
//                                ((EldTransmissionRecord) dataRec).getRequestGearStatus(),
//                                ((EldTransmissionRecord) dataRec).getTransmissionOilTemp_c(),
//                                torqueConverterLockupState
//                        ));
//
//                        call.enqueue(new Callback<TransmissionRecord>() {
//                            @Override
//                            public void onResponse(@NonNull Call<TransmissionRecord> call, @NonNull Response<TransmissionRecord> response) {
//
//                            }
//
//                            @Override
//                            public void onFailure(@NonNull Call<TransmissionRecord> call, @NonNull Throwable t) {
//                                Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                    else if (RecordType == EldBroadcastTypes.ELD_FUEL_RECORD) {
//                        fuelEnabled = true;
//                        int highrpmState = 0;
//
//                        switch (((EldFuelRecord)dataRec).getStateHighRPM()){
//                            case BAD: highrpmState = 1;
//                                break;
//
//                            case GOOD: highrpmState = 2;
//                                break;
//
//                            case INVALID: highrpmState = 3;
//                                break;
//
//                            case NORMAL: highrpmState = 4;
//                                break;
//
//                            case TERRIBLE: highrpmState = 5;
//                                break;
//
//                            case WARNING: highrpmState = 6;
//                                break;
//                        }
//
//                        int unsteadyState = 0;
//
//                        switch (((EldFuelRecord)dataRec).getStateUnsteady()){
//                            case BAD: unsteadyState = 1;
//                                break;
//
//                            case GOOD: unsteadyState = 2;
//                                break;
//
//                            case INVALID: unsteadyState = 3;
//                                break;
//
//                            case NORMAL: unsteadyState = 4;
//                                break;
//
//                            case TERRIBLE: unsteadyState = 5;
//                                break;
//
//                            case WARNING: unsteadyState = 6;
//                                break;
//                        }
//
//                        int enginepowerState = 0;
//
//                        switch (((EldFuelRecord)dataRec).getStateEnginePower()){
//                            case BAD: enginepowerState = 1;
//                                break;
//
//                            case GOOD: enginepowerState = 2;
//                                break;
//
//                            case INVALID: enginepowerState = 3;
//                                break;
//
//                            case NORMAL: enginepowerState = 4;
//                                break;
//
//                            case TERRIBLE: enginepowerState = 5;
//                                break;
//
//                            case WARNING: enginepowerState = 6;
//                                break;
//                        }
//
//                        int accelState = 0;
//
//                        switch (((EldFuelRecord)dataRec).getStateAccel()){
//                            case BAD: accelState = 1;
//                                break;
//
//                            case GOOD: accelState = 2;
//                                break;
//
//                            case INVALID: accelState = 3;
//                                break;
//
//                            case NORMAL: accelState = 4;
//                                break;
//
//                            case TERRIBLE: accelState = 5;
//                                break;
//
//                            case WARNING: accelState = 6;
//                                break;
//                        }
//
//                        int eco = 0;
//
//                        switch (((EldFuelRecord)dataRec).getStateEco()){
//                            case BAD: eco = 1;
//                                break;
//
//                            case GOOD: eco = 2;
//                                break;
//
//                            case INVALID: eco = 3;
//                                break;
//
//                            case NORMAL: eco = 4;
//                                break;
//
//                            case TERRIBLE: eco = 5;
//                                break;
//
//                            case WARNING: eco = 6;
//                                break;
//                        }
//
//                        int anticipateState = 0;
//
//                        switch (((EldFuelRecord)dataRec).getStateAnticipate()){
//                            case BAD: anticipateState = 1;
//                                break;
//
//                            case GOOD: anticipateState = 2;
//                                break;
//
//                            case INVALID: anticipateState = 3;
//                                break;
//
//                            case NORMAL: anticipateState = 4;
//                                break;
//
//                            case TERRIBLE: anticipateState = 5;
//                                break;
//
//                            case WARNING: anticipateState = 6;
//                                break;
//                        }
//
//                        Call<FuelRecord> call = apiInterface.sendFuel(new FuelRecord(
//                                ((EldFuelRecord) dataRec).getFuelLevelPercent(),
//                                ((EldFuelRecord) dataRec).getFuelIntegratedLiters(),
//                                ((EldFuelRecord) dataRec).getTotalFuelConsumedLiters(),
//                                ((EldFuelRecord) dataRec).getFuelRateLitersPerHours(),
//                                ((EldFuelRecord) dataRec).getIdleFuelConsumedLiters(),
//                                ((EldFuelRecord) dataRec).getIdleTimeHours(),
//                                highrpmState,
//                                unsteadyState,
//                                enginepowerState,
//                                accelState,
//                                eco,
//                                anticipateState
//                        ));
//
//                        call.enqueue(new Callback<FuelRecord>() {
//                            @Override
//                            public void onResponse(@NonNull Call<FuelRecord> call, @NonNull Response<FuelRecord> response) {
//
//                            }
//
//                            @Override
//                            public void onFailure(@NonNull Call<FuelRecord> call, @NonNull Throwable t) {
//                                Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                    } else if (RecordType == EldBroadcastTypes.ELD_DIAGNOSTIC_RECORD) {
//                        diagnosticEnabled = true;
//                    }
                } if (dataRec instanceof EldDataRecord){
                    latitude = ((EldDataRecord) dataRec).getLatitude();
                    longtitude = ((EldDataRecord) dataRec).getLongitude();
                    Toast.makeText(MainActivity.this,((EldDataRecord) dataRec).getFirmwareVersion(),Toast.LENGTH_SHORT).show();

                    ArrayList<Double> arrayList = new ArrayList<>();
                    arrayList.add(((EldDataRecord) dataRec).getLongitude());
                    arrayList.add(((EldDataRecord) dataRec).getLatitude());

                    Boolean b = null;
                    if(((EldDataRecord) dataRec).getEngineState() == EldEngineStates.ENGINE_OFF){
                        b = false;
                    }else if(((EldDataRecord) dataRec).getEngineState() == EldEngineStates.ENGINE_ON){
                        b = true;
                    }
                    try {
                        Call<LiveDataRecord> sendData = apiInterface.sendLive(new LiveDataRecord(
                                b,
                                ((EldDataRecord) dataRec).getVin(),
                                ((EldDataRecord) dataRec).getSpeed(),
                                ((EldDataRecord) dataRec).getOdometer(),
                                ((EldDataRecord) dataRec).getTripDistance(),
                                ((EldDataRecord) dataRec).getEngineHours(),
                                ((EldDataRecord) dataRec).getTripHours(),
                                ((EldDataRecord) dataRec).getVoltage(),
                                (float) 2.22,
                                new Point("Point",arrayList),
                                ((EldDataRecord) dataRec).getGpsSpeed(),
                                ((EldDataRecord) dataRec).getCourse(),
                                ((EldDataRecord) dataRec).getNumSats(),
                                ((EldDataRecord) dataRec).getMslAlt(),
                                ((EldDataRecord) dataRec).getDop(),
                                ((EldDataRecord) dataRec).getSequence(),
                                ((EldDataRecord) dataRec).getFirmwareVersion()

                        ));

                        sendData.enqueue(new Callback<LiveDataRecord>() {
                            @Override
                            public void onResponse(@NonNull Call<LiveDataRecord> call, @NonNull Response<LiveDataRecord> response) {

                            }

                            @Override
                            public void onFailure(@NonNull Call<LiveDataRecord> call, @NonNull Throwable t) {
                                Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });


                    } catch (Exception ignored) {
                    }
                }if (dataRec instanceof EldFuelRecord){
                    subscribedRecords.add(EldBroadcastTypes.ELD_FUEL_RECORD);
                    fuelEnabled = true;
                    int highrpmState = 0;
                    switch (((EldFuelRecord)dataRec).getStateHighRPM()){
                        case BAD: highrpmState = 1;
                            break;

                        case GOOD: highrpmState = 2;
                            break;

                        case INVALID: highrpmState = 3;
                            break;

                        case NORMAL: highrpmState = 4;
                            break;

                        case TERRIBLE: highrpmState = 5;
                            break;

                        case WARNING: highrpmState = 6;
                            break;
                    }

                    int unsteadyState = 0;

                    switch (((EldFuelRecord)dataRec).getStateUnsteady()){
                        case BAD: unsteadyState = 1;
                            break;

                        case GOOD: unsteadyState = 2;
                            break;

                        case INVALID: unsteadyState = 3;
                            break;

                        case NORMAL: unsteadyState = 4;
                            break;

                        case TERRIBLE: unsteadyState = 5;
                            break;

                        case WARNING: unsteadyState = 6;
                            break;
                    }

                    int enginepowerState = 0;

                    switch (((EldFuelRecord)dataRec).getStateEnginePower()){
                        case BAD: enginepowerState = 1;
                            break;

                        case GOOD: enginepowerState = 2;
                            break;

                        case INVALID: enginepowerState = 3;
                            break;

                        case NORMAL: enginepowerState = 4;
                            break;

                        case TERRIBLE: enginepowerState = 5;
                            break;

                        case WARNING: enginepowerState = 6;
                            break;
                    }

                    int accelState = 0;

                    switch (((EldFuelRecord)dataRec).getStateAccel()){
                        case BAD: accelState = 1;
                            break;

                        case GOOD: accelState = 2;
                            break;

                        case INVALID: accelState = 3;
                            break;

                        case NORMAL: accelState = 4;
                            break;

                        case TERRIBLE: accelState = 5;
                            break;

                        case WARNING: accelState = 6;
                            break;
                    }

                    int eco = 0;

                    switch (((EldFuelRecord)dataRec).getStateEco()){
                        case BAD: eco = 1;
                            break;

                        case GOOD: eco = 2;
                            break;

                        case INVALID: eco = 3;
                            break;

                        case NORMAL: eco = 4;
                            break;

                        case TERRIBLE: eco = 5;
                            break;

                        case WARNING: eco = 6;
                            break;
                    }

                    int anticipateState = 0;

                    switch (((EldFuelRecord)dataRec).getStateAnticipate()){
                        case BAD: anticipateState = 1;
                            break;

                        case GOOD: anticipateState = 2;
                            break;

                        case INVALID: anticipateState = 3;
                            break;

                        case NORMAL: anticipateState = 4;
                            break;

                        case TERRIBLE: anticipateState = 5;
                            break;

                        case WARNING: anticipateState = 6;
                            break;
                    }

                    Call<FuelRecord> call = apiInterface.sendFuel(new FuelRecord(
                            ((EldFuelRecord) dataRec).getFuelLevelPercent(),
                            ((EldFuelRecord) dataRec).getFuelIntegratedLiters(),
                            ((EldFuelRecord) dataRec).getTotalFuelConsumedLiters(),
                            ((EldFuelRecord) dataRec).getFuelRateLitersPerHours(),
                            ((EldFuelRecord) dataRec).getIdleFuelConsumedLiters(),
                            ((EldFuelRecord) dataRec).getIdleTimeHours(),
                            highrpmState,
                            unsteadyState,
                            enginepowerState,
                            accelState,
                            eco,
                            anticipateState
                    ));

                    call.enqueue(new Callback<FuelRecord>() {
                        @Override
                        public void onResponse(@NonNull Call<FuelRecord> call, @NonNull Response<FuelRecord> response) {

                        }

                        @Override
                        public void onFailure(@NonNull Call<FuelRecord> call, @NonNull Throwable t) {
                            Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }
    };

    private final EldBleScanCallback bleScanCallback = new EldBleScanCallback() {

        @Override
        public void onScanResult(EldScanObject device) {

            Log.d("BLETEST", "BleScanCallback single");

            final String strDevice;
            if (device != null) {
                strDevice = device.getDeviceId();
                runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("ELD " + strDevice + " found, now connecting...\n", "")));

                EldBleError res = mEldManager.ConnectToEld(bleDataCallback, subscribedRecords, bleConnectionStateChangeCallback);

                if (res != EldBleError.SUCCESS) {
                    runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("Connection Failed\n", "")));
                }

            } else {
                runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("No ELD found\n", "")));
            }
        }

        @Override
        public void onScanResult(ArrayList deviceList) {

            final String strDevice;
            EldScanObject so;

            if (deviceList != null) {
                so = (EldScanObject) deviceList.get(0);
                strDevice = so.getDeviceId();
                MAC = strDevice;
                runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("ELD " + strDevice + " found, now connecting...\n", "")));

                EldBleError res = mEldManager.ConnectToEld(bleDataCallback, subscribedRecords, bleConnectionStateChangeCallback, strDevice);
                if (res != EldBleError.SUCCESS) {
                    runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("Connection Failed\n", "")));
                }

            } else {
                runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("No ELD found\n", "")));
            }
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BT_ENABLE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                EventBus.getDefault().postSticky(new MessageModel("Bluetooth enabled - now scanning for ELD\n", ""));
                mEldManager.ScanForElds(bleScanCallback);
            } else {
                EventBus.getDefault().postSticky(new MessageModel("Unable to enable bluetooth\n", ""));
            }
        }
    }

    /*
   Scan for the ELD using EldBleLib  - if Bluetooth is not enabled it will return a NOT_ENABLED Error
   in which case invoke EnableBluetooth to enable bluetooth and in the custom intent on success
   invoke the scan functions
    */
    private void ScanForEld() {
        if (mEldManager.ScanForElds(bleScanCallback) == EldBleError.BLUETOOTH_NOT_ENABLED)
            mEldManager.EnableBluetooth(REQUEST_BT_ENABLE);
    }

    /**
     * Start service method
     * "data" for sending data
     */
    public void startService() {
        String data = "Service is running...";
        Intent intent = new Intent(this, ForegroundService.class);
        intent.putExtra("data", data);
        startService(intent);

        changeDateTimeBroadcast = new ChangeDateTimeBroadcast() {
            @Override
            public void onDayChanged() {
                time = "" + Calendar.getInstance().getTime();
                today = time.split(" ")[1] + " " + time.split(" ")[2];
                Log.d("data", "data");
                daoViewModel.deleteAllDays();
                for (int i = 14; i >= 0; i--) {
                    String time = Day.getCalculatedDate(-i);
                    try {
                        daoViewModel.insertDay(new DayEntity(Day.getDayTime1(time), Day.getDayName2(time)));
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                setTodayAttr(time, today);
                optimizeViewModels();
                customRulerChart.setArrayList(truckStatusEntities);
                if (logEntities.size() == 0) {
                    statusDaoViewModel.insertStatus(new LogEntity(lastStatusData.getLastStatus(), lastStatusData.getLastStatus(), null, null, null, today, 0));
                }else {
                    statusDaoViewModel.insertStatus(new LogEntity(lastStatusData.getLastStatus(),
                            lastStatusData.getLastStatus(),
                            logEntities.get(logEntities.size()-1).getLocation(),
                            logEntities.get(logEntities.size()-1).getNote(), null, today, 0));
                }
                update();
            }
        };
        this.registerReceiver(changeDateTimeBroadcast, ChangeDateTimeBroadcast.getIntentFilter());
    }

    /**
     * f
     * Stop service method
     */
    public void stopService() {
        Intent intent = new Intent(this, ForegroundService.class);
        stopService(intent);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView addTime = findViewById(R.id.idDefectTimeText);
        addTime.setText(String.format("%s:%s", timeString(hourOfDay), timeString(minute)));
    }

    private String timeString(int digit) {
        String s = "" + digit;
        if (s.length() == 1) s = "0" + s;
        return s;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(changeDateTimeBroadcast);
    }
}