package com.iosix.eldblesample.activity;

import static com.iosix.eldblesample.enums.Day.getCurrentSeconds;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProviders;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.broadcasts.NetworkConnectionLiveData;
import com.iosix.eldblesample.enums.EnumsConstants;
import com.iosix.eldblesample.enums.StateData;
import com.iosix.eldblesample.models.LoginResponse;
import com.iosix.eldblesample.models.Status;
import com.iosix.eldblesample.models.Student;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;
import com.iosix.eldblesample.shared_prefs.DriverSharedPrefs;
import com.iosix.eldblesample.shared_prefs.LastStopSharedPrefs;
import com.iosix.eldblesample.shared_prefs.SessionManager;
import com.iosix.eldblesample.utils.Utils;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.StatusDaoViewModel;
import com.iosix.eldblesample.viewModel.UserViewModel;
import com.iosix.eldblesample.viewModel.apiViewModel.EldJsonViewModel;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class LoginActivity extends BaseActivity {

    private EldJsonViewModel eldJsonViewModel;
    private StatusDaoViewModel statusDaoViewModel;
    private DayDaoViewModel daoViewModel;
    private SessionManager sessionManager;
    private ProgressDialog mProgress;
    private LastStopSharedPrefs lastStopSharedPrefs;
    private final String time = "" + Calendar.getInstance().getTime();
    private final String today = time.split(" ")[1] + " " + time.split(" ")[2];
    private EditText password;
    private EditText login;
    private Button button;
    private boolean isConnected;
    private DriverSharedPrefs driverSharedPrefs;
    private UserViewModel userViewModel;
    private ArrayList<LogEntity> logEntities;
    private ArrayList<LogEntity> logEntitiesDriver;
    private ArrayList<LogEntity> logEntitiesCurr;
    private ArrayList<LogEntity> logEntitiesLastDays;
    private ArrayList<DayEntity> dayEntities;
    private NetworkConnectionLiveData networkConnectionLiveData;
    private DateFormat dateFormat;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        super.initView();

        button = findViewById(R.id.idLoginButton);
        login = findViewById(R.id.idEditTextLogin);
        password = findViewById(R.id.idEditTextPassword);

        eldJsonViewModel = ViewModelProviders.of(this).get(EldJsonViewModel.class);
        statusDaoViewModel = ViewModelProviders.of(this).get(StatusDaoViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        daoViewModel = ViewModelProviders.of(this).get(DayDaoViewModel.class);


        driverSharedPrefs = DriverSharedPrefs.getInstance(getApplicationContext());

        lastStopSharedPrefs =  LastStopSharedPrefs.getInstance(this.getApplicationContext());
        networkConnectionLiveData = new NetworkConnectionLiveData(this);
        dateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());


        setUpViewModel();
        showProgressBar();
        observeHandlers();


        login();
    }

    private void login(){

        button.setOnClickListener(view -> {
            if (isConnected){
                if (login.getText().toString().equals("") || password.getText().toString().equals("")){
                    showDialog("FILL ALL FREE SPACES");
                }else {
                    eldJsonViewModel.makeLoginRequest(new Student(login.getText().toString(),password.getText().toString()));
                }
            }else {
                showDialog("CHECK INTERNET CONNECTION");
            }
        });

    }

    private void observeHandlers(){
        eldJsonViewModel.getLoginResponse().observe(this,this::handleBooks);
    }

    private void handleBooks(@NonNull StateData<LoginResponse> response) {
        switch (response.getStatus()) {
            case SUCCESS:
                assert response.getData() != null;
                sessionManager.saveAccessToken(response.getData().getAccessToken());
                sessionManager.saveToken(response.getData().getrefreshToken());
                sessionManager.saveEmail(login.getText().toString());
                sessionManager.savePassword(password.getText().toString());
                lastStopSharedPrefs.saveLastStopTime(getCurrentSeconds());
                lastStopSharedPrefs.saveLastStopDate(today);
                getUSerInfo();
                new Handler().postDelayed(()->{

                    for (int i = 0; i < logEntities.size(); i++) {
                        if (!logEntities.get(i).getDriverId().equals(driverSharedPrefs.getDriverId()) || logEntities.get(i).getTo_status() > 5){
                            logEntities.remove(logEntities.get(i));
                        }
                    }

                    if (logEntities.size() > 0){
                        for (int i = 0; i < dayEntities.size(); i++) {
                            logEntitiesCurr.clear();
                            logEntitiesLastDays.clear();
                            for (int j = 0; j < logEntities.size(); j++) {
                                if (logEntities.get(j).getTime().equals(dayEntities.get(i).getDay())){
                                    logEntitiesCurr.add(logEntities.get(i));
                                }
                            }
                            for (int j = 0; j < i; j++) {
                                for (int k = 0; k < logEntities.size(); k++) {
                                    if (logEntities.get(k).getTime().equals(dayEntities.get(j).getDay())){
                                        logEntitiesLastDays.add(logEntities.get(k));
                                    }
                                }
                            }
                            if (logEntitiesCurr.size() == 0){
                                if (logEntitiesLastDays.size() > 0){
                                    statusDaoViewModel.insertStatus(new LogEntity(
                                            logEntitiesLastDays.get(logEntitiesLastDays.size()-1).getDriverId(),
                                            logEntitiesLastDays.get(logEntitiesLastDays.size()-1).getTo_status(),
                                            logEntitiesLastDays.get(logEntitiesLastDays.size()-1).getLocation(),
                                            logEntitiesLastDays.get(logEntitiesLastDays.size()-1).getNote(),
                                            null,
                                            dayEntities.get(i).getDay(),
                                            0)
                                    );
                                }else {
                                    statusDaoViewModel.insertStatus(new LogEntity(
                                            driverSharedPrefs.getDriverId(),
                                            EnumsConstants.STATUS_OFF,
                                            null,
                                            null,
                                            null,
                                            dayEntities.get(i).getDay(),
                                            0)
                                    );
                                }
                            }
                        }
                    }else {
                        for (int i = 0; i < dayEntities.size(); i++) {
                            statusDaoViewModel.insertStatus(new LogEntity(
                                    driverSharedPrefs.getDriverId(),
                                    EnumsConstants.STATUS_OFF,
                                    null,
                                    null,
                                    null,
                                    dayEntities.get(i).getDay(),
                                    0)
                            );
                        }
                    }

                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("JSON",1);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    mProgress.cancel();
                },2000L);
                break;
            case ERROR:
                Throwable e = response.getError();
                long statusCode = response.getStatusCode();
                if (e instanceof IOException){
                    showDialog("BADLY INTERNET CONNECTION");
                    mProgress.cancel();
                }
                if (statusCode == 500){
                    showDialog("INCORRECT LOGIN OR PASSWORD");
                    mProgress.cancel();
                }

                break;
            case LOADING:
                mProgress.show();
                break;
            case COMPLETE:
                break;
        }
    }

    private void setUpViewModel(){
        logEntities = new ArrayList<>();
        logEntitiesCurr = new ArrayList<>();
        logEntitiesLastDays = new ArrayList<>();
        logEntitiesDriver = new ArrayList<>();
        dayEntities = new ArrayList<>();

        statusDaoViewModel.getmAllStatus().observe(this,statusEntites->{
            logEntities.addAll(statusEntites);
        });

        daoViewModel.getMgetAllDays().observe(this,dayEntities -> {
            if (dayEntities != null){
                this.dayEntities.addAll(dayEntities);
            }
        });

        networkConnectionLiveData.observe(LoginActivity.this, isConnected -> this.isConnected = isConnected);

    }

    private void showProgressBar(){
        sessionManager = SessionManager.getInstance(getApplicationContext());
        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        mProgress.setOnShowListener(dialogInterface -> {
            ProgressBar progressBar = mProgress.findViewById(android.R.id.progress);
            progressBar.getIndeterminateDrawable().setColorFilter(ResourcesCompat.getColor(getResources(),R.color.colorPrimary,null),
                    PorterDuff.Mode.MULTIPLY);
        });
    }

    private void getUSerInfo() {

        userViewModel.deleteUser();
        eldJsonViewModel.getUser().observe(this, user -> {
            if (user != null) {
                driverSharedPrefs.saveLastUsername(user.getName());
                driverSharedPrefs.saveLastSurname(user.getLastName());
                driverSharedPrefs.saveLastImage(user.getImage());
                driverSharedPrefs.saveLastHomeTerAdd(user.getHomeTerminalAddress());
                driverSharedPrefs.saveLastHomeTerTimeZone(user.getTimeZone());
                driverSharedPrefs.saveLastMainOffice(user.getMainOffice());
                driverSharedPrefs.saveLastPhoneNumber(user.getPhone());
                driverSharedPrefs.saveCompany(user.getCompany());
                driverSharedPrefs.saveLastDriverId(user.getDriverId());
                eldJsonViewModel.postStatus(new Status(Utils.getStatus(EnumsConstants.LOGIN),null,null,Utils.getDateFormat(Calendar.getInstance().getTime())));
                statusDaoViewModel.insertStatus(new LogEntity(user.getDriverId(),EnumsConstants.LOGIN,null,null,null,today,getCurrentSeconds()));

            }
        });
    }

    private void showDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyDialogTheme)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.cancel());
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ResourcesCompat.getColor(getResources(),R.color.colorPrimary,null)));
        dialog.show();
        TextView textView = dialog.findViewById(android.R.id.message);
        textView.setTypeface(ResourcesCompat.getFont(LoginActivity.this,R.font.montserrat));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.getViewModelStore().clear();
    }
}