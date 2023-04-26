package com.iosix.eldblesample.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.multidex.BuildConfig;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.broadcasts.NetworkConnectionLiveData;
import com.iosix.eldblesample.enums.StateData;
import com.iosix.eldblesample.models.ApkVersion;
import com.iosix.eldblesample.models.LoginResponse;
import com.iosix.eldblesample.models.Student;
import com.iosix.eldblesample.shared_prefs.DriverSharedPrefs;
import com.iosix.eldblesample.shared_prefs.LastStatusData;
import com.iosix.eldblesample.shared_prefs.SessionManager;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.GeneralViewModel;
import com.iosix.eldblesample.viewModel.LoginViewModel;
import com.iosix.eldblesample.viewModel.SignatureViewModel;
import com.iosix.eldblesample.viewModel.StatusDaoViewModel;
import com.iosix.eldblesample.viewModel.UserViewModel;
import com.iosix.eldblesample.viewModel.apiViewModel.EldJsonViewModel;

import java.io.IOException;
import java.util.Calendar;

public class LoginActivity extends BaseActivity {

    private LoginViewModel loginViewModel;
    private SessionManager sessionManager;
    private ProgressDialog mProgress;
    private EditText password;
    private EditText login;
    private Button button;
    private boolean isConnected;
    private NetworkConnectionLiveData networkConnectionLiveData;
    private StatusDaoViewModel statusDaoViewModel;
    private DayDaoViewModel daoViewModel;
    private DvirViewModel dvirViewModel;
    private UserViewModel userViewModel;
    private SignatureViewModel signatureViewModel;
    private GeneralViewModel generalViewModel;
    private EldJsonViewModel eldJsonViewModel;
    private LastStatusData lastStatusData;
    private DriverSharedPrefs driverSharedPrefs;

    private String time = "" + Calendar.getInstance().getTime();
    public String today = time.split(" ")[1] + " " + time.split(" ")[2];

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

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        sessionManager = SessionManager.getInstance(getApplicationContext());

        networkConnectionLiveData = new NetworkConnectionLiveData(this);

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
                    mProgress.show();
                    loginViewModel.makeLoginRequest(new Student(login.getText().toString(),password.getText().toString()));
                }
            }else {
                showDialog("CHECK INTERNET CONNECTION");
            }
        });

    }

    private void observeHandlers(){
        loginViewModel.getLoginResponse().observe(this,this::handleDatas);
    }


    private void handleDatas(@NonNull StateData<LoginResponse> response) {

            switch (response.getStatus()) {
                case SUCCESS:
                    assert response.getData() != null;
                    sessionManager.saveAccessToken(response.getData().getAccessToken());
                    sessionManager.saveToken(response.getData().getrefreshToken());
                    new Handler().postDelayed(() ->{
                        eldJsonViewModel = ViewModelProviders.of(this).get(EldJsonViewModel.class);
                        eldJsonViewModel.getAllDvirs(dvirViewModel);
                        eldJsonViewModel.getAllLogs(statusDaoViewModel,lastStatusData,today);
                        eldJsonViewModel.getAllSignatures(signatureViewModel);
                        eldJsonViewModel.getAllGenerals(generalViewModel);
                        eldJsonViewModel.getUser(driverSharedPrefs);
                        eldJsonViewModel.sendApkVersion(new ApkVersion(BuildConfig.BUILD_TYPE, Build.VERSION.RELEASE));
                        mProgress.cancel();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    },7000);
                    break;
                case ERROR:
                    Throwable e = response.getError();
                    long statusCode = response.getStatusCode();
                    if (e instanceof IOException){
                        showDialog("BADLY INTERNET CONNECTION");
                        mProgress.cancel();
                    }
                    else if (statusCode >= 400 && statusCode < 500){
                        showDialog("INCORRECT LOGIN OR PASSWORD");
                        mProgress.cancel();
                    }else if (statusCode >= 500){
                        showDialog("SERVER ERROR");
                        mProgress.cancel();
                    }
                    break;
                case LOADING:
//                mProgress.show();
                    break;
                case COMPLETE:
                    break;
            }
    }

    private void setUpViewModel(){
        dvirViewModel = ViewModelProviders.of(this).get(DvirViewModel.class);
        signatureViewModel = ViewModelProviders.of(this).get(SignatureViewModel.class);
        statusDaoViewModel = ViewModelProviders.of(this).get(StatusDaoViewModel.class);
        generalViewModel = ViewModelProviders.of(this).get(GeneralViewModel.class);
        lastStatusData = LastStatusData.getInstance(getApplicationContext());
        driverSharedPrefs = DriverSharedPrefs.getInstance(getApplicationContext());
        networkConnectionLiveData.observe(LoginActivity.this, isConnected -> this.isConnected = isConnected);
    }

    private void showProgressBar(){
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
}