package com.iosix.eldblesample.activity;

import static android.os.Build.VERSION_CODES.S;
import static androidx.lifecycle.ProcessLifecycleOwner.get;
import static com.iosix.eldblesample.enums.Day.getCurrentMillis;
import static com.iosix.eldblesample.enums.Day.getDayFormat;
import static com.iosix.eldblesample.utils.Utils.getDateFormat;
import static com.iosix.eldblesample.utils.Utils.getStatus;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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
import android.os.Looper;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.broadcasts.ChangeDateTimeBroadcast;
import com.iosix.eldblesample.broadcasts.NetworkConnectionLiveData;
import com.iosix.eldblesample.customViews.CustomLiveRulerChart;
import com.iosix.eldblesample.customViews.CustomStableRulerChart;
import com.iosix.eldblesample.dialogs.ConnectToEldDialog;
import com.iosix.eldblesample.dialogs.ManageStatusDialog;
import com.iosix.eldblesample.dialogs.SearchEldDeviceDialog;
import com.iosix.eldblesample.enums.EnumsConstants;
import com.iosix.eldblesample.enums.GPSTracker;
import com.iosix.eldblesample.interfaces.AlertDialogItemClickInterface;
import com.iosix.eldblesample.models.MessageModel;
import com.iosix.eldblesample.models.Status;
import com.iosix.eldblesample.models.eld_records.Eld;
import com.iosix.eldblesample.models.eld_records.LiveDataRecord;
import com.iosix.eldblesample.models.eld_records.Point;
import com.iosix.eldblesample.shared_prefs.DriverSharedPrefs;
import com.iosix.eldblesample.shared_prefs.LastStatusData;
import com.iosix.eldblesample.shared_prefs.SessionManager;
import com.iosix.eldblesample.shared_prefs.UserData;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.StatusDaoViewModel;
import com.iosix.eldblesample.viewModel.UserViewModel;
import com.iosix.eldblesample.viewModel.apiViewModel.EldJsonViewModel;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.Nullable;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZonedDateTime;

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
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends BaseActivity {

    int i = 0;
    private DrawerLayout drawerLayout;
    private Toolbar activity_main_toolbar;
    private CardView off, sb, dr, on;
    private LinearLayout visiblityViewCons;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch nightModeSwitch;
    private String current_status = EnumsConstants.STATUS_OFF;
    private String time = "" + Calendar.getInstance().getTime();
    public String today = time.split(" ")[1] + " " + time.split(" ")[2];
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
    private CustomLiveRulerChart customLiveRulerChart;
    private CustomStableRulerChart customRulerChart;
    private RecyclerView last_recycler_view;
    private boolean isConnected;
    private DriverSharedPrefs driverSharedPrefs;
    private NetworkConnectionLiveData networkConnectionLiveData;
    boolean isPaused;
    int startseq, endseq = 31;
    private UserData userData;
    private LastStatusData lastStatusData;
    private SessionManager sessionManager;
    private final Handler mHandler = new Handler();

    boolean engineState;
    boolean isDriving;
    Runnable mRunnable;
    ManageStatusDialog manageStatusDialog;

    String MAC;

    private EldManager mEldManager;
    private final Set<EldBroadcastTypes> subscribedRecords = EnumSet.of(EldBroadcastTypes.ELD_BUFFER_RECORD, EldBroadcastTypes.ELD_CACHED_RECORD, EldBroadcastTypes.ELD_FUEL_RECORD, EldBroadcastTypes.ELD_DATA_RECORD, EldBroadcastTypes.ELD_DRIVER_BEHAVIOR_RECORD, EldBroadcastTypes.ELD_EMISSIONS_PARAMETERS_RECORD, EldBroadcastTypes.ELD_ENGINE_PARAMETERS_RECORD, EldBroadcastTypes.ELD_TRANSMISSION_PARAMETERS_RECORD);

    private static final int REQUEST_BASE = 100;
    private static final int REQUEST_BT_ENABLE = REQUEST_BASE + 1;

    private boolean exit = false;

    private final ArrayList<Double> point = new ArrayList<>();

    private StatusDaoViewModel statusDaoViewModel;
    private DayDaoViewModel daoViewModel;
    private DvirViewModel dvirViewModel;
    private UserViewModel userViewModel;
    private EldJsonViewModel eldJsonViewModel;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                // Process the location result here
            }
        };

        if (!isGpsEnabled()){
            requestGps();
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        last_recycler_view = findViewById(R.id.idRecyclerView);
        customLiveRulerChart = findViewById(R.id.idCustomLiveChart);

        int orientations = this.getResources().getConfiguration().orientation;

        customLiveRulerChart.setOnClickListener(v -> {
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
                i = 0;
            }, 500);
        });

        //Required to allow bluetooth scanning
        if (!checkGrantResults(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, new int[]{0, 1})) {
            requestBlePermissions(this, 1);
        }

        idUsername = findViewById(R.id.idUsername);

        mEldManager = EldManager.GetEldManager(this, "123456789A");

        onClickCustomBtn();
        onClickVisiblityCanAndSaveBtn();
        getDrawerToggleEvent();
        getDrawerTouchEvent();
        clickLGDDButtons();
        startService();
        update();
        getAllDrivers();
        sendLocalData();
        automateConnectToEld();

        statusDaoViewModel.getCurDateStatus(customLiveRulerChart, customRulerChart, getDayFormat(ZonedDateTime.now()));
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
                        if (Objects.equals(lastStatusData.getLastStatus(), EnumsConstants.STATUS_OFF)) {
                            status.setText(R.string.off);
                            idStatusImage.setBackgroundResource(R.drawable.ic_baseline_power_settings_new_24);
                        }
                        if (Objects.equals(lastStatusData.getLastStatus(), EnumsConstants.STATUS_SB)) {
                            status.setText(R.string.sb);
                            idStatusImage.setBackgroundResource(R.drawable.ic__1748117516352401124513);
                        }
                        if (Objects.equals(lastStatusData.getLastStatus(), EnumsConstants.STATUS_DR)) {
                            status.setText(R.string.dr);
                            idStatusImage.setBackgroundResource(R.drawable.ic_steering_wheel_car_svgrepo_com);
                        }
                        if (Objects.equals(lastStatusData.getLastStatus(), EnumsConstants.STATUS_ON)) {
                            status.setText(R.string.on);
                            idStatusImage.setBackgroundResource(R.drawable.ic_truck);
                        }
                        if (Objects.equals(lastStatusData.getLastStatus(), EnumsConstants.STATUS_OF_PC)) {
                            status.setText(R.string.off_pc);
                            status.setText(R.string.off);
                            idStatusImage.setBackgroundResource(R.drawable.ic_baseline_power_settings_new_24);
                        }
                        if (Objects.equals(lastStatusData.getLastStatus(), EnumsConstants.STATUS_ON_YM)) {
                            status.setText(R.string.on_ym);
                            idStatusImage.setBackgroundResource(R.drawable.ic_truck);
                        }
                        idStatusImage.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);

                        long hour = (getCurrentMillis() - lastStatusData.getLasStatSecond()) / 3600;
                        long min = ((getCurrentMillis() - lastStatusData.getLasStatSecond()) % 3600) / 60;
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
                userViewModel.getMgetLocalDatas(eldJsonViewModel);
            }
        });
    }

    private void getAllDrivers() {

        eldJsonViewModel.getAllDrivers(userViewModel);
        eldJsonViewModel.getAllVehicles(userViewModel);
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

        if (!driverSharedPrefs.getFirstname().equals("") && !driverSharedPrefs.getLastname().equals("")) {
            idUsername.setText(String.format("%s %s", driverSharedPrefs.getFirstname(), driverSharedPrefs.getLastname()));
        }

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
                eldJsonViewModel.postStatus(new Status(EnumsConstants.LOGOUT, null, null, dateFormat.format(Calendar.getInstance().getTime())));
                statusDaoViewModel.insertStatus(new Status(EnumsConstants.LOGOUT, null, null, dateFormat.format(Calendar.getInstance().getTime())));
                Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
//                mEldManager.DisconnectEld();
                dialog.dismiss();
            });

            dialog.findViewById(R.id.idDvirNo).setOnClickListener(view -> dialog.dismiss());

            dialog.show();
        });

        dvir.setOnClickListener(v -> {
                    Intent intent = new Intent(this, LGDDActivity.class);
                    intent.putExtra("position", 3);
                    intent.putExtra("currDay", getDayFormat(ZonedDateTime.now()));
                    dvirViewModel.getCurrentName().postValue(getDayFormat(ZonedDateTime.now()));
                    startActivity(intent);
                }
        );

        log.setOnClickListener(v -> {
            Intent intent = new Intent(this, LGDDActivity.class);
            intent.putExtra("position", 0);
            intent.putExtra("currDay", getDayFormat(ZonedDateTime.now()));
            dvirViewModel.getCurrentName().postValue(getDayFormat(ZonedDateTime.now()));
            startActivity(intent);
                }
        );


        general.setOnClickListener(v -> {
            Intent intent = new Intent(this, LGDDActivity.class);
            intent.putExtra("position", 1);
            intent.putExtra("currDay", getDayFormat(ZonedDateTime.now()));
            dvirViewModel.getCurrentName().postValue(getDayFormat(ZonedDateTime.now()));
            startActivity(intent);
                }
        );

        doc.setOnClickListener(v -> {
            Intent intent = new Intent(this, LGDDActivity.class);
            intent.putExtra("currDay", getDayFormat(ZonedDateTime.now()));
            intent.putExtra("position", 2);
            dvirViewModel.getCurrentName().postValue(getDayFormat(ZonedDateTime.now()));
            startActivity(intent);
                }
        );

        inspectionMode.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, InspectionModuleActivity.class);
            startActivity(intent);
            drawerLayout.close();
        });
    }

    private void optimizeViewModels() {

        dvirViewModel = ViewModelProviders.of(this).get(DvirViewModel.class);
        statusDaoViewModel = ViewModelProviders.of(this).get(StatusDaoViewModel.class);
        daoViewModel = ViewModelProviders.of(this).get(DayDaoViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        eldJsonViewModel = ViewModelProviders.of(this).get(EldJsonViewModel.class);
        driverSharedPrefs = DriverSharedPrefs.getInstance(getApplicationContext());
        userData = new UserData(this);
        lastStatusData = LastStatusData.getInstance(getApplicationContext());
        sessionManager = SessionManager.getInstance(getApplicationContext());
        last_recycler_view = findViewById(R.id.idRecyclerView);

        statusDaoViewModel = ViewModelProviders.of(this).get(StatusDaoViewModel.class);
        networkConnectionLiveData.observe(get(), isConnected -> this.isConnected = isConnected);
        daoViewModel.getMgetAllDays(this, last_recycler_view, dvirViewModel, statusDaoViewModel);
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
            if (checkPC.get()) {
                checkPC.set(false);
                idRadioPC.setChecked(false);
            } else {
                checkPC.set(true);
                idRadioPC.setChecked(true);
            }
        });

        idRadioYM.setOnClickListener(view -> {
            if (checkYM.get()) {
                checkYM.set(false);
                idRadioYM.setChecked(false);
            } else {
                checkYM.set(true);
                idRadioYM.setChecked(true);
            }
        });

        findLocation.setOnClickListener(v -> {
            editLocation.setText(getLocation());
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

            if (!current_status.equals(lastStatusData.getLastStatus())) {

                if (idRadioPC.isChecked()) {
                    lastStatusData.saveLasStatus(EnumsConstants.STATUS_OF_PC, LocalTime.now().toSecondOfDay(), today);
                    eldJsonViewModel.postStatus(new Status(EnumsConstants.STATUS_OF_PC, new Point(point), note.getText().toString(), getDateFormat(Calendar.getInstance().getTime())));
                    statusDaoViewModel.insertStatus(new Status(EnumsConstants.STATUS_OF_PC, new Point(point), note.getText().toString(), getDateFormat(Calendar.getInstance().getTime())));
                    idRadioPC.setChecked(false);
                    idRadioYM.setChecked(false);
                } else if (idRadioYM.isChecked()) {
                    lastStatusData.saveLasStatus(EnumsConstants.STATUS_ON_YM, LocalTime.now().toSecondOfDay(), today);
                    eldJsonViewModel.postStatus(new Status(EnumsConstants.STATUS_ON_YM, new Point(point), note.getText().toString(), getDateFormat(Calendar.getInstance().getTime())));
                    statusDaoViewModel.insertStatus(new Status(EnumsConstants.STATUS_ON_YM, new Point(point), note.getText().toString(), getDateFormat(Calendar.getInstance().getTime())));
                    idRadioPC.setChecked(false);
                    idRadioYM.setChecked(false);
                } else {
                    lastStatusData.saveLasStatus(current_status, LocalTime.now().toSecondOfDay(), today);
                    eldJsonViewModel.postStatus(new Status(current_status, new Point(point), note.getText().toString(), getDateFormat(Calendar.getInstance().getTime())));
                    statusDaoViewModel.insertStatus(new Status(current_status, new Point(point), note.getText().toString(), getDateFormat(Calendar.getInstance().getTime())));
                    idRadioPC.setChecked(false);
                    idRadioYM.setChecked(false);
                }
            }
            statusDaoViewModel.getCurDateStatus(customLiveRulerChart, customRulerChart, getDayFormat(ZonedDateTime.now()));
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

        if (!lastStatusData.getLastDay().equals(today)) {
            lastStatusData.saveLasStatus(lastStatusData.getLastStatus(), 0, today);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_option_menus, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shareMenu:
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
        checkNightModeActivated();
        nightModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            userData.saveMode(isChecked);
            drawerLayout.closeDrawer(GravityCompat.START);
        });

    }

    private void checkNightModeActivated() {
        nightModeSwitch.setChecked(userData.getMode());
    }

    private void getDrawerToggleEvent() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, activity_main_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
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

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                findViewById(R.id.idActivityMainView).setTranslationX(drawerView.getWidth() * slideOffset);
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // Checking for fragment count on back stack
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                // Go to the previous fragment
                getSupportFragmentManager().popBackStack();
            } else {
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

    private void automateConnectToEld(){
        ScanForEld();
//        final SearchEldDeviceDialog searchEldDeviceDialog = new SearchEldDeviceDialog(MainActivity.this);
//        searchEldDeviceDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        searchEldDeviceDialog.show();
    }

    //ELD functions
    private void connectToEld() {

        final SearchEldDeviceDialog searchEldDeviceDialog = new SearchEldDeviceDialog(MainActivity.this);
        searchEldDeviceDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final ConnectToEldDialog dialog = new ConnectToEldDialog(this,isGpsEnabled(),lastStatusData);
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
                EldBleError eldBleError = mEldManager.DisconnectEld();
                if (eldBleError == EldBleError.ELD_NOT_CONNECTED){
                    Toast.makeText(MainActivity.this,"Device is not connected!",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this,"Device is disconnected successfully!",Toast.LENGTH_SHORT).show();
                }
                lastStatusData.saveLastConnState(String.valueOf(R.string.not_connected));
                dialog.cancel();
            }

            @Override
            public void onClickCancel() {
                dialog.cancel();
            }
        });

        dialog.show();
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
        @Override
        public void OnDataRecord(final EldBroadcast dataRec, final EldBroadcastTypes RecordType) {

            if (dataRec instanceof EldBufferRecord) {
                startseq = ((EldBufferRecord) dataRec).getStartSeqNo();
                endseq = ((EldBufferRecord) dataRec).getEndSeqNo();
            } else if (RecordType != EldBroadcastTypes.ELD_DATA_RECORD) {
                Log.d("Adverse","Do something!");
            } else {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
                ArrayList<Double> points = new ArrayList<>();
                points.add(((EldDataRecord) dataRec).getLongitude());
                points.add(((EldDataRecord) dataRec).getLatitude());
                if ((((EldDataRecord) dataRec).getEngineState() == EldEngineStates.ENGINE_OFF || ((EldDataRecord) dataRec).getEngineState() == EldEngineStates.ENGINE_INVALID) && engineState && !Objects.equals(lastStatusData.getLastStatus(), EnumsConstants.STATUS_OFF)) {
                    engineState = false;
                    lastStatusData.saveLasStatus(EnumsConstants.STATUS_OFF,LocalTime.now().toSecondOfDay(), today);
                    statusDaoViewModel.getCurDateStatus(customLiveRulerChart, customRulerChart, getDayFormat(ZonedDateTime.now()));
                    statusDaoViewModel.insertStatus(new Status(EnumsConstants.POWER_DOWN, new Point(points), null, dateFormat.format(((EldDataRecord) dataRec).getGpsDateTime())));
                    statusDaoViewModel.insertStatus(new Status(EnumsConstants.STATUS_OFF, new Point(points), null, dateFormat.format(((EldDataRecord) dataRec).getGpsDateTime())));
                    eldJsonViewModel.postStatus(new Status(EnumsConstants.STATUS_OFF, new Point(points), "off", dateFormat.format(((EldDataRecord) dataRec).getGpsDateTime())));
                    eldJsonViewModel.postStatus(new Status(EnumsConstants.POWER_DOWN, new Point(points), "power down", dateFormat.format(((EldDataRecord) dataRec).getGpsDateTime())));
                } else if (((EldDataRecord) dataRec).getEngineState() == EldEngineStates.ENGINE_ON && !engineState && !Objects.equals(lastStatusData.getLastStatus(), EnumsConstants.STATUS_ON)) {
                    engineState = true;
                    statusDaoViewModel.getCurDateStatus(customLiveRulerChart, customRulerChart, getDayFormat(ZonedDateTime.now()));
                    lastStatusData.saveLasStatus(EnumsConstants.STATUS_ON,LocalTime.now().toSecondOfDay(), today);
                    statusDaoViewModel.insertStatus(new Status(EnumsConstants.POWER_UP, new Point(points), null, dateFormat.format(((EldDataRecord) dataRec).getGpsDateTime())));
                    statusDaoViewModel.insertStatus(new Status(EnumsConstants.STATUS_ON, new Point(points), null, dateFormat.format(((EldDataRecord) dataRec).getGpsDateTime())));
                    eldJsonViewModel.postStatus(new Status(EnumsConstants.STATUS_ON, new Point(points), "on", dateFormat.format(((EldDataRecord) dataRec).getGpsDateTime())));
                    eldJsonViewModel.postStatus(new Status(EnumsConstants.POWER_UP, new Point(points), "power up", dateFormat.format(((EldDataRecord) dataRec).getGpsDateTime())));
                }
                if (((EldDataRecord) dataRec).getSpeed() > 5 && !isDriving && getStatus(lastStatusData.getLastStatus()) < 4 && !Objects.equals(lastStatusData.getLastStatus(), EnumsConstants.STATUS_ON)) {
                    isDriving = true;
                    runOnUiThread(() -> {
                        if (mRunnable != null) {
                            mHandler.removeCallbacks(mRunnable);
                        }
                        if (manageStatusDialog != null) {
                            manageStatusDialog.cancel();
                        }
                        Intent intent = new Intent(MainActivity.this, RecapActivity.class);
                        startActivity(intent);
                    });
                    statusDaoViewModel.getCurDateStatus(customLiveRulerChart, customRulerChart, getDayFormat(ZonedDateTime.now()));
                    lastStatusData.saveLasStatus(EnumsConstants.STATUS_DR,LocalTime.now().toSecondOfDay(), today);
                    statusDaoViewModel.insertStatus(new Status(EnumsConstants.STATUS_DR, new Point(points), "dr", getDateFormat(Calendar.getInstance().getTime())));
                    eldJsonViewModel.postStatus(new Status(EnumsConstants.STATUS_DR, new Point(points), "dr", dateFormat.format(((EldDataRecord) dataRec).getGpsDateTime())));
                } else if (((EldDataRecord) dataRec).getSpeed() == 0 && isDriving && getStatus(lastStatusData.getLastStatus()) < 4) {
                    isDriving = false;
                    runOnUiThread(() -> {
                        mRunnable = () -> {
                            if (RecapActivity.instance != null) {
                                RecapActivity.instance.finish();
                            }
                            manageStatusDialog = new ManageStatusDialog(MainActivity.this, eldJsonViewModel, statusDaoViewModel);
                            manageStatusDialog.show();
                        };
                        mHandler.postDelayed(mRunnable, 10000L);
                    });
                }

                if (((EldDataRecord) dataRec).getSequence() % 30 == 1) {
                    Boolean b = null;
                    if (((EldDataRecord) dataRec).getEngineState() == EldEngineStates.ENGINE_OFF) {
                        lastStatusData.saveLastEngineState(String.valueOf(R.string.off));
                        b = false;
                    } else if (((EldDataRecord) dataRec).getEngineState() == EldEngineStates.ENGINE_ON) {
                        lastStatusData.saveLastEngineState(String.valueOf(R.string.on));
                        b = true;
                    }
                    if (isConnected) {

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
                                new Point(points),
                                ((EldDataRecord) dataRec).getGpsSpeed(),
                                ((EldDataRecord) dataRec).getCourse(),
                                ((EldDataRecord) dataRec).getNumSats(),
                                ((EldDataRecord) dataRec).getMslAlt(),
                                ((EldDataRecord) dataRec).getDop(),
                                ((EldDataRecord) dataRec).getSequence(),
                                ((EldDataRecord) dataRec).getFirmwareVersion()
                        ));
                    } else {
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
                                new Point(points),
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
                eldJsonViewModel.sendEldNum(new Eld(strDevice, mEldManager.GetApiVersion()));
                runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("ELD " + strDevice + " found, now connecting...\n", "")));

                EldBleError res = mEldManager.ConnectToEld(bleDataCallback, subscribedRecords, bleConnectionStateChangeCallback);

                if (res == EldBleError.SUCCESS) {
                    runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("Conncected to Eld\n", "")));
                } if (res == EldBleError.ELD_CONNECTED){
                    runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("Already Connected\n", "")));
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
                eldJsonViewModel.sendEldNum(new Eld(MAC, mEldManager.GetApiVersion()));
                runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("ELD " + strDevice + " found, now connecting...\n", "")));

                EldBleError res = mEldManager.ConnectToEld(bleDataCallback, subscribedRecords, bleConnectionStateChangeCallback, strDevice);
                if (res == EldBleError.SUCCESS) {
                    runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("Conncected to Eld\n", "")));
                } if (res == EldBleError.ELD_CONNECTED){
                    runOnUiThread(() -> EventBus.getDefault().postSticky(new MessageModel("Already Connected\n", "")));
                }
                lastStatusData.saveLastConnState(String.valueOf(R.string.connected));
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
        changeDateTimeBroadcast = new ChangeDateTimeBroadcast() {
            @Override
            public void onDayChanged() {
                time = "" + Calendar.getInstance().getTime();
                today = time.split(" ")[1] + " " + time.split(" ")[2];
                lastStatusData.saveLasStatus(lastStatusData.getLastStatus(), 0, today);
                daoViewModel.getAllDays(dayEntities -> daoViewModel.deleteLastDay(dayEntities.get(0)));
                last_recycler_view = findViewById(R.id.idRecyclerView);
                daoViewModel.getMgetAllDays(MainActivity.this, last_recycler_view, dvirViewModel, statusDaoViewModel);
                statusDaoViewModel.getCurDateStatus(customLiveRulerChart, customRulerChart, getDayFormat(ZonedDateTime.now()));
            }
        };
        this.registerReceiver(changeDateTimeBroadcast, ChangeDateTimeBroadcast.getIntentFilter());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        daoViewModel.getMgetAllDays(MainActivity.this, last_recycler_view, dvirViewModel, statusDaoViewModel);
        statusDaoViewModel.getCurDateStatus(customLiveRulerChart, customRulerChart, getDayFormat(ZonedDateTime.now()));
        super.onResume();
        isPaused = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        sessionManager.clearToken();
        sessionManager.clearAccessToken();
        this.unregisterReceiver(changeDateTimeBroadcast);
    }

    private static final String[] BLE_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    @RequiresApi(S)
    private static final String[] ANDROID_12_BLE_PERMISSIONS = new String[]{
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    public static void requestBlePermissions(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= S)
            ActivityCompat.requestPermissions(activity, ANDROID_12_BLE_PERMISSIONS, requestCode);
        else
            ActivityCompat.requestPermissions(activity, BLE_PERMISSIONS, requestCode);
    }

    public boolean isGpsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return manager != null && manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void requestGps() {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,10 * 1000).build();

// Create a location settings request builder
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

// Check whether the current location settings are satisfied
        LocationServices.getSettingsClient(this)
                .checkLocationSettings(builder.build())
                .addOnSuccessListener(locationSettingsResponse -> {
                    // GPS is enabled; start location updates
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
                })
                .addOnFailureListener(new OnFailureListener() {
                    private static final int REQUEST_ENABLE_GPS = 0;

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ResolvableApiException) {
                            // GPS is disabled; prompt the user to enable it
                            try {
                                // Show the dialog requesting the user to enable GPS
                                ResolvableApiException resolvable = (ResolvableApiException) e;
                                resolvable.startResolutionForResult(MainActivity.this, REQUEST_ENABLE_GPS);
                            } catch (IntentSender.SendIntentException sendEx) {
                                // Ignore the error
                            }
                        }
                    }
                });
    }

    private String getLocation(){
        String add = "";
        GPSTracker gpsTracker = new GPSTracker(this);
        double longtitude = gpsTracker.getLongitude();
        double latitude = gpsTracker.getLatitude();
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        try {
            if (latitude != 0 && longtitude != 0) {
                point.add(longtitude);
                point.add(latitude);
                List<Address> addresses = geocoder.getFromLocation(latitude, longtitude, 1);
                Address obj = addresses.get(0);
                add = obj.getAddressLine(0);

                return add;

            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return e.getMessage();
        }

        return add;
    }
}
