package com.iosix.eldblesample.activity;

import static com.iosix.eldblesample.MyApplication.userData;
import static com.iosix.eldblesample.enums.Day.getCurrentSeconds;
import static java.lang.Thread.sleep;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.iosix.eldblelib.EldBleConnectionStateChangeCallback;
import com.iosix.eldblelib.EldBleDataCallback;
import com.iosix.eldblelib.EldBleError;
import com.iosix.eldblelib.EldBleScanCallback;
import com.iosix.eldblelib.EldBroadcast;
import com.iosix.eldblelib.EldBroadcastTypes;
import com.iosix.eldblelib.EldBufferRecord;
import com.iosix.eldblelib.EldDataRecord;
import com.iosix.eldblelib.EldDtcCallback;
import com.iosix.eldblelib.EldEngineStates;
import com.iosix.eldblelib.EldFirmwareUpdateCallback;
import com.iosix.eldblelib.EldFuelRecord;
import com.iosix.eldblelib.EldManager;
import com.iosix.eldblelib.EldParameterTypes;
import com.iosix.eldblelib.EldScanObject;
import com.iosix.eldblesample.BuildConfig;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.adapters.RecyclerViewLastAdapter;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.broadcasts.ChangeDateTimeBroadcast;
import com.iosix.eldblesample.broadcasts.NetworkConnectionLiveData;
import com.iosix.eldblesample.customViews.CustomLiveRulerChart;
import com.iosix.eldblesample.dialogs.ConnectToEldDialog;
import com.iosix.eldblesample.dialogs.SearchEldDeviceDialog;
import com.iosix.eldblesample.enums.Day;
import com.iosix.eldblesample.enums.EnumsConstants;
import com.iosix.eldblesample.fragments.InspectionModuleFragment;
import com.iosix.eldblesample.models.ApkVersion;
import com.iosix.eldblesample.models.Data;
import com.iosix.eldblesample.models.MessageModel;
import com.iosix.eldblesample.models.Status;
import com.iosix.eldblesample.models.User;
import com.iosix.eldblesample.models.VehicleData;
import com.iosix.eldblesample.models.eld_records.BufferRecord;
import com.iosix.eldblesample.models.eld_records.Eld;
import com.iosix.eldblesample.models.eld_records.FuelRecord;
import com.iosix.eldblesample.models.eld_records.LiveDataRecord;
import com.iosix.eldblesample.models.eld_records.Point;
import com.iosix.eldblesample.retrofit.APIInterface;
import com.iosix.eldblesample.retrofit.ApiClient;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.DvirEntity;
import com.iosix.eldblesample.roomDatabase.entities.LiveDataEntitiy;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;
import com.iosix.eldblesample.roomDatabase.entities.VehiclesEntity;
import com.iosix.eldblesample.services.foreground.ForegroundService;
import com.iosix.eldblesample.shared_prefs.DriverSharedPrefs;
import com.iosix.eldblesample.shared_prefs.LastStatusData;
import com.iosix.eldblesample.shared_prefs.SessionManager;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.StatusDaoViewModel;
import com.iosix.eldblesample.viewModel.UserViewModel;
import com.iosix.eldblesample.viewModel.apiViewModel.EldJsonViewModel;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public String today = time.split(" ")[1] + " " + time.split(" ")[2];
    private ArrayList<LogEntity> truckStatusEntities;
    private ArrayList<LogEntity> logEntities;
    private APIInterface apiInterface;
    private TextView idUsername;
    private TextView statusOff;
    private TextView statusSB;
    private TextView statusDR;
    private TextView statusON;
    private ImageView imageOff;
    private ImageView imageSB;
    private ImageView imageDR;
    private ImageView imageON;
    private ChangeDateTimeBroadcast changeDateTimeBroadcast;
    private CustomLiveRulerChart customRulerChart;
    private LastStatusData lastStatusData;
    private ArrayList<VehiclesEntity> vehiclesEntities;
    private ArrayList<User> users;
    private DriverSharedPrefs driverSharedPrefs;
    private NetworkConnectionLiveData networkConnectionLiveData;


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
    private EldJsonViewModel eldJsonViewModel;
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
        outState.clear();
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void initView() {
        super.initView();
        lastStatusData = LastStatusData.getInstance(getApplicationContext());
        logEntities = new ArrayList<>();

        userViewModel = new UserViewModel(this.getApplication());
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        eldJsonViewModel = new EldJsonViewModel(getApplication());
        eldJsonViewModel = ViewModelProviders.of(this).get(EldJsonViewModel.class);

        networkConnectionLiveData = new NetworkConnectionLiveData(getApplicationContext());
        setLocale(userData.getLang());

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        setContentView(R.layout.activity_main);

        optimizeViewModels();

        drawerLayout = findViewById(R.id.drawer_layout);
        RecyclerView last_recycler_view = findViewById(R.id.idRecyclerView);
        customRulerChart = findViewById(R.id.idCustomLiveChart);
        customRulerChart.setArrayList(truckStatusEntities);

        daoViewModel.getMgetAllDays().observe(this, dayEntities -> {

            lastAdapter = new RecyclerViewLastAdapter(this, daoViewModel, statusDaoViewModel, dvirViewModel);
            last_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
            last_recycler_view.setAdapter(lastAdapter);
            lastAdapter.notifyDataSetChanged();

            lastAdapterClicked();
        });

        // Set the toolbar
        activity_main_toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(activity_main_toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //Required to allow bluetooth scanning
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);

        mEldManager = EldManager.GetEldManager(this, "123456789A");

        vehiclesEntities = new ArrayList<>();
        driverSharedPrefs = DriverSharedPrefs.getInstance(getApplicationContext());

        if (getIntent().getIntExtra("JSON", 0) == 1) {
            getUSerInfo();
            sendApkVersion();
        }

        onClickCustomBtn();
        onClickVisiblityCanAndSaveBtn();

        getDrawerToggleEvent();
        getDrawerTouchEvent();

        clickLGDDButtons();

        startService();

        update();

        getAllDrivers();
        sendLocalData();
    }

    private void sendApkVersion() {
        apiInterface.sendApkVersion(new ApkVersion(BuildConfig.VERSION_NAME, Build.VERSION.RELEASE)).enqueue(new Callback<ApkVersion>() {
            @Override
            public void onResponse(Call<ApkVersion> call, Response<ApkVersion> response) {
            }

            @Override
            public void onFailure(Call<ApkVersion> call, Throwable t) {

            }
        });
    }

    private void update() {

        new Thread() {
            @SuppressLint("DefaultLocale")
            @SuppressWarnings("BusyWait")
            public void run() {
                while (true) {
                    runOnUiThread(() -> {
                        TextView statusTime = findViewById(R.id.idStatusTime);
                        TextView status = findViewById(R.id.idStatusText);
                        if (lastStatusData.getLastStatus() == EnumsConstants.STATUS_OFF) {
                            status.setText(R.string.off);
                        }
                        if (lastStatusData.getLastStatus() == EnumsConstants.STATUS_SB) {
                            status.setText(R.string.sb);
                        }
                        if (lastStatusData.getLastStatus() == EnumsConstants.STATUS_DR) {
                            status.setText(R.string.dr);
                        }
                        if (lastStatusData.getLastStatus() == EnumsConstants.STATUS_ON) {
                            status.setText(R.string.on);
                        }
                        int last = lastStatusData.getLasStatSecond();
                        int current = getCurrentSeconds();
                        int hour = (current - last) / 3600;
                        int min = ((current - last) % 3600) / 60;
                        statusTime.setText(String.format("%02dh %02dm", hour, min));
                    });
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sendLocalData() {
        networkConnectionLiveData.observe(this, isConnected -> {
            if (isConnected) {
                userViewModel.getMgetLocalDatas().observe(this, liveDataRecords -> {
                    ArrayList<Boolean> engine_state = new ArrayList<>();
                    ArrayList<String> vin = new ArrayList<>();
                    ArrayList<Double> speed = new ArrayList<>();
                    ArrayList<Double> odometer = new ArrayList<>();
                    ArrayList<Double> trip_distance = new ArrayList<>();
                    ArrayList<Double> engine_hours = new ArrayList<>();
                    ArrayList<Double> trip_hours = new ArrayList<>();
                    ArrayList<Double> battery_voltage = new ArrayList<>();
                    ArrayList<String> date = new ArrayList<>();
                    ArrayList<Point> point = new ArrayList<>();
                    ArrayList<Integer> gps_speed = new ArrayList<>();
                    ArrayList<Integer> course = new ArrayList<>();
                    ArrayList<Integer> number_of_satellites = new ArrayList<>();
                    ArrayList<Integer> altitude = new ArrayList<>();
                    ArrayList<Double> dop = new ArrayList<>();
                    ArrayList<Integer> sequence_number = new ArrayList<>();
                    ArrayList<String> firmware_version = new ArrayList<>();

                    for (int i = 0; i < liveDataRecords.size(); i++) {
                        engine_state.add(liveDataRecords.get(i).getEngine_state());
                        vin.add(liveDataRecords.get(i).getVin());
                        speed.add(liveDataRecords.get(i).getSpeed());
                        odometer.add(liveDataRecords.get(i).getOdometer());
                        trip_distance.add(liveDataRecords.get(i).getTrip_distance());
                        engine_hours.add(liveDataRecords.get(i).getEngine_hours());
                        trip_hours.add(liveDataRecords.get(i).getTrip_hours());
                        battery_voltage.add(liveDataRecords.get(i).getBattery_voltage());
                        point.add(liveDataRecords.get(i).getPoint());
                        date.add(liveDataRecords.get(i).getDate());
                        gps_speed.add(liveDataRecords.get(i).getGps_speed());
                        course.add(liveDataRecords.get(i).getCourse());
                        number_of_satellites.add(liveDataRecords.get(i).getNumber_of_satellites());
                        altitude.add(liveDataRecords.get(i).getAltitude());
                        dop.add(liveDataRecords.get(i).getDop());
                        sequence_number.add(liveDataRecords.get(i).getSequence_number());
                        firmware_version.add(liveDataRecords.get(i).getFirmware_version());
                    }

                    if (engine_state.size() > 0) {
                        apiInterface.sendLocalData(new LiveDataEntitiy(
                                engine_state,
                                vin,
                                speed,
                                odometer,
                                trip_distance,
                                engine_hours,
                                trip_hours,
                                battery_voltage,
                                date,
                                point,
                                gps_speed,
                                course,
                                number_of_satellites,
                                altitude,
                                dop,
                                sequence_number,
                                firmware_version
                        )).enqueue(new Callback<LiveDataEntitiy>() {
                            @Override
                            public void onResponse(Call<LiveDataEntitiy> call, Response<LiveDataEntitiy> response) {
                                if (response.isSuccessful()) {
                                    userViewModel.deleteLocalData();
                                }
                            }

                            @Override
                            public void onFailure(Call<LiveDataEntitiy> call, Throwable t) {
                            }
                        });
                    }
                });
            }
        });
    }

    private void getUSerInfo() {


        userViewModel.deleteUser();
        apiInterface.getUser().enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    assert user != null;
                    driverSharedPrefs.saveLastUsername(user.getName());
                    driverSharedPrefs.saveLastSurname(user.getLastName());
                    driverSharedPrefs.saveLastImage(user.getImage());
                    driverSharedPrefs.saveLastHomeTerAdd(user.getHomeTerminalAddress());
                    driverSharedPrefs.saveLastHomeTerTimeZone(user.getTimeZone());
                    driverSharedPrefs.saveLastMainOffice(user.getMainOffice());
                    driverSharedPrefs.saveLastPhoneNumber(user.getPhone());
                    driverSharedPrefs.saveCompany(user.getCompany());
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
            }
        });

    }

    private void getAllDrivers() {
        userViewModel.deleteUser();
        apiInterface.getAllDrivers().enqueue(new Callback<Data>() {
            @Override
            public void onResponse(@NonNull Call<Data> call, @NonNull Response<Data> response) {
                if (response.isSuccessful()) {
                    Data users = response.body();
                    assert users != null;
                    if (MainActivity.this.users.size() == 0) {
                        for (int i = 0; i < users.getDriver().getUser().size(); i++) {
                            userViewModel.insertUser(users.getDriver().getUser().get(i));
                        }
                    } else {
                        for (int i = 0; i < users.getDriver().getUser().size(); i++) {
                            for (int j = 0; j < MainActivity.this.users.size(); j++) {
                                if (!users.getDriver().getUser().get(i).getPhone().equals(MainActivity.this.users.get(j).getPhone())) {
                                    MainActivity.this.users.add(users.getDriver().getUser().get(i));
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Data> call, @NonNull Throwable t) {
            }
        });
        apiInterface.getAllVehicles().enqueue(new Callback<VehicleData>() {
            @Override
            public void onResponse(@NonNull Call<VehicleData> call, @NonNull Response<VehicleData> response) {
                if (response.isSuccessful()) {
                    VehicleData vehicleData = response.body();
                    assert vehicleData != null;
                    if (vehiclesEntities.size() != 0) {
                        int count = 0;
                        for (int i = 0; i < vehicleData.getVehicle().getVehicleList().size(); i++) {
                            for (int j = 0; j < vehiclesEntities.size(); j++) {
                                if (vehicleData.getVehicle().getVehicleList().get(i).getVehicle_id().equals(vehiclesEntities.get(j).getName())) {
                                    count++;
                                }
                            }
                            if (count == 0) {
                                daoViewModel.insertVehicle(new VehiclesEntity(vehicleData.getVehicle().getVehicleList().get(i).getVehicle_id()));
                            } else {
                                count = 0;
                            }
                        }
                    } else {
                        for (int i = 0; i < vehicleData.getVehicle().getVehicleList().size(); i++) {
                            daoViewModel.insertVehicle(new VehiclesEntity(vehicleData.getVehicle().getVehicleList().get(i).getVehicle_id()));
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<VehicleData> call, @NonNull Throwable t) {
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
            public void onclickDvir(String s, ArrayList<DvirEntity> dvirEntities) {

                if (dvirEntities.size() != 0) {
                    Intent intent = new Intent(MainActivity.this, LGDDActivity.class);
                    intent.putExtra("position", 3);
                    intent.putExtra("currDay", s);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, AddDvirActivity.class);
                    intent.putExtra("currDay", s);
                    startActivity(intent);
                }
            }
        });

    }

    private void clickLGDDButtons() {
        TextView inspectionMode, settings, hosRules;
        Button log, general, doc, dvir;
        log = findViewById(R.id.idTableBtnLog);
        general = findViewById(R.id.idTableBtnGeneral);
        doc = findViewById(R.id.idTableBtnDocs);
        dvir = findViewById(R.id.idTableBtnDVIR);
        inspectionMode = findViewById(R.id.idSpinnerInspection);
        settings = findViewById(R.id.idSettings);
        hosRules = findViewById(R.id.idHOSRules);
        TextView idLogout = findViewById(R.id.idLogout);
        idUsername = findViewById(R.id.idUsername);
        CircleImageView idAvatar = findViewById(R.id.idAvatar);

        Handler handler = new Handler();
        handler.postDelayed(() -> idUsername.setText(String.format("%s %s", driverSharedPrefs.getFirstname(), driverSharedPrefs.getLastname())), 1000);

        settings.setOnClickListener(view -> {
            Intent intent = new Intent(this, SettingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        hosRules.setOnClickListener(view -> {
            Intent intent = new Intent(this, RulesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        idLogout.setOnClickListener(v -> {
            SessionManager sessionManager = SessionManager.getInstance(getApplicationContext());

            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.custom_delete_dvir_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            TextView header = dialog.findViewById(R.id.idDvirHeader);
            TextView title = dialog.findViewById(R.id.idDvirTitle);
            header.setText(R.string.log_out);
            title.setText(R.string.do_you_log_out);

            dialog.findViewById(R.id.idDvirYes).setOnClickListener(view -> {
                sessionManager.clearAccessToken();
                sessionManager.clearToken();
                Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                dialog.dismiss();
            });

            dialog.findViewById(R.id.idDvirNo).setOnClickListener(view -> dialog.dismiss());

            dialog.show();
        });

        dvirViewModel = new DvirViewModel(this.getApplication());
        dvirViewModel = ViewModelProviders.of(this).get(DvirViewModel.class);
        dvirViewModel.getMgetDvirs().observe(this, dvirEntities -> {

            boolean hasDvir = false;

            if (dvirEntities.size() == 0) {

                dvir.setOnClickListener(v -> {
                    Intent intent = new Intent(this, LGDDActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("position", 3);
                    intent.putExtra("currDay", today);
                    dvirViewModel.getCurrentName().postValue(today);
                    startActivity(intent);
                    finish();
                        }
                );

            } else {

                for (int i = 0; i < dvirEntities.size(); i++) {
                    if (dvirEntities.get(i).getDay().equals(today)) {

                        hasDvir = true;

                        String c = dvirEntities.get(i).getDay();

                        dvir.setOnClickListener(v -> {
                            Intent intent = new Intent(this, LGDDActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("position", 3);
                            intent.putExtra("currDay", c);
                            dvirViewModel.getCurrentName().postValue(c);
                            startActivity(intent);
                            finish();
                                }
                        );
                    }
                }
                if (!hasDvir) {
                    dvir.setOnClickListener(v -> {
                        Intent intent = new Intent(this, LGDDActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("position", 3);
                        intent.putExtra("currDay", today);
                        dvirViewModel.getCurrentName().postValue(today);
                        startActivity(intent);
                        finish();
                            }
                    );
                }
            }
        });

        log.setOnClickListener(v -> {
            Intent intent = new Intent(this, LGDDActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("position", 0);
            intent.putExtra("currDay", today);
            dvirViewModel.getCurrentName().postValue(today);
            startActivity(intent);
            finish();
                }
        );


        general.setOnClickListener(v -> {
            Intent intent = new Intent(this, LGDDActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("position", 1);
            intent.putExtra("currDay", today);
            dvirViewModel.getCurrentName().postValue(today);
            startActivity(intent);
            finish();
                }
        );

        doc.setOnClickListener(v -> {
            Intent intent = new Intent(this, LGDDActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("currDay", today);
            intent.putExtra("position", 2);
            dvirViewModel.getCurrentName().postValue(today);
            startActivity(intent);
            finish();
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
        dvirViewModel = new DvirViewModel(this.getApplication());
        dvirViewModel = ViewModelProviders.of(this).get(DvirViewModel.class);

        apiInterface = ApiClient.getClient().create(APIInterface.class);
        truckStatusEntities = new ArrayList<>();
        users = new ArrayList<>();

        last_status = lastStatusData.getLastStatus();

        daoViewModel.getGetAllVehicles().observe(this, vehiclesEntities -> this.vehiclesEntities.addAll(vehiclesEntities));

        userViewModel.getMgetDrivers().observe(this, drivers -> users.addAll(drivers));

        statusDaoViewModel = ViewModelProviders.of(this).get(StatusDaoViewModel.class);
        daoViewModel.getmAllStatus().observe(this, truckStatusEntities -> {

            for (int i = 0; i < truckStatusEntities.size(); i++) {
                if (truckStatusEntities.get(i).getTime().equalsIgnoreCase(today)) {
                    MainActivity.this.truckStatusEntities.add(truckStatusEntities.get(i));
                }
                logEntities.add(truckStatusEntities.get(i));
            }
        });

        daoViewModel = ViewModelProviders.of(this).get(DayDaoViewModel.class);
        daoViewModel.getMgetAllDays().observe(this, dayEntities -> {
        });

    }

    @SuppressLint("SetTextI18n")
    private void setTodayAttr(String time, String today) {
        TextView day = findViewById(R.id.idTableDay);
        TextView month = findViewById(R.id.idTableMonth);
        day.setText(time.split(" ")[0]);
        month.setText(today);
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

    public void restartActivity() {
        finish();
        getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(getIntent());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
            dvirViewModel.getIsConnectedtoEld().observe(this, isConnected -> {
                if (!isConnected) {
                    FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Log.d("Adverse Diving","Eld is not Connected");
                            if (location != null){
                                Log.d("Adverse Diving",location.getLatitude()+location.getLongitude()+" " + location.toString());
                                Log.d("Adverse Diving","Eld is not Connected");
                            }
                        }
                    });
                }
            });
//            else {
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//                alertDialog.setTitle("Location not found")
//                        .setMessage("Check Eld connection!")
//                        .setPositiveButton("OK", (dialog, which) -> alertDialog.setCancelable(true));
//                AlertDialog alert = alertDialog.create();
//                alert.show();
//            }

    });

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

//                if (editLocation.getText().toString().equals("")){
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//                    alertDialog.setTitle("No location created")
//                            .setMessage("Create current location!")
//                            .setPositiveButton("OK", (dialog, which) -> alertDialog.setCancelable(true));
//                    AlertDialog alert = alertDialog.create();
//                    alert.show();
//                }
//                else
//                    if (note.getText().toString().equals("")){
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//                    alertDialog.setTitle("No note created")
//                            .setMessage("Create note!")
//                            .setPositiveButton("OK", (dialog, which) -> alertDialog.setCancelable(true));
//                    AlertDialog alert = alertDialog.create();
//                    alert.show();
//                }
//                else{
//                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
//                    try {
//                        List<Address> geoResults = geocoder.getFromLocationName(editLocation.getText().toString(), 1);
//                        geoResults.size();
//                        Address addr = geoResults.get(0);
//                        latitude = addr.getLatitude();
//                        longtitude = addr.getLongitude();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    ArrayList<Double> arrayList = new ArrayList<>();
                    arrayList.add(latitude);
                    arrayList.add(longtitude);
                    if (current_status == EnumsConstants.STATUS_OFF){
                        Call<Status> call = apiInterface.postStatus(new Status("OFF",new Point("Point",arrayList)
                                ,note.getText().toString(),new Date().toInstant().toString()));

                        call.enqueue(new Callback<Status>() {
                            @Override
                            public void onResponse(Call<Status> call, Response<Status> response) {
                                if (response.isSuccessful()){
                                }
                            }

                            @Override
                            public void onFailure(Call<Status> call, Throwable t) {

                            }
                        });
                    }else if (current_status == EnumsConstants.STATUS_SB){
                        Call<Status> call = apiInterface.postStatus(new Status("SB",new Point("Point",arrayList)
                                ,note.getText().toString(),new Date().toInstant().toString()));
                        call.enqueue(new Callback<Status>() {
                            @Override
                            public void onResponse(Call<Status> call, Response<Status> response) {
                            }

                            @Override
                            public void onFailure(Call<Status> call, Throwable t) {

                            }
                        });
                    }else if (current_status == EnumsConstants.STATUS_DR){
                            Call<Status> call = apiInterface.postStatus(new Status("D",new Point("Point",arrayList)
                                    ,note.getText().toString(),new Date().toInstant().toString()));
                            call.enqueue(new Callback<Status>() {
                                @Override
                                public void onResponse(Call<Status> call, Response<Status> response) {
                                }

                                @Override
                                public void onFailure(Call<Status> call, Throwable t) {

                                }
                            });

                    }else if (current_status == EnumsConstants.STATUS_ON){
                        Call<Status> call = apiInterface.postStatus(new Status("ON",new Point("Point",arrayList)
                                ,note.getText().toString(),new Date().toInstant().toString()));
                        call.enqueue(new Callback<Status>() {
                            @Override
                            public void onResponse(Call<Status> call, Response<Status> response) {
                                if (response.isSuccessful()){
                                }
                            }

                            @Override
                            public void onFailure(Call<Status> call, Throwable t) {
                            }
                        });
                    }
                statusDaoViewModel.insertStatus(new LogEntity(last_status, current_status, editLocation.getText().toString(), note.getText().toString(), null, today, getCurrentSeconds()));
                lastStatusData.saveLasStatus(current_status,getCurrentSeconds());
                restartActivity();
//                }
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

        dvirViewModel.getIsConnectedtoEld().observe(this,isConnected ->{
            if (isConnected){
                dvirViewModel.getTruckSpeed().observe(MainActivity.this,speed ->{
                    ArrayList<Double> arrayList = new ArrayList<>();
                    arrayList.add(latitude);
                    arrayList.add(longtitude);
                    if (speed){
                    }else {
                        if (last_status == EnumsConstants.STATUS_DR) {
                            final int[] count = {60};
                            Dialog dialog = new Dialog(this);
                            dialog.setContentView(R.layout.custom_confirmation_layout);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            TextView textView = dialog.findViewById(R.id.idConfirmationTitle);
                            Button stay = dialog.findViewById(R.id.idDialogStayDriving);
                            Button go = dialog.findViewById(R.id.idDialogChangeStatus);
                            Timer t = new Timer();
                            t.scheduleAtFixedRate(new TimerTask() {
                                @Override
                                public void run() {
                                    count[0]--;
                                    runOnUiThread(()->{
                                        if (count[0] > 0){
                                            textView.setText(count[0] + " sec");
                                        }else {
                                            current_status = EnumsConstants.STATUS_ON;
                                            Call<Status> call = apiInterface.postStatus(new Status("ON",new Point("Point",arrayList)
                                                    ,note.getText().toString(),new Date().toInstant().toString()));

                                            call.enqueue(new Callback<Status>() {
                                                @Override
                                                public void onResponse(Call<Status> call, Response<Status> response) {
                                                    if (response.isSuccessful()){
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Status> call, Throwable t) {

                                                }
                                            });
                                            statusDaoViewModel.insertStatus(new LogEntity(last_status, current_status, editLocation.getText().toString(), note.getText().toString(), null, today, getCurrentSeconds()));
                                            lastStatusData.saveLasStatus(current_status,getCurrentSeconds());
                                            restartActivity();
                                            t.cancel();
                                            dialog.dismiss();
                                        }
                                    });
                                }
                            },1000,1000);

                            stay.setOnClickListener(view -> {
                                dialog.dismiss();
                                t.cancel();
                            });

                            go.setOnClickListener(view -> {
                                dialog.dismiss();
                                t.cancel();
                                current_status = EnumsConstants.STATUS_OFF;
                                Call<Status> call = apiInterface.postStatus(new Status("OFF",new Point("Point",arrayList)
                                        ,note.getText().toString(),new Date().toInstant().toString()));

                                call.enqueue(new Callback<Status>() {
                                    @Override
                                    public void onResponse(Call<Status> call, Response<Status> response) {
                                        if (response.isSuccessful()){
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Status> call, Throwable t) {

                                    }
                                });
                                statusDaoViewModel.insertStatus(new LogEntity(last_status, current_status, editLocation.getText().toString(), note.getText().toString(), null, today, getCurrentSeconds()));
                                lastStatusData.saveLasStatus(current_status,getCurrentSeconds());
                                restartActivity();
                            });
                            dialog.show();
                        }
                    }
                });
            }
        });
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

        final SearchEldDeviceDialog searchEldDeviceDialog = new SearchEldDeviceDialog(MainActivity.this);
        searchEldDeviceDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dvirViewModel.getIsConnectedtoEld().observe(this,isConnectedToEld -> {
            final ConnectToEldDialog dialog = new ConnectToEldDialog(this,isConnectedToEld);
            dialog.setCancelable(false);

            if (isConnectedToEld){
                dialog.setAlerrtDialogItemClickInterface(() ->{
                    dialog.cancel();
                    mEldManager.DisconnectEld();
                    searchEldDeviceDialog.show();
                });
                searchEldDeviceDialog.dismiss();
            }else {
                dialog.setAlerrtDialogItemClickInterface(() -> {
                    dialog.cancel();
                    ScanForEld();
                    searchEldDeviceDialog.show();
                });
                searchEldDeviceDialog.dismiss();
            }

            dialog.show();
        });

    }

    private final EldDtcCallback dtcCallback = new EldDtcCallback() {
        @Override
        public void onDtcDetected(final String status, final String jsonString) {
            runOnUiThread(() -> {
                EventBus.getDefault().postSticky(new MessageModel(status, "EldDtcCallback"));
            });
        }
    };

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
                });
            } else {
                runOnUiThread(() -> {
//                        mStatusView.append("Request Debug Succeeded\n");
                    EventBus.getDefault().postSticky(new MessageModel("Request Debug Succeeded\n", ""));
//                        EventBus.getDefault().postSticky(new MessageModel("Request Debug Succeeded5", "5"));

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
                                    } else if (downloadInput.getText().toString().equalsIgnoreCase("FLASHERROR")) {
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
                if (err == EldBleError.RECORD_OUT_OF_RANGE) {
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
            });
        }
    };

    private final EldBleDataCallback bleDataCallback = new EldBleDataCallback() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void OnDataRecord(final EldBroadcast dataRec, final EldBroadcastTypes RecordType) {

            subscribedRecords.add(EldBroadcastTypes.ELD_DATA_RECORD);
            subscribedRecords.add(EldBroadcastTypes.ELD_BUFFER_RECORD);
            subscribedRecords.add(EldBroadcastTypes.ELD_ENGINE_PARAMETERS_RECORD);
            subscribedRecords.add(EldBroadcastTypes.ELD_FUEL_RECORD);
 
            mEldManager.EnableFuelData();
            mEldManager.UpdateSubscribedRecordTypes(subscribedRecords);

            runOnUiThread(() -> {

                EventBus.getDefault().postSticky(new MessageModel("", dataRec.getBroadcastString().trim() + "\n"));

                networkConnectionLiveData.observe(MainActivity.this,isConnected ->{
                    if (isConnected){
                        Toast.makeText(MainActivity.this,"Connection state is" + isConnected + "\n" + dataRec.getBroadcastString(),Toast.LENGTH_LONG).show();
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
                        }else if (dataRec instanceof EldDataRecord){
                            latitude = ((EldDataRecord) dataRec).getLatitude();
                            longtitude = ((EldDataRecord) dataRec).getLongitude();

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
                                Call<LiveDataRecord> sendData = null;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    sendData = apiInterface.sendLive(new LiveDataRecord(
                                            b,
                                            ((EldDataRecord) dataRec).getVin(),
                                            ((EldDataRecord) dataRec).getSpeed(),
                                            ((EldDataRecord) dataRec).getOdometer(),
                                            ((EldDataRecord) dataRec).getTripDistance(),
                                            ((EldDataRecord) dataRec).getEngineHours(),
                                            ((EldDataRecord) dataRec).getTripHours(),
                                            ((EldDataRecord) dataRec).getVoltage(),
                                            ((EldDataRecord) dataRec).getGpsDateTime().toInstant().toString(),
                                            new Point("Point",arrayList),
                                            ((EldDataRecord) dataRec).getGpsSpeed(),
                                            ((EldDataRecord) dataRec).getCourse(),
                                            ((EldDataRecord) dataRec).getNumSats(),
                                            ((EldDataRecord) dataRec).getMslAlt(),
                                            ((EldDataRecord) dataRec).getDop(),
                                            ((EldDataRecord) dataRec).getSequence(),
                                            ((EldDataRecord) dataRec).getFirmwareVersion()

                                    ));
                                }

                                sendData.enqueue(new Callback<LiveDataRecord>() {
                                    @Override
                                    public void onResponse(@NonNull Call<LiveDataRecord> call, @NonNull Response<LiveDataRecord> response) {
                                        if (response.isSuccessful()){
                                            Toast.makeText(MainActivity.this,"truck speed:" + ((EldDataRecord) dataRec).getSpeed(),Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<LiveDataRecord> call, @NonNull Throwable t) {
                                        Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });


                            } catch (Exception ignored) {
                            }
                        }else
                            if (RecordType == EldBroadcastTypes.ELD_FUEL_RECORD){
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
                                    Toast.makeText(MainActivity.this,"response is not successful for fuel record",Toast.LENGTH_LONG).show();

                                }

                                @Override
                                public void onFailure(@NonNull Call<FuelRecord> call, @NonNull Throwable t) {
                                    Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }else {
                        Toast.makeText(MainActivity.this,"Connection state is" + isConnected + "\n" + dataRec.getBroadcastString(),Toast.LENGTH_LONG).show();
                        if (dataRec instanceof EldDataRecord) {
                            latitude = ((EldDataRecord) dataRec).getLatitude();
                            longtitude = ((EldDataRecord) dataRec).getLongitude();

                            ArrayList<Double> arrayList = new ArrayList<>();
                            arrayList.add(((EldDataRecord) dataRec).getLongitude());
                            arrayList.add(((EldDataRecord) dataRec).getLatitude());

                            Boolean b = null;
                            if (((EldDataRecord) dataRec).getEngineState() == EldEngineStates.ENGINE_OFF) {
                                b = false;
                            } else if (((EldDataRecord) dataRec).getEngineState() == EldEngineStates.ENGINE_ON) {
                                b = true;
                            }

                            userViewModel.insertLocalData(new LiveDataRecord(
                                    b,
                                    ((EldDataRecord) dataRec).getVin(),
                                    ((EldDataRecord) dataRec).getSpeed(),
                                    ((EldDataRecord) dataRec).getOdometer(),
                                    ((EldDataRecord) dataRec).getTripDistance(),
                                    ((EldDataRecord) dataRec).getEngineHours(),
                                    ((EldDataRecord) dataRec).getTripHours(),
                                    ((EldDataRecord) dataRec).getVoltage(),
                                    ((EldDataRecord) dataRec).getGpsDateTime().toInstant().toString(),
                                    new Point("Point",arrayList),
                                    ((EldDataRecord) dataRec).getGpsSpeed(),
                                    ((EldDataRecord) dataRec).getCourse(),
                                    ((EldDataRecord) dataRec).getNumSats(),
                                    ((EldDataRecord) dataRec).getMslAlt(),
                                    ((EldDataRecord) dataRec).getDop(),
                                    ((EldDataRecord) dataRec).getSequence(),
                                    ((EldDataRecord) dataRec).getFirmwareVersion()
                            ));
                        }
                    }
                });
            });
            try {
                sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    private final EldBleScanCallback bleScanCallback = new EldBleScanCallback() {

        @Override
        public void onScanResult(EldScanObject device) {

            Log.d("BLETEST", "BleScanCallback single");

            final String strDevice;
            if (device != null) {
                strDevice = device.getDeviceId();
                apiInterface.sendEldNum(new Eld(strDevice,mEldManager.GetApiVersion())).enqueue(new Callback<Eld>() {
                    @Override
                    public void onResponse(Call<Eld> call, Response<Eld> response) {

                    }

                    @Override
                    public void onFailure(Call<Eld> call, Throwable t) {

                    }
                });
                runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("ELD " + strDevice + " found, now connecting...\n", "")));

                EldBleError res = mEldManager.ConnectToEld(bleDataCallback, subscribedRecords, bleConnectionStateChangeCallback);

                if (res != EldBleError.SUCCESS) {
                    dvirViewModel.getIsConnectedtoEld().postValue(false);

                    runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("Connection Failed\n", "")));
                }else {
                    dvirViewModel.getIsConnectedtoEld().postValue(true);
                    runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("Conncected to Eld\n", "")));
                }

            } else {
                dvirViewModel.getIsConnectedtoEld().postValue(false);
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
                apiInterface.sendEldNum(new Eld(MAC,mEldManager.GetApiVersion())).enqueue(new Callback<Eld>() {
                    @Override
                    public void onResponse(Call<Eld> call, Response<Eld> response) {

                    }

                    @Override
                    public void onFailure(Call<Eld> call, Throwable t) {

                    }
                });
                runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("ELD " + strDevice + " found, now connecting...\n", "")));

                EldBleError res = mEldManager.ConnectToEld(bleDataCallback, subscribedRecords, bleConnectionStateChangeCallback, strDevice);
                if (res != EldBleError.SUCCESS) {
                    dvirViewModel.getIsConnectedtoEld().postValue(false);
                    runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("Connection Failed\n", "")));
                } else {
                    dvirViewModel.getIsConnectedtoEld().postValue(true);
                    runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("Conncected to Eld\n", "")));
                }

            } else {
                dvirViewModel.getIsConnectedtoEld().postValue(false);
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

                Log.d("Adverse Diving","Day is changed");
                Toast.makeText(getApplicationContext(),"Day is Changed",Toast.LENGTH_SHORT).show();
                daoViewModel.deleteAllDays();
                for (int i = 7; i >= 0; i--) {
                    String time = Day.getCalculatedDate(-i);
                    try {
                        daoViewModel.insertDay(new DayEntity(Day.getDayTime1(time), Day.getDayName2(time)));
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (truckStatusEntities.size() == 0){
                    if (logEntities.size() != 0){
                        try {
                            daoViewModel.insertStatus(new LogEntity(logEntities.get(logEntities.size()-1).getTo_status(),logEntities.get(logEntities.size()-1).getTo_status(),
                                    logEntities.get(logEntities.size()-1).getLocation(),logEntities.get(logEntities.size()-1).getNote(),null,today,0));
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else {
                        try {
                            daoViewModel.insertStatus(new LogEntity(EnumsConstants.STATUS_OFF,EnumsConstants.STATUS_OFF,
                                    null,null,null,today,0));
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                setTodayAttr(time, today);
                optimizeViewModels();
                restartActivity();
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