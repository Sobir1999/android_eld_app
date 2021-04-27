package com.iosix.eldblesample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
import com.iosix.eldblelib.EldDriverBehaviorRecord;
import com.iosix.eldblelib.EldDtcCallback;
import com.iosix.eldblelib.EldEmissionsRecord;
import com.iosix.eldblelib.EldEngineRecord;
import com.iosix.eldblelib.EldFirmwareUpdateCallback;
import com.iosix.eldblelib.EldFuelRecord;
import com.iosix.eldblelib.EldManager;
import com.iosix.eldblelib.EldParameterTypes;
import com.iosix.eldblelib.EldScanObject;
import com.iosix.eldblelib.EldTransmissionRecord;
import com.iosix.eldblesample.adapters.RecyclerViewLastAdapter;
import com.iosix.eldblesample.customViews.CustomRulerChart;
import com.iosix.eldblesample.customViews.StatusCustomButtonView;
import com.iosix.eldblesample.dialogs.ConnectToEldDialog;
import com.iosix.eldblesample.dialogs.EditLanguageDialog;
import com.iosix.eldblesample.dialogs.SearchEldDeviceDialog;
import com.iosix.eldblesample.interfaces.AlertDialogItemClickInterface;
import com.iosix.eldblesample.interfaces.EditLanguageDialogListener;
import com.iosix.eldblesample.models.MessageModel;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import io.reactivex.rxjava3.core.Observable;

import static com.iosix.eldblesample.R.anim.fade_out_custom_expanded;

public class MainActivity extends AppCompatActivity {

    private String[] country = { "English", "Spain", "French"};

    private DrawerLayout drawerLayout;
    private Toolbar activity_main_toolbar;
    private RecyclerViewLastAdapter lastAdapter;
    private RecyclerView last_recycler_view;
    private CustomRulerChart customRulerChart;
    private StatusCustomButtonView off, sb, dr, on;
    private ConstraintLayout visiblityViewCons;
    private Button cancel, save;
    private Switch nightModeSwitch;

    String MAC;
    private int updateSelection;

    private EldManager mEldManager;
    private Set<EldBroadcastTypes> subscribedRecords = EnumSet.of(EldBroadcastTypes.ELD_BUFFER_RECORD, EldBroadcastTypes.ELD_CACHED_RECORD, EldBroadcastTypes.ELD_FUEL_RECORD, EldBroadcastTypes.ELD_DATA_RECORD, EldBroadcastTypes.ELD_DRIVER_BEHAVIOR_RECORD, EldBroadcastTypes.ELD_EMISSIONS_PARAMETERS_RECORD, EldBroadcastTypes.ELD_ENGINE_PARAMETERS_RECORD, EldBroadcastTypes.ELD_TRANSMISSION_PARAMETERS_RECORD);
    private boolean diagnosticEnabled = false, fuelEnabled = false, engineEnabled = false, transmissionEnabled = false, emissionsEnabled = false, driverEnabled = false;

    private static final int REQUEST_BASE = 100;
    private static final int REQUEST_BT_ENABLE = REQUEST_BASE + 1;

    private boolean exit = false;

    boolean reqdelinprogress = false;
    int startseq, endseq;
    int reccount = 0;

    public static final String MyPREFERENCES = "nightModePrefs";
    public static final String Key_ISNIGHTMODE = "isNightMODE";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLocale(loadLocal());

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        drawerLayout = findViewById(R.id.drawer_layout);
        last_recycler_view = findViewById(R.id.idRecyclerView);
        customRulerChart = findViewById(R.id.idCustomChart);

        onClickCustomBtn();
        onClickVisiblityCanAndSaveBtn();

        findViewById(R.id.idDocumentIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/*");
                startActivityForResult(intent, 0);
            }
        });

        //Last Days Recycler Adapter
        lastAdapter = new RecyclerViewLastAdapter();
        last_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        last_recycler_view.setAdapter(lastAdapter);

        // Set the toolbar
        activity_main_toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(activity_main_toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //Required to allow bluetooth scanning
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        mEldManager = EldManager.GetEldManager(this, "123456789A");

        getDrawerToggleEvent();
        getDrawerTouchEvent();

        setLanguageDialog();

        Log.d("TAG", "onCreate: oncreate MainActivity");

    }

    private void setLanguageDialog() {
        TextView textView = findViewById(R.id.idSpinnerLanguage);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                drawerLayout.closeDrawer(Gravity.LEFT);
                final EditLanguageDialog dialog = new EditLanguageDialog(MainActivity.this);
                dialog.show();

                dialog.setListener(new EditLanguageDialogListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public void onClick(int id) {
                        if (id == R.id.idEng) {
                            setLocale("");
                            restartActivity();
                            dialog.cancel();
                        }
                        if (id == R.id.idEs) {
                            setLocale("es");
                            restartActivity();
                            dialog.cancel();
                        }
                        if (id == R.id.idFr) {
                            setLocale("fr");
                            restartActivity();
                            dialog.cancel();
                        }
                    }
                });
            }
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
                .getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("lan", language);
        editor.apply();
    }

    public void restartActivity() {
        recreate();
    }

    private String loadLocal() {
        SharedPreferences pref = getApplicationContext()
                .getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        return pref.getString("lan", "en");
    }

    private void onClickVisiblityCanAndSaveBtn(){
        save = findViewById(R.id.idVisButtonSave);
        cancel = findViewById(R.id.idVisButtonCancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visiblityViewCons.setVisibility(View.GONE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void onClickCustomBtn() {
        off = findViewById(R.id.statusOFF);
        sb = findViewById(R.id.statusSB);
        dr = findViewById(R.id.statusDR);
        on = findViewById(R.id.statusON);
        visiblityViewCons = findViewById(R.id.idVisibilityViewCons);

        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visiblityViewCons.setVisibility(View.VISIBLE);
//                visiblityViewCons.set(fade_out_custom_expanded);

            }
        });

        sb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visiblityViewCons.setVisibility(View.VISIBLE);
            }
        });

        dr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visiblityViewCons.setVisibility(View.VISIBLE);
            }
        });

        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visiblityViewCons.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_option_menus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shareMenu:
                return true;
            case R.id.connectMenu:
                connectToEld();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getDrawerTouchEvent() {
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        nightModeSwitch = findViewById(R.id.idNightChoose);
        checkNightModeActivated();
        nightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    saveNightModeState(true);
                    restartActivity();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    saveNightModeState(false);
                    restartActivity();
                }
            }
        });
    }

    private void saveNightModeState(boolean nightMode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Key_ISNIGHTMODE, nightMode);
        editor.apply();
    }

    private void checkNightModeActivated() {
        if (sharedPreferences.getBoolean(Key_ISNIGHTMODE, false)) {
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
        } else {
            // Checking for fragment count on back stack
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                // Go to the previous fragment
                getSupportFragmentManager().popBackStack();
            } else {
                // Exit the app
                super.onBackPressed();
            }
        }
    }

    //ELD functions
    private void connectToEld() {

        final ConnectToEldDialog dialog = new ConnectToEldDialog(this);
        dialog.setCancelable(false);
        dialog.show();

        final SearchEldDeviceDialog searchEldDeviceDialog = new SearchEldDeviceDialog(this);


        dialog.setAlerrtDialogItemClickInterface(new AlertDialogItemClickInterface() {
            @Override
            public void onClick() {
                dialog.cancel();

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                if (mEldManager.ScanForElds(bleScanCallback) == EldBleError.BLUETOOTH_NOT_ENABLED)
                    mEldManager.EnableBluetooth(REQUEST_BT_ENABLE);

                searchEldDeviceDialog.show();
//                final ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, "", "Please wait...");
//                new Thread() {
//                    public void run() {
//                        try{
//                            //your code here.....
//                        }
//                        catch (Exception e) {
//                            Log.e("tag", e.getMessage());
//                        }
//                        // dismiss the progress dialog
//                        progressDialog.dismiss();
//                    }
//                }.start();
            }
        });
    }

    private EldDtcCallback dtcCallback = new EldDtcCallback() {
        @Override
        public void onDtcDetected(final String status, final String jsonString) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    mDataView.append(status);
//                    mDataView.append(jsonString);
                    EventBus.getDefault().postSticky(new MessageModel(status, jsonString));
//                    mScrollView.fullScroll(View.FOCUS_DOWN);
                }
            });
        }
    };

    private EldFirmwareUpdateCallback fwUpdateCallback = new EldFirmwareUpdateCallback() {
        @Override
        public void onUpdateNotification(final String status) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final String data = status;
//                    mDataView.append(status + "\n");
                    EventBus.getDefault().postSticky(new MessageModel("", status));
//                    mScrollView.fullScroll(View.FOCUS_DOWN);
                }
            });
        }
    };


    public void onCheckUpdateClicked(View v) {
        if (v.getId() == R.id.FW_CHECK) {
            final String Status = mEldManager.CheckFirmwareUpdate();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    mStatusView.append("Current firmware: " + mEldManager.GetFirmwareVersion() + " Available firmware: " + mEldManager.CheckFirmwareUpdate() + "\r\n");
                    EventBus.getDefault().postSticky(new MessageModel("Current firmware: " + mEldManager.GetFirmwareVersion() + " Available firmware: " + mEldManager.CheckFirmwareUpdate() + "\r\n", ""));
                }
            });
        }
    }

    public void onReqDebugClicked(View v) {
        if (v.getId() == R.id.REQ_DEBUG) {
            EldBleError status = mEldManager.RequestDebugData();
            if (status != EldBleError.SUCCESS) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        mStatusView.append("Request Debug Failed\n");
                        EventBus.getDefault().postSticky(new MessageModel("Request Debug Failed\n", ""));
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        mStatusView.append("Request Debug Succeeded\n");
                        EventBus.getDefault().postSticky(new MessageModel("Request Debug Succeeded\n", ""));
                    }
                });
            }
        }
    }

    public void onUpdateFwClicked(View v) {
        if (v.getId() == R.id.updateFw) {

            updateSelection = 0;

            // get prompts.xml view
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.update_popup, null);
            final EditText downloadInput = (EditText) promptsView.findViewById(R.id.downloadInput);
            final EditText localInput = (EditText) promptsView.findViewById(R.id.localInput);
            final RadioGroup radioGroup = (RadioGroup) promptsView.findViewById(R.id.radiogroup);

            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setView(promptsView);

            alertDialogBuilder
                    .setCancelable(true)
                    .setPositiveButton("START",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
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
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                      public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                          switch (checkedId) {
                                                              case R.id.downloadbutton:
                                                                  updateSelection = 0;
                                                                  break;
                                                              case R.id.localbutton:
                                                                  updateSelection = 1;
                                                                  break;
                                                          }
                                                      }
                                                  }
            );

            // create alert dialog
            android.app.AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    }

    public void onEnableParametersClicked(View v) {
        if (v.getId() == R.id.ENABLE_PARAM) {
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.param_popup, null);
            final Switch dtcSwitch = (Switch) promptsView.findViewById(R.id.diagnostics);
            final Switch fuelSwitch = (Switch) promptsView.findViewById(R.id.fuel);
            final Switch engineSwitch = (Switch) promptsView.findViewById(R.id.engine);
            final Switch transmissionSwitch = (Switch) promptsView.findViewById(R.id.transmission);
            final Switch emissionsSwitch = (Switch) promptsView.findViewById(R.id.emissions);
            final Switch driverSwitch = (Switch) promptsView.findViewById(R.id.driver);

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
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (dtcSwitch.isChecked() && !diagnosticEnabled) {
                                        final EldBleError status = mEldManager.EnableAdditionalParameters(EldParameterTypes.DIAGNOSTIC_PARAMETERS);
                                        subscribedRecords.add(EldBroadcastTypes.ELD_DIAGNOSTIC_RECORD);
                                        diagnosticEnabled = true;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //todo mStatusView.append("EnableAdditionalParameters (" + status + ")\n");
                                            }
                                        });
                                    } else if (!dtcSwitch.isChecked() && diagnosticEnabled) {
                                        final EldBleError status = mEldManager.DisableAdditionalParameters(EldParameterTypes.DIAGNOSTIC_PARAMETERS);
                                        subscribedRecords.remove(EldBroadcastTypes.ELD_DIAGNOSTIC_RECORD);
                                        diagnosticEnabled = false;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //todo mStatusView.append("DisableAdditionalParameters (" + status + ")\n");
                                            }
                                        });
                                    }

                                    if (fuelSwitch.isChecked() && !fuelEnabled) {
                                        final EldBleError status = mEldManager.EnableAdditionalParameters(EldParameterTypes.FUEL_PARAMETERS);
                                        subscribedRecords.add(EldBroadcastTypes.ELD_FUEL_RECORD);
                                        fuelEnabled = true;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //todo mStatusView.append("EnableAdditionalParameters (" + status + ")\n");
                                            }
                                        });
                                    } else if (!fuelSwitch.isChecked() && fuelEnabled) {
                                        final EldBleError status = mEldManager.DisableAdditionalParameters(EldParameterTypes.FUEL_PARAMETERS);
                                        subscribedRecords.remove(EldBroadcastTypes.ELD_FUEL_RECORD);
                                        fuelEnabled = false;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //todo mStatusView.append("DisableAdditionalParameters (" + status + ")\n");
                                            }
                                        });
                                    }

                                    if (engineSwitch.isChecked() && !engineEnabled) {
                                        final EldBleError status = mEldManager.EnableAdditionalParameters(EldParameterTypes.ENGINE_PARAMETERS);
                                        subscribedRecords.add(EldBroadcastTypes.ELD_ENGINE_PARAMETERS_RECORD);
                                        engineEnabled = true;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //todo mStatusView.append("EnableAdditionalParameters (" + status + ")\n");
                                            }
                                        });
                                    } else if (!engineSwitch.isChecked() && engineEnabled) {
                                        final EldBleError status = mEldManager.DisableAdditionalParameters(EldParameterTypes.ENGINE_PARAMETERS);
                                        subscribedRecords.remove(EldBroadcastTypes.ELD_ENGINE_PARAMETERS_RECORD);
                                        engineEnabled = false;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //todo mStatusView.append("DisableAdditionalParameters (" + status + ")\n");
                                            }
                                        });
                                    }

                                    if (transmissionSwitch.isChecked() && !transmissionEnabled) {
                                        final EldBleError status = mEldManager.EnableAdditionalParameters(EldParameterTypes.TRANSMISSION_PARAMETERS);
                                        subscribedRecords.add(EldBroadcastTypes.ELD_TRANSMISSION_PARAMETERS_RECORD);
                                        transmissionEnabled = true;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //todo mStatusView.append("EnableAdditionalParameters (" + status + ")\n");
                                            }
                                        });
                                    } else if (!transmissionSwitch.isChecked() && transmissionEnabled) {
                                        final EldBleError status = mEldManager.DisableAdditionalParameters(EldParameterTypes.TRANSMISSION_PARAMETERS);
                                        subscribedRecords.remove(EldBroadcastTypes.ELD_TRANSMISSION_PARAMETERS_RECORD);
                                        transmissionEnabled = false;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //todo mStatusView.append("DisableAdditionalParameters (" + status + ")\n");
                                            }
                                        });
                                    }

                                    if (emissionsSwitch.isChecked() && !emissionsEnabled) {
                                        final EldBleError status = mEldManager.EnableAdditionalParameters(EldParameterTypes.EMISSIONS_PARAMETERS);
                                        subscribedRecords.add(EldBroadcastTypes.ELD_EMISSIONS_PARAMETERS_RECORD);
                                        emissionsEnabled = true;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //todo mStatusView.append("EnableAdditionalParameters (" + status + ")\n");
                                            }
                                        });
                                    } else if (!emissionsSwitch.isChecked() && emissionsEnabled) {
                                        final EldBleError status = mEldManager.DisableAdditionalParameters(EldParameterTypes.EMISSIONS_PARAMETERS);
                                        subscribedRecords.remove(EldBroadcastTypes.ELD_EMISSIONS_PARAMETERS_RECORD);
                                        emissionsEnabled = false;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //todo mStatusView.append("DisableAdditionalParameters (" + status + ")\n");
                                            }
                                        });
                                    }

                                    if (driverSwitch.isChecked() && !driverEnabled) {
                                        final EldBleError status = mEldManager.EnableAdditionalParameters(EldParameterTypes.DRIVER_BEHAVIOR);
                                        subscribedRecords.add(EldBroadcastTypes.ELD_DRIVER_BEHAVIOR_RECORD);
                                        driverEnabled = true;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //todo mStatusView.append("EnableAdditionalParameters (" + status + ")\n");
                                            }
                                        });
                                    } else if (!driverSwitch.isChecked() && driverEnabled) {
                                        final EldBleError status = mEldManager.DisableAdditionalParameters(EldParameterTypes.DRIVER_BEHAVIOR);
                                        subscribedRecords.remove(EldBroadcastTypes.ELD_DRIVER_BEHAVIOR_RECORD);
                                        driverEnabled = false;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //todo mStatusView.append("DisableAdditionalParameters (" + status + ")\n");
                                            }
                                        });
                                    }

                                    mEldManager.UpdateSubscribedRecordTypes(subscribedRecords);
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    public void onSetPeriodClicked(View v) {
        if (v.getId() == R.id.SET_PERIOD) {
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.period_popup, null);
            final EditText periodInput = (EditText) promptsView.findViewById(R.id.periodinput);

            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setView(promptsView);

            alertDialogBuilder
                    .setCancelable(true)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    int period = Integer.parseInt(periodInput.getText().toString()) * 1000;
                                    final EldBleError status = mEldManager.SetRecordingInterval(period);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //todo mStatusView.append("Set Period Status (" + status + ")\n");
                                        }
                                    });
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }


    public void onReqRecordClicked(View v) {
        if (v.getId() == R.id.reqButton) {
            final EldBleError status = mEldManager.RequestRecord();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //todo mStatusView.append("ReqRecordStatus (" + status + ")\n");
                }
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //todo mStatusView.append("DelRecordStatus (" + status + ")\n");
                }
            });
        }
    }

    public void onSetOdoClicked(View v) {
        if (v.getId() == R.id.SET_ODO) {
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.odo_popup, null);
            final EditText odoInput = (EditText) promptsView.findViewById(R.id.odoinput);

            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setView(promptsView);

            alertDialogBuilder
                    .setCancelable(true)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    int odo = Integer.parseInt(odoInput.getText().toString());
                                    if (odo > 0) {
                                        mEldManager.SetOdometer(odo);
                                    }
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

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
            final EditText timeInput = (EditText) promptsView.findViewById(R.id.timeinput);

            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setView(promptsView);

            alertDialogBuilder
                    .setCancelable(true)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    int time = Integer.parseInt(timeInput.getText().toString());
                                    if (time > 0) {
                                        mEldManager.SetTime(time);
                                    }
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

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

    public boolean hasBlePermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
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

    private EldBleConnectionStateChangeCallback bleConnectionStateChangeCallback = new EldBleConnectionStateChangeCallback() {
        @Override
        public void onConnectionStateChange(final int newState) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //todo mDataView.append("New State of connection" + Integer.toString(newState, 10) + "\n");
                }
            });
        }
    };

    private EldBleDataCallback bleDataCallback = new EldBleDataCallback() {
        @Override
        public void OnDataRecord(final EldBroadcast dataRec, final EldBroadcastTypes RecordType) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

//                    mDataView.append(dataRec.getBroadcastString().trim() + "\n");
                    EventBus.getDefault().postSticky(new MessageModel("", dataRec.getBroadcastString().trim() + "\n"));

                    if (dataRec instanceof EldBufferRecord) {
                        startseq = ((EldBufferRecord) dataRec).getStartSeqNo();
                        endseq = ((EldBufferRecord) dataRec).getEndSeqNo();
                    } else if (RecordType == EldBroadcastTypes.ELD_DATA_RECORD) {
//                        mDataView.append("RPM: " + Double.toString(((EldDataRecord) (dataRec)).getRpm()));
//                        mDataView.append(" Satellites: " + Double.toString(((EldDataRecord) (dataRec)).getNumSats()));
//                        mDataView.append(" Latitude: " + Double.toString(((EldDataRecord) (dataRec)).getLatitude()));
//                        mDataView.append(" Longitude: " + Double.toString(((EldDataRecord) (dataRec)).getLongitude()));
//                        mDataView.append(" Firmware: " + ((EldDataRecord) (dataRec)).getFirmwareVersion() + "\n");

                    } else if (RecordType == EldBroadcastTypes.ELD_CACHED_RECORD) {
                        //Shows how to get to the specific record types created based on the broadcast info

//                        mStatusView.append(dataRec.getBroadcastString());
                        EventBus.getDefault().postSticky(new MessageModel(dataRec.getBroadcastString(), ""));

                        if (reqdelinprogress) {
                            reccount++;
                            Log.d("TESTING", "received " + reccount + " records");
                            if (reccount == 10) {
                                Log.d("TESTING", "delete " + startseq + "-" + (startseq + 9));
                                mEldManager.DeleteRecord(startseq, startseq + 9);
                                Log.d("TESTING", "request " + (startseq + 10));
                                mEldManager.RequestRecord(startseq + 10);
                            } else if (reccount == 11) {
                                Log.d("TESTING", "success!");

                                reqdelinprogress = false;
                                reccount = 0;
                            }
                        }

                        if (dataRec instanceof EldCachedPeriodicRecord) {

                            Log.d("TESTING", "Odometer " + ((EldCachedPeriodicRecord) (dataRec)).getOdometer());
                            Log.d("TESTING", "Engine Hours " + ((EldCachedPeriodicRecord) (dataRec)).getEngineHours());
                            Log.d("TESTING", "RPM " + ((EldCachedPeriodicRecord) (dataRec)).getRpm());
                            Log.d("TESTING", "Satellites " + ((EldCachedPeriodicRecord) (dataRec)).getNumSats());
                            Log.d("TESTING", "Lat " + ((EldCachedPeriodicRecord) (dataRec)).getLatitude());
                            Log.d("TESTING", "Lon " + ((EldCachedPeriodicRecord) (dataRec)).getLongitude());
                            Log.d("TESTING", "Unix Time " + ((EldCachedPeriodicRecord) (dataRec)).getUnixTime());
                            Log.d("TESTING", "Sequence Number " + ((EldCachedPeriodicRecord) (dataRec)).getSeqNum());

                            // mDataView.append("CACHED REC"+((EldCachedPeriodicRecord)(dataRec)).getBroadcastString());

                        } else if (dataRec instanceof EldCachedNewTimeRecord) {
                            ((EldCachedNewTimeRecord) (dataRec)).getEngineHours();
                            ((EldCachedNewTimeRecord) (dataRec)).getNewUnixTime();
                        } else if (dataRec instanceof EldCachedNewVinRecord) {
                            Log.d("TESTING", "Vin " + ((EldCachedNewVinRecord) (dataRec)).getVin());
                            Log.d("TESTING", "Odometer " + ((EldCachedNewVinRecord) (dataRec)).getOdometer());
                            Log.d("TESTING", "Engine Hours " + ((EldCachedNewVinRecord) (dataRec)).getEngineHours());
                            Log.d("TESTING", "Unix Time " + ((EldCachedNewVinRecord) (dataRec)).getUnixTime());
                            Log.d("TESTING", "Sequence Number " + ((EldCachedNewVinRecord) (dataRec)).getSeqNum());
                        }

                    } else if (RecordType == EldBroadcastTypes.ELD_DRIVER_BEHAVIOR_RECORD) {
                        EldDriverBehaviorRecord rec = (EldDriverBehaviorRecord) dataRec;
                        driverEnabled = true;
                        if (rec instanceof EldDriverBehaviorRecord) {
                            ((EldDriverBehaviorRecord) (rec)).getAbsStatus();
                            //((EldCachedPeriodicRecord)(rec)).getUnixTime();

//                            mStatusView.append("" + rec.getCruiseSetSpeed_kph() + " ");
//                            mStatusView.append("" + rec.getCruiseStatus() + " ");
//                            mStatusView.append("" + rec.getThrottlePosition_pct() + " ");
//                            mStatusView.append("" + rec.getAcceleratorPosition_pct() + " ");
//                            mStatusView.append("" + rec.getBrakePosition_pct() + " ");
//                            mStatusView.append("" + rec.getSeatBeltStatus() + " ");
//                            mStatusView.append("" + rec.getSteeringWheelAngle_deg() + " ");
//                            mStatusView.append("" + rec.getAbsStatus() + " ");
//                            mStatusView.append("" + rec.getTractionStatus() + " ");
//                            mStatusView.append("" + rec.getStabilityStatus() + " ");
//                            mStatusView.append("" + rec.getBrakeSystemPressure_kpa() + " ");
//                            mStatusView.append("\n");

                            // mDataView.append("CACHED REC"+((EldCachedPeriodicRecord)(rec)).getBroadcastString());

                        }
                    } else if (RecordType == EldBroadcastTypes.ELD_ENGINE_PARAMETERS_RECORD) {
                        EldEngineRecord rec = (EldEngineRecord) dataRec;
                        engineEnabled = true;

//                        mStatusView.append("" + rec.getOilPressure_kpa() + " ");
//                        mStatusView.append("" + rec.getTurboBoost_kpa() + " ");
//                        mStatusView.append("" + rec.getIntakePressure_kpa() + " ");
//                        mStatusView.append("" + rec.getFuelPressure_kpa() + " ");
//                        mStatusView.append("" + rec.getCrankCasePressure_kpa() + " ");
//                        mStatusView.append("" + rec.getLoad_pct() + " ");
//                        mStatusView.append("" + rec.getMassAirFlow_galPerSec() + " ");
//                        mStatusView.append("" + rec.getTurboRpm() + " ");
//                        mStatusView.append("" + rec.getIntakeTemp_c() + " ");
//                        mStatusView.append("" + rec.getEngineCoolantTemp_c() + " ");
//                        mStatusView.append("" + rec.getEngineOilTemp_c() + " ");
//                        mStatusView.append("" + rec.getFuelTemp_c() + " ");
//                        mStatusView.append("" + rec.getChargeCoolerTemp_c() + " ");
//                        mStatusView.append("" + rec.getTorgue_Nm() + " ");
//                        mStatusView.append("" + rec.getEngineOilLevel_pct() + " ");
//                        mStatusView.append("" + rec.getEngineCoolandLevel_pct() + " ");
//                        mStatusView.append("" + rec.getTripFuel_L() + " ");
//                        mStatusView.append("" + rec.getDrivingFuelEconomy_LPerKm() + " ");
//                        mStatusView.append("\n");

                        //mDataView.append("Engine Rec was sent" + ((EldEngineRecord) (rec)).getBroadcastString());


                    } else if (RecordType == EldBroadcastTypes.ELD_EMISSIONS_PARAMETERS_RECORD) {
                        EldEmissionsRecord rec = (EldEmissionsRecord) dataRec;
                        emissionsEnabled = true;

//                        mStatusView.append("" + rec.getNOxInlet() + " ");
//                        mStatusView.append("" + rec.getNOxOutlet() + " ");
//                        mStatusView.append("" + rec.getAshLoad() + " ");
//                        mStatusView.append("" + rec.getDpfSootLoad() + " ");
//                        mStatusView.append("" + rec.getDpfRegenStatus() + " ");
//                        mStatusView.append("" + rec.getDpfDifferentialPressure() + " ");
//                        mStatusView.append("" + rec.getEgrValvePosition() + " ");
//                        mStatusView.append("" + rec.getAfterTreatmentFuelPressure() + " ");
//                        mStatusView.append("" + rec.getEngineExhaustTemperature() + " ");
//                        mStatusView.append("" + rec.getExhaustTemperature1() + " ");
//                        mStatusView.append("" + rec.getExhaustTemperature2() + " ");
//                        mStatusView.append("" + rec.getExhaustTemperature3() + " ");
//                        mStatusView.append("" + rec.getDefFluidLevel() + " ");
//                        mStatusView.append("" + rec.getDefTankTemperature() + " ");
//                        mStatusView.append("" + rec.getScrInducementFaultStatus() + " ");
//                        mStatusView.append("\n");


                    } else if (RecordType == EldBroadcastTypes.ELD_TRANSMISSION_PARAMETERS_RECORD) {
                        EldTransmissionRecord rec = (EldTransmissionRecord) dataRec;
                        transmissionEnabled = true;

//                        mStatusView.append("" + rec.getOutputShaftRpm() + " ");
//                        mStatusView.append("" + rec.getGearStatus() + " ");
//                        mStatusView.append("" + rec.getRequestGearStatus() + " ");
//                        mStatusView.append("" + rec.getTransmissionOilTemp_c() + " ");
//                        mStatusView.append("" + rec.getTorqueConverterLockupStatus() + " ");
//                        mStatusView.append("" + rec.getTorqueConverterOilOutletTemp_c() + " ");
//                        mStatusView.append("\n");

                    } else if (RecordType == EldBroadcastTypes.ELD_FUEL_RECORD) {
                        EldFuelRecord rec = (EldFuelRecord) dataRec;
                        fuelEnabled = true;

//                        mStatusView.append("" + rec.getFuelLevelPercent() + " ");
//                        mStatusView.append("" + rec.getFuelIntegratedLiters() + " ");
//                        mStatusView.append("" + rec.getTotalFuelConsumedLiters() + " ");
//                        mStatusView.append("" + rec.getFuelRateLitersPerHours() + " ");
//                        mStatusView.append("" + rec.getIdleFuelConsumedLiters() + " ");
//                        mStatusView.append("" + rec.getIdleTimeHours() + " ");
//                        mStatusView.append("" + rec.getStateHighRPM() + " ");
//                        mStatusView.append("" + rec.getStateUnsteady() + " ");
//                        mStatusView.append("" + rec.getStateEnginePower() + " ");
//                        mStatusView.append("" + rec.getStateAccel() + " ");
//                        mStatusView.append("" + rec.getStateEco() + " ");
//                        mStatusView.append("" + rec.getStateAnticipate() + " ");
//                        mStatusView.append("\n");

                    } else if (RecordType == EldBroadcastTypes.ELD_DIAGNOSTIC_RECORD) {
                        diagnosticEnabled = true;
                    }

//                    mScrollView.fullScroll(View.FOCUS_DOWN);
                }
            });
        }
    };

    private EldBleScanCallback bleScanCallback = new EldBleScanCallback() {

        @Override
        public void onScanResult(EldScanObject device) {

            Log.d("BLETEST", "BleScanCallback single");

            final String strDevice;
            if (device != null) {
                strDevice = device.getDeviceId();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        mStatusView.append("ELD " + strDevice + " found, now connecting...\n");
                        EventBus.getDefault().postSticky(new MessageModel("ELD " + strDevice + " found, now connecting...\n", ""));
                    }
                });

                EldBleError res = mEldManager.ConnectToEld(bleDataCallback, subscribedRecords, bleConnectionStateChangeCallback);

                if (res != EldBleError.SUCCESS) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            mStatusView.append("Connection Failed\n");
                            EventBus.getDefault().postSticky(new MessageModel("Connection Failed\n", ""));
                        }
                    });
                }

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        mStatusView.append("No ELD found\n");
                        EventBus.getDefault().postSticky(new MessageModel("No ELD found\n", ""));
                    }
                });
            }
        }

        @Override
        public void onScanResult(ArrayList deviceList) {

            Log.d("BLETEST", "BleScanCallback multiple");

            final String strDevice;
            EldScanObject so;

            if (deviceList != null) {
                so = (EldScanObject) deviceList.get(0);
                strDevice = so.getDeviceId();
                MAC = strDevice;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        mStatusView.append("ELD " + strDevice + " found, now connecting...\n");
                        EventBus.getDefault().postSticky(new MessageModel("ELD " + strDevice + " found, now connecting...\n", ""));
                    }
                });

                EldBleError res = mEldManager.ConnectToEld(bleDataCallback, subscribedRecords, bleConnectionStateChangeCallback, strDevice);

                if (res != EldBleError.SUCCESS) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            mStatusView.append("Connection Failed\n");
                            EventBus.getDefault().postSticky(new MessageModel("Connection Failed\n", ""));
                        }
                    });
                }

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        mStatusView.append("No ELD found\n");
                        EventBus.getDefault().postSticky(new MessageModel("No ELD found\n", ""));
                    }
                });
            }
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BT_ENABLE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
//                mStatusView.append("Bluetooth enabled - now scanning for ELD\n");
                EventBus.getDefault().postSticky(new MessageModel("Bluetooth enabled - now scanning for ELD\n", ""));
                mEldManager.ScanForElds(bleScanCallback);
            } else {
//                mStatusView.append("Unable to enable bluetooth\n");
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
}