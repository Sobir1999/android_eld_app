package com.iosix.eldblesample.activity;

import static androidx.lifecycle.ProcessLifecycleOwner.get;
import static com.iosix.eldblesample.MyApplication.userData;
import static com.iosix.eldblesample.enums.Day.getCurrentSeconds;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import com.iosix.eldblelib.EldBleConnectionStateChangeCallback;
import com.iosix.eldblelib.EldBleDataCallback;
import com.iosix.eldblelib.EldBleError;
import com.iosix.eldblelib.EldBleScanCallback;
import com.iosix.eldblelib.EldBroadcast;
import com.iosix.eldblelib.EldBroadcastTypes;
import com.iosix.eldblelib.EldCachedMotionStartRecord;
import com.iosix.eldblelib.EldCachedMotionStopRecord;
import com.iosix.eldblelib.EldDataRecord;
import com.iosix.eldblelib.EldEngineStates;
import com.iosix.eldblelib.EldManager;
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
import com.iosix.eldblesample.enums.GPSTracker;
import com.iosix.eldblesample.fragments.InspectionModuleFragment;
import com.iosix.eldblesample.models.ApkVersion;
import com.iosix.eldblesample.models.MessageModel;
import com.iosix.eldblesample.models.Status;
import com.iosix.eldblesample.models.User;
import com.iosix.eldblesample.models.eld_records.Eld;
import com.iosix.eldblesample.models.eld_records.LiveDataRecord;
import com.iosix.eldblesample.models.eld_records.Point;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.DvirEntity;
import com.iosix.eldblesample.roomDatabase.entities.LiveDataEntitiy;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;
import com.iosix.eldblesample.roomDatabase.entities.VehiclesEntity;
import com.iosix.eldblesample.services.foreground.ForegroundService;
import com.iosix.eldblesample.shared_prefs.DriverSharedPrefs;
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
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends BaseActivity implements TimePickerDialog.OnTimeSetListener {

    private DrawerLayout drawerLayout;
    private Toolbar activity_main_toolbar;
    private CardView off, sb, dr, on;
    private LinearLayout visiblityViewCons;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch nightModeSwitch;
    private int current_status = EnumsConstants.STATUS_OFF;
    private int last_status;
    private int getLastTime;
    private String eldConnectionState = "not Connected";
    private double speed = 0.0;
    private String time = "" + Calendar.getInstance().getTime();
    public String today = time.split(" ")[1] + " " + time.split(" ")[2];
    private ArrayList<LogEntity> truckStatusEntities;
    private ArrayList<LogEntity> logEntities;
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
    private boolean isConnected;
    private ArrayList<VehiclesEntity> vehiclesEntities;
    private ArrayList<User> users;
    private DriverSharedPrefs driverSharedPrefs;
    private NetworkConnectionLiveData networkConnectionLiveData;


    private double latitude;
    private double longtitude;

    String MAC;

    private EldManager mEldManager;
    private final Set<EldBroadcastTypes> subscribedRecords = EnumSet.of(EldBroadcastTypes.ELD_BUFFER_RECORD, EldBroadcastTypes.ELD_CACHED_RECORD, EldBroadcastTypes.ELD_FUEL_RECORD, EldBroadcastTypes.ELD_DATA_RECORD, EldBroadcastTypes.ELD_DRIVER_BEHAVIOR_RECORD, EldBroadcastTypes.ELD_EMISSIONS_PARAMETERS_RECORD, EldBroadcastTypes.ELD_ENGINE_PARAMETERS_RECORD, EldBroadcastTypes.ELD_TRANSMISSION_PARAMETERS_RECORD);

    private static final int REQUEST_BASE = 100;
    private static final int REQUEST_BT_ENABLE = REQUEST_BASE + 1;

    boolean reqdelinprogress = false;
    int reccount = 0;
    private boolean exit = false;

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
        logEntities = new ArrayList<>();

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        eldJsonViewModel = ViewModelProviders.of(this).get(EldJsonViewModel.class);

        networkConnectionLiveData = new NetworkConnectionLiveData(this);
        setLocale(userData.getLang());

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        setContentView(R.layout.activity_main);

        optimizeViewModels();

        drawerLayout = findViewById(R.id.drawer_layout);
        RecyclerView last_recycler_view = findViewById(R.id.idRecyclerView);
        customRulerChart = findViewById(R.id.idCustomLiveChart);


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
        if (!checkGrantResults(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, new int[]{0,1})){
            requestBlePermissions(this,1);
        }

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
        eldJsonViewModel.sendApkVersion(new ApkVersion(BuildConfig.VERSION_NAME, Build.VERSION.RELEASE));
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
                        if (last_status == EnumsConstants.STATUS_OFF) {
                            status.setText(R.string.off);
                        }
                        if (last_status == EnumsConstants.STATUS_SB) {
                            status.setText(R.string.sb);
                        }
                        if (last_status == EnumsConstants.STATUS_DR) {
                            status.setText(R.string.dr);
                        }
                        if (last_status == EnumsConstants.STATUS_ON) {
                            status.setText(R.string.on);
                        }
                        int last = getLastTime;
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
                        eldJsonViewModel.sendLocalData(new LiveDataEntitiy(
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
                        )).observe(this, localData -> {
                            if (localData != null) {
                                userViewModel.deleteLocalData();
                            }
                        });
                    }
                });
            }
        });
    }

    private void getUSerInfo() {


        userViewModel.deleteUser();
        eldJsonViewModel.getUser().observe(this, user -> {
            if (user != null) {
                idUsername.setText(String.format("%s %s", user.getName(), user.getLastName()));
                driverSharedPrefs.saveLastUsername(user.getName());
                driverSharedPrefs.saveLastSurname(user.getLastName());
                driverSharedPrefs.saveLastImage(user.getImage());
                driverSharedPrefs.saveLastHomeTerAdd(user.getHomeTerminalAddress());
                driverSharedPrefs.saveLastHomeTerTimeZone(user.getTimeZone());
                driverSharedPrefs.saveLastMainOffice(user.getMainOffice());
                driverSharedPrefs.saveLastPhoneNumber(user.getPhone());
                driverSharedPrefs.saveCompany(user.getCompany());
            }
        });
    }

    private void getAllDrivers() {

        eldJsonViewModel.getAllDrivers().observe(this, users -> {
            if (users != null) {
                if (this.users.size() != 0) {
                    int count = 0;
                    for (int i = 0; i < users.getDriver().getUser().size(); i++) {
                        for (int j = 0; j < this.users.size(); j++) {
                            if (users.getDriver().getUser().get(i).getName().equals(this.users.get(j).getName())) {
                                count++;
                            }
                        }
                        if (count == 0) {
                            userViewModel.insertUser(users.getDriver().getUser().get(i));
                        } else {
                            count = 0;
                        }
                    }
                } else {
                    for (int i = 0; i < users.getDriver().getUser().size(); i++) {
                        userViewModel.insertUser(users.getDriver().getUser().get(i));
                    }
                }
            }
        });

        eldJsonViewModel.getAllVehicles().observe(this, vehicleData -> {
            if (vehicleData != null) {
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
                    dvirViewModel.getCurrentName().postValue(s);
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



        settings.setOnClickListener(view -> {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            drawerLayout.close();
        });

        hosRules.setOnClickListener(view -> {
            Intent intent = new Intent(this, RulesActivity.class);
            startActivity(intent);
            drawerLayout.close();
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

        dvirViewModel.getMgetDvirs().observe(this, dvirEntities -> {

            boolean hasDvir = false;

            if (dvirEntities.size() == 0) {

                dvir.setOnClickListener(v -> {
                    Intent intent = new Intent(this, LGDDActivity.class);
                    intent.putExtra("position", 3);
                    intent.putExtra("currDay", today);
                    dvirViewModel.getCurrentName().postValue(today);
                    startActivity(intent);
                        }
                );

            } else {

                for (int i = 0; i < dvirEntities.size(); i++) {
                    if (dvirEntities.get(i).getDay().equals(today)) {

                        hasDvir = true;

                        String c = dvirEntities.get(i).getDay();

                        dvir.setOnClickListener(v -> {
                            Intent intent = new Intent(this, LGDDActivity.class);
                            intent.putExtra("position", 3);
                            intent.putExtra("currDay", c);
                            dvirViewModel.getCurrentName().postValue(c);
                            startActivity(intent);
                                }
                        );
                    }
                }
                if (!hasDvir) {
                    dvir.setOnClickListener(v -> {
                        Intent intent = new Intent(this, LGDDActivity.class);
                        intent.putExtra("position", 3);
                        intent.putExtra("currDay", today);
                        dvirViewModel.getCurrentName().postValue(today);
                        startActivity(intent);
                            }
                    );
                }
            }
        });

        log.setOnClickListener(v -> {
            Intent intent = new Intent(this, LGDDActivity.class);
            intent.putExtra("position", 0);
            intent.putExtra("currDay", today);
            dvirViewModel.getCurrentName().postValue(today);
            startActivity(intent);
                }
        );


        general.setOnClickListener(v -> {
            Intent intent = new Intent(this, LGDDActivity.class);
            intent.putExtra("position", 1);
            intent.putExtra("currDay", today);
            dvirViewModel.getCurrentName().postValue(today);
            startActivity(intent);
                }
        );

        doc.setOnClickListener(v -> {
            Intent intent = new Intent(this, LGDDActivity.class);
            intent.putExtra("currDay", today);
            intent.putExtra("position", 2);
            dvirViewModel.getCurrentName().postValue(today);
            startActivity(intent);
                }
        );

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
        dvirViewModel = ViewModelProviders.of(this).get(DvirViewModel.class);
        daoViewModel = ViewModelProviders.of(this).get(DayDaoViewModel.class);

        truckStatusEntities = new ArrayList<>();
        users = new ArrayList<>();

        daoViewModel.getGetAllVehicles().observe(this, vehiclesEntities -> this.vehiclesEntities.addAll(vehiclesEntities));

        userViewModel.getMgetDrivers().observe(this, drivers ->{
            Log.d("Adverse String",drivers.size() + "A");
            users.addAll(drivers);
        });

        statusDaoViewModel = ViewModelProviders.of(this).get(StatusDaoViewModel.class);
        networkConnectionLiveData.observe(get(),isConnected -> this.isConnected = isConnected);
        daoViewModel.getmAllStatus().observe(this, truckStatusEntities -> {
            last_status = truckStatusEntities.get(truckStatusEntities.size()-1).getTo_status();
            getLastTime = truckStatusEntities.get(truckStatusEntities.size()-1).getSeconds();
            this.truckStatusEntities.clear();
            for (int i = 0; i < truckStatusEntities.size(); i++) {
                if (truckStatusEntities.get(i).getTime().equalsIgnoreCase(today)) {
                    this.truckStatusEntities.add(truckStatusEntities.get(i));
                }
                logEntities.add(truckStatusEntities.get(i));
            }
            customRulerChart.setArrayList(this.truckStatusEntities);
        });

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
        recreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onClickVisiblityCanAndSaveBtn() {
        Button save = findViewById(R.id.idSaveStatus);
        Button cancel = findViewById(R.id.idCancelStatus);
        TextView findLocation = findViewById(R.id.idCurrentLocation);
        final EditText editLocation = findViewById(R.id.idTvCurrentLocation);
        final EditText note = findViewById(R.id.idNoteEdit);

        findLocation.setOnClickListener(v -> {
            GPSTracker gpsTracker = new GPSTracker(this);
            longtitude = gpsTracker.getLongitude();
            latitude = gpsTracker.getLatitude();
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longtitude, 1);
                Address obj = addresses.get(0);
                String add = obj.getCountryName();
                add = add + ", " + obj.getSubAdminArea();
                Log.d("Adverse Diving",add);

                editLocation.setText(add);
                editLocation.setClickable(false);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
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

            ArrayList<Double> arrayList = new ArrayList<>();
            if (latitude != 0 && longtitude != 0){
                arrayList.add(latitude);
                arrayList.add(longtitude);
                if (current_status == EnumsConstants.STATUS_OFF){

                    eldJsonViewModel.postStatus(new Status("OFF",new Point("Point",arrayList)
                            ,note.getText().toString(),Calendar.getInstance().getTime().toInstant().toString()));

                }else if (current_status == EnumsConstants.STATUS_SB){

                    eldJsonViewModel.postStatus(new Status("SB",new Point("Point",arrayList)
                            ,note.getText().toString(),Calendar.getInstance().getTime().toInstant().toString()));

                }else if (current_status == EnumsConstants.STATUS_DR){

                    eldJsonViewModel.postStatus(new Status("D",new Point("Point",arrayList)
                            ,note.getText().toString(),Calendar.getInstance().getTime().toInstant().toString()));

                }else if (current_status == EnumsConstants.STATUS_ON){

                    eldJsonViewModel.postStatus(new Status("ON",new Point("Point",arrayList)
                            ,note.getText().toString(),Calendar.getInstance().getTime().toInstant().toString()));

                }
            }else {
                if (current_status == EnumsConstants.STATUS_OFF){

                    eldJsonViewModel.postStatus(new Status("OFF",null
                            ,note.getText().toString(),Calendar.getInstance().getTime().toInstant().toString()));

                }else if (current_status == EnumsConstants.STATUS_SB){

                    eldJsonViewModel.postStatus(new Status("SB",null
                            ,note.getText().toString(),Calendar.getInstance().getTime().toInstant().toString()));

                }else if (current_status == EnumsConstants.STATUS_DR){

                    eldJsonViewModel.postStatus(new Status("D",null
                            ,note.getText().toString(),Calendar.getInstance().getTime().toInstant().toString()));

                }else if (current_status == EnumsConstants.STATUS_ON){

                    eldJsonViewModel.postStatus(new Status("ON",null
                            ,note.getText().toString(),Calendar.getInstance().getTime().toInstant().toString()));

                }
            }
            statusDaoViewModel.insertStatus(new LogEntity(last_status, current_status, editLocation.getText().toString(), note.getText().toString(), null, today, getCurrentSeconds()));
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
            if (last_status != EnumsConstants.STATUS_OFF) {
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
            }
        });


        sb.setOnClickListener(v -> {
            if (last_status != EnumsConstants.STATUS_SB) {
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
            }
        });

        dr.setOnClickListener(v -> {
            if (last_status != EnumsConstants.STATUS_DR) {
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
            }
        });

        on.setOnClickListener(v -> {
            if (last_status != EnumsConstants.STATUS_ON) {
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
            }
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
                if (exit) {
                    mEldManager.DisconnectEld();
                    finish(); // finish activity
                } else {
                    Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
                    exit = true;
                    new Handler().postDelayed(() -> exit = false, 3 * 1000);
                }
                super.onBackPressed();
//            }
            }
        }
    }

    //ELD functions
    private void connectToEld() {

        final SearchEldDeviceDialog searchEldDeviceDialog = new SearchEldDeviceDialog(MainActivity.this);
        searchEldDeviceDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            final ConnectToEldDialog dialog = new ConnectToEldDialog(this,eldConnectionState);
            dialog.setCancelable(false);

        dialog.setAlerrtDialogItemClickInterface(() -> {
            if (!eldConnectionState.equals("Conncected to Eld")){
                dialog.cancel();
                ScanForEld();
                searchEldDeviceDialog.show();
            }else {
                mEldManager.DisconnectEld();
            }
        });

        dialog.show();

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
                EventBus.getDefault().postSticky(new MessageModel(newState + "", ""));
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
            subscribedRecords.add(EldBroadcastTypes.ELD_CACHED_RECORD);

            mEldManager.EnableFuelData();
            mEldManager.UpdateSubscribedRecordTypes(subscribedRecords);
            if (RecordType != EldBroadcastTypes.ELD_DATA_RECORD){

                if (dataRec instanceof EldCachedMotionStartRecord){
                    latitude = ((EldCachedMotionStartRecord) dataRec).getLatitude();
                    longtitude = ((EldCachedMotionStartRecord) dataRec).getLongitude();

                    ArrayList<Double> arrayList = new ArrayList<>();
                    arrayList.add(((EldCachedMotionStartRecord) dataRec).getLongitude());
                    arrayList.add(((EldCachedMotionStartRecord) dataRec).getLatitude());
                    if (last_status != EnumsConstants.STATUS_DR){
                        if (isConnected){
                            eldJsonViewModel.postStatus(new Status("D",new Point("Point",arrayList),"Driving",
                                    ((EldCachedMotionStartRecord) dataRec).getGpsDateTime().toInstant().toString()));
                        }
                        statusDaoViewModel.insertStatus(new LogEntity(last_status,EnumsConstants.STATUS_DR,null,"Driving",null,
                                ((EldCachedMotionStartRecord) dataRec).getGpsDateTime().toInstant().toString(),getCurrentSeconds()));
                    }
                }else if (dataRec instanceof EldCachedMotionStopRecord){
                    ArrayList<Double> arrayList = new ArrayList<>();
                    arrayList.add(((EldCachedMotionStopRecord) dataRec).getLongitude());
                    arrayList.add(((EldCachedMotionStopRecord) dataRec).getLatitude());
                    Timer t = new Timer();
                    if (last_status == EnumsConstants.STATUS_DR) {
                        final int[] count = {60};
                        Dialog dialog = new Dialog(MainActivity.this);
                        dialog.setContentView(R.layout.custom_confirmation_layout);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        TextView textView = dialog.findViewById(R.id.idConfirmationTitle);
                        Button stay = dialog.findViewById(R.id.idDialogStayDriving);
                        Button go = dialog.findViewById(R.id.idDialogChangeStatus);
                        t.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                count[0]--;
                                runOnUiThread(()->{
                                    if (count[0] > 0){
                                        textView.setText(count[0] + " sec");
                                    }else {
                                        if (isConnected){
                                            eldJsonViewModel.postStatus(new Status("ON",new Point("Point",arrayList),"on",
                                                    ((EldCachedMotionStopRecord) dataRec).getGpsDateTime().toInstant().toString()));
                                        }
                                        current_status = EnumsConstants.STATUS_ON;
                                        statusDaoViewModel.insertStatus(new LogEntity(last_status, current_status, null, "on", null, ((EldCachedMotionStopRecord) dataRec).getGpsDateTime().toInstant().toString(), getCurrentSeconds()));
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
                            if (isConnected){
                                eldJsonViewModel.postStatus(new Status("OFF",new Point("Point",arrayList),"off",
                                        ((EldCachedMotionStopRecord) dataRec).getGpsDateTime().toInstant().toString()));
                            }
                            statusDaoViewModel.insertStatus(new LogEntity(last_status, current_status, null, "off", null, ((EldCachedMotionStopRecord) dataRec).getGpsDateTime().toInstant().toString(), getCurrentSeconds()));
                        });
                        dialog.show();
                    }

                }
            }else {
                if (dataRec instanceof EldDataRecord){
                    if (((EldDataRecord) dataRec).getSpeed() != 0.0 && speed  == 0.0){
                        speed = ((EldDataRecord) dataRec).getSpeed();
                    }else if (((EldDataRecord) dataRec).getSpeed() == 0.0 && speed  != 0.0){
                        speed = 0.0;
                    }
                    Runnable r = () -> {

                        if (isConnected){
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
                            eldJsonViewModel.sendLive(new LiveDataRecord(
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
                        }else {
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
                    };
                    ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
                    service.scheduleAtFixedRate(r, 1, 60, TimeUnit.SECONDS);
                }
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
                eldJsonViewModel.sendEldNum(new Eld(strDevice,mEldManager.GetApiVersion()));
                runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("ELD " + strDevice + " found, now connecting...\n", "")));

                EldBleError res = mEldManager.ConnectToEld(bleDataCallback, subscribedRecords, bleConnectionStateChangeCallback);

                if (res != EldBleError.SUCCESS) {
                    eldConnectionState = "Connection Failed";
                    runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("Connection Failed\n", "")));
                }else {
                    eldConnectionState = "Conncected to Eld";
                    runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("Conncected to Eld\n", "")));
                }

            } else {
                eldConnectionState = "No ELD found";
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
                eldJsonViewModel.sendEldNum(new Eld(MAC,mEldManager.GetApiVersion()));
                runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("ELD " + strDevice + " found, now connecting...\n", "")));

                EldBleError res = mEldManager.ConnectToEld(bleDataCallback, subscribedRecords, bleConnectionStateChangeCallback, strDevice);
                if (res != EldBleError.SUCCESS) {
                    eldConnectionState = "Connection Failed";
                    runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("Connection Failed\n", "")));
                } else {
                    eldConnectionState = "Conncected to Eld";
                    runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("Conncected to Eld\n", "")));
                }

            } else {
                eldConnectionState = "No ELD found";
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
                daoViewModel.deleteAllDays();
                for (int i = 8; i >= 0; i--) {
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
    protected void onPause() {
        super.onPause();
        this.getViewModelStore().clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.getViewModelStore().clear();
        this.unregisterReceiver(changeDateTimeBroadcast);
        mEldManager.DisconnectEld();
    }
}