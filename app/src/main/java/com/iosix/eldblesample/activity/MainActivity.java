package com.iosix.eldblesample.activity;

import static androidx.lifecycle.ProcessLifecycleOwner.get;
import static com.iosix.eldblesample.MyApplication.executorService;
import static com.iosix.eldblesample.enums.Day.getCurrentSeconds;
import static com.iosix.eldblesample.enums.HOSConstants.BREAK;
import static com.iosix.eldblesample.enums.HOSConstants.CYCLE;
import static com.iosix.eldblesample.enums.HOSConstants.CYCLELIMIT;
import static com.iosix.eldblesample.enums.HOSConstants.DRIVING;
import static com.iosix.eldblesample.enums.HOSConstants.DRIVINGLIMIT;
import static com.iosix.eldblesample.enums.HOSConstants.SHIFT;
import static com.iosix.eldblesample.enums.HOSConstants.SHIFTLIMIT;
import static com.iosix.eldblesample.enums.HOSConstants.mBreak;
import static com.iosix.eldblesample.enums.HOSConstants.mCycle;
import static com.iosix.eldblesample.enums.HOSConstants.mDriving;
import static com.iosix.eldblesample.enums.HOSConstants.mDrivingCurr;
import static com.iosix.eldblesample.enums.HOSConstants.mShift;
import static com.iosix.eldblesample.utils.Utils.BLUETOOTH_PERMISSIONS_S;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.multidex.BuildConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.iosix.eldblelib.EldBleConnectionStateChangeCallback;
import com.iosix.eldblelib.EldBleDataCallback;
import com.iosix.eldblelib.EldBleError;
import com.iosix.eldblelib.EldBleScanCallback;
import com.iosix.eldblelib.EldBroadcast;
import com.iosix.eldblelib.EldBroadcastTypes;
import com.iosix.eldblelib.EldBufferRecord;
import com.iosix.eldblelib.EldDataRecord;
import com.iosix.eldblelib.EldEngineStates;
import com.iosix.eldblelib.EldManager;
import com.iosix.eldblelib.EldScanObject;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.adapters.RecyclerViewLastAdapter;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.broadcasts.ChangeDateTimeBroadcast;
import com.iosix.eldblesample.broadcasts.NetworkConnectionLiveData;
import com.iosix.eldblesample.customViews.CustomLiveRulerChart;
import com.iosix.eldblesample.dialogs.ConnectToEldDialog;
import com.iosix.eldblesample.dialogs.ManageStatusDialog;
import com.iosix.eldblesample.dialogs.SearchEldDeviceDialog;
import com.iosix.eldblesample.enums.Day;
import com.iosix.eldblesample.enums.EnumsConstants;
import com.iosix.eldblesample.enums.GPSTracker;
import com.iosix.eldblesample.interfaces.AlertDialogItemClickInterface;
import com.iosix.eldblesample.models.ApkVersion;
import com.iosix.eldblesample.models.MessageModel;
import com.iosix.eldblesample.models.Status;
import com.iosix.eldblesample.models.User;
import com.iosix.eldblesample.models.eld_records.Eld;
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
import com.iosix.eldblesample.shared_prefs.SessionManager;
import com.iosix.eldblesample.shared_prefs.UserData;
import com.iosix.eldblesample.utils.Utils;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.StatusDaoViewModel;
import com.iosix.eldblesample.viewModel.UserViewModel;
import com.iosix.eldblesample.viewModel.apiViewModel.EldJsonViewModel;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements TimePickerDialog.OnTimeSetListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    int i =0;
    private DrawerLayout drawerLayout;
    private Toolbar activity_main_toolbar;
    private CardView off, sb, dr, on;
    private LinearLayout visiblityViewCons;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch nightModeSwitch;
    private int current_status = EnumsConstants.STATUS_OFF;
    private int last_status;
    private int getLastTime;
    private String time = "" + Calendar.getInstance().getTime();
    public String today = time.split(" ")[1] + " " + time.split(" ")[2];
    private ArrayList<LogEntity> logEntities;
    private ArrayList<LogEntity> truckStatusEntities;
    private ArrayList<LogEntity> otherStatusEntities;
    private TextView idUsername;
    private TextView statusOff;
    private TextView statusSB;
    private TextView statusDR;
    private TextView statusON;
    private ImageView imageOff;
    private ImageView imageSB;
    private ImageView imageDR;
    private ImageView imageON;
    private ImageView idStatusImage;
    private RadioButton idRadioPC;
    private RadioButton idRadioYM;
    private ConstraintLayout idConstraintPC;
    private ConstraintLayout idConstraintYM;
    private ChangeDateTimeBroadcast changeDateTimeBroadcast;
    private CustomLiveRulerChart customRulerChart;
    RecyclerView last_recycler_view;
    private boolean isConnected;
    private ArrayList<VehiclesEntity> vehiclesEntities;
    private ArrayList<User> users;
    private DriverSharedPrefs driverSharedPrefs;
    private NetworkConnectionLiveData networkConnectionLiveData;
    boolean isPaused;
    boolean isDriving = false;
    boolean engineState = false;
    int startseq, endseq = 31;
    int getEldConnectionState = 0;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    PendingResult<LocationSettingsResult> result;
    final static int REQUEST_LOCATION = 199;
    private UserData userData;
    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    private Dialog manageStatusDialog;


    private double latitude;
    private double longtitude;

    String MAC;

    private EldManager mEldManager;
    private final Set<EldBroadcastTypes> subscribedRecords = EnumSet.of(EldBroadcastTypes.ELD_BUFFER_RECORD, EldBroadcastTypes.ELD_CACHED_RECORD, EldBroadcastTypes.ELD_FUEL_RECORD, EldBroadcastTypes.ELD_DATA_RECORD, EldBroadcastTypes.ELD_DRIVER_BEHAVIOR_RECORD, EldBroadcastTypes.ELD_EMISSIONS_PARAMETERS_RECORD, EldBroadcastTypes.ELD_ENGINE_PARAMETERS_RECORD, EldBroadcastTypes.ELD_TRANSMISSION_PARAMETERS_RECORD);

    private static final int REQUEST_BASE = 100;
    private static final int REQUEST_BT_ENABLE = REQUEST_BASE + 1;

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
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void initView() {
        super.initView();


        networkConnectionLiveData = new NetworkConnectionLiveData(this);

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        // Set the toolbar
        activity_main_toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(activity_main_toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        optimizeViewModels();
        setLocale(userData.getLang());

        drawerLayout = findViewById(R.id.drawer_layout);
        last_recycler_view = findViewById(R.id.idRecyclerView);
        customRulerChart = findViewById(R.id.idCustomLiveChart);

        int orientations = this.getResources().getConfiguration().orientation;

        customRulerChart.setOnClickListener(v -> {
            i++;
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                if (i == 2) {
                    if (orientations == Configuration.ORIENTATION_PORTRAIT) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    } else if (orientations == Configuration.ORIENTATION_LANDSCAPE) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    }
                }
                i=0;
            }, 500);
        });

        //Required to allow bluetooth scanning
        if (!checkGrantResults(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, new int[]{0, 1})) {
            requestBlePermissions(this, 1);
        }

        mEldManager = EldManager.GetEldManager(this, "123456789A");

        sendApkVersion();
        onClickCustomBtn();
        onClickVisiblityCanAndSaveBtn();
        getDrawerToggleEvent();
        getDrawerTouchEvent();
        clickLGDDButtons();
        startService();
        update();
        getAllDrivers();
        calculateTimeLimits();
        sendLocalData();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
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
                        idStatusImage = findViewById(R.id.idStatusImage);
                        if (last_status == EnumsConstants.STATUS_OFF) {
                            status.setText(R.string.off);
                            idStatusImage.setBackgroundResource(R.drawable.ic_baseline_power_settings_new_24);
                            idStatusImage.setColorFilter(MainActivity.this.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
                        }
                        if (last_status == EnumsConstants.STATUS_SB) {
                            status.setText(R.string.sb);
                            idStatusImage.setBackgroundResource(R.drawable.ic__1748117516352401124513);
                            idStatusImage.setColorFilter(MainActivity.this.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
                        }
                        if (last_status == EnumsConstants.STATUS_DR) {
                            status.setText(R.string.dr);
                            idStatusImage.setBackgroundResource(R.drawable.ic_steering_wheel_car_svgrepo_com);
                            idStatusImage.setColorFilter(MainActivity.this.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
                        }
                        if (last_status == EnumsConstants.STATUS_ON) {
                            status.setText(R.string.on);
                            idStatusImage.setBackgroundResource(R.drawable.ic_truck);
                            idStatusImage.setColorFilter(MainActivity.this.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
                        }if (last_status == EnumsConstants.STATUS_OF_PC){
                            status.setText(R.string.off_pc);
                            idStatusImage.setBackgroundResource(R.drawable.ic_baseline_power_settings_new_24);
                            idStatusImage.setColorFilter(MainActivity.this.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
                        }if (last_status == EnumsConstants.STATUS_ON_YM){
                            status.setText(R.string.on_ym);
                            idStatusImage.setBackgroundResource(R.drawable.ic_truck);
                            idStatusImage.setColorFilter(MainActivity.this.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
                        }

                        int last = getLastTime;
                        int current = getCurrentSeconds();
                        int hour = (current - last) / 3600;
                        int min = ((current - last) % 3600) / 60;
                        statusTime.setText(String.format("%02dh %02dm", hour, min));
                    });
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

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
                        )).observe(this,liveDataEntitiy -> {
                            if (liveDataEntitiy != null){
                                userViewModel.deleteLocalData();
                            }
                        });
                    }
                });
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
                            userViewModel.insertVehicle(new VehiclesEntity(vehicleData.getVehicle().getVehicleList().get(i).getVehicle_id()));
                        } else {
                            count = 0;
                        }
                    }
                } else {
                    for (int i = 0; i < vehicleData.getVehicle().getVehicleList().size(); i++) {
                        userViewModel.insertVehicle(new VehiclesEntity(vehicleData.getVehicle().getVehicleList().get(i).getVehicle_id()));
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

            @Override
            public void onclickSignature(String s) {
                Intent intent = new Intent(MainActivity.this, LGDDActivity.class);
                intent.putExtra("currDay", s);
                intent.putExtra("position", 2);
                dvirViewModel.getCurrentName().postValue(s);
                startActivity(intent);
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

        idUsername.setText(String.format("%s %s", driverSharedPrefs.getFirstname(), driverSharedPrefs.getLastname()));

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
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView header = dialog.findViewById(R.id.idDvirHeader);
            TextView title = dialog.findViewById(R.id.idDvirTitle);
            header.setText(R.string.log_out);
            title.setText(R.string.do_you_log_out);

            dialog.findViewById(R.id.idDvirYes).setOnClickListener(view -> {
                sessionManager.clearAccessToken();
                sessionManager.clearToken();
                eldJsonViewModel.postStatus(new Status(Utils.getStatus(EnumsConstants.LOGOUT),null,null,Utils.getDateFormat(Calendar.getInstance().getTime())));
                statusDaoViewModel.insertStatus(new LogEntity(driverSharedPrefs.getDriverId(),EnumsConstants.LOGOUT,null,null,null,today,getCurrentSeconds()));
                Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                mEldManager.DisconnectEld();
                dialog.dismiss();
            });

            dialog.findViewById(R.id.idDvirNo).setOnClickListener(view -> dialog.dismiss());

            dialog.show();
        });

        dvir.setOnClickListener(v -> {
                    Intent intent = new Intent(this, LGDDActivity.class);
                    intent.putExtra("position", 3);
                    intent.putExtra("currDay", today);
                    startActivity(intent);
                }
        );

        log.setOnClickListener(v -> {
            Intent intent = new Intent(this, LGDDActivity.class);
            intent.putExtra("position", 0);
            intent.putExtra("currDay", today);
            startActivity(intent);
                }
        );


        general.setOnClickListener(v -> {
            Intent intent = new Intent(this, LGDDActivity.class);
            intent.putExtra("position", 1);
            intent.putExtra("currDay", today);
            startActivity(intent);
                }
        );

        doc.setOnClickListener(v -> {
            Intent intent = new Intent(this, LGDDActivity.class);
            intent.putExtra("currDay", today);
            intent.putExtra("position", 2);
            startActivity(intent);
                }
        );

        inspectionMode.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,InspectionModuleActivity.class);
            startActivity(intent);
            drawerLayout.close();
        });
    }

    private void optimizeViewModels() {
        dvirViewModel = ViewModelProviders.of(this).get(DvirViewModel.class);
        daoViewModel = ViewModelProviders.of(this).get(DayDaoViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        eldJsonViewModel = ViewModelProviders.of(this).get(EldJsonViewModel.class);
        driverSharedPrefs = DriverSharedPrefs.getInstance(getApplicationContext());
        userData = new UserData(this);

        truckStatusEntities = new ArrayList<>();
        otherStatusEntities = new ArrayList<>();
        vehiclesEntities = new ArrayList<>();
        logEntities = new ArrayList<>();

        users = new ArrayList<>();

        userViewModel.getGetAllVehicles().observe(this, vehiclesEntities ->
        {
            if (vehiclesEntities != null) {
                this.vehiclesEntities.addAll(vehiclesEntities);
            }
        });

        dvirViewModel.getMgetDvirs().observe(this,dvirEntities -> {
            lastAdapter = new RecyclerViewLastAdapter(this, daoViewModel, statusDaoViewModel, dvirViewModel);
            last_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
            last_recycler_view.setAdapter(lastAdapter);
            lastAdapterClicked();
        });

        daoViewModel.getMgetAllDays().observe(this, dayEntities -> {
            lastAdapter = new RecyclerViewLastAdapter(this, daoViewModel, statusDaoViewModel, dvirViewModel);
            last_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
            last_recycler_view.setAdapter(lastAdapter);
            lastAdapterClicked();

        });

        userViewModel.getMgetDrivers().observe(this, drivers -> users.addAll(drivers));

        statusDaoViewModel = ViewModelProviders.of(this).get(StatusDaoViewModel.class);
        networkConnectionLiveData.observe(get(),isConnected -> this.isConnected = isConnected);
        statusDaoViewModel.getmAllStatus().observe(this, truckStatusEntities -> {
            String time = "" + Calendar.getInstance().getTime();
            String today = time.split(" ")[1] + " " + time.split(" ")[2];
            for (int j = truckStatusEntities.size() - 1; j >= 0 ; j--) {
                if (truckStatusEntities.get(j).getTo_status() < 6){
                    last_status = truckStatusEntities.get(j).getTo_status();
                    getLastTime = truckStatusEntities.get(j).getSeconds();
                    break;
                }
            }
            this.truckStatusEntities.clear();
            otherStatusEntities.clear();
            logEntities.clear();
            for (int i = 0; i < truckStatusEntities.size(); i++) {
                if (truckStatusEntities.get(i).getDriverId().equals(driverSharedPrefs.getDriverId())){
                    if (truckStatusEntities.get(i).getTime().equalsIgnoreCase(today)) {
                        if (truckStatusEntities.get(i).getTo_status() < 6){
                            this.truckStatusEntities.add(truckStatusEntities.get(i));
                        }else {
                            otherStatusEntities.add(truckStatusEntities.get(i));
                        }
                    }
                }
                if (truckStatusEntities.get(i).getTo_status() < 6){
                    logEntities.add(truckStatusEntities.get(i));
                }
            }
            mDriving = 0;
            mBreak = 0;
            mShift = 0;
            mCycle = 0;
            mDrivingCurr = 0;
            for (int j = truckStatusEntities.size()-1; j >=0; j--) {
                if (truckStatusEntities.get(j).getTo_status() == EnumsConstants.STATUS_DR){
                    if (j != truckStatusEntities.size()-1) mDriving += truckStatusEntities.get(j+1).getSeconds() - truckStatusEntities.get(j).getSeconds();
                    else {
                        mDriving += getCurrentSeconds() - truckStatusEntities.get(j).getSeconds();
                    }
                }
                if (truckStatusEntities.get(j).getTo_status() == EnumsConstants.STATUS_OFF || truckStatusEntities.get(j).getTo_status() == EnumsConstants.STATUS_SB){
                    if (j != truckStatusEntities.size()-1) mBreak += truckStatusEntities.get(j+1).getSeconds() - truckStatusEntities.get(j).getSeconds();
                    else mBreak += getCurrentSeconds() - truckStatusEntities.get(j).getSeconds();
                }else break;

                if (last_status == EnumsConstants.STATUS_ON){
                    if (j != truckStatusEntities.size()-1) mShift += truckStatusEntities.get(j+1).getSeconds() - truckStatusEntities.get(j).getSeconds();
                    else mShift += getCurrentSeconds() - truckStatusEntities.get(j).getSeconds();
                }
            }

            customRulerChart.setArrayList(this.truckStatusEntities,otherStatusEntities);
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

    private void onClickVisiblityCanAndSaveBtn() {
        Button save = findViewById(R.id.idSaveStatus);
        Button cancel = findViewById(R.id.idCancelStatus);
        TextView findLocation = findViewById(R.id.idCurrentLocation);
        final EditText editLocation = findViewById(R.id.idTvCurrentLocation);
        final EditText note = findViewById(R.id.idNoteEdit);
        idRadioPC = findViewById(R.id.idRadioPC);
        idRadioYM = findViewById(R.id.idRadioYM);
        AtomicBoolean checkPC = new AtomicBoolean(false);
        AtomicBoolean checkYM = new AtomicBoolean(false);

        idRadioPC.setOnClickListener(view -> {
            if (checkPC.get()){
               checkPC.set(false);
               idRadioPC.setChecked(false);
            }else {
                checkPC.set(true);
                idRadioPC.setChecked(true);
            }
        });

        idRadioYM.setOnClickListener(view -> {
            if (checkYM.get()){
                checkYM.set(false);
                idRadioYM.setChecked(false);
            }else {
                checkYM.set(true);
                idRadioYM.setChecked(true);
            }
        });

        findLocation.setOnClickListener(v -> {
            GPSTracker gpsTracker = new GPSTracker(this);
            longtitude = gpsTracker.getLongitude();
            latitude = gpsTracker.getLatitude();
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            try {
                if (latitude != 0 && longtitude != 0){
                List<Address> addresses = geocoder.getFromLocation(latitude, longtitude, 1);
                Address obj = addresses.get(0);
                String add = obj.getAddressLine(0);

                editLocation.setText(add);
                editLocation.setClickable(false);
                }
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

            if (idRadioPC.isChecked()){
                eldJsonViewModel.postStatus(new Status(Utils.getStatus(EnumsConstants.STATUS_OF_PC),Utils.hasCoordinates(longtitude,latitude),note.getText().toString(),Utils.getDateFormat(Calendar.getInstance().getTime())));
                statusDaoViewModel.insertStatus(new LogEntity(driverSharedPrefs.getDriverId(),EnumsConstants.STATUS_OF_PC,Utils.hasCoordinates(longtitude,latitude),note.getText().toString(),null,today,getCurrentSeconds()));
                idRadioPC.setChecked(false);
                idRadioYM.setChecked(false);
            }else if (idRadioYM.isChecked()){
                eldJsonViewModel.postStatus(new Status(Utils.getStatus(EnumsConstants.STATUS_ON_YM),Utils.hasCoordinates(longtitude,latitude),note.getText().toString(),Utils.getDateFormat(Calendar.getInstance().getTime())));
                statusDaoViewModel.insertStatus(new LogEntity(driverSharedPrefs.getDriverId(),EnumsConstants.STATUS_ON_YM,Utils.hasCoordinates(longtitude,latitude),note.getText().toString(),null,today,getCurrentSeconds()));
                idRadioPC.setChecked(false);
                idRadioYM.setChecked(false);
            }else {
                eldJsonViewModel.postStatus(new Status(Utils.getStatus(current_status),Utils.hasCoordinates(longtitude,latitude),note.getText().toString(),Utils.getDateFormat(Calendar.getInstance().getTime())));
                statusDaoViewModel.insertStatus(new LogEntity(driverSharedPrefs.getDriverId(),current_status,Utils.hasCoordinates(longtitude,latitude),note.getText().toString(),null,today,getCurrentSeconds()));
                idRadioPC.setChecked(false);
                idRadioYM.setChecked(false);
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
        idConstraintPC = findViewById(R.id.idConstraintPC);
        idConstraintYM = findViewById(R.id.idConstraintYM);

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
            idConstraintPC.setVisibility(View.VISIBLE);
            idConstraintYM.setVisibility(View.GONE);
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
            idConstraintPC.setVisibility(View.GONE);
            idConstraintYM.setVisibility(View.GONE);

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
            idConstraintPC.setVisibility(View.GONE);
            idConstraintYM.setVisibility(View.GONE);

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
            idConstraintYM.setVisibility(View.VISIBLE);
            idConstraintPC.setVisibility(View.GONE);

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
                isPaused = false;
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
        nightModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> userData.saveMode(isChecked));

    }

    private void checkNightModeActivated() {
        nightModeSwitch.setChecked(userData.getMode());
    }

    private void getDrawerToggleEvent() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, activity_main_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                isPaused = true;
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
                isPaused = false;
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
//            }
            }
        }
    }

    //ELD functions
    private void connectToEld() {

        final SearchEldDeviceDialog searchEldDeviceDialog = new SearchEldDeviceDialog(MainActivity.this);
        searchEldDeviceDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            final ConnectToEldDialog dialog = new ConnectToEldDialog(this,getEldConnectionState);
            dialog.setCancelable(false);

            dialog.setAlerrtDialogItemClickInterface(new AlertDialogItemClickInterface() {
                @Override
                public void onClickConnect() {
                    ScanForEld();
                    dialog.cancel();
                    searchEldDeviceDialog.show();
                }

                @Override
                public void onClickDisCocnnect() {
                    mEldManager.DisconnectEld();
                    dialog.cancel();
                }

                @Override
                public void onClickCancel() {
                    dialog.cancel();
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
                getEldConnectionState = newState;
                //todo mDataView.append("New State of connection" + Integer.toString(newState, 10) + "\n");
                EventBus.getDefault().postSticky(new MessageModel(newState + "", ""));
            });
        }
    };

    private final EldBleDataCallback bleDataCallback = new EldBleDataCallback() {
        @Override
        public void OnDataRecord(final EldBroadcast dataRec, final EldBroadcastTypes RecordType) {

            if (dataRec instanceof EldBufferRecord){
                startseq = ((EldBufferRecord) dataRec).getStartSeqNo();
                endseq = ((EldBufferRecord) dataRec).getEndSeqNo();
            } else if (RecordType != EldBroadcastTypes.ELD_DATA_RECORD){
            }else {
                ArrayList<Double> arrayList2 = new ArrayList<>();
                arrayList2.add(((EldDataRecord) dataRec).getLongitude());
                arrayList2.add(((EldDataRecord) dataRec).getLatitude());
                DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ",Locale.getDefault());
                if ((((EldDataRecord) dataRec).getEngineState() == EldEngineStates.ENGINE_OFF || ((EldDataRecord) dataRec).getEngineState() == EldEngineStates.ENGINE_INVALID) && engineState && truckStatusEntities.get(truckStatusEntities.size()-1).getTo_status() != EnumsConstants.STATUS_OFF){
                    engineState = false;
                    statusDaoViewModel.insertStatus(new LogEntity(driverSharedPrefs.getDriverId(),EnumsConstants.POWER_DOWN,new Point("Point",arrayList2),null,null,today,getCurrentSeconds()));
                    statusDaoViewModel.insertStatus(new LogEntity(driverSharedPrefs.getDriverId(),EnumsConstants.STATUS_OFF,new Point("Point",arrayList2),null,null,today,getCurrentSeconds()));
                    eldJsonViewModel.postStatus(new Status(Utils.getStatus(EnumsConstants.STATUS_OFF),Utils.hasCoordinates(((EldDataRecord) dataRec).getLongitude(),((EldDataRecord) dataRec).getLatitude()),"off",dateFormat.format(((EldDataRecord) dataRec).getGpsDateTime())));
                    eldJsonViewModel.postStatus(new Status(Utils.getStatus(EnumsConstants.POWER_DOWN),Utils.hasCoordinates(((EldDataRecord) dataRec).getLongitude(),((EldDataRecord) dataRec).getLatitude()),"power down",dateFormat.format(((EldDataRecord) dataRec).getGpsDateTime())));
                }else if (((EldDataRecord) dataRec).getEngineState() == EldEngineStates.ENGINE_ON && !engineState){
                    engineState = true;
                    statusDaoViewModel.insertStatus(new LogEntity(driverSharedPrefs.getDriverId(),EnumsConstants.POWER_UP,new Point("Point",arrayList2),null,null,today,getCurrentSeconds()));
                    statusDaoViewModel.insertStatus(new LogEntity(driverSharedPrefs.getDriverId(),EnumsConstants.STATUS_ON,new Point("Point",arrayList2),null,null,today,getCurrentSeconds()));
                    eldJsonViewModel.postStatus(new Status(Utils.getStatus(EnumsConstants.STATUS_ON),Utils.hasCoordinates(((EldDataRecord) dataRec).getLongitude(),((EldDataRecord) dataRec).getLatitude()),"on",dateFormat.format(((EldDataRecord) dataRec).getGpsDateTime())));
                    eldJsonViewModel.postStatus(new Status(Utils.getStatus(EnumsConstants.POWER_UP),Utils.hasCoordinates(((EldDataRecord) dataRec).getLongitude(),((EldDataRecord) dataRec).getLatitude()),"power up",dateFormat.format(((EldDataRecord) dataRec).getGpsDateTime())));
                }
                if (((EldDataRecord)dataRec).getSpeed() > 5 && !isDriving && truckStatusEntities.get(truckStatusEntities.size() - 1).getTo_status() < 4 && truckStatusEntities.get(truckStatusEntities.size()-1).getTo_status() != EnumsConstants.STATUS_ON){
                    isDriving = true;
                    runOnUiThread(()->{
                        if (mHandler != null && mRunnable != null){
                            mHandler.removeCallbacks(mRunnable);
                        }
                        if (manageStatusDialog != null){
                            manageStatusDialog.cancel();
                        }
                        Intent intent = new Intent(MainActivity.this,RecapActivity.class);
                        startActivity(intent);
                    });
                    statusDaoViewModel.insertStatus(new LogEntity(driverSharedPrefs.getDriverId(),EnumsConstants.STATUS_DR,new Point("Point",arrayList2),"dr",null,today,getCurrentSeconds()));
                    eldJsonViewModel.postStatus(new Status(Utils.getStatus(EnumsConstants.STATUS_DR),Utils.hasCoordinates(((EldDataRecord) dataRec).getLongitude(),((EldDataRecord) dataRec).getLatitude()),"dr",dateFormat.format(((EldDataRecord) dataRec).getGpsDateTime())));
                }else if (((EldDataRecord) dataRec).getSpeed() == 0 && isDriving && truckStatusEntities.get(truckStatusEntities.size() - 1).getTo_status() < 4){
                    isDriving = false;
                    runOnUiThread(() ->{
                        mRunnable = () -> {
                            if (RecapActivity.instance != null){
                                RecapActivity.instance.finish();
                            }
                            manageStatusDialog = new ManageStatusDialog(MainActivity.this,eldJsonViewModel,statusDaoViewModel,latitude,longtitude);
                            manageStatusDialog.show();
                        };
                        mHandler.postDelayed(mRunnable,10000L);
                    });
                }

                if (((EldDataRecord)dataRec).getSequence() % 30 == 1){
                    Boolean b = null;
                    if(((EldDataRecord) dataRec).getEngineState() == EldEngineStates.ENGINE_OFF){
                        b = false;
                    }else if(((EldDataRecord) dataRec).getEngineState() == EldEngineStates.ENGINE_ON){
                        b = true;
                    }
                    ArrayList<Double> arrayList = new ArrayList<>();
                    arrayList.add(((EldDataRecord) dataRec).getLongitude());
                    arrayList.add(((EldDataRecord) dataRec).getLatitude());
                    if (isConnected){

                        eldJsonViewModel.sendLive(new LiveDataRecord(
                                b,
                                "vin",
                                ((EldDataRecord) dataRec).getSpeed(),
                                ((EldDataRecord) dataRec).getOdometer(),
                                ((EldDataRecord) dataRec).getTripDistance(),
                                ((EldDataRecord) dataRec).getEngineHours(),
                                ((EldDataRecord) dataRec).getTripHours(),
                                ((EldDataRecord) dataRec).getVoltage(),
                                dateFormat.format(((EldDataRecord) dataRec).getGpsDateTime()),
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
                        userViewModel.insertLocalData(new LiveDataRecord(
                                b,
                                "vin",
                                ((EldDataRecord) dataRec).getSpeed(),
                                ((EldDataRecord) dataRec).getOdometer(),
                                ((EldDataRecord) dataRec).getTripDistance(),
                                ((EldDataRecord) dataRec).getEngineHours(),
                                ((EldDataRecord) dataRec).getTripHours(),
                                ((EldDataRecord) dataRec).getVoltage(),
                                dateFormat.format(((EldDataRecord) dataRec).getGpsDateTime()),
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
                    runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("Connection Failed\n", "")));
                }else {
                    runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("Conncected to Eld\n", "")));
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
                eldJsonViewModel.sendEldNum(new Eld(MAC,mEldManager.GetApiVersion()));
                runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("ELD " + strDevice + " found, now connecting...\n", "")));

                EldBleError res = mEldManager.ConnectToEld(bleDataCallback, subscribedRecords, bleConnectionStateChangeCallback, strDevice);
                if (res != EldBleError.SUCCESS) {
                    runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("Connection Failed\n", "")));
                } else {
                    runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("Conncected to Eld\n", "")));
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
                daoViewModel.deleteAllDays();
                for (int i = 8; i >= 0; i--) {
                    String time = Day.getCalculatedDate(-i);
                    try {
                        daoViewModel.insertDay(new DayEntity(Day.getDayTime1(time), Day.getDayName2(time)));
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
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
//    public void stopService() {
//        Intent intent = new Intent(this, ForegroundService.class);
//        stopService(intent);
//    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView addTime = findViewById(R.id.idDefectTimeText);
        if (hourOfDay > 12){
            addTime.setText(String.format("%s:%s %s", timeString(hourOfDay-12), timeString(minute),"PM"));
        }else {
            addTime.setText(String.format("%s:%s %s", timeString(hourOfDay), timeString(minute),"AM"));
        }

    }

    private String timeString(int digit) {
        String s = "" + digit;
        if (s.length() == 1) s = "0" + s;
        return s;
    }

    private void calculateTimeLimits(){
        Runnable runnable = () -> {
            if (last_status == EnumsConstants.STATUS_OFF || last_status == EnumsConstants.STATUS_SB){
                mBreak++;
                if (mBreak >= 1800){
                    BREAK = 28800;
                }
            }
            if (last_status == EnumsConstants.STATUS_DR || last_status == EnumsConstants.STATUS_ON){
                BREAK--;
                mBreak = 0;
            }
            if (last_status == EnumsConstants.STATUS_DR){
                mDriving++;
                mDrivingCurr++;
            }
            DRIVING = DRIVINGLIMIT - mDriving;

            if (last_status == EnumsConstants.STATUS_ON){
                mShift++;
            }
            SHIFT = SHIFTLIMIT - (mDriving + mShift);
            CYCLE = CYCLELIMIT - (mDriving + mShift + mCycle);
        };

        executorService.scheduleAtFixedRate(runnable,3,1,TimeUnit.SECONDS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isPaused = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(changeDateTimeBroadcast);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(result -> {
            final com.google.android.gms.common.api.Status status = result.getStatus();
            //final LocationSettingsStates state = result.getLocationSettingsStates();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                    //...
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    // Location settings are not satisfied. But could be fixed by showing the user
                    // a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(
                                MainActivity.this,
                                REQUEST_LOCATION);
                    } catch (IntentSender.SendIntentException e) {
                        // Ignore the error.
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    // Location settings are not satisfied. However, we have no way to fix the
                    // settings so we won't show the dialog.
                    //...
                    break;
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private static final String[] BLE_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    private static final String[] ANDROID_12_BLE_PERMISSIONS = new String[]{
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    public static void requestBlePermissions(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            ActivityCompat.requestPermissions(activity, ANDROID_12_BLE_PERMISSIONS, requestCode);
        else
            ActivityCompat.requestPermissions(activity, BLE_PERMISSIONS, requestCode);
    }
}
